package eu.qualityontime.timeline;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import eu.qualityontime.*;

public class TimelineTest extends ASpringToolboxTest {

  @Test
  public void testDailyGroupingHalfDay() {
    TimelineGenerator g = new TimelineGenerator(new TimelineFactory());
    Date start = AppDate.date("2012-12-12");
    Date end = AppDate.date("2012-12-14");
    Timeline t = g.generateTimeline(start, end, ETimelineUnit.HALF_DAY);

    List<TimelineItemGroup> groups = t.getDailyGroups();

    assertNotNull(groups);
    assertEquals(2, groups.size());

    assertNotNull(groups.get(0));
    assertNotNull(groups.get(0).getItems());
    assertEquals(2, groups.get(0).getItems().size());

    assertNotNull(groups.get(0).getCode());
    assertEquals("12/12/2012", groups.get(0).getCode());
    assertNotNull(groups.get(1));
    assertNotNull(groups.get(1).getCode());
    assertEquals("13/12/2012", groups.get(1).getCode());

  }

  @Test
  public void testDailyGroupingWC() {
    TimelineGenerator g = new TimelineGenerator(new TimelineFactory());
    Date start = AppDate.date("2012-12-12");
    Date end = AppDate.date("2012-12-14");
    Timeline t = g.generateTimeline(start, end, ETimelineUnit.WC_SLOT);

    List<TimelineItemGroup> groups = t.getDailyGroups();

    assertNotNull(groups);
    assertEquals(2, groups.size());

    assertNotNull(groups.get(0));
    assertNotNull(groups.get(0).getItems());
    assertEquals(3, groups.get(0).getItems().size());

  }

  @Test
  public void testDailyGroupingOneHour() {
    TimelineGenerator g = new TimelineGenerator(new TimelineFactory());
    Date start = AppDate.date("2012-12-12");
    Date end = AppDate.date("2012-12-14");
    Timeline t = g.generateTimeline(start, end, ETimelineUnit.ONE_HOUR);

    List<TimelineItemGroup> groups = t.getDailyGroups();

    assertNotNull(groups);
    assertEquals(2, groups.size());

    assertNotNull(groups.get(0));
    assertNotNull(groups.get(0).getItems());
    assertEquals(24, groups.get(0).getItems().size());

  }

  @Test
  public void testDailyGroupingHalfHour() {
    TimelineGenerator g = new TimelineGenerator(new TimelineFactory());
    Date start = AppDate.date("2012-12-12");
    Date end = AppDate.date("2012-12-14");
    Timeline t = g.generateTimeline(start, end, ETimelineUnit.HALF_HOUR);

    List<TimelineItemGroup> groups = t.getDailyGroups();

    assertNotNull(groups);
    assertEquals(2, groups.size());

    assertNotNull(groups.get(0));
    assertNotNull(groups.get(0).getItems());
    assertEquals(48, groups.get(0).getItems().size());

  }

  @Test
  public void testDailyGroupingQuarterHour() {
    TimelineGenerator g = new TimelineGenerator(new TimelineFactory());
    Date start = AppDate.date("2012-12-12");
    Date end = AppDate.date("2012-12-14");
    Timeline t = g.generateTimeline(start, end, ETimelineUnit.QUARTER_HOUR);

    List<TimelineItemGroup> groups = t.getDailyGroups();

    assertNotNull(groups);
    assertEquals(2, groups.size());

    assertNotNull(groups.get(0));
    assertNotNull(groups.get(0).getItems());
    assertEquals(96, groups.get(0).getItems().size());

  }
}
