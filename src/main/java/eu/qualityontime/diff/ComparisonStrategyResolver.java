package eu.qualityontime.diff;

import static eu.qualityontime.AppCollections.IMap;

import java.util.Map;

import lombok.*;

@NoArgsConstructor
public class ComparisonStrategyResolver {
  @Setter
  private CollectionComparisonStrategy globalCollectionComparisonStrategy = new IndexedComparisonStrategy();
  private Map<String, ? extends CollectionComparisonStrategy> strategiesByPath = IMap();
  private PathMatcher matcher = new PathMatcher();

  public ComparisonStrategyResolver(Map<String, CollectionComparisonStrategy> strategiesByPath) {
    this.strategiesByPath = strategiesByPath;
  }

  /**
   * Node specific comparison strategy. If not defined it is returning global strategy.
   */
  public CollectionComparisonStrategy collectionComparisonStrategy(DiffNode theNode) {
    CollectionComparisonStrategy currentStrategy = findCustomStrategy(theNode);
    if (null == currentStrategy)
      return globalCollectionComparisonStrategy;
    else
      return currentStrategy;
  }

  private CollectionComparisonStrategy findCustomStrategy(DiffNode theNode) {
    String nodePath = theNode.getPath();
    for (val e : strategiesByPath.entrySet())
      if (matcher.match(e.getKey(), nodePath))
        return e.getValue();
    return null;
  }

}
