package eu.qualityontime.functionals;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.*;
import com.google.common.collect.Maps.EntryTransformer;

public class FMap<K, V> extends ForwardingMap<K, V> {
  private final Map<K, V> delegate;

  public static <K, V> FMap<K, V> FMap(Map<K, V> map) {
    return (map instanceof FMap) ? (FMap<K, V>) map : new FMap<K, V>(map);
  }

  FMap(Map<K, V> delegate) {
    this.delegate = delegate;
  }

  @Override
  protected Map<K, V> delegate() {
    return delegate;
  }

  /**
   * `Maps.transformValues(delegate, function)`
   */
  public <V2> FMap<K, V2> mapValues(Function<? super V, V2> function) {
    return FMap(Maps.transformValues(delegate, function));
  }

  /**
   * `Maps.transformEntries(delegate, transformer)`
   */
  public <V2> FMap<K, V2> mapEntries(EntryTransformer<? super K, ? super V, V2> transformer) {
    return FMap(Maps.transformEntries(delegate, transformer));
  }

  public <K2> FMap<K2, V> mapKeys(Function<? super K, K2> function) {
    ImmutableMap.Builder<K2, V> builder = ImmutableMap.builder();
    for (Entry<K, V> e : delegate.entrySet()) {
      builder.put(function.apply(e.getKey()), e.getValue());
    }
    return FMap(builder.build());
  }
}
