package eu.qualityontime.diff;

import static eu.qualityontime.commons.JakartaPropertyUtils.getProperty;

import java.util.*;

import lombok.AllArgsConstructor;

import com.google.common.base.Objects;

import eu.qualityontime.collections.EqualsRelation;
import eu.qualityontime.diff.DiffNode.State;

/**
 * ...
 * It make sense to us in case when collection is holding beans.
 */
public class CustomKeyCollectionComparisonStrategy implements CollectionComparisonStrategy {
  private Key key;
  private KeyEquals equals;

  public CustomKeyCollectionComparisonStrategy(Key key) {
    this.key = key;
    this.equals = new KeyEquals(this.key);
  }

  @Override
  public void compare(ObjectDiffer differ, DiffNode current, Collection<?> working, Collection<?> base) {
    new CustomKeyDiffer(differ, current, working, base).compare();
  }

  @AllArgsConstructor
  private class CustomKeyDiffer {
    private ObjectDiffer differ;
    private DiffNode parent;
    private Collection<?> working;
    private Collection<?> base;

    public void compare() {
      Collection<Instances> same = same();
      Collection<Instances> added = added(same);
      Collection<Instances> removed = removed(same);
      compareSame(same);
      compareAdded(added);
      compareRemoved(removed);
    }

    private Collection<Instances> same() {
      List<Instances> ret = new ArrayList<Instances>();
      for (Object w : working)
        for (Object b : base)
          if (equals.equals(w, b))
            ret.add(new Instances(w, b));
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

    private void compareSame(Collection<Instances> instances) {
      for (Instances i : instances) {
        DiffNode node = DiffNode.SetSameNode(parent, i, equals.keyVals(i.getWorking()));
        parent.addChild(node);
        differ.compare(node, i.getWorking(), i.getBase());
      }
    }

    private void compareAdded(Collection<Instances> instances) {
      for (Instances i : instances) {
        DiffNode node = DiffNode.SetNode(parent, i);
        parent.addChild(node);
        node.setState(State.ADDED);
      }
    }

    private void compareRemoved(Collection<Instances> instances) {
      for (Instances i : instances) {
        DiffNode node = DiffNode.SetNode(parent, i);
        parent.addChild(node);
        node.setState(State.REMOVED);
      }
    }

  }

  @AllArgsConstructor
  public static class KeyEquals implements EqualsRelation {
    private final Key key;

    @Override
    public boolean equals(Object left, Object right) {
      return left == right || _equals(left, right);
    }

    private boolean _equals(Object left, Object right) {
      if (null == left)
        return false;
      if (null == right)
        return false;
      Map<String, Object> keyLeft = keyVals(left);
      Map<String, Object> keyRight = keyVals(right);
      return Objects.equal(keyLeft, keyRight);
    }

    public Map<String, Object> keyVals(Object o) {
      Map<String, Object> ret = new HashMap<String, Object>();
      for (String attrs : key.getKeyAttributes()) {
        ret.put(attrs, getProperty(o, attrs));
      }
      return ret;
    }
  }
}
