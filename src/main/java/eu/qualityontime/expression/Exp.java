package eu.qualityontime.expression;

import eu.qualityontime.expression.interpreter.ExpInterpreter;

public abstract class Exp {
  private String expId;

  public void accept(ExpInterpreter interpreter) {
    interpreter.interpret(this);
  }

  public Exp setExpId(String string) {
    this.expId = string;
    return this;
  }

  public String getExpId() {
    return expId;
  }

}
