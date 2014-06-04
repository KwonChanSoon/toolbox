package eu.qualityontime.base;

import com.google.common.base.Function;

/*
 * consider to review: http://benjiweber.co.uk/blog/2013/11/29/try-as-expression-in-java-8/
 */
/**
 * Functional Exception handling like in Scala.
 */
public abstract class Try<T> {
  /**
   * Returns the value from this Success or throws the exception if this is a Failure.
   */
  public abstract T get();

  /**
   * Returns true if the Try is a Failure, false otherwise.
   */
  public abstract boolean isFailure();

  /**
   * Returns true if the Try is a Success, false otherwise.
   */
  public abstract boolean isSuccess();

  /**
   * Maps the given function to the value from this Success or returns this if this is a Failure.
   */
  public abstract <U> Try<U> map(Function<T, U> mapper);

  /**
   * Returns the given function applied to the value from this Success or returns this if this is a Failure.
   */
  public abstract <U> Try<U> flatMap(Function<T, Try<U>> mapper);

  /**
   * Applies the given function f if this is a Failure, otherwise returns this if this is a Success. This is like map for the exception.
   */
  public abstract <U extends T> Try<U> recover(Recovery<U> mapper);

  /**
   * Applies the given function f if this is a Failure, otherwise returns this if this is a Success. This is like flatMap for the exception.
   */
  public abstract <U extends T> Try<U> recoverWith(Recovery<Try<U>> mapper);

  public static <T> Failure<T> of(Throwable t) {
    return new Failure<T>(t);
  }

  public static <T> Success<T> of(T value) {
    return new Success<T>(value);
  }

  static class Failure<T> extends Try<T> {
    public final Throwable t;

    public Failure(Throwable t) {
      this.t = t;
    }

    @Override
    public T get() {
      if (t instanceof RuntimeException)
        throw (RuntimeException) t;
      throw new RuntimeException(t);
    }

    @Override
    public boolean isFailure() {
      return true;
    }

    @Override
    public boolean isSuccess() {
      return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> Try<U> map(Function<T, U> mapper) {
      return (Try<U>) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> Try<U> flatMap(Function<T, Try<U>> mapper) {
      return (Try<U>) this;
    }

    @Override
    public <U extends T> Try<U> recover(Recovery<U> mapper) {
      return Try.of(mapper.apply(t));
    }

    @Override
    public <U extends T> Try<U> recoverWith(Recovery<Try<U>> mapper) {
      return mapper.apply(t);
    }

  }

  static class Success<T> extends Try<T> {
    public final T value;

    public Success(T value) {
      this.value = value;
    }

    @Override
    public T get() {
      return value;
    }

    @Override
    public boolean isFailure() {
      return false;
    }

    @Override
    public boolean isSuccess() {
      return true;
    }

    @Override
    public <U> Try<U> map(Function<T, U> mapper) {
      return Try.of(mapper.apply(value));
    }

    @Override
    public <U> Try<U> flatMap(Function<T, Try<U>> mapper) {
      return mapper.apply(value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U extends T> Try<U> recover(Recovery<U> mapper) {
      return (Try<U>) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U extends T> Try<U> recoverWith(Recovery<Try<U>> mapper) {
      return (Try<U>) this;
    }
  }

  public static interface Recovery<T> extends Function<Throwable, T> {
  };
}
