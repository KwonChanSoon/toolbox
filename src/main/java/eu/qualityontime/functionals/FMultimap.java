package eu.qualityontime.functionals;

import com.google.common.base.Function;
import com.google.common.collect.*;

import eu.qualityontime.AppCollections;

import java.util.Collection;

public class FMultimap<K, V> extends ForwardingMultimap<K, V> {
  private final Multimap<K, V> multimap;

  public static <K, V> FMultimap<K, V> with(Multimap<K, V> mm) {
    return mm instanceof FMultimap ? (FMultimap<K, V>) mm : new FMultimap<K, V>(mm);
  }

  public static <K, V> FMultimap<K, V> FMultimap(Multimap<K, V> mm) {
    return with(mm);
  }

  FMultimap(Multimap<K, V> multimap) {
    super();
    this.multimap = multimap;
  }

  public <V1> FMultimap<K, V1> transformValues(Function<V, V1> valuesFunction) {
    return with(AppCollections.transformValues(multimap, valuesFunction));
  }

  public ListMultimap<K, V> asListMultimap() {
    return ImmutableListMultimap.copyOf(multimap);
  }

  @Override
  protected Multimap<K, V> delegate() {
    return multimap;
  }

  @Override
  public FMap<K, Collection<V>> asMap() {
    return FMap.FMap(super.asMap());
  }


}
