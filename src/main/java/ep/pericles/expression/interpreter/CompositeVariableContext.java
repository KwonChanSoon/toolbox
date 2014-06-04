package ep.pericles.expression.interpreter;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

import ep.pericles.*;

public class CompositeVariableContext implements IVariableContext {
  private final Collection<? extends IVariableContext> ctxs;

  public CompositeVariableContext(IVariableContext... ctxs) {
    this(ImmutableList.copyOf(ctxs));
  }

  public CompositeVariableContext(Collection<? extends IVariableContext> ctxs) {
    super();
    this.ctxs = ctxs;
  }

  @Override
  public Object get(String var) {
    if (!containsKey(var)) {
      throw new IllegalArgumentException(AppStrings.rs("No variable bound to `{}`", var));
    }
    for (IVariableContext c : ctxs) {
      if (c.hasVariable(var)) {
        return c.get(var);
      }
    }
    throw new Defect("Cannot happen");
  }

  private boolean containsKey(String var) {
    for (IVariableContext c : ctxs) {
      if (c.hasVariable(var)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean hasVariable(String var) {
    return containsKey(var);
  }

}
