package eu.qualityontime.expression;

import java.util.List;

import com.google.common.base.*;
import com.google.common.collect.*;

public class FunctionExp extends ConditionalExp {
  private String name;
  private List<? extends ValueExp> params;

  public FunctionExp(String name, List<ValueExp> params) {
    super();
    this.name = name;
    this.params = params;
  }

  public FunctionExp(String name) {
    this(name, ImmutableList.<ValueExp> of());
  }

  public FunctionExp(String name, ValueExp... params) {
    this(name, ImmutableList.copyOf(params));
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<? extends ValueExp> getParams() {
    return params;
  }

  public void setParams(List<LiteralExp> params) {
    this.params = params;
  }

  @Override
  public String toString() {
    return name + "(" + Joiner.on(", ").join(Iterables.transform(params, Functions.toStringFunction())) + ")";
  }

  @Override
  public FunctionExp setExpId(String string) {
    return (FunctionExp) super.setExpId(string);
  }

}
