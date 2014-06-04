package eu.qualityontime;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import eu.qualityontime.type.Message;

/**
 * Learning test
 */
public class ExpressionParserTest {

  @Test
  public void simple() {
    ExpressionParser parser = new SpelExpressionParser();
    Expression exp = parser.parseExpression("'Hello World'");
    String message = (String) exp.getValue();
    assertEquals("Hello World", message);
  }

  @Test
  public void simple_context_variable() {
    Message m = new Message(null, "Otto");
    ExpressionParser parser = new SpelExpressionParser();
    Expression exp = parser.parseExpression("text");
    EvaluationContext context = new StandardEvaluationContext(m);
    String message = (String) exp.getValue(context);
    //System.out.println(message);
    assertEquals("Otto", message);
  }

  @Test
  public void simple_safe_navigation() {
    A a = new A();
    assertEquals("AAA", eval(a, "nameA"));
    assertEquals("BBB", eval(a, "bbb.nameB"));
    assertNull(eval(a, "bbb.ccc"));
    assertNull(eval(a, "bbb.ccc?.nameC"));
    a.bbb.ccc = new C();
    assertEquals("CCC", eval(a, "bbb.ccc?.nameC"));
  }

  private String eval(Object a, String propertyPath) {
    ExpressionParser parser = new SpelExpressionParser();
    Expression exp = parser.parseExpression(propertyPath);
    EvaluationContext context = new StandardEvaluationContext(a);
    String message = (String) exp.getValue(context);
    return message;
  }

  class A {
    public String nameA = "AAA";
    public B bbb = new B();
  }

  class B {
    public String nameB = "BBB";
    public C ccc;
  }

  class C {
    public String nameC = "CCC";
  }

}
