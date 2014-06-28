package eu.qualityontime.equivalencerelation;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class JakartaEquivalenceRelationSimpleTest {

  JakartaEquivalenceRelation relation = new JakartaEquivalenceRelation();

  private void eq(Object o1, Object o2) {
    assertTrue(relation.areEquals(o1, o2));
  }

  private void neq(Object o1, Object o2) {
    assertFalse(relation.areEquals(o1, o2));
  }

  @Test
  public void simpleEq() {
    eq(null, null);
    neq(null, 1);
    neq(1, null);
    eq(1, 1);
    eq(1, new Integer(1));
    eq(new Integer(1), 1);
    neq(1, 2);
    neq(1, new BigDecimal(1));
  }

  @Test
  public void objectEq() throws Exception {
    Object o = new Object();
    eq(o, o);
    neq(o, new Object());
  }

}
