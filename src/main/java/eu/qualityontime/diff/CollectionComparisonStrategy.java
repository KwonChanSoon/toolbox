package eu.qualityontime.diff;

import java.util.Collection;

public interface CollectionComparisonStrategy {
  public void compare(ObjectDiffer differ, DiffNode current, Collection<?> working, Collection<?> base);
}
