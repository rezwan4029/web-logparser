package com.ef.libs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Utils {

  public static Map<String, String> argumentParser(String args[]) {
    final Map<String, String> params = new HashMap<String, String>();
    for (int i = 0; i < args.length; i++) {
      String keyValue[] = args[i].split("=");
      if (keyValue.length == 2 && keyValue[0].startsWith("--")) {
        params.put(keyValue[0].substring(2), keyValue[1]);
      } else {
        System.err.println("Invalid argument");
      }

    }
    return params;
  }

  public static LocalDateTime stringTodateTime(String date, String formatter) {
    return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(formatter, Locale.US));

  }
}
