package eu.qualityontime.expression.interpreter;

import java.util.Map;

import eu.qualityontime.AppStrings;

public class MapVariableContext implements IVariableContext {
  private final Map<String, ? extends Object> vars;

  public MapVariableContext(Map<String, ? extends Object> vars) {
    this.vars = vars;
  }

  @Override
  public Object get(String var) {
    if (!vars.containsKey(var)) {
      throw new IllegalArgumentException(AppStrings.rs("No variable bound to `{}`", var));
    }
    return vars.get(var);
  }

  @Override
  public boolean hasVariable(String var) {
    return vars.containsKey(var);
  }

}
