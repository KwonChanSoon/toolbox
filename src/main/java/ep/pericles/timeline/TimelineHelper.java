package ep.pericles.timeline;

import java.util.Date;

import org.joda.time.*;

import ep.pericles.*;

public class TimelineHelper {

  public static final int TARGET_TIMELINE_ITEMS = 35;
  public static final int MAX_TIMELINE_ITEMS = 100;
  public static final int MAX_TIMELINE_DAYS = MAX_TIMELINE_ITEMS / ETimelineUnit.getLargestUnit().getUnitsPerDays();

  public static TimelineCriteria initCriteria(TimelineCriteria timeCrit, Date start, Date end) {
    return initCriteria(timeCrit, start, end, TARGET_TIMELINE_ITEMS);
  }

  public static TimelineCriteria initCriteria(TimelineCriteria timeCrit, Date start, Date end, int targetNbItems) {
    TimelineCriteria aCriteria;

    if (timeCrit == null) {
      aCriteria = new TimelineCriteria();
    }
    else {
      aCriteria = timeCrit;
    }

    Date actualEnd = getActualEnd(start, end);

    if (aCriteria.getRange() == null) {
      aCriteria.setRange(DateRange.of(start, actualEnd));
    }
    if (aCriteria.getUnit() == null) {
      aCriteria.setUnit(resolveUnit(start, actualEnd, targetNbItems));
    }

    return aCriteria;
  }

  protected static Date getActualEnd(Date start, Date end) {
    Date maxEndDate = AppDate.addDays(start, MAX_TIMELINE_DAYS - 1);
    return end.getTime() > maxEndDate.getTime() ? maxEndDate : end;
  }

  protected static ETimelineUnit resolveUnit(Date start, Date end, int targetNbItems) {
    int daysDif = Days.daysBetween(new DateTime(start), new DateTime(end)).getDays() + 1;

    ETimelineUnit bestUnit = ETimelineUnit.ONE_HOUR;
    int bestDelta = -1;

    for (ETimelineUnit aUnit : ETimelineUnit.values()) {
      int nbItems = daysDif * aUnit.getUnitsPerDays();
      int delta = Math.abs(nbItems - targetNbItems);

      if (bestDelta < 0 || delta < bestDelta) {
        bestUnit = aUnit;
        bestDelta = delta;
      }
    }

    return bestUnit;
  }

}
