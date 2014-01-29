package ec.ep.pericles.service.services.impl.comparator;

public interface EquivalenceRelation {
  public final static EquivalenceRelation DEFAULT = new EquivalenceRelation() {
    public boolean areEquals(Object o1, Object o2) {
      return o1 == o2 || o1 != null && o1.equals(o2);
    }
  };

  public boolean areEquals(Object o1, Object o2);
}
