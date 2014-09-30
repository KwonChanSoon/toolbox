package eu.qualityontime.collections;

/**
 * This is the same for `equals()` as the `Comparator` for `compareTo()`
 */
public interface EqualsRelation {
  public boolean equals(Object left, Object right);
}
