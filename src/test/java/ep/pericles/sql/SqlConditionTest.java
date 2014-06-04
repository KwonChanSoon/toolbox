package ep.pericles.sql;

import static ep.pericles.sql.SqlCondition.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SqlConditionTest {

  @Test
  public void simple() {
    assertEquals("a", condition("a").getStatement());
  }

  @Test
  public void and() {
    assertEquals("a AND b", condition("a").AND("b").getStatement());
    assertEquals("a AND b AND c", condition("a").AND("b").AND("c").getStatement());
  }

  @Test
  public void or() {
    assertEquals("a OR b", condition("a").OR("b").getStatement());
    assertEquals("a OR b OR c", condition("a").OR("b").OR("c").getStatement());
  }

  @Test
  public void and_or() {
    assertEquals("a AND b OR c", condition("a").AND(OR("b", "c")).getStatement());
  }

  @Test
  public void or_and() {
    assertEquals("(a OR b) AND c", condition("a").OR("b").AND("c").getStatement());
  }

  @Test
  public void brackets() {
    assertEquals("(a AND b) OR (c AND d)", AND("a", "b").OR(AND("c", "d")).getStatement());
  }
}
