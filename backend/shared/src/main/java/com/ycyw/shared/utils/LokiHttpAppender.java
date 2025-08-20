package com.ycyw.shared.utils;

import java.io.ByteArrayOutputStream;
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
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.zip.GZIPOutputStream;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jdt.annotation.Nullable;

public final class LokiHttpAppender extends AppenderBase<ILoggingEvent> {

  private final ObjectMapper mapper = new ObjectMapper();

  @Nullable private HttpClient httpClient = null;

  // configurable via logback XML setters
  private boolean enabled = true;
  private @Nullable String url = null;
  private String serviceName = "unknown-service";
  private boolean includeMdc = true;
  private boolean mdcAsLabels = false;
  private Set<String> mdcLabelKeys = Collections.emptySet();
  private boolean gzipEnabled = false;
  private Duration requestTimeout = Duration.ofSeconds(5);

  // setter methods used by logback XML
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public void setIncludeMdc(boolean includeMdc) {
    this.includeMdc = includeMdc;
  }

  public void setMdcAsLabels(boolean mdcAsLabels) {
    this.mdcAsLabels = mdcAsLabels;
  }

  /**
   * Comma-separated list of MDC keys to promote into labels when mdcAsLabels is true. Example:
   * "env,instance"
   */
  public void setMdcLabelKeys(String csv) {
    if (csv == null || csv.isBlank()) {
      this.mdcLabelKeys = Collections.emptySet();
    } else {
      var parts =
          Arrays.stream(csv.split(","))
              .map(String::trim)
              .filter(s -> !s.isEmpty())
              .collect(java.util.stream.Collectors.toUnmodifiableSet());
      this.mdcLabelKeys = parts;
    }
  }

  public void setGzipEnabled(boolean gzipEnabled) {
    this.gzipEnabled = gzipEnabled;
  }

  public void setRequestTimeoutSeconds(int seconds) {
    this.requestTimeout = Duration.ofSeconds(seconds);
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
    if (!enabled) {
      return;
    }
    if (url == null || (url != null && url.isBlank())) {
      addError("HttpClient is not initialized; cannot send log to Loki.");
      return;
    }

    try {
      // build timestamp and content
      String ts = nanosFromMillis(event.getTimeStamp());

      // Build labels and the JSON line independently
      Map<String, String> labels = buildLabels(event);
      String lineJson = buildLineJson(event);

      // Build final payload bytes (possibly gzipped)
      byte[] body = buildPayloadBytes(labels, ts, lineJson);

      // Build request
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create(url))
              .timeout(requestTimeout)
              .header("Content-Type", "application/json")
              .POST(HttpRequest.BodyPublishers.ofByteArray(body))
              .build();

      if (httpClient != null) {
        CompletableFuture<HttpResponse<Void>> future =
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.discarding());
        future.whenComplete(
            (resp, ex) -> {
              if (ex != null) {
                addError("Failed to send log to Loki (async)", ex);
                return;
              }
              if (resp.statusCode() >= 400) {
                addError("Loki returned HTTP " + resp.statusCode());
              }
            });
      } else {
        addError("HttpClient is not initialized; cannot send log to Loki.");
      }

    } catch (Exception e) {
      addError("Failed to prepare or send log to Loki", e);
    }
  }

  private static String nanosFromMillis(long millis) {
    return Long.toString(Math.multiplyExact(millis, 1_000_000L));
  }

  private Map<String, String> buildLabels(ILoggingEvent event) {
    Map<String, String> labels = new LinkedHashMap<>();
    labels.put("service", serviceName);
    labels.put("logger", event.getLoggerName());
    labels.put("level", event.getLevel().levelStr);

    Map<String, String> mdcMap = event.getMDCPropertyMap();
    if (mdcAsLabels && !mdcMap.isEmpty() && !mdcLabelKeys.isEmpty()) {
      for (String key : mdcLabelKeys) {
        String v = mdcMap.get(key);
        if (!v.isEmpty()) {
          labels.put(key, v);
        }
      }
    }
    return labels;
  }

  private String buildLineJson(ILoggingEvent event) throws Exception {
    Map<String, Object> line = new LinkedHashMap<>();
    line.put("timestamp", Instant.ofEpochMilli(event.getTimeStamp()).toString());
    line.put("message", Optional.ofNullable(event.getFormattedMessage()).orElse(""));
    line.put("thread", Optional.ofNullable(event.getThreadName()).orElse(""));

    if (includeMdc) {
      Map<String, String> mdc = event.getMDCPropertyMap();
      if (mdc != null && !mdc.isEmpty()) {
        line.put("mdc", new LinkedHashMap<>(mdc));
      }
    }

    IThrowableProxy tp = event.getThrowableProxy();
    if (tp != null) {
      line.put("throwable", tp.getMessage());
    }

    return mapper.writeValueAsString(line);
  }

  private byte[] buildPayloadBytes(Map<String, String> labels, String ts, String lineJson)
      throws Exception {
    Map<String, Object> streamEntry = new HashMap<>();
    streamEntry.put("stream", labels);
    List<List<String>> values = Collections.singletonList(Arrays.asList(ts, lineJson));
    streamEntry.put("values", values);

    Map<String, Object> payload = new HashMap<>();
    payload.put("streams", Collections.singletonList(streamEntry));

    byte[] body = mapper.writeValueAsBytes(payload);

    if (gzipEnabled) {
      try {
        return gzip(body);
      } catch (Exception e) {
        // If gzip fails, fallback to uncompressed body but report the issue
        addError("Gzip compression failed, sending uncompressed payload", e);
        return body;
      }
    } else {
      return body;
    }
  }

  private static byte[] gzip(byte[] input) throws Exception {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPOutputStream gos = new GZIPOutputStream(bos)) {
      gos.write(input);
      gos.finish();
      return bos.toByteArray();
    }
  }
}
