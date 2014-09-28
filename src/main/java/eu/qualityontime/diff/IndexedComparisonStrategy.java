package eu.qualityontime.diff;

import static java.lang.Math.max;

import java.util.*;

import lombok.experimental.ExtensionMethod;
import eu.qualityontime.collections.ListTryGet;

@ExtensionMethod({ ListTryGet.class })
public class IndexedComparisonStrategy implements CollectionComparisonStrategy {
  @Override
  public void compare(ObjectDiffer differ, DiffNode current, Collection<?> working, Collection<?> base) {
    _compare(differ, current, (List<?>) working, (List<?>) base);
  }

  private void _compare(ObjectDiffer differ, DiffNode current, List<?> working, List<?> base) {
    final int maxLength = max(working.size(), base.size());
    for (int i = 0; i < maxLength; i++) {
      DiffNode node = current.addChild(DiffNode.IndexedNode(current, i, new Instances(working.tryGet(i), base.tryGet(i))));
      differ.compare(node, working.tryGet(i), base.tryGet(i));
    }
  }

}
