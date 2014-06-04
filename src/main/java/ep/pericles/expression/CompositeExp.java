package ep.pericles.expression;

import java.util.List;

import com.google.common.base.*;
import com.google.common.collect.Iterables;

public abstract class CompositeExp extends ConditionalExp {
  private CompositeOp op;
  private List<? extends ConditionalExp> exps;

  public CompositeExp(CompositeOp op) {
    this.op = op;
  }

  public CompositeExp(CompositeOp op, List<? extends ConditionalExp> exps) {
    this.op = op;
    this.exps = exps;
  }

  public CompositeExp(List<? extends ConditionalExp> exps) {
    this.exps = exps;
  }

  @Override
  public String toString() {
    return Joiner.on(" " + op.toString() + " ").join(Iterables.transform(exps, Functions.toStringFunction()));
  }

  public List<? extends ConditionalExp> getExps() {
    return exps;
  }

  public void setExps(List<? extends ConditionalExp> exps) {
    this.exps = exps;
  }

  public CompositeOp getOp() {
    return op;
  }

}
