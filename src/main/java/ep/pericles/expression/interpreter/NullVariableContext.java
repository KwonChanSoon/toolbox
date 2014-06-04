package ep.pericles.expression.interpreter;

import ep.pericles.AppStrings;

/**
 * Always throw IllegalArgumentException
 */
public class NullVariableContext implements IVariableContext {

  @Override
  public Object get(String var) {
    throw new IllegalArgumentException(AppStrings.rs("No variable bound to `{}`", var));
  }

  @Override
  public boolean hasVariable(String var) {
    return false;
  }

}
