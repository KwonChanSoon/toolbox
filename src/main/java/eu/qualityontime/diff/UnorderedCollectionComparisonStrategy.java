package eu.qualityontime.diff;

import java.util.*;

import lombok.*;
import eu.qualityontime.collections.*;
import eu.qualityontime.diff.DiffNode.State;

/**
 * Set based comparison. And as like that it stops comparing details.
 * (It does not go into depth in the object graph)
 * By default is using equals to identify same/diff objects.
 * It can be overridee by changing `equals` attribute.
 */
public class UnorderedCollectionComparisonStrategy implements CollectionComparisonStrategy {
  @Setter
  private EqualsRelation equals = NaturalEquals.INSTANCE;

  @Override
  public void compare(ObjectDiffer differ, DiffNode current, Collection<?> working, Collection<?> base) {
    new UnorderedCollectionDiffer(differ, current, working, base).compare();
  }

  @AllArgsConstructor
  private class UnorderedCollectionDiffer {
    @SuppressWarnings("unused")
    private ObjectDiffer differ;//TODO: check whether it might be needed or not.
    private DiffNode parent;
    private Collection<?> working;
    private Collection<?> base;

    public void compare() {
      Collection<Instances> same = same();
      Collection<Instances> added = added(same);
      Collection<Instances> removed = removed(same);
      compareNode(same);
      compareNode(added);
      compareNode(removed);
    }

    private void compareNode(Collection<Instances> instances) {
      for (Instances i : instances) {
        DiffNode node = DiffNode.SetNode(parent, i);
        parent.addChild(node);
        if (i.isAdded())
          node.setState(State.ADDED);
        else if (i.isRemoved())
          node.setState(State.REMOVED);
        else
          node.setState(State.UNTOUCHED);
      }
    }

    private Collection<Instances> same() {
      List<Instances> ret = new ArrayList<Instances>();
      for (Object w : working) {
        for (Object b : base) {
          if (equals.equals(w, b))
            ret.add(new Instances(w, b));
        }
      }
      return ret;
    }

    private Collection<Instances> added(Collection<Instances> instancesCommon) {
      List<Instances> ret = new ArrayList<Instances>();
      outer:
      for (Object w : working) {
        for (Instances i : instancesCommon) {
          if (equals.equals(w, i.getWorking()))
            continue outer;
        }
        ret.add(new Instances(w, null));
      }
      return ret;
    }

    private Collection<Instances> removed(Collection<Instances> instancesCommon) {
      List<Instances> ret = new ArrayList<Instances>();
      outer:
      for (Object b : base) {
        for (Instances i : instancesCommon) {
          if (equals.equals(b, i.getBase()))
            continue outer;
        }
        ret.add(new Instances(null, b));
      }
      return ret;
    }

  }

}
