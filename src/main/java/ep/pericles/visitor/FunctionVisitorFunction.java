package ep.pericles.visitor;

import com.google.common.base.Function;

public class FunctionVisitorFunction<T> implements Function<Object, T> {
  private final FunctionVisitor<T> visitor;

  public FunctionVisitorFunction(FunctionVisitor<T> visitor) {
    this.visitor = visitor;
  }

  @Override
  public T apply(Object input) {
    return visitor.visit(input);
  }

}
