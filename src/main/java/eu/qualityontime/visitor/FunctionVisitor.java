package eu.qualityontime.visitor;

public interface FunctionVisitor<T> {
  public T visit(Object o);
}
