package eu.qualityontime;

import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

public class SerializationUtilsTest {

  private static class A implements Serializable {
    Date d;
    int i;
    String s;

    public Date getD() {
      return d;
    }

    public void setD(Date d) {
      this.d = d;
    }

    public int getI() {
      return i;
    }

    public void setI(int i) {
      this.i = i;
    }

    public String getS() {
      return s;
    }

    public void setS(String s) {
      this.s = s;
    }
  }

  private static class B implements Serializable {
    int y;
    A a;

    public int getY() {
      return y;
    }

    public void setY(int y) {
      this.y = y;
    }

    public A getA() {
      return a;
    }

    public void setA(A a) {
      this.a = a;
    }
  }

  @Test
  public void testSerialization() {
    String date = "01/01/2012";
    A a = new A();
    a.setD(AppDate.date(date));
    a.setI(10);
    a.setS("string");

    A aclone = (A) SerializationUtils.clone(a);

    assertNotNull(aclone.getD());
    assertNotNull(aclone.getI());
    assertNotNull(aclone.getS());

  }

  @Test
  public void testNestedSerialization() {
    String date = "01/01/2012";
    A a = new A();
    a.setD(AppDate.date(date));
    a.setI(10);
    a.setS("string");

    B b = new B();
    b.setA(a);
    b.setY(20);

    B bclone = (B) SerializationUtils.clone(b);

    assertNotNull(bclone.getA());
    assertNotNull(bclone.getA().getD());
    assertNotNull(bclone.getA().getI());
    assertNotNull(bclone.getA().getS());
    assertNotNull(bclone.getY());

  }
}
