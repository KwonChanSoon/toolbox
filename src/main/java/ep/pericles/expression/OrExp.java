package ep.pericles.expression;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class OrExp extends CompositeExp {
  public OrExp(CompositeOp op) {
    super(CompositeOp.OR);
  }

  public OrExp(List<? extends ConditionalExp> exps) {
    super(CompositeOp.OR, exps);
  }

  public OrExp(ConditionalExp... exps) {
    super(CompositeOp.OR, ImmutableList.copyOf(exps));
  }

}
