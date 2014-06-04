package eu.qualityontime.timeline;

import static eu.qualityontime.AppDate.date;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import eu.qualityontime.*;

public class TimelineHelperTest extends ASpringToolboxTest {
  /*
  QUARTER_HOUR(1, 96),
  HALF_HOUR(2, 48),
  ONE_HOUR(3, 24),
  HALF_DAY(4, 2),
  WC_SLOT(5, 3);
  */

  @Test
  public void testResolve1DayUnit() {
    Date start = AppDate.date("01/01/2013");
    Date end = AppDate.date("01/01/2013");

    assertEquals(ETimelineUnit.HALF_DAY, TimelineHelper.resolveUnit(start, end, 1));
    assertEquals(ETimelineUnit.HALF_DAY, TimelineHelper.resolveUnit(start, end, 2));
    assertEquals(ETimelineUnit.WC_SLOT, TimelineHelper.resolveUnit(start, end, 3));
    assertEquals(ETimelineUnit.WC_SLOT, TimelineHelper.resolveUnit(start, end, 4));
    assertEquals(ETimelineUnit.WC_SLOT, TimelineHelper.resolveUnit(start, end, 13));
    assertEquals(ETimelineUnit.ONE_HOUR, TimelineHelper.resolveUnit(start, end, 14));
    assertEquals(ETimelineUnit.ONE_HOUR, TimelineHelper.resolveUnit(start, end, 24));
    assertEquals(ETimelineUnit.HALF_HOUR, TimelineHelper.resolveUnit(start, end, 36));
    assertEquals(ETimelineUnit.HALF_HOUR, TimelineHelper.resolveUnit(start, end, 36));
    assertEquals(ETimelineUnit.HALF_HOUR, TimelineHelper.resolveUnit(start, end, 48));
    assertEquals(ETimelineUnit.QUARTER_HOUR, TimelineHelper.resolveUnit(start, end, 72));
    assertEquals(ETimelineUnit.QUARTER_HOUR, TimelineHelper.resolveUnit(start, end, 73));
    assertEquals(ETimelineUnit.QUARTER_HOUR, TimelineHelper.resolveUnit(start, end, 96));
    assertEquals(ETimelineUnit.QUARTER_HOUR, TimelineHelper.resolveUnit(start, end, 97));
  }

  @Test
  public void testResolve2DaysUnit() {
    Date start = AppDate.date("01/01/2013");
    Date end = AppDate.date("02/01/2013");

    assertEquals(ETimelineUnit.WC_SLOT, TimelineHelper.resolveUnit(start, end, 6));
    assertEquals(ETimelineUnit.HALF_DAY, TimelineHelper.resolveUnit(start, end, 5));
    assertEquals(ETimelineUnit.HALF_DAY, TimelineHelper.resolveUnit(start, end, 4));
    assertEquals(ETimelineUnit.HALF_DAY, TimelineHelper.resolveUnit(start, end, 2));
    assertEquals(ETimelineUnit.HALF_DAY, TimelineHelper.resolveUnit(start, end, 1));
  }

  @Test
  public void testActualEnd() {

    ETimelineUnit expectedLargest = ETimelineUnit.HALF_DAY;

    assertEquals(expectedLargest, ETimelineUnit.getLargestUnit());

    int maxDays = TimelineHelper.MAX_TIMELINE_ITEMS / expectedLargest.getUnitsPerDays();
    assertEquals(50, maxDays);

    assertActualEnd(date("01/01/2013"), date("01/01/2013"), date("01/01/2013"));
    assertActualEnd(date("01/01/2013"), date("19/02/2013"), date("19/02/2013"));
    assertActualEnd(date("01/01/2013"), date("20/02/2013"), date("19/02/2013"));
  }

  private void assertActualEnd(Date start, Date end, Date expectedEnd) {
    Date actualEnd = TimelineHelper.getActualEnd(start, end);
    assertNotNull(actualEnd);
    assertEquals(expectedEnd, actualEnd);
  }

}
