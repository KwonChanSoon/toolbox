package ep.pericles.visitor;

public interface FunctionVisitor<T> {
  public T visit(Object o);
}
