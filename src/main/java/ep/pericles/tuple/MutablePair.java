package ep.pericles.tuple;

public class MutablePair<L, R> extends Pair<L, R> {

  public L _1;
  public R _2;

  public static <L, R> MutablePair<L, R> of(L left, R right) {
    return new MutablePair<L, R>(left, right);
  }

  public MutablePair(L left, R right) {
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

  public void setLeft(L left) {
    this._1 = left;
  }

  public void setRight(R right) {
    this._2 = right;
  }

}