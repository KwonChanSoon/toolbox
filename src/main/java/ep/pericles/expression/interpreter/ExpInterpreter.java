package ep.pericles.expression.interpreter;

import ep.pericles.expression.Exp;

public abstract interface ExpInterpreter {

  public void interpret(Exp exp);
}
