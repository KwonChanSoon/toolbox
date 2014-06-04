package eu.qualityontime;

import java.util.*;

public class AppCalendarBean {

  public Date now() {
    return new Date();
  }

  public Calendar nowCalendar() {
    return Calendar.getInstance();
  }
}
