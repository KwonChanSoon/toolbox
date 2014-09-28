package eu.qualityontime.diff;

import java.util.*;

import lombok.*;

@Getter
@Setter
public abstract class DiffNode {
  private DiffNode parent;
  private List<DiffNode> children;
  private State state = State.UNTOUCHED;
  private Instances instances;

  public boolean isChanged() {
    if (state != State.UNTOUCHED)
      return true;
    for (DiffNode child : getChildren())
      if (child.isChanged())
        return true;
    return false;
  }

  public static enum State {
    UNTOUCHED, CHANGED, DIFFERENT_TYPE, ADDED, REMOVED;
  }

  public boolean isAdded() {
    return state == State.ADDED;
  }

  public boolean isRemoved() {
    return state == State.REMOVED;
  }

  public DiffNode addChild(DiffNode child) {
    getChildren().add(child);
    return child;
  }

  public List<DiffNode> getChildren() {
    if (null == children)
      children = new ArrayList<DiffNode>();
    return children;
  }

  public void visit(NodeVisitor visitor) {
    visitor.visit(this);
  }

  public abstract String getPath();

  public Iterable<DiffNode> preorderIterable() {
    List<DiffNode> res = new ArrayList<DiffNode>();
    add(res, this);
    return res;
  }

  private static void add(List<DiffNode> res, DiffNode diffNode) {
    res.add(diffNode);
    for (DiffNode child : diffNode.getChildren()) {
      add(res, child);
    }
  }

  public static IndexedNode IndexedNode(DiffNode parent, int i, Instances instances) {
    IndexedNode node = new IndexedNode();
    node.setParent(parent);
    node.setIndex(i);
    node.setInstances(instances);
    return node;
  }

  public static SimpleNode SimpleNode(DiffNode parent, String propertyName, Instances instances) {
    SimpleNode node = new SimpleNode();
    node.setParent(parent);
    node.setPropertyName(propertyName);
    node.setInstances(instances);
    return node;
  }

  public static SetNode SetNode(DiffNode parent, Instances instances) {
    SetNode node = new SetNode();
    node.setInstances(instances);
    node.setParent(parent);
    return node;
  }

  public static SetSameNode SetSameNode(DiffNode parent, Instances instances, Map<String, Object> key) {
    SetSameNode node = new SetSameNode();
    node.setInstances(instances);
    node.setParent(parent);
    node.setKey(key);
    return node;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  public static class SimpleNode extends DiffNode {
    private String propertyName;

    @Override
    public String getPath() {
      String parentPath = getParent().getPath();
      return parentPath + ("/".equals(parentPath) ? "" : ".") + propertyName;
    }

  }

  @Getter
  @Setter
  @NoArgsConstructor
  public static class IndexedNode extends DiffNode {
    private int index;

    @Override
    public String getPath() {
      String parentPath = getParent().getPath();
      return parentPath + "[" + index + "]";
    }

  }

  @Getter
  @Setter
  @NoArgsConstructor
  public static class SetNode extends DiffNode {
    @Override
    public String getPath() {
      String parentPath = getParent().getPath();
      return parentPath + "<" + getState() + ">";
    }
  }

  public static class RootNode extends DiffNode {
    @Override
    public String getPath() {
      return "/";
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor
  public static class SetSameNode extends SetNode {
    private Map<String, Object> key;

    @Override
    public String getPath() {
      String parentPath = getParent().getPath();
      return parentPath + getKey();
    }
  }

  public static DiffNode RootNode() {
    return new RootNode();
  }

}
