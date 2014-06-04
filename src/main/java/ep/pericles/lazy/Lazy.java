package ep.pericles.lazy;

import com.google.common.base.Throwables;

import ep.pericles.type.Procedure;

public class Lazy<T> {
  private T reference;
  private boolean initialized = false;

  public static <T> Lazy<T> of(T value) {
    return new Lazy<T>(value);
  }

  public static <T> Lazy<T> of() {
    return new Lazy<T>();
  }

  /**
   * same as of() but avoid casting when generics is used.
   */
  public static <T> Lazy<T> of(Class<T> clazz) {
    return new Lazy<T>();
  }

  private Lazy() {
  }

  private Lazy(T value) {
    this.reference = value;
    this.initialized = true;
  }

  public boolean isInitialized() {
    return initialized;
  }

  public void set(T value) {
    this.reference = value;
    if (check_needed.get()) {
      this.initialized = true;
    }
  }

  public T get() {
    if (check_needed.get() && !initialized) {
      throw new IllegalStateException("Attribute is not initialized. Configure fetching parameters.");
    }
    return this.reference;
  }

  private static ThreadLocal<Boolean> check_needed = new ThreadLocal<Boolean>() {
    @Override
    protected Boolean initialValue() {
      return Boolean.TRUE;
    }
  };

  public static void withoutCheck(Procedure p) {
    check_needed.set(Boolean.FALSE);
    try {
      p.execute();
    } catch (Exception e) {
      Throwables.propagateIfPossible(e);
    } finally {
      check_needed.set(Boolean.TRUE);
    }
  }
}
