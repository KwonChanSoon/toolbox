package ep.pericles;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

import org.apache.commons.lang3.reflect.ConstructorUtils;

import com.google.common.base.*;

public class AppObjects {
  public static <T> T defaultIfNull(T val, Supplier<T> factory) {
    Preconditions.checkNotNull(factory);
    if (null == val) {
      return factory.get();
    }
    return val;
  }

  public static <T> T defaultIfNull(T val, T def) {
    return Objects.firstNonNull(val, def);
  }

  @SuppressWarnings("unchecked")
  // at your own risk
  public static <T> T cast(Object o) {
    return (T) o;
  }

  @SuppressWarnings("unchecked")
  public static <T> List<T> cast(List<?> o) {
    return (List<T>) o;
  }

  @SuppressWarnings("unchecked")
  public static <T> Collection<T> cast(Collection<?> o) {
    return (Collection<T>) o;
  }

  @SuppressWarnings("unchecked")
  public static <T> Iterable<T> cast(Iterable<?> o) {
    return (Iterable<T>) o;
  }

  public static final Long longValue(BigDecimal bd) {
    return null == bd ? null : bd.longValue();
  }

  public static <T> T invokeConstructor(Class<T> cls, Object... args) {
    try {
      return cls.cast(ConstructorUtils.invokeConstructor(cls, args));
    } catch (NoSuchMethodException e) {
      throw Throwables.propagate(e);
    } catch (IllegalAccessException e) {
      throw Throwables.propagate(e);
    } catch (InvocationTargetException e) {
      throw Throwables.propagate(e);
    } catch (InstantiationException e) {
      throw Throwables.propagate(e);
    }
  }

  public static <T> Optional<T> Optional(T val) {
    return Optional.of(val);
  }

  public static <T> Optional<T> OptionalAbsent(Class<T> type) {
    return Optional.absent();
  }
}
