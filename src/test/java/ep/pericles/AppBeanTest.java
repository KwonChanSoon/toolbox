package ep.pericles;

import static org.junit.Assert.*;

import org.junit.Test;

public class AppBeanTest {

  @Test
  public void simple() {
    A a = new A();
    String val = AppBean.get(a, "nameA");
    assertEquals("AAA", val);
    assertEquals("AAA", AppBean.get(a, "nameA"));
    assertTrue(AppBean.get(a, "nameA", String.class) instanceof String);
    assertEquals("AAA", AppBean.get(a, "nameA", String.class));
  }

  @Test
  public void nested_elvis() {
    A a = new A();
    assertEquals("BBB", AppBean.get(a, "bbb.nameB"));
    assertNull(AppBean.get(a, "bbb.ccc"));
    assertNull(AppBean.get(a, "bbb.ccc?.nameC"));
    a.bbb.ccc = new C();
    assertEquals("CCC", AppBean.get(a, "bbb.ccc?.nameC"));
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

  @Test
  public void setValue() {
    A a = new A();
    AppBean.set(a, "nameA", "OTTO");
    AppBean.set(a, "bbb.nameB", "BARACK");
    assertEquals("OTTO", a.nameA);
    assertEquals("BARACK", a.bbb.nameB);
  }

  // TODO implement mapping using XML files or migrate to > Tomcat 7.x
  // cause version of Dozer which use annotations (>= 5.3.2) requires
  // javax.el-api version 2.2 which is not compatible with Tomcat 6.x 

  //  public static class Source {
  //    @Mapping("g")
  //    public String f="f";
  //  }
  //
  //  public static class SourceNested {
  //    public Source s = new Source();
  //  }
  //  
  //  public static class Dest {
  //    public Dest() {
  //    }
  //    @Mapping("f")
  //    public String g;
  //    
  //    public void setG(String g) {
  //      this.g = g;
  //    }
  //  }
  //  
  //  public static class DestNested {
  //    public DestNested() {
  //    }
  //    @Mapping("s.f")
  //    public String g;
  //    
  //    public void setG(String g) {
  //      this.g = g;
  //    }
  //  }  
  //  
  //  @Test
  //  public void test_map() {
  //    Dest d = AppBean.map(new Source(), Dest.class);
  //    assertNotNull(d);
  //    assertEquals("f", d.g);
  //  }
  //  
  //  @Test
  //  public void test_reverse_map() {
  //    Dest d = new Dest();
  //    d.setG("g");
  //    Source s = AppBean.map(d, Source.class);
  //    assertNotNull(s);
  //    assertEquals("g", s.f);
  //  }
  //  
  //  @Test
  //  public void test_nested_map() {
  //    DestNested d = AppBean.map(new SourceNested(), DestNested.class);
  //    assertNotNull(d);
  //    assertEquals("f", d.g);
  //  }
  //  
  //  @Test
  //  public void test_reverse_nested_map() {
  //    DestNested d = new DestNested();
  //    d.setG("g");
  //    SourceNested nested = AppBean.map(d, SourceNested.class);
  //    assertNotNull(nested);
  //    assertEquals("g", nested.s.f);
  //  }

}
