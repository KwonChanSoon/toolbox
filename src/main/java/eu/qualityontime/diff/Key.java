package eu.qualityontime.diff;

import lombok.Getter;

public class Key {
  @Getter
  private final String[] keyAttributes;

  public Key(String... keyAttributes) {
    this.keyAttributes = keyAttributes;
  }
}
