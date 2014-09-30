package eu.qualityontime.functionals;

import com.google.common.base.*;

public class FAdapters {

  public static <X, Y> Function<X, Y> toGuava(final F1<X, Y> in) {
    return new F1_to_GuavaFunction<X, Y>(in);
  }

  private static class F1_to_GuavaFunction<T, V> implements Function<T, V> {
    private final F1<T, V> ref;

    public F1_to_GuavaFunction(F1<T, V> ref) {
      this.ref = ref;
    }

    @Override
    public V apply(T input) {
      return ref.f(input);
    }
  }

  public static <T> Predicate<T> toGuava(final P<T> p) {
    return new Predicate<T>() {
      @Override
      public boolean apply(T in) {
        return p.p(in);
      }
    };
  }

  public static <X, Y> F1<X, Y> fromGuava(final Function<X, Y> in) {
    return new GuavaFunction_to_F1<X, Y>(in);
  }

  private static class GuavaFunction_to_F1<T, V> implements F1<T, V> {
    private final Function<T, V> ref;

    public GuavaFunction_to_F1(Function<T, V> ref) {
      this.ref = ref;
    }

    @Override
    public V f(T input) {
      return ref.apply(input);
    }
  }

}
