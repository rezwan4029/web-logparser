package com.ef;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.hibernate.Session;

import com.ef.libs.HibernateUtil;
import com.ef.libs.Utils;
import com.ef.schema.LogEntity;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogAnalyser {

  private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
  private static short LOG_PATTERN_LENGTH = 5;
  private static String SEPARATOR = "\\|";

  private ArrayList<LogEntity> logs;
  private Session session;

  public LogAnalyser() {
    logs = new ArrayList<>();
    session = HibernateUtil.getSessionFactory().openSession();
  }


  @SneakyThrows(value = {FileNotFoundException.class, IOException.class})
  public void run(String filePath, LocalDateTime start, LocalDateTime end, int threshold) {
    log.info("Reading file - {}", filePath);

    Path path = Paths.get(filePath);
    log.debug("File found");

    Stream<String> lines = Files.lines(path);
    HashMap<String, Integer> ipTrackers = new HashMap<>();

    lines.forEach(line -> {
      LogEntity l = parseEntry(line);
      logs.add(l);
      if (l.getStartDate().isAfter(start) && l.getStartDate().isBefore(end)) {
        ipTrackers.put(l.getIpAddress(), ipTrackers.getOrDefault(l.getIpAddress(), 0) + 1);
      }

      saveToDatabase(l);

    });
    lines.close();

    log.debug("Total {} lines found", logs.size());

    List<String> results = new ArrayList<String>();
    for (Entry<String, Integer> entry : ipTrackers.entrySet()) {
      if (entry.getValue() >= threshold) {
        results.add(entry.getKey());
      }
    }

    log.info("Results - {}", results);
    session.close();
  }

  public void saveToDatabase(LogEntity l) {

    session.beginTransaction();
    session.saveOrUpdate(l);
    session.getTransaction().commit();
    session.close();

    HibernateUtil.shutdown();
  }

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
    } else {
      log.error("Invalid log pattern - {}", line);
    }

    return entity;
  }
}
