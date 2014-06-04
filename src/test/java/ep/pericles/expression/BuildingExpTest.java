package ep.pericles.expression;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class BuildingExpTest {

  @Test
  public void basic() {
    Exp e = new SimpleConditionExp() {
      {
        field = new FieldExp("field1");
        op = BinaryOp.EQ;
        value = new LiteralExp("Otto");
      }
    };
    assertEquals("FIELD[field1] EQ VALUE[Otto]", e.toString());
  }

  @Test
  public void notCondition() {
    Exp e = new NotExp() {
      {
        setExp(new SimpleConditionExp() {
          {
            setField(new FieldExp("field1"));
            setOp(BinaryOp.EQ);
            setValue(new LiteralExp("Otto"));
          }
        });
      }
    };
    assertEquals("NOT FIELD[field1] EQ VALUE[Otto]", e.toString());
  }

  @Test
  public void SimpleAndCondition() {
    Exp e = new AndExp(ImmutableList.of(new SimpleConditionExp() {
      {
        field = new FieldExp("field1");
        op = BinaryOp.EQ;
        value = new LiteralExp("Otto");
      }
    }));
    assertEquals("FIELD[field1] EQ VALUE[Otto]", e.toString());
  }

  @Test
  public void MultimpleAndCondition() {
    Exp e = new AndExp(ImmutableList.of(new SimpleConditionExp() {
      {
        field = new FieldExp("field1");
        op = BinaryOp.EQ;
        value = new LiteralExp("Otto");
      }
    }, new SimpleConditionExp() {
      {
        field = new FieldExp("field2");
        op = BinaryOp.EQ;
        value = new LiteralExp("Takacs");
      }
    }));
    assertEquals("FIELD[field1] EQ VALUE[Otto] AND FIELD[field2] EQ VALUE[Takacs]", e.toString());
  }

  @Test
  public void AndWithBraces() {
    Exp e = new AndExp(ImmutableList.of(new PrecedenceExp(new SimpleConditionExp() {
      {
        field = new FieldExp("field1");
        op = BinaryOp.EQ;
        value = new LiteralExp("Otto");
      }
    }), new SimpleConditionExp() {
      {
        field = new FieldExp("field2");
        op = BinaryOp.EQ;
        value = new LiteralExp("Takacs");
      }
    }));
    assertEquals("(FIELD[field1] EQ VALUE[Otto]) AND FIELD[field2] EQ VALUE[Takacs]", e.toString());
  }

  @Test
  public void Function() throws Exception {
    Exp e = new FunctionExp("fn", new LiteralExp("Otto"), new LiteralExp("Takacs"));
    assertEquals("fn(VALUE[Otto], VALUE[Takacs])", e.toString());
  }

  @Test
  public void Var() {
    Exp e = new SimpleConditionExp() {
      {
        field = new FieldExp("field1");
        op = BinaryOp.EQ;
        value = new VariableExp("var_1");
      }
    };
    assertEquals("FIELD[field1] EQ VAR[var_1]", e.toString());
  }

}
