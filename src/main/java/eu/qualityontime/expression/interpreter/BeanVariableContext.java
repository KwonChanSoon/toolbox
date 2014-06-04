package eu.qualityontime.expression.interpreter;

import com.google.common.base.Throwables;

import eu.qualityontime.AppReflection;

public class BeanVariableContext implements IVariableContext {
  private final Object target;

  public BeanVariableContext(Object target) {
    super();
    this.target = target;
  }

  @Override
  public Object get(String var) {
    try {
      return AppReflection.getField(target, var);
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public boolean hasVariable(String var) {
    try {
      AppReflection.getField(target, var);
    } catch (IllegalArgumentException e) {
      return false;
    }
    return true;
  }

}
