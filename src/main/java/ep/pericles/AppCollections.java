package ep.pericles;

import static ep.pericles.AppObjects.cast;

import java.util.*;
import java.util.Map.Entry;

import com.google.common.base.*;
import com.google.common.collect.*;

import ep.pericles.tuple.Pair;

public class AppCollections {
  public static <K, V> Map<K, V> toMap(Pair<K, V>... kvs) {
    ImmutableMap.Builder<K, V> b = ImmutableMap.builder();
    for (Pair<K, V> p : kvs) {
      b.put(p);
    }
    return b.build();
  }

  public static <K, V> Map<K, V> toMap(Collection<Pair<K, V>> kvs) {
    ImmutableMap.Builder<K, V> b = ImmutableMap.builder();
    for (Pair<K, V> p : kvs) {
      b.put(p);
    }
    return b.build();
  }

  public static <E> List<E> List() {
    return new ArrayList<E>();
  }

  public static <E> List<E> IList() {
    return ImmutableList.of();
  }

  public static <E> List<E> EmptyIList(Class<E> type) {
    return ImmutableList.of();
  }

  public static <E> List<E> List(int initialSize) {
    return new ArrayList<E>(initialSize);
  }

  public static <E> List<E> List(Collection<E> c) {
    return new ArrayList<E>(c);
  }

  public static <E> List<E> safeList(E... c) {
    return c != null ? new ArrayList<E>(Arrays.asList(c)) : null;
  }

  public static <E> List<E> List(E... c) {
    return new ArrayList<E>(Arrays.asList(c));
  }

  public static <E> List<E> IList(E... c) {
    return ImmutableList.copyOf(c);
  }

  /**
   * TODO MAYBE Guava Iterables.partition
   */
  public static <E> List<List<E>> toSubLists(List<E> aList, int size) {
    List<List<E>> subLists = List();
    for (int i = 0; i < Math.ceil(((double) aList.size()) / size); i++) {
      int toIndex = Math.min((i + 1) * size, aList.size());
      subLists.add(aList.subList(i * size, toIndex));
    }
    return subLists;
  }

  public static <K, V> Map<K, V> Map() {
    return new HashMap<K, V>();
  }

  public static <K, V> Map<K, V> IMap() {
    return ImmutableMap.of();
  }

  public static <K, V> Map<K, V> EmptyIMap(Class<K> keyType, Class<V> valueType) {
    return ImmutableMap.of();
  }

  @SuppressWarnings("unchecked")
  public static <K, V> Map<K, V> IMap(K k1, V v1, Object... kvPairs) {
    Preconditions.checkArgument(kvPairs.length % 2 == 0, "even nr of arguments only");
    ImmutableMap.Builder<K, V> b = new ImmutableMap.Builder<K, V>();
    b.put(k1, v1);
    for (int i = 0; i + 1 < kvPairs.length; i += 2) {
      b.put((K) kvPairs[i], (V) kvPairs[i + 1]);
    }
    return b.build();
  }

  @SuppressWarnings("unchecked")
  public static <K, V> Map<K, V> Map(K k1, V v1, Object... kvPairs) {
    Preconditions.checkArgument(kvPairs.length % 2 == 0, "even nr of arguments only");
    Map<K, V> b = new HashMap<K, V>();
    b.put(k1, v1);
    for (int i = 0; i + 1 < kvPairs.length; i += 2) {
      b.put((K) kvPairs[i], (V) kvPairs[i + 1]);
    }
    return b;
  }

  @SuppressWarnings("unchecked")
  public static <K, V> Map<K, V> IMapExtend(Map<K, V> toExtend, Object... kvPairs) {
    Preconditions.checkArgument(kvPairs.length % 2 == 0, "even nr of arguments only");
    ImmutableMap.Builder<K, V> b = new ImmutableMap.Builder<K, V>();
    b.putAll(toExtend);
    for (int i = 0; i + 1 < kvPairs.length; i += 2) {
      b.put((K) kvPairs[i], (V) kvPairs[i + 1]);
    }
    return b.build();
  }

  public static <E> Set<E> Set() {
    return new HashSet<E>();
  }

  public static <E> Set<E> Set(Collection<E> coll) {
    return new HashSet<E>(coll);
  }

  public static <E> Set<E> safeSet(Collection<E> coll) {
    if (coll != null) {
      return Set(coll);
    }
    return null;
  }

  public static <E> List<E> safeList(Collection<E> coll) {
    if (coll != null) {
      return List(coll);
    }
    return null;
  }

  public static <E> Set<E> Set(E... t) {
    return new HashSet<E>(Arrays.asList(t));
  }

  public static <E> Set<E> intersection(Set<E> set1, Set<E> set2) {
    return Sets.intersection(set1, set2).immutableCopy();
  }

