package eu.qualityontime.expression;

import com.google.common.base.Preconditions;

public class VariableExp extends ValueExp {
  private String variableName;

  public VariableExp(String value) {
    super();
    Preconditions.checkNotNull(value);
    this.variableName = value;
  }

  @Override
  public String toString() {
    return "VAR[" + variableName + "]";
  }

  public String getVariableName() {
    return variableName;
  }

  public void setVariableName(String variableName) {
    this.variableName = variableName;
  }

}
