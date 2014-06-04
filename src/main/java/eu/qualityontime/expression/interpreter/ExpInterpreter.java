package eu.qualityontime.expression.interpreter;

import eu.qualityontime.expression.Exp;

public abstract interface ExpInterpreter {

  public void interpret(Exp exp);
}
