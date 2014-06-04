package ep.pericles;

import java.util.*;

import com.google.common.base.Function;
import com.google.common.collect.*;

/**
 * Utility when you want to build big SQL IN close but it is limited. So you can split then perform and join finally
 */
public class SplitAndJoin {
  public static <T> List<T> split_and_join(Collection<Long> ids, int max, Function<Collection<Long>, List<T>> op) {
    if (ids.size() > max) {
      Iterable<T> init = ImmutableList.of();
      for (List<Long> partIds : Iterables.partition(ids, max)) {
        init = Iterables.concat(init, op.apply(partIds));
      }
      return ImmutableList.copyOf(init);
    }
    return op.apply(ids);
  }
}
