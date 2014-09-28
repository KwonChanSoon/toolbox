package eu.qualityontime.diff;

import java.math.BigDecimal;
import java.util.*;

import lombok.*;

public class DiffSampleMother {
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class C {
    private String name;
    private BigDecimal money;
    private Date valueDate;
    private List<Long> termRefs;
    private List<B> babSet;
    private List<A> assSet;

    public C termRefs(List<Long> vals) {
      setTermRefs(vals);
      return this;
    }

    public C babSet(List<B> babSet) {
      this.setBabSet(babSet);
      return this;
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class A {
    private Integer i;
    private String s;
    private B b;

    public A(Integer i, String s) {
      this.i = i;
      this.s = s;
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class B {
    private String name;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Parent {
    private List<C> ccc;
  }
}
