package ep.pericles.collections;

import java.util.*;

public abstract class AbstractMapDecorator<K, V> implements Map<K, V> {
  protected transient Map<K, V> map;

  protected AbstractMapDecorator() {
    super();
  }

  public AbstractMapDecorator(Map<K, V> map) {
    if (map == null) {
      throw new IllegalArgumentException("Map must not be null");
    }
    this.map = map;
  }

  protected Map<K, V> getMap() {
    return map;
  }

  public void clear() {
    map.clear();
  }

  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  public boolean containsValue(Object value) {
    return map.containsValue(value);
  }

  public Set<java.util.Map.Entry<K, V>> entrySet() {
    return map.entrySet();
  }

  @Override
  public boolean equals(Object o) {
    return map.equals(o);
  }

  public V get(Object key) {
    return map.get(key);
  }

  @Override
  public int hashCode() {
    return map.hashCode();
  }

  public boolean isEmpty() {
    return map.isEmpty();
  }

  public Set<K> keySet() {
    return map.keySet();
  }

  public V put(K key, V value) {
    return map.put(key, value);
  }

  public void putAll(Map<? extends K, ? extends V> m) {
    map.putAll(m);
  }

  public V remove(Object key) {
    return map.remove(key);
  }

  public int size() {
    return map.size();
  }

  public Collection<V> values() {
    return map.values();
  }

  @Override
  public String toString() {
    return map.toString();
  }
}