  public static <K, O> ListMultimap<K, O> groupBy(Collection<O> src, final Function<? super O, K> groupByInfo) {
    return Multimaps.index(src, groupByInfo);
  }

  public static <K, O extends Comparable<O>> ListMultimap<K, O> sortValues(ListMultimap<K, O> map, Comparator<O> comp) {
    if (map == null) {
      return null;
    }
    ListMultimap<K, O> sortedMap = ArrayListMultimap.create();
    for (K key : map.keySet()) {
      List<O> val = map.get(key);
      List<O> sorted = comp != null ? sort(val, comp) : sort(val);
      sortedMap.putAll(key, sorted);
    }
    return sortedMap;
  }

  public static <K, O extends Comparable<O>> ListMultimap<K, O> sortValues(ListMultimap<K, O> map) {
    return sortValues(map, null);
  }

  public static <K, O> Map<K, O> toMap(Collection<O> src, final Function<O, K> attrExtractor) {
    return Maps.uniqueIndex(src, attrExtractor);
  }

  public static <K, V> SortedMap<K, V> sortedMap(Map<K, V> map) {
    return ImmutableSortedMap.copyOf(map);
  }

  public static <T> List<T> sort(Collection<T> src, Comparator<T> comp) {
    return Ordering.from(comp).sortedCopy(src);
  }

  public static <T extends Comparable<T>> List<T> sort(Collection<T> src) {
    return Ordering.natural().sortedCopy(src);
  }

  public static boolean isEmpty(Object[] coll) {
    return (coll == null || coll.length == 0);
  }

  public static boolean isEmpty(Collection<?> coll) {
    return (coll == null || coll.isEmpty());
  }

  public static boolean isEmpty(Set<?> coll) {
    return (coll == null || coll.isEmpty());
  }

  public static boolean isEmpty(Map<?, ?> coll) {
    return (coll == null || coll.isEmpty());
  }

  public static <I, R extends Object & Comparable<? super R>> R maxBy(Collection<I> coll, Function<I, R> transformer) {
    Collection<R> maxOf = transform(coll, transformer);
    return Collections.max(maxOf);
  }

  public static <I, R extends Object & Comparable<? super R>> I max(Collection<I> coll, final Function<I, R> transformer) {
    Ordering<I> o = new Ordering<I>() {
      @Override
      public int compare(I left, I right) {
        return transformer.apply(left).compareTo(transformer.apply(right));
      }
    };
    return o.max(coll);
  }

  public static <T> Integer intSum(Collection<T> col, Function<T, Integer> transformer) {
    Collection<Integer> r = transform(col, transformer);
    Integer res = 0;
    for (Integer i : r) {
      res += i;
    }
    return res;
  }

  public static <K, V> Map<V, K> reverseMap(Map<K, V> in) {
    return ImmutableBiMap.copyOf(in).inverse();
  }

