package ep.pericles;

import static ep.pericles.AppDate.*;
import static ep.pericles.DateRange.range;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.*;

import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.*;

import com.google.common.collect.*;

import ep.pericles.test.AssertThrowable;
import ep.pericles.type.*;

public class AppDateTest {

  @Test
  public void test_parse() {
    String src = "2012-03-01";
    Date d = parse(src);
    String res = AppDate.formatIfNotNull(d, "yyyy-MM-dd");
    Assert.assertEquals(src, res);
    Assert.assertNotNull(AppDate.parse("2012-11-29 11:00"));
  }

  @Test
  public void between_() {
    Date d = parse("2012-06-05");
    Date from = parse("2012-06-05");
    Date to = parse("2012-06-05");
    Assert.assertTrue(between(d, from, to));
  }

  @Test
  public void test_isCoverWholeDay() {
    Assert.assertTrue(isCoverWholeDay(parse("2012-06-05"), range(parse("2012-06-05"), parse("2012-06-06"))));
    Assert.assertTrue(isCoverWholeDay(parse("2012-06-05 08:08:08"), range(parse("2012-06-05"),
        parse("2012-06-06"))));
    Assert.assertFalse(isCoverWholeDay(parse("2012-06-05"), range(parse("2012-06-05 08:00:00"),
        parse("2012-06-16 17:00:00"))));
    Assert.assertTrue(isCoverWholeDay(parse("2012-06-06"), range(parse("2012-06-05 08:00:00"),
        parse("2012-06-16 17:00:00"))));
  }

  @Test
  public void test_join() {
    Assert.assertTrue(range(parse("2012-06-05"), parse("2012-06-07")).isEqual(join(range(
        parse("2012-06-05"), parse("2012-06-06")), range(parse("2012-06-06"), parse("2012-06-07")))));
    Assert.assertTrue(range(parse("2012-06-05"), parse("2012-06-07")).isEqual(join(range(
        parse("2012-06-06"), parse("2012-06-07")), range(parse("2012-06-05"), parse("2012-06-06")))));
    Assert.assertNull((join(range(parse("2012-06-05"), parse("2012-06-06")), range(parse("2012-06-07"),
        parse("2012-06-08")))));
  }

  @Test
  public void test_isCoverWholeDayCol() {
    Assert.assertTrue(isCoverWholeDay(parse("2012-06-05"), range(parse("2012-06-04 18:00:00"),
        parse("2012-06-05 06:00:00")), range(parse("2012-06-05 18:00:00"), parse("2012-06-06")), range(
        parse("2012-06-05 06:00:00"), parse("2012-06-05 18:00:00"))));
    Assert.assertTrue(isCoverWholeDay(parse("2012-06-05"), range(parse("2012-06-04 18:00:00"),
        parse("2012-06-05 06:00:00")), range(parse("2012-06-04"), parse("2012-06-16")), range(
        parse("2012-06-05 06:00:00"), parse("2012-06-05 18:00:00"))));
    Assert.assertFalse(isCoverWholeDay(parse("2012-06-05"), range(parse("2012-06-04 18:00:00"),
        parse("2012-06-05 06:00:00")), range(parse("2012-06-05 18:00:00"), parse("2012-06-06"))));
  }

  @Test
  public void test_thisMonday() {
    Assert.assertEquals(parse("2012-08-27"), thisMonday(parse("2012-08-28 10:00")));
  }

  @Test
  public void test_mondayOf() {
    Assert.assertEquals(parse("2012-08-27"), mondayOf(2012, 35));
    Assert.assertEquals(parse("2012-01-02"), mondayOf(2012, 1));
    Assert.assertEquals(parse("2013-12-23"), mondayOf(2013, 52));
    Assert.assertEquals(parse("2013-12-30"), mondayOf(2014, 1));

  }

  @Test
  public void test_dayNameOfWeek() {
    Assert.assertEquals("Monday", dayNameOfWeek(parse("2012-09-10")));
  }

