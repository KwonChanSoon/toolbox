package eu.qualityontime.functionals;

public interface F2<X, Y, Z> {
  public Z f(X p1, Y p2);

  public static interface Simple<T> extends F2<T, T, T> {
  }

  public static interface Collector<V, S> extends F2<V, S, V> {
  }

}