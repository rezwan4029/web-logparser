package com.ef;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.util.ResourceUtils;

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
      }else{
        end = start.plusHours(1);
      }
      int threshold = Integer.parseInt(arguments.get(Constant.Params.THRESHOLD));
      run(logFilePath, start, end, threshold);
    } catch (Exception ex) {
      System.err.println(ex);
      System.err.println("Invalid argument or parameter. Example - ");
      System.out.println(
          "java -cp \"parser.jar\" com.ef.Parser --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100");
      System.out.println(
          "java -cp \"parser.jar\" com.ef.Parser --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=daily --threshold=100");

    }
    log.info("Finished ....");
    //String loc = "/Users/rezwan/Documents/monstar-lab/parser/src/main/resources/access.log";
    //run(loc, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 100);
  }

  public static void run(String filePath, LocalDateTime start, LocalDateTime end, int threshold) {
    System.err.println(start);
    System.err.println(end);
    System.err.println(threshold);
    try {

      LogAnalyser analiser = new LogAnalyser();

      analiser.run(filePath, start, end, threshold);
    } catch (Exception e) {
      log.error("File not found !" ,e);
    }
  }

}
