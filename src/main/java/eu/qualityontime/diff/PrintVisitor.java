package eu.qualityontime.diff;

import org.apache.commons.lang3.StringUtils;

import eu.qualityontime.diff.DiffNode.State;

public class PrintVisitor implements NodeVisitor {

  private int depth = 0;

  @Override
  public void visit(DiffNode diffNode) {
    printPrefix();
    printNode(diffNode);
    printPostfix();
    for (DiffNode child : diffNode.getChildren()) {
      depth++;
      visit(child);
      depth--;
    }
  }

  private void printNode(DiffNode diffNode) {
    print("path=" + diffNode.getPath() + ", state=" + diffNode.getState());
    if (State.CHANGED == diffNode.getState()) {
      print(", working=" + diffNode.getInstances().getWorking());
      print(", base=" + diffNode.getInstances().getBase());
    }
    else if (State.ADDED == diffNode.getState()) {
      print(", added=" + diffNode.getInstances().getWorking());
    }
    else if (State.REMOVED == diffNode.getState()) {
      print(", removed=" + diffNode.getInstances().getBase());
    }
  }

  protected void printPostfix() {
    System.out.println();
  }

  protected void print(String string) {
    System.out.print(string);
  }

  protected void printPrefix() {
    System.out.print(indent());
  }

  protected String indent() {
    return StringUtils.repeat("    ", depth);
  }

}
