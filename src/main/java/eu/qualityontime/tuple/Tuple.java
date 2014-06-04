package eu.qualityontime.tuple;

import org.apache.commons.lang3.builder.*;

import eu.qualityontime.AppReflection;

public abstract class Tuple {
  abstract int size();

  public Object get(int index) {
    if (index < size()) {
      return AppReflection.getField(this, "_" + (index + 1));
    }
    throw new IndexOutOfBoundsException("not supported tuple index");
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }

  public static <T1, T2> Pair<T1, T2> of(T1 _1, T2 _2) {
    return ImmutablePair.of(_1, _2);
  }

  public static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 _1, T2 _2, T3 _3) {
    return new Tuple3<T1, T2, T3>(_1, _2, _3);
  }

  public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 _1, T2 _2, T3 _3, T4 _4) {
    return new Tuple4<T1, T2, T3, T4>(_1, _2, _3, _4);
  }
}
