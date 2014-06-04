package eu.qualityontime.type;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class ParametricMessage extends Message {
  private List<Object> params = ImmutableList.of();

  public static ParametricMessage of(EMessageType type, String text, Object... params) {
    return new ParametricMessage(type, text, params);
  }

  public ParametricMessage() {
    super();
  }

  public ParametricMessage(EMessageType type, String text, Object... params) {
    super(type, text);
    this.params = ImmutableList.copyOf(params);
  }

  public List<Object> getParams() {
    return params;
  }

  public void setParams(List<Object> params) {
    this.params = params;
  }
}
