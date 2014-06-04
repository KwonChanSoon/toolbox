package ep.pericles;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.Objects;

public class AppNumberTest {
  @Test
  public void long_GreaterThan_Wo_AppNumber() {
    Long l1 = Long.valueOf(2);
    Long l2 = new Long(1L);
    assertTrue(l1 > l2);
  }

  @Test
  public void long_Equal_Wo_AppNumber() {
    Long l1 = Long.valueOf(1);
    Long l2 = new Long(1L);
    assertFalse(l1 == l2);//!!!!
    assertTrue(l1.equals(l2));
    assertTrue(Objects.equal(l1, l2));
  }

  @Test
  public void long_literare_Equals_Wo_AppNumber() {
    Long l1 = new Long(1L);
    assertTrue(1 == l1);
    assertFalse(Objects.equal(1, l1));//!!!!!
    assertFalse(Objects.equal(l1, 1));//!!!!!
    assertTrue(Objects.equal(l1, 1L));//!!!!!
  }

  @Test
  public void long_GreaterThan() {
    Long l1 = Long.valueOf(2);
    Long l2 = new Long(1L);
    assertTrue(AppNumber.gt(l1, l2));
  }

  @Test
  public void long_LessThan() {
    Long l1 = Long.valueOf(2);
    Long l2 = new Long(11L);
    assertTrue(AppNumber.lt(l1, l2));
    assertTrue(AppNumber.lt(1, 2));
  }

  @Test
  public void long_Equal() {
    Long l1 = Long.valueOf(1);
    Long l2 = new Long(1L);
    assertFalse(l1 == l2);//!!!!
    assertTrue(AppNumber.equal(l1, l2));
  }

  @Test
  public void long_literare_Equals_W_AppNumber() {
    Long l1 = new Long(1L);
    assertTrue(1 == l1);
    assertFalse(Objects.equal(1, l1));//!!!!!
    assertFalse(Objects.equal(l1, 1));//!!!!!
    assertTrue(Objects.equal(l1, 1L));//!!!!!

    //!!primitive literal int and Long
    assertTrue(AppNumber.eq(1, l1));
    assertTrue(AppNumber.eq(l1, 1));
  }
}
