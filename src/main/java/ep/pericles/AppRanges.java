package ep.pericles;

import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.*;
import static ep.pericles.AppCollections.List;

import java.util.*;

import org.slf4j.*;

import com.google.common.base.*;
import com.google.common.collect.*;

import ep.pericles.type.*;

public class AppRanges {
  private static final Logger log = LoggerFactory.getLogger(AppRanges.class);

  /**
   * Assuming closedOpen ranges.
   * 
   * @return closedOpen non intersectioning ranges.
   */
  public static <C extends Comparable<C>> Collection<Range<C>> dinstinctRanges(Iterable<Range<C>> input) {
    Preconditions.checkNotNull(input);
    if (Iterables.isEmpty(input) || 1 == Iterables.size(input)) {
      return ImmutableSet.copyOf(input);
    }
    List<Range<C>> collector = List(Iterables.size(input));
    doRanges(collector, ImmutableSet.copyOf(input));
    Set<Range<C>> res = ImmutableSet.copyOf(collector);
    log.trace("Result: {}", res);
    return res;
  }

  public static <C extends Comparable<C>> Collection<Range<C>> dinstinctRangesOfRangeables(
      Iterable<? extends IRangeable<C>> input) {
    Function<IRangeable<C>, Range<C>> to_range = Rangeables.TO_CLOSED_OPEN_RANGE();
    Iterable<Range<C>> in = Iterables.transform(input, to_range);
    Collection<Range<C>> dinstinctRanges = dinstinctRanges(in);
    return dinstinctRanges;
  }

  private static <C extends Comparable<C>> void doRanges(List<Range<C>> collector, Iterable<Range<C>> input) {
    List<Range<C>> sorted = Rangeables.RANGE_ORDERING.sortedCopy(ImmutableSet.copyOf(input));
    log.trace("Params: {} # {}", collector, sorted);
    if (ImmutableList.of(0, 1).contains(sorted.size())) {
      collector.addAll(sorted);
      return;
    }
    Range<C> first = sorted.get(0);
    Range<C> second = sorted.get(1);
    if (first.isConnected(second)) {
      Range<C> inters = first.intersection(second);
      if (inters.isEmpty()) {
        collector.add(first);
        doRanges(collector, ImmutableList.copyOf(Iterables.skip(sorted, 1)));
      }
      else {
        Set<Range<C>> newRanges = null;
        if (first.encloses(second)) {
          newRanges = ImmutableSet.of(
              Range.closedOpen(first.lowerEndpoint(), second.lowerEndpoint())
              , second
              , Range.closedOpen(second.upperEndpoint(), first.upperEndpoint()));
        }
        else {
          newRanges = ImmutableSet.of(
              Range.closedOpen(first.lowerEndpoint(), second.lowerEndpoint())
              , Range.closedOpen(second.lowerEndpoint(), first.upperEndpoint())
              , Range.closedOpen(first.upperEndpoint(), second.upperEndpoint()));

        }
        newRanges = Sets.filter(newRanges, not(Rangeables.EMPTY_RANGE));
        doRanges(collector, ImmutableList.copyOf(concat(newRanges, skip(sorted, 2))));
      }
    }
    else {
      collector.add(first);
      doRanges(collector, ImmutableList.copyOf(Iterables.skip(sorted, 1)));
    }
  }

  public static <O extends IRangeable<C>, C extends Comparable<C>> ListMultimap<Range<C>, O> groupByDinstinctRanges(
      Collection<O> input) {
    Function<IRangeable<C>, Range<C>> to_range = Rangeables.TO_CLOSED_OPEN_RANGE();
    Collection<Range<C>> dinstinctRanges = dinstinctRanges(Collections2.transform(input, to_range));
    ListMultimap<Range<C>, O> res = groupByDinstinctRanges(dinstinctRanges, input);
    return res;
  }

  public static <O extends IRangeable<C>, C extends Comparable<C>> ListMultimap<Range<C>, O> groupByDinstinctRanges(
      Collection<Range<C>> dinstinctRanges, Collection<O> input) {
    return groupByDinstinctRanges(dinstinctRanges, input, false);
  }

  public static <O extends IRangeable<C>, C extends Comparable<C>> ListMultimap<Range<C>, O> groupByDinstinctRanges(
      Collection<Range<C>> dinstinctRanges, Collection<O> input, Boolean nullGroup) {
    Function<IRangeable<C>, Range<C>> to_range = Rangeables.TO_CLOSED_OPEN_RANGE();
    ListMultimap<Range<C>, O> res = ArrayListMultimap.create();
    for (Range<C> dr : dinstinctRanges) {
      boolean found = false;
      for (O rr : input) {
        Range<C> cr = to_range.apply(rr);
        if (dr.isConnected(cr) && !dr.intersection(cr).isEmpty()) {
          res.put(dr, rr);
          found = true;
        }
      }
      if (nullGroup && !found) {
        res.put(dr, null);
      }
    }
    return res;
  }
}
