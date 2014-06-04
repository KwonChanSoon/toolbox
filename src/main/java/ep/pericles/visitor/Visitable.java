package ep.pericles.visitor;

public interface Visitable {
  public void accept(ReflectiveVisitor visitor);
}
