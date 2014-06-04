package ep.pericles.tuple;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * Because commond-lang 3.0 is not avaialble in EP.
 */
public abstract class Pair<L, R> extends Tuple implements Map.Entry<L, R>, Comparable<Pair<L, R>>, Serializable {
  public static <L, R> Pair<L, R> of(L left, R right) {
    return new ImmutablePair<L, R>(left, right);
  }

  public static <L, R> Pair<L, R> pair(L left, R right) {
    return of(left, right);
  }

  public static <L, R> Pair<L, R> mutable(L left, R right) {
    return new MutablePair<L, R>(left, right);
  }

  public abstract L getLeft();

  public abstract R getRight();

  public abstract L _1();

  public abstract R _2();

  @Override
  public final L getKey() {
    return getLeft();
  }

  @Override
  public R getValue() {
    return getRight();
  }

  @Override
  public int compareTo(Pair<L, R> other) {
    return new CompareToBuilder().append(getLeft(), other.getLeft()).append(getRight(), other.getRight())
        .toComparison();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof Map.Entry<?, ?>) {
      Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
      return ObjectUtils.equals(getKey(), other.getKey()) && ObjectUtils.equals(getValue(), other.getValue());
    }
    return false;
  }

  @Override
  public int hashCode() {
    // see Map.Entry API specification
    return (getKey() == null ? 0 : getKey().hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode());
  }

  @Override
  public String toString() {
    return new StringBuilder().append('(').append(getLeft()).append(',').append(getRight()).append(')').toString();
  }

  public String toString(String format) {
    return String.format(format, getLeft(), getRight());
  }

  @Override
  public int size() {
    return 2;
  }

}