  public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> fromMap,
      Function<? super V1, V2> function) {
    return Maps.transformValues(fromMap, function);
  }

  public static <K, V1, V2> ListMultimap<K, V2> transformValues(ListMultimap<K, V1> fromMap,
      Function<? super V1, V2> function) {
    AppPreconditions.checkNotNull(fromMap, function);
    ListMultimap<K, V2> res = ArrayListMultimap.create();
    for (Entry<K, Collection<V1>> e : fromMap.asMap().entrySet()) {
      res.putAll(e.getKey(), Iterables.transform(e.getValue(), function));
    }
    return ImmutableListMultimap.copyOf(res);
  }

  @SuppressWarnings("unchecked")
  public static <K, V1, V2, T1 extends Multimap<K, V1>, T2 extends Multimap<K, V2>> T2 transformValues(T1 fromMap,
      Function<? super V1, V2> function) {
    AppPreconditions.checkNotNull(fromMap, function);
    ListMultimap<K, V2> res = ArrayListMultimap.create();
    for (Entry<K, Collection<V1>> e : fromMap.asMap().entrySet()) {
      res.putAll(e.getKey(), Iterables.transform(e.getValue(), function));
    }
    return (T2) res;
  }

  public static <T> boolean exists(Collection<T> col, Predicate<T> p) {
    return Iterables.filter(col, p).iterator().hasNext();
  }

  /**
   * Returns the first element in {@code iterable} that satisfies the given
   * predicate; use this method only when such an element is known to exist
   * (throws NoSuchElementException if not found)
   * 
   * @param it
   * @param p
   * @return the object looked for
   */
  public static <T> Optional<T> find(Iterable<T> it, Predicate<? super T> p) {
    try {
      return Optional.of(Iterables.find(it, p));
    } catch (NoSuchElementException e) {
      return Optional.absent();
    }
  }

  /**
   * Return the first object matching the predicate or null if not found
   * 
   * @param it
   * @param p
   * @return the object or null if found
   */
  public static <T> T tryFind(Iterable<T> it, Predicate<T> p) {
    Optional<T> opt = Iterables.tryFind(it, p);
    return opt.isPresent() ? opt.get() : null;
  }

  public static <T> Collection<T> extract(Collection<T> col, Predicate<T> p) {
    return Collections2.filter(col, p);
  }

  /**
   * @return new Collection in whihc p predicate is true.
   */
  public static <T> Collection<T> filter(Collection<T> col, Predicate<? super T> p) {
    return Collections2.filter(col, p);
  }

  /**
   * new collection in do not include items where predicate is true.
   * Opposite of filter.
   */
  public static <T> Collection<T> reject(Collection<T> col, Predicate<T> p) {
    return Collections2.filter(col, Predicates.not(p));
  }

  /**
   * ADD operation of collections and handling immutable collections.
   * If collection is mutable returning the original collection. If immutable
   * returning a new collection.
   */
  public static <T> List<T> add(List<T> c, T item) {
    if (c instanceof ImmutableCollection) {
      return new ImmutableList.Builder<T>().addAll(c).add(item).build();
    }
    c.add(item);
    return c;
  }

  private static class CustomSetWrapper<T> {
    T val;
    Function<T, Object> extractor;

    CustomSetWrapper(T v, Function<T, Object> extractor) {
      this.val = v;
      this.extractor = extractor;
    }

    @Override
    public int hashCode() {
      return extractor.apply(val).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      @SuppressWarnings("unchecked")
      T thatVal = ((CustomSetWrapper<T>) obj).val;
      return extractor.apply(val).equals(extractor.apply(thatVal));
    }
  }

  public static <O, T> List<T> transformList(List<O> list, Function<O, T> function) {
    return Lists.transform(list, function);
  }

  public static <O, T> List<T> transform(Collection<O> list, Function<O, T> function) {
    return Lists.transform(ImmutableList.copyOf(list), function);
  }

  public static <O> Set<O> filter(Set<O> set, Predicate<O> predicate) {
    return Sets.filter(set, predicate);
  }

  public static <T> T[] toArray(Iterable<T> iterable, Class<T> clazz) {
    return Iterables.toArray(iterable, clazz);
  }

  /**
   * Usefull when you want to achieve unique set of obejcts (by some certain fields)
   * but for some reason you do not want to override `hashCode` and `equals` methods.
   * Why not override? becouse italready have an implementation you do not want to change
   * becouse otherpart of the systsem is relying on that implementation.
   */
  public static <T> Set<T> customUniqSet(Collection<T> in, Function<T, Object> extractor) {
    Set<CustomSetWrapper<T>> tmp = new HashSet<AppCollections.CustomSetWrapper<T>>(in.size());
    for (T v : in) {
      tmp.add(new CustomSetWrapper<T>(v, extractor));
    }
    return ImmutableSet.copyOf(Iterables.transform(tmp, new Function<CustomSetWrapper<T>, T>() {
      @Override
      public T apply(CustomSetWrapper<T> input) {
        return input.val;
      }

    }));
  }

  public static <K, V> Map<K, V> put(Map<K, V> map, K key, V value) {
    if (map instanceof ImmutableMap) {
      return new ImmutableMap.Builder<K, V>().putAll(map).put(key, value).build();
    }
    map.put(key, value);
    return map;
  }

  public static <K, V> Map<K, V> Map(Class<K> kc, Class<V> vc, Object... objects) {
    return Map(objects);
  }

  public static <K, V> Map<K, V> Map(Object... objects) {
    if (null == objects || 0 == objects.length) {
      return ImmutableMap.of();
    }
    Preconditions.checkArgument(objects.length % 2 == 0, "key value pairs are expected so always even (devidable by 2)");
    HashMap<K, V> ret = new HashMap<K, V>();
    for (int i = 0; i < objects.length; i += 2) {
      K k = cast(objects[i]);
      V v = cast(objects[i + 1]);
      ret.put(k, v);
    }
    return ImmutableMap.copyOf(ret);
  }

  public static <T> Iterable<T> between(Iterable<T> src, int from, int to) {
    return Iterables.skip(Iterables.limit(src, to), from);
  }

  public static <K, V> Map<K, V> merge(Map<? extends K, ? extends V> m1, Map<? extends K, ? extends V> m2) {
    return new ImmutableMap.Builder<K, V>().putAll(m1).putAll(m2).build();
  }

  /**
   * Force casting childs
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static <K1, V1, K2, V2> Map<K1, V1> convert(Map<K1, V1> out, Map<K2, V2> in) {
    Preconditions.checkNotNull(in);
    Preconditions.checkNotNull(out);
    //    for (Entry<K2, V2> e : in.entrySet()) {
    //      out.put((K1) e.getKey(), (V1) e.getValue());
    //    }
    //    return out;
    Map interm = in;
    out.putAll(interm);
    return out;
  }
}
