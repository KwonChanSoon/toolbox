package eu.qualityontime.tuple;

/**
 * Because commond-lang 3.0 is not avaialble in EP.
 */
public class ImmutablePair<L, R> extends Pair<L, R> {

  public final L _1;
  public final R _2;

  public static <L, R> ImmutablePair<L, R> of(L left, R right) {
    return new ImmutablePair<L, R>(left, right);
  }

  public ImmutablePair(L left, R right) {
    super();
    this._1 = left;
    this._2 = right;
  }

  @Override
  public L getLeft() {
    return _1;
  }

  @Override
  public R getRight() {
    return _2;
  }

  @Override
  public L _1() {
    return _1;
  }

  @Override
  public R _2() {
    return _2;
  }

  @Override
  public R setValue(R value) {
    throw new UnsupportedOperationException();
  }

}