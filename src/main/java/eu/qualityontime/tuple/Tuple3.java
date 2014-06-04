package eu.qualityontime.tuple;

public class Tuple3<T1, T2, T3> extends Tuple {
  public final T1 _1;
  public final T2 _2;
  public final T3 _3;

  public static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 _1, T2 _2, T3 _3) {
    return new Tuple3<T1, T2, T3>(_1, _2, _3);
  }

  public Tuple3(T1 _1, T2 _2, T3 _3) {
    super();
    this._1 = _1;
    this._2 = _2;
    this._3 = _3;
  }

  @Override
  int size() {
    return 3;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_1 == null) ? 0 : _1.hashCode());
    result = prime * result + ((_2 == null) ? 0 : _2.hashCode());
    result = prime * result + ((_3 == null) ? 0 : _3.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    @SuppressWarnings("unchecked")
    Tuple3<T1, T2, T3> other = (Tuple3<T1, T2, T3>) obj;
    if (_1 == null) {
      if (other._1 != null)
        return false;
    }
    else if (!_1.equals(other._1))
      return false;
    if (_2 == null) {
      if (other._2 != null)
        return false;
    }
    else if (!_2.equals(other._2))
      return false;
    if (_3 == null) {
      if (other._3 != null)
        return false;
    }
    else if (!_3.equals(other._3))
      return false;
    return true;
  }
}
