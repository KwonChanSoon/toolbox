package ep.pericles;

import static com.google.common.collect.Range.closedOpen;
import static ep.pericles.AppCollections.List;
import static ep.pericles.AppRanges.dinstinctRanges;
import static ep.pericles.type.Rangeables.*;
import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.*;

import com.google.common.base.Function;
import com.google.common.collect.*;

import ep.pericles.test.AssertThrowable;
import ep.pericles.type.*;

public class AppRangesTest {

  @Test
  public void null_input() {
    AssertThrowable.assertThrowable(NullPointerException.class, new Procedure() {
      public void execute() throws Exception {
        dinstinctRanges((List<Range<Integer>>) null);
      }
    });
  }

  @Test
  public void empty_input() throws Exception {
    Assert.assertTrue(0 == dinstinctRanges(ImmutableList.<Range<Integer>> of()).size());
  }

  @Test
  public void single_item() throws Exception {
    Range<Integer> r = Range.closedOpen(0, 10);
    assertEquals(ImmutableSet.of(r), dinstinctRanges(ImmutableList.of(r)));
  }

  @Test
  public void already_dinstinct_ranges() throws Exception {
    List<Range<Integer>> rss = ImmutableList.of(Range.closedOpen(0, 10), Range.closedOpen(11, 20),
        Range.closedOpen(21, 30));
    Collection<Range<Integer>> expected = ImmutableSet.copyOf(rss);
    assertEquals(expected, dinstinctRanges(rss));
  }

  @Test
  public void simple_joining() throws Exception {
    List<Range<Integer>> rss = ImmutableList.of(closedOpen(0, 10), closedOpen(10, 20));
    Collection<Range<Integer>> expected = ImmutableSet.copyOf(rss);
    assertEquals(expected, dinstinctRanges(rss));
  }

  @Test
  public void several_joining() throws Exception {
    List<Range<Integer>> rss = ImmutableList.of(closedOpen(0, 10), closedOpen(10, 20),
        closedOpen(10, 20), closedOpen(-5, 0));
    Collection<Range<Integer>> expected = ImmutableSet.copyOf(rss);
    assertEquals(expected, dinstinctRanges(rss));
  }

  @Test
  public void simple_overlap() throws Exception {
    List<Range<Integer>> rss = ImmutableList.of(closedOpen(0, 10), closedOpen(5, 15));
    Collection<Range<Integer>> expected = ImmutableSet.of(closedOpen(0, 5), closedOpen(5, 10), closedOpen(10, 15));
    assertEquals(expected, dinstinctRanges(rss));
  }

  @Test
  public void full_inclusion() throws Exception {
    List<Range<Integer>> rss = ImmutableList.of(closedOpen(0, 10), closedOpen(3, 7));
    Collection<Range<Integer>> expected = ImmutableSet.of(closedOpen(0, 3), closedOpen(3, 7), closedOpen(7, 10));
    assertEquals(expected, dinstinctRanges(rss));
  }

  @Test
  public void inclusion_upperEndpointSame() throws Exception {
    List<Range<Integer>> rss = ImmutableList.of(closedOpen(0, 10), closedOpen(3, 10));
    Collection<Range<Integer>> expected = ImmutableSet.of(closedOpen(0, 3), closedOpen(3, 10));
    assertEquals(expected, dinstinctRanges(rss));
  }

  @Test
  public void inclusion_lowerEndpointSame() throws Exception {
    List<Range<Integer>> rss = ImmutableList.of(closedOpen(0, 10), closedOpen(0, 3));
    Collection<Range<Integer>> expected = ImmutableSet.of(closedOpen(0, 3), closedOpen(3, 10));
    assertEquals(expected, dinstinctRanges(rss));
  }

  @Test
  public void complex_example() throws Exception {
    List<Range<Integer>> rss = List();
    rss.add(closedOpen(7, 9));
    rss.add(closedOpen(8, 10));
    rss.add(closedOpen(9, 12));
    rss.add(closedOpen(11, 12));
    rss.add(closedOpen(13, 14));
    rss.add(closedOpen(7, 14));
    Collection<Range<Integer>> expected = AppCollections.List();
    expected.add(closedOpen(7, 8));
    expected.add(closedOpen(8, 9));
    expected.add(closedOpen(9, 10));
    expected.add(closedOpen(10, 11));
    expected.add(closedOpen(11, 12));
    expected.add(closedOpen(12, 13));
    expected.add(closedOpen(13, 14));
    assertEquals(ImmutableSet.copyOf(expected), ImmutableSet.copyOf(dinstinctRanges(rss)));
  }

  @Test
  public void groupByDinstinctRanges_complex_example() throws Exception {
    List<Range<Integer>> rss = List();
    rss.add(closedOpen(7, 9));
    rss.add(closedOpen(8, 10));
    rss.add(closedOpen(9, 12));
    rss.add(closedOpen(11, 12));
    rss.add(closedOpen(13, 14));
    rss.add(closedOpen(7, 14));
    List<IRangeable<Integer>> input = Lists.transform(rss, new Function<Range<Integer>, IRangeable<Integer>>() {
      public IRangeable<Integer> apply(Range<Integer> input) {
        return rangeableBy(input);
      }
    });
    ListMultimap<Range<Integer>, IRangeable<Integer>> expected = ArrayListMultimap.create();
    expected.put(closedOpen(7, 8), rangeableBy(closedOpen(7, 14)));
    expected.put(closedOpen(7, 8), rangeableBy(closedOpen(7, 9)));
    expected.put(closedOpen(8, 9), rangeableBy(closedOpen(7, 14)));
    expected.put(closedOpen(8, 9), rangeableBy(closedOpen(7, 9)));
    expected.put(closedOpen(8, 9), rangeableBy(closedOpen(8, 10)));
    expected.put(closedOpen(9, 10), rangeableBy(closedOpen(7, 14)));
    expected.put(closedOpen(9, 10), rangeableBy(closedOpen(8, 10)));
    expected.put(closedOpen(9, 10), rangeableBy(closedOpen(9, 12)));
    expected.put(closedOpen(10, 11), rangeableBy(closedOpen(7, 14)));
    expected.put(closedOpen(10, 11), rangeableBy(closedOpen(9, 12)));
    expected.put(closedOpen(11, 12), rangeableBy(closedOpen(7, 14)));
    expected.put(closedOpen(11, 12), rangeableBy(closedOpen(9, 12)));
    expected.put(closedOpen(11, 12), rangeableBy(closedOpen(11, 12)));
    expected.put(closedOpen(12, 13), rangeableBy(closedOpen(7, 14)));
    expected.put(closedOpen(13, 14), rangeableBy(closedOpen(7, 14)));
    expected.put(closedOpen(13, 14), rangeableBy(closedOpen(13, 14)));
    ListMultimap<Range<Integer>, IRangeable<Integer>> actual = AppRanges.groupByDinstinctRanges(input);
    assertEquals(expected.keySet().size(), actual.keySet().size());
    for (Range<Integer> k : actual.keySet()) {
      //System.out.println(k);
      List<IRangeable<Integer>> expectedL = RANGEABLE_ORDERING.sortedCopy(expected.get(k));
      List<IRangeable<Integer>> actualL = RANGEABLE_ORDERING.sortedCopy(actual.get(k));
      //System.out.println(expectedL);
      //System.out.println(actualL);
      assertEquals(expectedL.size(), actualL.size());
      assertEquals(expectedL, actualL);
    }
  }
}
