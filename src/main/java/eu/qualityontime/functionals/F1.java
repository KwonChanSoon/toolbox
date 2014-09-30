package eu.qualityontime.functionals;

public interface F1<X, Y> {
  public Y f(X p);

  public static interface Simple<T> extends F1<T, T> {
  }

}
