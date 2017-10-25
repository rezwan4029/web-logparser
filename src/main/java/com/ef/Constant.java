package com.ef;

import java.util.Arrays;
import java.util.List;

public class Constant {

  public static class Params {
    public static final String ACCESS_LOG = "accesslog";
    public static final String DURATION = "duration";
    public static final String THRESHOLD = "threshold";
    public static final String STARTDATE = "startDate";
  }

  public static class Duration {
    public static final String DAILY = "daily";
    public static final String HOURLY = "hourly";
  }

  public static final List<String> DURATION_TYPES = Arrays.asList(Duration.DAILY, Duration.HOURLY);

}
