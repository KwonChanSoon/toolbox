package ep.pericles.generator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BuilderGeneratorTest {

  @Test
  public void test() {
    String props = "  private int id;\n" +
        "private String name;\n" +
        "private Long long_id;\n" +
        "private String[] values;\n" +
        "private ChildBean childObject;\n" +
        "private List<ChildBean> others;";
    //@formatter:off
    String expected = "" +
    		"class SampleBeanBuilder{\n"+
"  private int id;\n"+
"  private String name;\n"+
"  private Long long_id;\n"+
"  private String[] values;\n"+
"  private ChildBean childObject;\n"+
"  private List<ChildBean> others;\n"+
"  public SampleBeanBuilder id(int id){\n"+
"    this.id=id;\n"+
"    return this;\n"+
"  }\n"+
"  public SampleBeanBuilder name(String name){\n"+
"    this.name=name;\n"+
"    return this;\n"+
"  }\n"+
"  public SampleBeanBuilder long_id(Long long_id){\n"+
"    this.long_id=long_id;\n"+
"    return this;\n"+
"  }\n"+
"  public SampleBeanBuilder values(String[] values){\n"+
"    this.values=values;\n"+
"    return this;\n"+
"  }\n"+
"  public SampleBeanBuilder childObject(ChildBean childObject){\n"+
"    this.childObject=childObject;\n"+
"    return this;\n"+
"  }\n"+
"  public SampleBeanBuilder others(List<ChildBean> others){\n"+
"    this.others=others;\n"+
"    return this;\n"+
"  }\n"+
"  public SampleBean build(){\n"+
"    SampleBean inst = new SampleBean();\n"+
"    inst.id = id;\n"+
"    inst.name = name;\n"+
"    inst.long_id = long_id;\n"+
"    inst.values = values;\n"+
"    inst.childObject = childObject;\n"+
"    inst.others = others;\n"+
"\n"+
"    return inst;\n"+
"  }\n"+
"}\n";
    //@formatter:on

    String s = new BuilderGenerator().generate("SampleBean", props);
    //System.out.println(s);
    assertEquals(expected, s);

  }
}
