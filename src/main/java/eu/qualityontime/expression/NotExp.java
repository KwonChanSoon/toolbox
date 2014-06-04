package eu.qualityontime.expression;

public class NotExp extends ConditionalExp {
  private ConditionalExp exp;

  public NotExp() {
  }

  public NotExp(ConditionalExp exp) {
    super();
    this.exp = exp;
  }

  @Override
  public String toString() {
    return "NOT " + exp;
  }

  public ConditionalExp getExp() {
    return exp;
  }

  public void setExp(ConditionalExp exp) {
    this.exp = exp;
  }

}
