package com.ef;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.stream.Stream;

import com.ef.libs.Utils;
import com.ef.schema.LogEntity;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogAnalyser {
  private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
  private static short LOG_PATTERN_LENGTH = 5;
  private static String SEPARATOR = "\\|";


  private ArrayList<LogEntity> results;

  public LogAnalyser() {
    this.results = new ArrayList<LogEntity>();
  }


  @SneakyThrows(value = {FileNotFoundException.class, IOException.class})
  public void run(String filePath, LocalDateTime start, LocalDateTime end, int threshold) {
    System.err.println("hello");
    Path path = Paths.get(filePath);

    StringBuilder data = new StringBuilder();
    Stream<String> lines = Files.lines(path);
    // AtomicInteger i = new AtomicInteger(10);
    HashMap<String, Integer> ipTrackers = new HashMap<>();
    HashSet<LocalDate> st = new HashSet<>();
    try {
      lines.forEach(line -> {
        LogEntity lg = parseEntry(line);
        results.add(lg);
        st.add(lg.getStartDate().toLocalDate());
        if(lg.getStartDate().isAfter(start) && lg.getStartDate().isBefore(end)){
          ipTrackers.put(lg.getIpAddress(), ipTrackers.getOrDefault(lg.getIpAddress(), 0) + 1);
        }
        // i.getAndDecrement();
        // if(i.get() == 0){ throw new RuntimeException("done"); };
      });
    } catch (Exception e) {
       log.error("Broken !!!!");
    }
    log.error("Set size {}", st.size());
    lines.close();
    System.err.println(results.size());
   
    for (Entry<String, Integer> entry : ipTrackers.entrySet())
    {
        if(entry.getValue() >= threshold){
          log.info(entry.getKey());
        }
      //log.info(entry.getKey() + "=" + entry.getValue());
    }
    

  }


  // log format:
  // 2017-01-01 04:24:43.059|192.168.89.111|"GET / HTTP/1.1"|200|
  // "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko)
  // Chrome/61.0.3163.79 Safari/537.36"
  public static LogEntity parseEntry(String line) {

    LogEntity entity = null;
    String info[] = line.split(SEPARATOR);

    if (info.length == LOG_PATTERN_LENGTH) {
      entity = new LogEntity();
      entity.setStartDate(Utils.stringTodateTime(info[0], DATETIME_FORMAT));
      entity.setIpAddress(info[1]);
      entity.setMethodType(info[2]);
      entity.setStatus(Integer.parseInt(info[3]));
      entity.setUserAgent(info[4]);
    }else{
      System.err.println("missed - " + line );
    }

    return entity;
  }
}
