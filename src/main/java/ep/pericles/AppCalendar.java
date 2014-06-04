package ep.pericles;

import static ep.pericles.AppCollections.List;
import static ep.pericles.AppDate.betweenCloseClose;

import java.util.*;

import org.joda.time.*;

public class AppCalendar {
  public final static String FUTURE_PERFORMAT = "31/12/2099";
  public final static String FUTURE_DASHFORMAT = "31-12-2099";
  public final static Date FUTURE = AppDate.parse(FUTURE_PERFORMAT);

  private static AppCalendarBean delegate = new AppCalendarBean();

  public static void implementation(AppCalendarBean impl) {
    delegate = impl;
  }

  public static Date now() {
    return delegate.now();
  }

  public static Calendar nowCalendar() {
    return delegate.nowCalendar();
  }

  public static Date yesterday_now() {
    return new DateTime(now()).minusDays(1).toDate();
  }

  public static Date today() {
    return new DateMidnight(now()).toDate();
  }

  public static Date yesterday() {
    return new DateMidnight(now()).minusDays(1).toDate();
  }

  public static Date week_ago() {
    return AppDate.week_ago(now());
  }

  public static Date month_ago() {
    return new DateMidnight(now()).minusMonths(1).toDate();
  }

  public static Date tomorrow() {
    return AppDate.tomorrowMidnight(now());
  }

  public static Date middleOf(Date start, Date end) {
    AppPreconditions.checkNotNull(start, end);
    AppPreconditions.checkArgument(start.getTime() < end.getTime());

    DateTime s = new DateTime(start);

    return s.plus((end.getTime() - start.getTime()) / 2).toDate();
  }

  public static Date middleOf(DateRange range) {
    AppPreconditions.checkNotNull(range);
    return middleOf(range.getFrom(), range.getTo());
  }

  /**
   * Return individual Date (at midnight) for each day within the interval.
   */
  public static List<Date> daysMidnight(Date from, Date to) {
    AppPreconditions.checkNotNull(from, to);
    List<Date> ret = List();
    DateMidnight dmFrom = new DateMidnight(from);
    DateMidnight dmTo = new DateMidnight(to);

    if (dmFrom.plusDays(1).isEqual(dmTo)) {
      Interval wholeDay = new Interval(dmFrom, dmTo);
      if (betweenCloseClose(wholeDay, from) && betweenCloseClose(wholeDay, to)) {
        ret.add(dmFrom.toDate());
        return ret;
      }
    }

    for (; dmFrom.isBefore(dmTo) || dmFrom.isEqual(dmTo); dmFrom = dmFrom.plusDays(1)) {
      ret.add(dmFrom.toDate());
    }
    return ret;
  }
}
