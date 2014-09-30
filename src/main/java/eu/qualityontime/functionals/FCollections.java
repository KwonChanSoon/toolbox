package eu.qualityontime.functionals;


/**
 * facade for factory methods o fcollections to simplify when writing.
 */
public class FCollections {
  public static <E> FIterable<E> FIterable(final E[] values) {
    return FIterable.FIterable(values);
  }

  public static <E> FIterable<E> FIterable(final Iterable<E> iterable) {
    return FIterable.FIterable(iterable);
  }

  public static <T> FSet<T> FSet(final Iterable<T> in) {
    return FSet.FSet(in);
  }
}
