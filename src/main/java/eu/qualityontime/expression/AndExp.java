package eu.qualityontime.expression;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class AndExp extends CompositeExp {

  public AndExp() {
    super(CompositeOp.AND);
  }

  public AndExp(List<? extends ConditionalExp> exps) {
    this();
    setExps(exps);
  }

  public AndExp(ConditionalExp... exps) {
    this(ImmutableList.copyOf(exps));
  }

}
