package ep.pericles.expression.interpreter;

import static ep.pericles.test.AssertString.assertLike;
import static ep.pericles.test.AssertThrowable.assertThrowable;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.*;

import com.google.common.collect.*;

import ep.pericles.Defect;
import ep.pericles.expression.*;
import ep.pericles.type.Procedure;

public class ExpSqlInterpreterTest {
  ExpSqlInterpreter interp;

  @Before
  public void setUp() {
    interp = new ExpSqlInterpreter();
  }

  @Test
  public void simple_field() {
    Exp e = new FieldExp("field1");
    e.accept(interp);
    String sql = interp.getSql();
    //Map<String, ?> param = interp.getParams();
    assertLike("field1", sql);
  }

  @Test
  public void translated_field() {
    interp.setFieldMapping(ImmutableMap.of("field1", "custom_field_name"));
    Exp e = new FieldExp("field1");
    e.accept(interp);
    String sql = interp.getSql();
    //Map<String, ?> param = interp.getParams();
    assertLike("custom_field_name", sql);
  }

  @Test
  public void simpleConditionString() {
    Exp e = new SimpleConditionExp() {
      {
        this.field = new FieldExp("field1");
        this.op = BinaryOp.EQ;
        this.value = new LiteralExp("Otto");
      }
    };
    e.accept(interp);
    String sql = interp.getSql();
    Map<String, ?> param = interp.getParams();
    assertLike("field1 = :xyz_1", sql);
    assertTrue(param.keySet().contains("xyz_1"));
    assertEquals("Otto", param.get("xyz_1"));
  }

  @Test
  public void simpleConditionNumber() {
    Exp e = new SimpleConditionExp() {
      {
        this.field = new FieldExp("field1");
        this.op = BinaryOp.EQ;
        this.value = new LiteralExp(Long.valueOf(11L));
      }
    };
    e.accept(interp);
    String sql = interp.getSql();
    Map<String, ?> param = interp.getParams();
    assertLike("field1 = :xyz_1", sql);
    assertTrue(param.keySet().contains("xyz_1"));
    assertEquals(Long.valueOf(11L), param.get("xyz_1"));
  }

  @Test
  public void Not() {
    Exp e = new NotExp(new SimpleConditionExp() {
      {
        this.field = new FieldExp("field1");
        this.op = BinaryOp.EQ;
        this.value = new LiteralExp(Long.valueOf(11L));
      }
    });
    e.accept(interp);
    String sql = interp.getSql();
    Map<String, ?> param = interp.getParams();
    assertLike("NOT (field1 = :xyz_1)", sql);
    assertEquals(Long.valueOf(11L), param.get("xyz_1"));
  }

  @Test
  public void MultipleAndCondition() {
    Exp e = new AndExp(ImmutableList.of(new SimpleConditionExp() {
      {
        field = new FieldExp("field1");
        op = BinaryOp.EQ;
        value = new LiteralExp("Otto");
      }
    }, new SimpleConditionExp() {
      {
        field = new FieldExp("field1");
        op = BinaryOp.EQ;
        value = new LiteralExp("Takacs");
      }
    }));
    e.accept(interp);
    String sql = interp.getSql();
    Map<String, ?> param = interp.getParams();
    assertLike("field1 = :xyz_1 AND field1 = :xyz_2", sql);
    assertEquals("Otto", param.get("xyz_1"));
    assertEquals("Takacs", param.get("xyz_2"));
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
    e.accept(interp);
    String sql = interp.getSql();
    Map<String, ?> param = interp.getParams();
    assertLike("(field1 = :xyz_1) AND field2 = :xyz_2", sql);
    assertEquals("Otto", param.get("xyz_1"));
    assertEquals("Takacs", param.get("xyz_2"));
  }

  @Test
  public void FunctionNoMapping() throws Exception {
    assertThrowable(Defect.class, "No mapping for function 'fn'", new Procedure() {
      public void execute() throws Exception {
        Exp e = new FunctionExp("fn", new LiteralExp("Otto"), new LiteralExp("Takacs"));
        e.accept(interp);
      }
    });
  }

  @Test
  public void FunctionNoParam() throws Exception {
    Map<String, String> functionMapping = ImmutableMap.of("fn", "fn_replacement");
    interp.setFunctionMapping(functionMapping);
    Exp e = new FunctionExp("fn", new LiteralExp("Otto"), new LiteralExp("Takacs"));
    e.accept(interp);
    String sql = interp.getSql();
    assertEquals("fn_replacement", sql);
  }

  @Test
  public void FunctionWithParam() throws Exception {
    Map<String, String> functionMapping = ImmutableMap.of("fn", "fn_replacement(${0}, ${1})");
    interp.setFunctionMapping(functionMapping);
    Exp e = new FunctionExp("fn", new LiteralExp("Otto"), new LiteralExp("Takacs"));
    e.accept(interp);
    String sql = interp.getSql();
    assertEquals("fn_replacement(:xyz_fn1_0, :xyz_fn1_1)", sql);
    Map<String, ?> param = interp.getParams();
    assertEquals("Otto", param.get("xyz_fn1_0"));
    assertEquals("Takacs", param.get("xyz_fn1_1"));
  }

  @Test
  public void FunctionMultipleCallOfTheSame() throws Exception {
    Map<String, String> functionMapping = ImmutableMap.of("fn", "fn_repl(${0}, ${1})", "fn2", "fn2_repl(${0})");
    interp.setFunctionMapping(functionMapping);
    Exp e = new AndExp(
        new FunctionExp("fn", new LiteralExp("Otto"), new LiteralExp("Takacs")),
        new FunctionExp("fn2", new LiteralExp("Eric")),
        new FunctionExp("fn", new LiteralExp("Goran"), new LiteralExp("Markovich"))
        );
    e.accept(interp);
    String sql = interp.getSql();
    assertEquals("fn_repl(:xyz_fn1_0, :xyz_fn1_1) AND fn2_repl(:xyz_fn21_0) AND fn_repl(:xyz_fn2_0, :xyz_fn2_1)", sql);
    Map<String, ?> param = interp.getParams();
    assertEquals("Otto", param.get("xyz_fn1_0"));
    assertEquals("Takacs", param.get("xyz_fn1_1"));
  }

  @Test
  public void VarNotBound() throws Exception {
    assertThrowable(IllegalArgumentException.class, "No variable bound to `var_1`", new Procedure() {

      @Override
      public void execute() throws Exception {
        Exp e = new SimpleConditionExp() {
          {
            field = new FieldExp("field1");
            op = BinaryOp.EQ;
            value = new VariableExp("var_1");
          }
        };
        e.accept(interp);
      }
    });
  }

  @Test
  public void VarWithBound() throws Exception {
    IVariableContext ctx = new MapVariableContext(ImmutableMap.of("var_1", 11));
    interp.setContext(ctx);
    Exp e = new SimpleConditionExp() {
      {
        field = new FieldExp("field1");
        op = BinaryOp.EQ;
        value = new VariableExp("var_1");
      }
    };
    e.accept(interp);
    String sql = interp.getSql();
    assertEquals("field1 = :xyz_1", sql);
    Map<String, ?> param = interp.getParams();
    assertNotNull(param.get("xyz_1"));
    assertEquals(Integer.valueOf(11), param.get("xyz_1"));
  }
}
