package ep.pericles;

import static ep.pericles.AppDate.parse;
import static ep.pericles.DateRange.range;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.google.common.collect.Range;

public class GuavaRangeTest {

  @Test
  public void date_range() {
    DateRange a = range(parse("2012-01-01 10:00:00"), parse("2012-01-01 11:30:00"));
    DateRange a_overlap = range(parse("2012-01-01 11:25:00"), parse("2012-01-01 11:35:00"));
    DateRange b = range(parse("2012-01-01 11:30:00"), parse("2012-01-01 12:00:00"));
    DateRange b_inclose = range(parse("2012-01-01 11:45:00"), parse("2012-01-01 12:00:00"));

    Range<Date> r1 = Range.closedOpen(a.getFrom(), a.getTo());
    Range<Date> r1_overlap = Range.closedOpen(a_overlap.getFrom(), a_overlap.getTo());
    Range<Date> r2 = Range.closedOpen(b.getFrom(), b.getTo());
    Range<Date> r2_inclose = Range.closedOpen(b_inclose.getFrom(), b_inclose.getTo());

    assertTrue(r1.contains(parse("2012-01-01 11:29:59")));
    assertFalse(r1.contains(parse("2012-01-01 11:30:00")));
    assertTrue(r1.isConnected(r2));
    assertFalse(r1.encloses(r2));
    assertFalse(r2.encloses(r1));

    assertTrue(r1.isConnected(r1_overlap));
    assertFalse(r1.encloses(r1_overlap));

    assertTrue(r2.encloses(r2_inclose));
    assertFalse(r2_inclose.encloses(r2));
  }
}
