package ep.pericles.cache;

public interface ICacheMonitor {
  public Long getHitsNumber();

  public Long getRequestsNumber();

  public Integer getCacheSize();
}
