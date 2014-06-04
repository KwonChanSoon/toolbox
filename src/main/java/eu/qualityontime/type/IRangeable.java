package eu.qualityontime.type;

@SuppressWarnings("rawtypes")
public interface IRangeable<C extends Comparable> {
  public C lowerEndpoint();

  public C upperEndpoint();

}