  @Test
  public void test_overlaps() {
    DateRange r1 = DateRange.of(parse("2012-12-10"), parse("2012-12-11"));
    DateRange r2 = DateRange.of(parse("2012-12-10 12:00"), parse("2012-12-11"));
    DateRange rO2 = DateRange.of(null, parse("2012-12-11"));
    DateRange r2O = DateRange.of(parse("2012-12-10 12:00"), null);
    DateRange r3 = DateRange.of(parse("2012-12-11"), parse("2012-12-12"));
    DateRange rO3 = DateRange.of(null, parse("2012-12-12"));
    DateRange r3O = DateRange.of(parse("2012-12-11"), null);
    DateRange rOO = DateRange.of(null, null);

    assertTrue(overlaps(r1, r2));
    assertTrue(overlaps(r1, rO2));
    assertTrue(overlaps(r1, r2O));

    assertFalse(overlaps(r1, r3));
    assertTrue(overlaps(r1, rO3));
    assertFalse(overlaps(r1, r3O));

    // Identity
    assertTrue(overlaps(r2, r2));
    assertTrue(overlaps(rO2, rO2));
    assertTrue(overlaps(r2O, r2O));
    assertTrue(overlaps(rOO, rOO));

    // Open both sides
    assertTrue(overlaps(rOO, r1));
    assertTrue(overlaps(rOO, r2));
    assertTrue(overlaps(rOO, rO2));
    assertTrue(overlaps(rOO, r2O));
    assertTrue(overlaps(rOO, r3));
    assertTrue(overlaps(rOO, rO3));
    assertTrue(overlaps(rOO, r3O));

    assertFalse(overlaps(range(parse("2013-10-28 01:00"), parse("2013-10-28 02:00")),
        range(parse("2013-10-28 02:00"), parse("2013-10-28 03:00"))));
  }

  @Test
  public void test_overlaps_Range_local_time() throws Exception {
    DateRange base = DateRange.of(parse("2012-12-11 08:30"), parse("2012-12-11 13:30"));

    Range<LocalTime> range = Range.open(new LocalTime(parse("2012-12-11 08:30")), new LocalTime(parse("2012-12-11 13:30")));
    assertTrue(AppDate.overlaps(base, range));

    range = Range.open(new LocalTime(parse("2012-12-11 08:00")), new LocalTime(parse("2012-12-11 13:38")));
    assertTrue(AppDate.overlaps(base, range));

    range = Range.open(new LocalTime(parse("2012-12-11 08:45")), new LocalTime(parse("2012-12-11 13:00")));
    assertTrue(AppDate.overlaps(base, range));

    range = Range.open(new LocalTime(parse("2012-12-11 08:00")), new LocalTime(parse("2012-12-11 13:00")));
    assertTrue(AppDate.overlaps(base, range));
  }

  @Test
  public void test_overlaps_late_afternoon() throws Exception {
    Range<LocalTime> range = Range.openClosed(new LocalTime(13, 30), new LocalTime(23, 59, 59));
    DateRange base = DateRange.range(parse("2012-12-11 22:00"), parse("2012-12-12 00:30"));
    assertTrue(AppDate.overlaps(base, range));
  }

  @Test
  public void test_overlaps_early_morning() throws Exception {
    Range<LocalTime> range = Range.closedOpen(new LocalTime(0, 0), new LocalTime(8, 30));
    DateRange base = DateRange.range(parse("2012-12-11 22:00"), parse("2012-12-12 00:30"));
    assertTrue(AppDate.overlaps(base, range));
  }

  @Test
  public void test_isSameDay() throws Exception {
    assertFalse(isSameDay(parse("2012-12-11 22:00"), parse("2012-12-12 00:30")));
    assertTrue(isSameDay(parse("2012-12-11 22:00"), parse("2012-12-11 00:30")));
  }

