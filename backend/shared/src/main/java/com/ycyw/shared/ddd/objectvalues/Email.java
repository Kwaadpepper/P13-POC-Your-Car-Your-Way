package com.ycyw.shared.ddd.objectvalues;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ycyw.shared.utils.Domain;

public record Email(String value) {

  public Email {
    Domain.checkDomain(() -> value.length() <= 256, "Email cannot exceed 256 characters");
    Domain.checkDomain(() -> isValidEmail(value), "Email has to be a valid email address");
  }

  private boolean isValidEmail(String value) {
    return isValidEmailRfc5322Simplified(value);
  }

  /**
   * Simplified RFC5322-like validation. Built with String.format to improve readability
   * (interpolation).
   */
  private boolean isValidEmailRfc5322Simplified(String email) {
    if (email.isBlank()) {
      return false;
    }

    if (email.length() - email.replace("@", "").length() != 1) {
      return false;
    }

    // split local part and domain
    final String emailSplitRegex = "^(.+)@(.+)$";
    final Pattern splitPattern = Pattern.compile(emailSplitRegex);
    final Matcher splitMatcher = splitPattern.matcher(email);
    if (!splitMatcher.find()) {
      return false;
    }

    final String localPart = splitMatcher.group(1);
    final String domain = splitMatcher.group(2);

    // token building pieces
    final String atomCharRegex = "[a-zA-Z0-9!#$%&'*+\\-/=?^_`{|}~]";
    final String quotedPairRegex = "\\\\[\\x00-\\x7F]";
    final String qtextRegex = "[\\x20-\\x21\\x23-\\x5B\\x5D-\\x7E]";

    // Using String.format for "interpolation" to create the local-part regex
    final String localPartValidationRegex =
        String.format(
            "^(?:%1$s+|\\\"(?:%2$s|%3$s)+\\\")(?:\\.(?:%1$s+|\\\"(?:%2$s|%3$s)+\\\"))*$",
            atomCharRegex, qtextRegex, quotedPairRegex);

    final Pattern localPattern = Pattern.compile(localPartValidationRegex);
    if (!localPattern.matcher(localPart).matches()) {
      return false;
    }

    // Domain regex pieces
    final String subDomainRegex = "[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?";
    final String domainNameRegex =
        String.format("^%1$s(?:\\.%1$s)*\\.[a-zA-Z]{2,63}$", subDomainRegex);

    // Simplified IPv4 regex using \d and reduced complexity
    final String ipv4Regex =
        "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";

    if (domain.startsWith("[") && domain.endsWith("]")) {
      String ipLiteral = domain.substring(1, domain.length() - 1);
      final Pattern ipv4Pattern = Pattern.compile(ipv4Regex);
      if (!ipv4Pattern.matcher(ipLiteral).matches()) {
        return false;
      }
    } else {
      final Pattern domainPattern = Pattern.compile(domainNameRegex, Pattern.CASE_INSENSITIVE);
      if (!domainPattern.matcher(domain).matches()) {
        return false;
      }
    }

    return true;
  }
}
