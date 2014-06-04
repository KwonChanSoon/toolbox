package eu.qualityontime.web;

import static org.junit.Assert.*;

import org.junit.Test;

public class DoublerowAlteringColorTest {

  @Test
  public void test() {
    int rowId = 1;
    assertFalse((((rowId++) - 1) & 2) == 2);
    assertFalse((((rowId++) - 1) & 2) == 2);
    assertTrue((((rowId++) - 1) & 2) == 2);
    assertTrue((((rowId++) - 1) & 2) == 2);
    assertFalse((((rowId++) - 1) & 2) == 2);
    assertFalse((((rowId++) - 1) & 2) == 2);
  }

  @Test
  public void test_helper() {
    int rowId = 1;
    assertEquals("even", DoubleRowAlterintColorHelper.doubleRowcolor(rowId++, "even", "odd"));
    assertEquals("even", DoubleRowAlterintColorHelper.doubleRowcolor(rowId++, "even", "odd"));
    assertEquals("odd", DoubleRowAlterintColorHelper.doubleRowcolor(rowId++, "even", "odd"));
    assertEquals("odd", DoubleRowAlterintColorHelper.doubleRowcolor(rowId++, "even", "odd"));
    assertEquals("even", DoubleRowAlterintColorHelper.doubleRowcolor(rowId++, "even", "odd"));
    assertEquals("even", DoubleRowAlterintColorHelper.doubleRowcolor(rowId++, "even", "odd"));
    assertEquals("odd", DoubleRowAlterintColorHelper.doubleRowcolor(rowId++, "even", "odd"));
    assertEquals("odd", DoubleRowAlterintColorHelper.doubleRowcolor(rowId++, "even", "odd"));
  }

}