  @Test
  public void test_startbefore() {
    DateRange r1 = DateRange.of(parse("2012-12-10"), parse("2012-12-11"));
    DateRange r2 = DateRange.of(parse("2012-12-10 12:00"), parse("2012-12-11"));
    DateRange rO2 = DateRange.of(null, parse("2012-12-11"));
    DateRange r2O = DateRange.of(parse("2012-12-10 12:00"), null);
    DateRange r3 = DateRange.of(parse("2012-12-11"), parse("2012-12-12"));
    DateRange rO3 = DateRange.of(null, parse("2012-12-12"));
    DateRange r3O = DateRange.of(parse("2012-12-11"), null);
    DateRange rOO = DateRange.of(null, null);

    assertTrue(r1.startsBefore(r2));
    assertFalse(r1.startsBefore(rO2));
    assertTrue(r1.startsBefore(r2O));
    assertTrue(r1.startsBefore(r3));
    assertFalse(r1.startsBefore(rO3));
    assertTrue(r1.startsBefore(r3O));
    assertFalse(r1.startsBefore(rOO));

    assertFalse(r2.startsBefore(r1));
    assertTrue(rO2.startsBefore(r1));
    assertFalse(r2O.startsBefore(r1));
    assertFalse(r3.startsBefore(r1));
    assertTrue(rO3.startsBefore(r1));
    assertFalse(r3O.startsBefore(r1));
    assertTrue(rOO.startsBefore(r1));

    assertFalse(r1.startsBefore(r1));
    assertFalse(r2.startsBefore(r2));
    assertFalse(rO2.startsBefore(rO2));
    assertFalse(r2O.startsBefore(r2O));
    assertFalse(r3.startsBefore(r3));
    assertFalse(rO3.startsBefore(rO3));
    assertFalse(r3O.startsBefore(r3O));
    assertFalse(rOO.startsBefore(rOO));

  }

  @Test
  public void test_endsAfter() {
    DateRange r1 = DateRange.of(parse("2012-12-10"), parse("2012-12-11"));
    DateRange r2 = DateRange.of(parse("2012-12-10 12:00"), parse("2012-12-11"));
    DateRange rO2 = DateRange.of(null, parse("2012-12-11"));
    DateRange r2O = DateRange.of(parse("2012-12-10 12:00"), null);
    DateRange r3 = DateRange.of(parse("2012-12-11"), parse("2012-12-12"));
    DateRange rO3 = DateRange.of(null, parse("2012-12-12"));
    DateRange r3O = DateRange.of(parse("2012-12-11"), null);
    DateRange rOO = DateRange.of(null, null);

    assertFalse(r1.endsAfter(r2));
    assertFalse(r1.endsAfter(rO2));
    assertFalse(r1.endsAfter(r2O));
    assertFalse(r1.endsAfter(r3));
    assertFalse(r1.endsAfter(rO3));
    assertFalse(r1.endsAfter(r3O));
    assertFalse(r1.endsAfter(rOO));

    assertFalse(r2.endsAfter(r1));
    assertFalse(rO2.endsAfter(r1));
    assertTrue(r2O.endsAfter(r1));
    assertTrue(r3.endsAfter(r1));
    assertTrue(rO3.endsAfter(r1));
    assertTrue(r3O.endsAfter(r1));
    assertTrue(rOO.endsAfter(r1));

    assertFalse(r1.endsAfter(r1));
    assertFalse(r2.endsAfter(r2));
    assertFalse(rO2.endsAfter(rO2));
    assertFalse(r2O.endsAfter(r2O));
    assertFalse(r3.endsAfter(r3));
    assertFalse(rO3.endsAfter(rO3));
    assertFalse(r3O.endsAfter(r3O));
    assertFalse(rOO.endsAfter(rOO));
  }

