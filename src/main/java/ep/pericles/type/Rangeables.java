package ep.pericles.type;

import com.google.common.base.*;
import com.google.common.collect.*;

public class Rangeables {
  public static <C extends Comparable<C>> Function<IRangeable<C>, Range<C>> TO_CLOSED_OPEN_RANGE() {
    return new Function<IRangeable<C>, Range<C>>() {
      public Range<C> apply(IRangeable<C> input) {
        return Range.closedOpen(input.lowerEndpoint(), input.upperEndpoint());
      }
    };
  }

  public static <C extends Comparable<C>> IRangeable<C> rangeableBy(Range<C> input) {
    return new RangeBackedRangeable<C>(input);
  }

  private static class RangeBackedRangeable<C extends Comparable<C>> implements IRangeable<C> {
    private final Range<C> value;

    public RangeBackedRangeable(Range<C> value) {
      this.value = value;
    }

    @Override
    public C lowerEndpoint() {
      return value.lowerEndpoint();
    }

    @Override
    public C upperEndpoint() {
      return value.upperEndpoint();
    }

    @Override
    public String toString() {
      return value.toString();
    }

    @Override
    public boolean equals(Object obj) {
      return value.equals(((RangeBackedRangeable<?>) obj).value);
    }

  }

  public static final Ordering<IRangeable<?>> RANGEABLE_ORDERING = new Ordering<IRangeable<?>>() {
    @Override
    public int compare(IRangeable<?> left, IRangeable<?> right) {
      if (left.lowerEndpoint().compareTo(right.lowerEndpoint()) == 0) {
        return right.upperEndpoint().compareTo(left.upperEndpoint());
      }
      return left.lowerEndpoint().compareTo(right.lowerEndpoint());
    }
  };

  public static final Ordering<Range<?>> RANGE_ORDERING = new Ordering<Range<?>>() {
    @Override
    public int compare(Range<?> left, Range<?> right) {
      if (left.lowerEndpoint().compareTo(right.lowerEndpoint()) == 0) {
        return right.upperEndpoint().compareTo(left.upperEndpoint());
      }
      return left.lowerEndpoint().compareTo(right.lowerEndpoint());
    }
  };

  public static final Predicate<Range<?>> EMPTY_RANGE = new Predicate<Range<?>>() {
    public boolean apply(Range<?> input) {
      return input.isEmpty();
    }
  };
}
