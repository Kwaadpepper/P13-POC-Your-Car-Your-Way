package com.ycyw.shared.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Minimal Logback appender that sends each logging event to Loki via HTTP /loki/api/v1/push. -
 * Intended for POC / low volume. - Wrap this appender with an AsyncAppender in logback config to
 * avoid blocking application threads.
 *
 * <p>Notes: - For production, add batching, retries, publisher confirms (if using RabbitMQ), or use
 * an agent (Promtail/Vector). - This class targets Java 21 (uses java.net.http.HttpClient).
 */
public final class LokiHttpAppender extends AppenderBase<ILoggingEvent> {

  private final ObjectMapper mapper = new ObjectMapper();
  private @Nullable HttpClient httpClient = null;

  // configurable via logback XML setters
  private String url = "http://localhost:3100/loki/api/v1/push";
  private String serviceName = "unknown-service";
  private boolean includeMdc = true;

  public void setUrl(String url) {
    this.url = url;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public void setIncludeMdc(boolean includeMdc) {
    this.includeMdc = includeMdc;
  }

  @Override
  public void start() {
    this.httpClient =
        HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    super.start();
  }

  @Override
  protected void append(ILoggingEvent event) {

    try {
      // Loki expects timestamp in nanoseconds as string
      final String tsNanos = Long.toString(event.getTimeStamp() * 1_000_000L);

      // Build labels (stream) - Loki labels must be strings
      Map<String, String> labels = new LinkedHashMap<>();
      labels.put("service", serviceName);
      labels.put("logger", event.getLoggerName());
      labels.put("level", event.getLevel().toString());

      if (includeMdc && event.getMDCPropertyMap() != null) {
        event
            .getMDCPropertyMap()
            .forEach(
                (k, v) -> {
                  if (k != null && v != null) {
                    labels.put(k, v);
                  }
                });
      }

      // Build the line content as structured JSON
      Map<String, Object> line = new LinkedHashMap<>();
      line.put("timestamp", Instant.ofEpochMilli(event.getTimeStamp()).toString());
      line.put("message", event.getFormattedMessage());
      line.put("thread", event.getThreadName());

      IThrowableProxy tp = event.getThrowableProxy();
      if (tp != null) {
        line.put("throwable", tp.getMessage());
      }

      String lineJson = mapper.writeValueAsString(line);

      // single stream entry for payload
      Map<String, Object> streamEntry = new HashMap<>();
      streamEntry.put("stream", labels);
      // values is a list of arrays [ [ "<unix_nano>", "<line>" ] ]
      List<List<String>> values = Collections.singletonList(Arrays.asList(tsNanos, lineJson));
      streamEntry.put("values", values);

      Map<String, Object> payload = new HashMap<>();
      payload.put("streams", Collections.singletonList(streamEntry));

      byte[] body = mapper.writeValueAsBytes(payload);

      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create(url))
              .header("Content-Type", "application/json")
              .timeout(Duration.ofSeconds(5))
              .POST(HttpRequest.BodyPublishers.ofByteArray(body))
              .build();

      if (httpClient != null) {
        HttpResponse<Void> resp = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        if (resp.statusCode() >= 400) {
          addError("Loki returned HTTP " + resp.statusCode());
        }
      } else {
        addError("HttpClient is not initialized, cannot send log to Loki");
      }
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
      addError("Thread was interrupted while sending log to Loki", ie);
    } catch (Exception e) {
      // appenders must not throw; report via addError so Logback can surface the problem
      addError("Failed to send log to Loki", e);
    }
  }
}
