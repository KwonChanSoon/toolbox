package eu.qualityontime.expression;

import com.google.common.base.Preconditions;

public class LiteralExp extends ValueExp {
  private Object value;

  public LiteralExp(Object value) {
    super();
    Preconditions.checkNotNull(value);
    this.value = value;
  }

  @Override
  public String toString() {
    return "VALUE[" + value + "]";
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    Preconditions.checkNotNull(value);
    this.value = value;
  }
}
