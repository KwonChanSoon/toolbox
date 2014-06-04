package ep.pericles;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gson.Gson;

public class GsonExperimentTest {

  @Test
  public void toJson() {
    Gson g = new Gson();
    String res = g.toJson(new A());
    //    System.out.println(res);
    assertEquals("{\"apple\":\"alma\",\"nr\":12,\"tuple\":[\"Otto\",\"Timi\"],\"otuple\":[\"Sample\",{\"name\":\"B class\"}]}",
        res);
  }

  @Test
  public void fromJson() {
    Gson g = new Gson();
    A a = g.fromJson("{apple:\"korte\"}", A.class);
    assertEquals("korte", a.apple);
    a = g.fromJson("{b:{name:\"Takacs\"}}", A.class);
    assertEquals("Takacs", a.b.name);
  }

  @SuppressWarnings("unused")
  public static class A {
    private String apple = "alma";
    private int nr = 12;
    private String[] tuple = new String[] { "Otto", "Timi" };
    private Object[] otuple = new Object[] { "Sample", new B() };
    private B b;
  }

  public static class B {
    private String name = "B class";
  }
}
