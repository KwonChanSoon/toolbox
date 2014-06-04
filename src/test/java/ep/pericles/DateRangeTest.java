package ep.pericles;

import static ep.pericles.AppDate.parse;
import static ep.pericles.DateRange.range;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.*;

import org.joda.time.Duration;
import org.junit.Test;

import com.google.common.collect.*;

public class DateRangeTest {

  @Test
  public void test_duration() {
    assertEquals("01:29", DateRange.of(parse("2012-01-01 10:00:00"), parse("2012-01-01 11:29:00")).duration());
    assertEquals("13:29", DateRange.of(parse("2012-01-01 10:00:00"), parse("2012-01-01 23:29:00")).duration());
    assertEquals("37:29", DateRange.of(parse("2012-01-01 10:00:00"), parse("2012-01-02 23:29:00")).duration());
    assertEquals("37:09", DateRange.of(parse("2012-01-01 10:00:00"), parse("2012-01-02 23:09:00")).duration());
  }

  @Test
  public void test_compare() {
    DateRange a = DateRange.of(parse("2012-01-01 10:00:00"), parse("2012-01-01 11:29:00"));
    DateRange a2 = DateRange.of(parse("2012-01-01 10:00:00"), parse("2012-01-01 11:29:00"));
    DateRange b = DateRange.of(parse("2012-01-01 10:01:00"), parse("2012-01-01 11:29:00"));
    DateRange c = DateRange.of(parse("2012-01-01 10:00:00"), parse("2012-01-01 11:30:00"));
    assertEquals(0, DateRange.DATE_RANGE_ORDERING.compare(a, a));
    assertEquals(0, DateRange.DATE_RANGE_ORDERING.compare(a, a2));

    assertTrue(0 > DateRange.DATE_RANGE_ORDERING.compare(a, b));
    assertTrue(0 > DateRange.DATE_RANGE_ORDERING.compare(a, c));
  }

  @Test
  public void timestamp_equals() {
    Date d1 = parse("2012-01-01 11:00:00");
    Date d2 = parse("2012-01-01 11:30:00");
    DateRange withPlainDate = DateRange.of(d1, d2);
    DateRange withTimestamp = DateRange.of(new Timestamp(d1.getTime()), new Timestamp(d2.getTime()));
    assertTrue(withPlainDate.equals(withTimestamp));
    assertTrue(withTimestamp.equals(withPlainDate));
  }

  @Test
  public void eliminateInclusiveRanges_single() {
    DateRange a = range(parse("2012-01-01 10:00:00"), parse("2012-01-01 11:29:00"));
    List<DateRange> ranges = ImmutableList.of(a);

    List<DateRange> actual = ImmutableList.copyOf(DateRange.eliminateInclusizeRanges(ranges));
    List<DateRange> expected = ImmutableList.of(a);
    assertEquals(expected, actual);
  }

  @Test
  public void eliminateInclusiveRanges_Two_non_intersection() {
    DateRange a = range(parse("2012-01-01 10:00:00"), parse("2012-01-01 11:30:00"));
    DateRange b = range(parse("2012-01-01 11:30:00"), parse("2012-01-01 12:00:00"));
    List<DateRange> ranges = ImmutableList.of(a, b);

    Set<DateRange> actual = ImmutableSet.copyOf(DateRange.eliminateInclusizeRanges(ranges));
    Set<DateRange> expected = ImmutableSet.of(a, b);
    assertEquals(expected, actual);
  }

  @Test
  public void eliminateInclusiveRanges_Multiple_non_intersection() {
    DateRange a = range(parse("2012-01-01 10:00:00"), parse("2012-01-01 11:30:00"));
    DateRange b = range(parse("2012-01-01 11:30:00"), parse("2012-01-01 12:00:00"));
    DateRange c = range(parse("2012-01-01 12:00:00"), parse("2012-01-01 13:00:00"));
    List<DateRange> ranges = ImmutableList.of(a, b, c);

    Set<DateRange> actual = ImmutableSet.copyOf(DateRange.eliminateInclusizeRanges(ranges));
    Set<DateRange> expected = ImmutableSet.of(a, b, c);
    assertEquals(expected, actual);
  }

  @Test
  public void eliminateInclusiveRanges_partial_assigned() {
    DateRange a = range(parse("2012-01-01 10:00:00"), parse("2012-01-01 11:30:00"));
    DateRange b = range(parse("2012-01-01 11:00:00"), parse("2012-01-01 11:30:00"));
    DateRange c = range(parse("2012-01-01 11:30:00"), parse("2012-01-01 13:00:00"));
    List<DateRange> ranges = ImmutableList.of(a, b, c);

    Set<DateRange> actual = ImmutableSet.copyOf(DateRange.eliminateInclusizeRanges(ranges));
    Set<DateRange> expected = ImmutableSet.of(a, c);
    assertEquals(expected, actual);
  }

  @Test
  public void getDuration() throws Exception {
    Duration d = range(parse("2012-01-01 10:00:00"), parse("2012-01-01 11:30:00")).getDuration();
    assertEquals(90L, d.getStandardMinutes());
  }

  @Test
  public void containsADate() throws Exception {
    DateRange range = range(parse("2012-01-01 10:00:00"), parse("2012-01-01 11:30:00"));
    assertTrue(range.contains(parse("2012-01-01 10:00:00")));
    assertFalse(range.contains(parse("2012-01-01 11:30:00")));
    assertTrue(range.contains(parse("2012-01-01 11:00:00")));
    assertFalse(range.contains(parse("2012-01-01 11:45:00")));
  }
}
