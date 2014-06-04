package eu.qualityontime.visitor;

public interface Visitable {
  public void accept(ReflectiveVisitor visitor);
}
