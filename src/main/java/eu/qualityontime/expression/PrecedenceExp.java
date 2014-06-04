package eu.qualityontime.expression;

/**
 * In real these are the brackets around expressions.
 */
public class PrecedenceExp extends ConditionalExp {
  private ConditionalExp exp;

  public PrecedenceExp(ConditionalExp exp) {
    super();
    this.exp = exp;
  }

  @Override
  public String toString() {
    return "(" + exp + ")";
  }

  public ConditionalExp getExp() {
    return exp;
  }

  public void setExp(ConditionalExp exp) {
    this.exp = exp;
  }

  @Override
  public PrecedenceExp setExpId(String string) {
    return (PrecedenceExp) super.setExpId(string);
  }

}
