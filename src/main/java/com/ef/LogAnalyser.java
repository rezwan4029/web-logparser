package com.ef;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.hibernate.Session;
import org.hibernate.Transaction;

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

  private Session session;

  private static short BATCH_SIZE = 100;

  public LogAnalyser() {
    session = HibernateUtil.getSessionFactory().openSession();
  }


  @SneakyThrows(value = {FileNotFoundException.class, IOException.class})
  public void run(String filePath, LocalDateTime start, LocalDateTime end, int threshold) {
    log.info("Reading file - {}", filePath);

    Path path = Paths.get(filePath);
    log.debug("File found");

    Stream<String> lines = Files.lines(path);
    HashMap<String, Integer> ipTrackers = new HashMap<>();
    HashSet<String> results = new HashSet<>();
    AtomicInteger totalLog = new AtomicInteger(0);

    List<LogEntity> dbUpdateList = new ArrayList<>();
    Transaction transaction = session.beginTransaction();
    lines.forEach(line -> {
      LogEntity l = parseEntry(line);
      dbUpdateList.add(l);
      if (l.getStartDate().isAfter(start) && l.getStartDate().isBefore(end)) {
        ipTrackers.put(l.getIpAddress(), ipTrackers.getOrDefault(l.getIpAddress(), 0) + 1);
        if (ipTrackers.get(l.getIpAddress()) > threshold) {
          results.add(l.getIpAddress());
        }
      }
      totalLog.incrementAndGet();
      if (dbUpdateList.size() % BATCH_SIZE == 0) {
        saveToDatabase(dbUpdateList);
      }


    });
    lines.close();

    saveToDatabase(dbUpdateList);

    log.debug("Total {} lines found", totalLog);

    log.info("Results - {}", results);
    transaction.commit();
    session.close();
    HibernateUtil.shutdown();
  }

  public void saveToDatabase(List<LogEntity> entities) {

    if (entities.isEmpty()) {
      return;
    }

    for (LogEntity logEntity : entities) {
      session.persist(logEntity);
    }
    entities.clear();
    session.flush();
    session.clear();
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
