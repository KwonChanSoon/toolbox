package eu.qualityontime.expression;

public class SimpleConditionExp extends ConditionalExp {
  protected FieldExp field;
  protected ValueExp value;
  protected BinaryOp op;

  public SimpleConditionExp() {

  }

  public SimpleConditionExp(FieldExp field, BinaryOp op, ValueExp value) {
    this.field = field;
    this.op = op;
    this.value = value;
  }

  @Override
  public String toString() {
    return field + " " + op + " " + value;
  }

  public FieldExp getField() {
    return field;
  }

  public void setField(FieldExp field) {
    this.field = field;
  }

  public ValueExp getValue() {
    return value;
  }

  public void setValue(ValueExp value) {
    this.value = value;
  }

  public BinaryOp getOp() {
    return op;
  }

  public void setOp(BinaryOp op) {
    this.op = op;
  }

}
