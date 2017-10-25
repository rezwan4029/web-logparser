package com.ef;

import java.time.LocalDateTime;
import java.util.Map;

import com.ef.libs.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Parser {

  private static final String DATETIME_FORMAT = "yyyy-MM-dd.HH:mm:ss";

  public static void main(String args[]) {
    Map<String, String> arguments = Utils.argumentParser(args);

    try {
      String logFilePath = arguments.get(Constant.Params.ACCESS_LOG);
      LocalDateTime start =
          Utils.stringTodateTime(arguments.get(Constant.Params.STARTDATE), DATETIME_FORMAT);
      LocalDateTime end = null;
      if (arguments.get(Constant.Params.DURATION).equals(Constant.Duration.DAILY)) {
        end = start.plusDays(1);
      } else {
        end = start.plusHours(1);
      }
      int threshold = Integer.parseInt(arguments.get(Constant.Params.THRESHOLD));
      run(logFilePath, start, end, threshold);
    } catch (Exception ex) {
      log.error("Exception - {}", ex);
      log.error("Invalid argument or parameter. Example - ");
      log.error(
          "java -cp \"parser.jar\" com.ef.Parser --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100");
      log.error(
          "java -cp \"parser.jar\" com.ef.Parser --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=daily --threshold=100");

    }
    log.info("Finished ....");
  }

  public static void run(String filePath, LocalDateTime start, LocalDateTime end, int threshold) {
    log.info("Parsing file[{}] using startDate[{}] , endDate[{}] and threshold[{}]", filePath,
        start, end, threshold);
    try {

      LogAnalyser analyser = new LogAnalyser();
      analyser.run(filePath, start, end, threshold);
    } catch (Exception e) {
      log.error("File not found !", e);
    }
  }

}
