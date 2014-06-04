package eu.qualityontime.functionals;

import java.util.*;

import com.google.common.base.*;
import com.google.common.collect.*;

import eu.qualityontime.*;

/**
 * Functional Iterable. Similar wht Java 8 will have in the future.
 * Why not FluentIterable of Guava? Because that is not complete and difficult to extend because if the final methods.
 */
public class FIterable<T> implements Iterable<T> {
  private final Iterable<T> iterable;

  /**
   * `from` and `with` are synonims
   */
  public static <E> FIterable<E> from(final Iterable<E> iterable) {
    return with(iterable);
  }

  public static <E> FIterable<E> from(final E[] values) {
    return with(ImmutableList.copyOf(values));
  }

  public static <E> FIterable<E> FIterable(final E[] values) {
    return with(ImmutableList.copyOf(values));
  }

  public static <E> FIterable<E> FIterable(final Iterable<E> iterable) {
    return with(iterable);
  }

  /**
   * `from` and `with` are synonims
   */
  public static <E> FIterable<E> with(final Iterable<E> iterable) {
    return (iterable instanceof FIterable) ? (FIterable<E>) iterable : new FIterable<E>(iterable);
  }

  protected FIterable(Iterable<T> iterable) {
    this.iterable = iterable;
  }

  @Override
  public Iterator<T> iterator() {
    return iterable.iterator();
  }

  public List<T> asList() {
    return ImmutableList.copyOf(this.iterable);
  }

  public Set<T> asSet() {
    return ImmutableSet.copyOf(this.iterable);
  }

  /**
   * synonim of transform
   */
  public <O> FIterable<O> map(Function<? super T, O> transformer) {
    return with(Iterables.transform(this.iterable, transformer));
  }

  public <O> FIterable<O> flatMap(Function<? super T, ? extends Iterable<O>> transformer) {
    return with(Iterables.concat(with(Iterables.transform(this.iterable, transformer))));
  }

  /**
   * synonim of transform
   */
  public <O> FIterable<O> collect(Function<T, O> transformer) {
    return map(transformer);
  }

  public <O> FIterable<O> transform(Function<T, O> transformer) {
    return map(transformer);
  }

  public FIterable<T> filter(Predicate<T> predicate) {
    return with(Iterables.filter(this.iterable, predicate));
  }

  public FIterable<T> select(Predicate<T> predicate) {
    return filter(predicate);
  }

  public <O> FMultimap<O, T> groupBy(Function<T, O> keyFunctions) {
    return FMultimap.FMultimap(Multimaps.index(this.iterable, keyFunctions));
  }

  public FIterable<T> sort(Comparator<T> comparator) {
    return with(Ordering.from(comparator).immutableSortedCopy(this.iterable));
  }

  public FIterable<T> sort(Ordering<T> comparator) {
    return with(comparator.immutableSortedCopy(this.iterable));
  }

  public FIterable<T> concat(Iterable<? extends T> a) {
    return with(Iterables.concat(this.iterable, a));
  }

  public boolean isEmpty() {
    return Iterables.isEmpty(this.iterable);
  }

  public boolean any(Predicate<? super T> predicate) {
    return Iterables.any(this.iterable, predicate);
  }

  public <K> ImmutableMap<K, T> asMap(Function<? super T, K> keyFunction) {
    return Maps.uniqueIndex(this.iterable, keyFunction);
  }

  @Override
  public String toString() {
    return String.valueOf(this.iterable);
  }

  /**
   * Like oracle `ORDER BY com, comp2`.
   * Order by `comperator`and within it is ordering by `comperator2`
   */
  public FIterable<T> sort(Function<T, ? extends Comparable<?>> comparator, Function<T, ? extends Comparable<?>> comparator2) {
    Ordering<T> primary = Ordering.natural().onResultOf(comparator);
    Ordering<T> secondary = Ordering.natural().onResultOf(comparator2);
    Ordering<T> compond = primary.compound(secondary);
    return with(compond.immutableSortedCopy(this.iterable));
  }

  public FIterable<T> sortBy(Function<? super T, ? extends Comparable<?>> fieldExtract) {
    Ordering<? super T> primary = Ordering.natural().onResultOf(fieldExtract);
    return with(primary.immutableSortedCopy(this.iterable));
  }

  public String join(String on) {
    return Joiner.on(on).join(this.map(Functions.toStringFunction()));
  }

  /**
   * Unsafe cast. But untill java Generic is not becomming more inteligent... Like Scala generics
   */
  public <N> FIterable<N> cast(Class<N> clazz) {
    return AppObjects.cast((Object) this);
  }

  public T[] asArray() {
    T[] array = AppObjects.cast(new Object[this.size()]);
    return asList().toArray(array);
  }

  public int size() {
    return Iterables.size(this.iterable);
  }

  public <A> FIterable<A> pluck(String attribute, Class<A> clazz) {
    Function<T, A> fieldExtractor = AppFunction.byProp(attribute, clazz);
    return this.map(fieldExtractor);
  }
}
