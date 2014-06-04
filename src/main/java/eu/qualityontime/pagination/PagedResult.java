package eu.qualityontime.pagination;

import java.util.List;

public class PagedResult<T> {
  private int counter;
  private List<T> result;

  public static <T> PagedResult<T> of(int counter, List<T> result) {
    return new PagedResult<T>(counter, result);
  }

  public PagedResult(int counter, List<T> result) {
    this.counter = counter;
    this.result = result;
  }

  public PagedResult() {
  }

  public int getCounter() {
    return counter;
  }

  public void setCounter(int counter) {
    this.counter = counter;
  }

  public List<T> getResult() {
    return result;
  }

  public void setResult(List<T> result) {
    this.result = result;
  }
}
