package ep.pericles.collections;

import java.util.Map;

import com.google.common.base.Function;

public class DefaultValueMap<K, V> extends AbstractMapDecorator<K, V> implements Map<K, V> {
  private transient Function<K, V> factory;

  /**
   * If no value found the factory will produce the result
   */
  public static <K, V> Map<K, V> decorate(Map<K, V> map, Function<K, V> factory) {
    return new DefaultValueMap<K, V>(map, factory);
  }

  protected DefaultValueMap(Map<K, V> map, Function<K, V> factory) {
    super(map);
    if (factory == null) {
      throw new IllegalArgumentException("Factory must not be null");
    }
    this.factory = factory;
  }

  @Override
  @SuppressWarnings("unchecked")
  public V get(Object key) {
    // create value for key if key is not currently in the map
    if (map.containsKey(key) == false) {
      V value = factory.apply((K) key);
      return value;
    }
    return map.get(key);
  }

}
