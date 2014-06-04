package eu.qualityontime.expression;

public class FieldExp extends Exp {
  private Object name;

  public FieldExp(Object name) {
    super();
    this.name = name;
  }

  @Override
  public String toString() {
    return "FIELD[" + name + "]";
  }

  public Object getName() {
    return name;
  }

  public void setName(Object name) {
    this.name = name;
  }
}
