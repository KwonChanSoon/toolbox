package eu.qualityontime.tuple;

import com.google.common.base.Function;

public class Pairs {
  public static class LeftExtractor<T> implements Function<Pair<T, ?>, T> {
    @Override
    public T apply(Pair<T, ?> input) {
      return input._1();
    }
  }

  public static class RightExtractor<F> implements Function<Pair<?, F>, F> {
    @Override
    public F apply(Pair<?, F> input) {
      return input._2();
    }
  }
}
