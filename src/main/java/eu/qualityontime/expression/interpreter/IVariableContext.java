package eu.qualityontime.expression.interpreter;

/**
 * Contex object of holding varaibales (represented by VariableExp) and used in ExpInterpreter if needed.
 */
public interface IVariableContext {
  public Object get(String var);

  public boolean hasVariable(String var);
}