  @Test
  public void dinstinct_interval() throws Exception {
    Collection<DateRange> inputs = AppCollections.List();
    inputs.add(range(parse("2013-04-29 07:00"), parse("2013-04-29 09:00")));
    inputs.add(range(parse("2013-04-29 08:00"), parse("2013-04-29 10:00")));
    inputs.add(range(parse("2013-04-29 09:00"), parse("2013-04-29 12:00")));
    inputs.add(range(parse("2013-04-29 11:00"), parse("2013-04-29 12:00")));
    inputs.add(range(parse("2013-04-29 13:00"), parse("2013-04-29 14:00")));
    inputs.add(range(parse("2013-04-29 07:00"), parse("2013-04-29 14:00")));
    Set<DateRange> expected = AppCollections.Set();
    expected.add(range(parse("2013-04-29 07:00"), parse("2013-04-29 08:00")));
    expected.add(range(parse("2013-04-29 08:00"), parse("2013-04-29 09:00")));
    expected.add(range(parse("2013-04-29 09:00"), parse("2013-04-29 10:00")));
    expected.add(range(parse("2013-04-29 10:00"), parse("2013-04-29 11:00")));
    expected.add(range(parse("2013-04-29 11:00"), parse("2013-04-29 12:00")));
    expected.add(range(parse("2013-04-29 12:00"), parse("2013-04-29 13:00")));
    expected.add(range(parse("2013-04-29 13:00"), parse("2013-04-29 14:00")));
    assertEquals(expected, ImmutableSet.copyOf(AppDate.dinstinctRanges(inputs)));
  }

  @Test
  public void test_isMonday() throws Exception {
    assertTrue(AppDate.isMonday(parse("2013-05-06")));
  }

  @Test
  public void betweenLocalTime() throws Exception {
    Range<LocalTime> range = Range.closedOpen(new LocalTime(8, 30), new LocalTime(13, 30));
    assertTrue(AppDate.between(parse("2013-04-29 13:15"), range));
    assertTrue(AppDate.between(parse("2013-04-29 08:30"), range));
    assertTrue(AppDate.between(parse("2013-04-29 13:29"), range));
    assertFalse(AppDate.between(parse("2013-04-29 13:30"), range));
    Range<LocalTime> range2 = Range.closed(new LocalTime(8, 30), new LocalTime(23, 59, 59));
    assertTrue(AppDate.between(parse("2013-04-29 13:15"), range2));
  }

  @Test
  public void jodaDurationFormat() throws Exception {
    Duration d = new Duration(new DateTime(parse("2013-04-29 08:00")), new DateTime(parse("2013-04-29 09:15")));
    PeriodFormatter daysHoursMinutes = new PeriodFormatterBuilder()
        .appendHours()
        .appendSeparator(":")
        .appendMinutes()
        .toFormatter();
    //System.out.println(daysHoursMinutes.print(d.toPeriod()));
    assertEquals("1:15", daysHoursMinutes.print(d.toPeriod()));
  }

  @Test
  public void TestIsWeekday() throws Exception {
    LocalDate ld = new LocalDate(2013, 5, 20);
    assertTrue(AppDate.isWeekday(EWeekday.MONDAY, ld));
  }

  @Test
  public void beginningOfYear() throws Exception {
    assertEquals(AppDate.midnight(parse("2013-01-01")), AppDate.beginningOfYear(parse("2013-06-03 14:31")));
  }

  @Test
  public void endOfYear() throws Exception {
    assertEquals(AppDate.midnight(parse("2014-01-01")), AppDate.endOfYear(parse("2013-06-03 14:31")));
  }

  @Test
  public void date_equals() throws Exception {
    java.util.Date d1 = parse("2013-06-12 08:00");
    java.sql.Timestamp d2 = new Timestamp(d1.getTime());
    assertTrue(d1.equals(d2));
    assertFalse("!!!", d2.equals(d1));
    assertTrue(AppDate.equals(d1, d2));
    assertTrue(AppDate.equals(d2, d1));
  }

