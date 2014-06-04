package ep.pericles;

import static ep.pericles.AppDate.parse;
import static org.junit.Assert.*;

import java.util.Date;

import org.joda.time.*;
import org.junit.*;

public class AppCalendarTest {

  @Test
  public void joda_days() {
    assertEquals(1, Days.daysBetween(new DateTime(), new DateTime().plusDays(1)).getDays());
    assertEquals(0, Days.daysBetween(new DateTime(), new DateTime().plusHours(1)).getDays());
    assertEquals(1, Days.daysBetween(new DateTime(), new DateTime().plusDays(1).plusHours(1)).getDays());
    assertEquals(1, Days.daysBetween(new DateTime(), new DateTime().plusHours(30)).getDays());
  }

  @Test
  public void joda_hours() {
    assertEquals(24, Hours.hoursBetween(new DateTime(), new DateTime().plusDays(1)).getHours());
    assertEquals(1, Hours.hoursBetween(new DateTime(), new DateTime().plusHours(1)).getHours());
    assertEquals(25, Hours.hoursBetween(new DateTime(), new DateTime().plusDays(1).plusHours(1)).getHours());
  }

  @Test
  public void joda_datetiem_datemidnight_equality() {
    Date today_midnight = AppDate.parse("2012-07-26 00:00:00");
    DateTime dt = new DateTime(today_midnight);
    DateMidnight dm = new DateMidnight(today_midnight);
    Assert.assertEquals(dm, dt);
  }

  @Test
  public void daysMidnight() {
    Date today_midnight = AppDate.parse("2012-07-26 00:00:00");
    Date tomorrow_midnight = AppDate.parse("2012-07-27 00:00:00");
    Date today_morning = AppDate.parse("2012-07-26 08:00:00");
    Date today_afternoon = AppDate.parse("2012-07-26 16:00:00");
    Date tomorrow_morning = AppDate.parse("2012-07-27 08:00:00");
    Date night_shift_start = AppDate.parse("2012-07-26 22:00:00");
    Date night_shift_end = AppDate.parse("2012-07-27 04:00:00");
    Date long_start = AppDate.parse("2012-07-26 22:00:00");
    Date long_end = AppDate.parse("2012-08-05 04:00:00");
    assertEquals(1, AppCalendar.daysMidnight(today_midnight, today_midnight).size());
    assertEquals(1, AppCalendar.daysMidnight(today_morning, today_afternoon).size());
    assertEquals(2, AppCalendar.daysMidnight(today_morning, tomorrow_morning).size());
    assertEquals(2, AppCalendar.daysMidnight(night_shift_start, night_shift_end).size());
    assertEquals(1, AppCalendar.daysMidnight(today_midnight, tomorrow_midnight).size());
    assertEquals(11, AppCalendar.daysMidnight(long_start, long_end).size());
  }

  @Test
  public void test_joda_week() {
    Assert.assertEquals(52, new LocalDate(parse("2012-01-01")).getWeekOfWeekyear());
    Assert.assertEquals(1, new LocalDate(parse("2012-01-02")).getWeekOfWeekyear());
    Assert.assertEquals(35, new LocalDate(parse("2012-08-28")).getWeekOfWeekyear());
  }

  @Test
  public void middleOf() {
    Date start = AppDate.parse("2012-12-10 00:00:00");
    Date end = AppDate.parse("2012-12-11 00:00:00");

    Date mid = AppCalendar.middleOf(start, end);
    assertNotNull(mid);
    assertEquals(mid, AppDate.parse("2012-12-10 12:00:00"));

    try {
      mid = AppCalendar.middleOf(end, start);
    } catch (IllegalArgumentException iae) {

    } catch (Exception e) {
      Assert.fail(e.getMessage());
    }
  }

}
