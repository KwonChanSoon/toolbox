package eu.qualityontime.timeline;

import java.util.Date;

import org.joda.time.*;

import eu.qualityontime.AppDate;

public class TimelineGenerator {

  private ITimelineFactory factory;

  public TimelineGenerator(ITimelineFactory factory) {
    this.factory = factory;
  }

  public Timeline generateTimeline(Date start, Date end, ETimelineUnit unit) {
    Timeline t = factory.newTimeline();
    DateTime s = new DateTime(AppDate.midnight(start));
    DateTime e = new DateTime(AppDate.midnight(end));
    int minInUnit = (60 * 24 / unit.getUnitsPerDays()) - 1;
    int daysDiff = Days.daysBetween(s, e).getDays();

    DateTime prev = s;
    for (int i = 0; i < unit.getUnitsPerDays() * daysDiff; i++) {
      DateTime next = prev.plusMinutes(minInUnit);
      t.addItem(factory.newTimelineItem(Long.valueOf(i), AppDate.formatIfNotNull(prev.toDate(), "yyyyMMddHHmm"), prev.toDate(),
          next.toDate()));
      prev = next.plusMinutes(1);
    }

    return t;
  }
}