  @Test
  public void complete_days_period() {
    assertCompleteDaysPeriod("18/04/2012 09:00", "18/04/2012 13:00", "18/04/2012 00:00", "19/04/2012 00:00");
    assertCompleteDaysPeriod("18/04/2012 09:00", "19/04/2012 13:00", "18/04/2012 00:00", "20/04/2012 00:00");
    assertCompleteDaysPeriod("18/04/2012 09:00", "19/04/2012 00:00", "18/04/2012 00:00", "19/04/2012 00:00");
    assertCompleteDaysPeriod("18/04/2012 00:00", "18/04/2012 00:00", "18/04/2012 00:00", "19/04/2012 00:00");
  }

  private void assertCompleteDaysPeriod(String from, String to, String adjFrom, String adjTo) {
    DateRange p = DateRange.of(parse(from), parse(to));
    DateRange res = completeDaysPeriod(p);
    assertNotNull(res);
    assertEquals(DateRange.of(parse(adjFrom), parse(adjTo)), res);
  }

  @Test
  public void last_minute_of_day() {
    Date exp = parse("2013-07-29 23:59");
    assertEquals(exp, lastMinuteOfDay(parse("2013-07-29 00:00")));
    assertEquals(exp, lastMinuteOfDay(parse("2013-07-29 23:59")));
    assertEquals(exp, lastMinuteOfDay(parse("2013-07-29 12:00")));
  }
  
  @Test
  public void month_test() {
    Integer exp = 1;
    assertEquals(exp, month(parse("2014-01-29 00:00")));
    assertEquals(exp, month(parse("2014-01-01 00:00")));
    assertEquals(exp, month(parse("2014-01-31 00:00")));
    assertEquals(exp, month(parse("2014-01-31 23:59")));
    exp = 12;
    assertEquals(exp, month(parse("2014-12-31 23:59")));
  }
  
  @Test
  public void dayOfMonth_test() {
    assertEquals(29, dayOfMonth(parse("2014-01-29 00:00")));
    assertEquals(29, dayOfMonth(parse("2014-01-29 10:00")));
    assertEquals(29, dayOfMonth(parse("2014-01-29 23:59")));
    assertEquals(1, dayOfMonth(parse("2014-01-01 00:00")));
    assertEquals(31, dayOfMonth(parse("2014-01-31 00:00")));
  }
  
  @Test
  public void calendarDaysBetween_test() {
    assertEquals(0, calendarDaysBetween(parse("2014-04-24 01:00"), parse("2014-04-24 23:00")));
    assertEquals(1, calendarDaysBetween(parse("2014-04-24 01:00"), parse("2014-04-25 23:00")));
    assertEquals(2, calendarDaysBetween(parse("2014-04-24 01:00"), parse("2014-04-26 23:00")));
    assertEquals(-1, calendarDaysBetween(parse("2014-04-25 01:00"), parse("2014-04-24 23:00")));
    assertEquals(1, calendarDaysBetween(parse("2014-12-31 01:00"), parse("2015-01-01 23:00")));
    assertEquals(1, calendarDaysBetween(parse("2014-12-31 00:00"), parse("2015-01-01 00:00")));
  }

  @Test
  public void calendarDaysBetween_check_test() {
    AssertThrowable.assertThrowable(NullPointerException.class, new Procedure() {
      public void execute() throws Exception {
        calendarDaysBetween(null, parse("2014-04-24 23:00"));
      }
    });
    
    AssertThrowable.assertThrowable(NullPointerException.class, new Procedure() {
      public void execute() throws Exception {
        calendarDaysBetween(parse("2014-04-24 23:00"), null);
      }
    });
    
    AssertThrowable.assertThrowable(NullPointerException.class, new Procedure() {
      public void execute() throws Exception {
        calendarDaysBetween(null, null);
      }
    });
  }
  
}

