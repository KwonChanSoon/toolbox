package eu.qualityontime.collections;

public final class NaturalEquals implements EqualsRelation {
  public static NaturalEquals INSTANCE = new NaturalEquals();

  @Override
  public boolean equals(Object left, Object right) {
    return left == right || (left != null && left.equals(right));
  }

  private NaturalEquals() {
  }

}
