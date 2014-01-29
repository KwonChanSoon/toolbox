package ec.ep.pericles.service.services.impl.comparator;

import java.math.BigDecimal;

public class BaseEq {
  protected JakartaEquivalenceRelation relation = new JakartaEquivalenceRelation(getClass().getSimpleName()+"================================");

  public static class A {
    int i;
    float f;
    double d;
    Integer ii;
    Double dd;
    BigDecimal bigDecimal;
    String s;
    B b;
    String[] ss;

    public String[] getSs() {
      return ss;
    }

    public void setSs(String[] ss) {
      this.ss = ss;
    }

    public int getI() {
      return i;
    }

    public void setI(int i) {
      this.i = i;
    }

    public float getF() {
      return f;
    }

    public void setF(float f) {
      this.f = f;
    }

    public double getD() {
      return d;
    }

    public void setD(double d) {
      this.d = d;
    }

    public Integer getIi() {
      return ii;
    }

    public void setIi(Integer ii) {
      this.ii = ii;
    }

    public Double getDd() {
      return dd;
    }

    public void setDd(Double dd) {
      this.dd = dd;
    }

    public BigDecimal getBigDecimal() {
      return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
      this.bigDecimal = bigDecimal;
    }

    public String getS() {
      return s;
    }

    public void setS(String s) {
      this.s = s;
    }

    public B getB() {
      return b;
    }

    public void setB(B b) {
      this.b = b;
    }

  }

  public static class B {
    String name;
    A a;

    public B() {
    }

    public B(String string) {
      name = string;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public A getA() {
      return a;
    }

    public void setA(A a) {
      this.a = a;
    }
  }

}
