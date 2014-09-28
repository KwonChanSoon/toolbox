package eu.qualityontime.diff;

import lombok.*;
import eu.qualityontime.Defect;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Instances {
  private Object working;
  private Object base;

  public boolean isAdded() {
    return working != null && base == null;
  }

  public boolean isRemoved() {
    return working == null && base != null;
  }

  public Object getAdded() {
    if (isAdded())
      return working;
    throw new Defect("Notpossible");
  }

  public Object getRemoved() {
    if (isRemoved())
      return base;
    throw new Defect("Notpossible");
  }
}
