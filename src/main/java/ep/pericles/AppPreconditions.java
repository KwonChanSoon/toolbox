package ep.pericles;

import java.util.Collection;

import com.google.common.base.*;
import com.google.common.collect.Iterators;

public class AppPreconditions {
  public static void checkNotNull(Object reference) {
    Preconditions.checkNotNull(reference, "Parameter cannot be NULL");
  }

  public static void checkNotNull(String message, Object reference) {
    Preconditions.checkNotNull(reference, message);
  }

  public static void checkNotNull(String message, Object... references) {
    for (Object object : references) {
      Preconditions.checkNotNull(object, message);
    }
  }

  public static void checkNotNull(Object... references) {
    for (Object object : references) {
      Preconditions.checkNotNull(object, "Parameters cannot be NULL");
    }
  }

  public static void checkArgument(boolean expr) {
    Preconditions.checkArgument(expr);
  }

  public static void check(boolean expr, String message) {
    if (!expr) {
      throw new IllegalArgumentException(message);
    }
  }

  public static <T> void checkValueSet(final T in, Collection<T> list) {
    AppPreconditions.checkNotNull(in, list);
    if (!Iterators.any(list.iterator(), new Predicate<T>() {
      @Override
      public boolean apply(T input) {
        return in.equals(input);
      }
    })) {
      throw new RuntimeException("Value `" + in + "` is not belonging to value set if '" + list + "'");
    }
  }
}
