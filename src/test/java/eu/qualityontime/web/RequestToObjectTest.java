package eu.qualityontime.web;

import static com.google.common.collect.ImmutableMap.of;
import static eu.qualityontime.AppCollections.Map;
import static eu.qualityontime.web.RequestToObject.getObject;
import static org.junit.Assert.*;

import java.util.*;

import org.apache.commons.beanutils.Converter;
import org.junit.Test;

import com.google.common.collect.*;

import eu.qualityontime.tuple.Pair;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RequestToObjectTest {
  /*
  data[assignments][0][booth_uid]=765285, 
  data[assignments][0][pers_uid]=16410, 
  data[assignments][1][booth_uid]=765285, 
  data[assignments][1][pers_uid]=16410, 
  data[assignments][2][booth_uid]=765285, 
  data[assignments][2][pers_uid]=28704, 
  data[assignments][3][booth_uid]=765285, 
  data[assignments][3][pers_uid]=16832, 
  data[assignments][4][booth_uid]=765285, 
  data[assignments][4][pers_uid]=16477, 
  data[assignments][5][booth_uid]=765285, 
  data[assignments][5][pers_uid]=16186, 
  data[assignments][6][booth_uid]=765285}
  data[assignments][6][pers_uid]=17734, 
  data[assignments][7][booth_uid]=765285, 
  data[assignments][7][pers_uid]=18686, 
  data[assignments][8][booth_uid]=765285, 
  data[assignments][8][pers_uid]=17735, 
  data[assignments][9][booth_uid]=765285, 
  data[assignments][9][pers_uid]=15814, 
  data[sess_uid]=201154, 
   */

  //  Map<String, String> req;

  @Test
  public void have_or_not() {
    assertNull(getObject("a", of("a", "a")));
    assertNotNull(getObject("a", of("a[b]", "1")));
  }

  @Test
  public void splitParam() throws Exception {
    assertEquals(ImmutableList.of("b"), RequestToObject.splitParams("a", "a[b]"));
    assertEquals(ImmutableList.of("b", "1", "c"), RequestToObject.splitParams("a", "a[b][1][c]"));
  }

  @Test
  public void first_level_simple() {
    assertEquals("1", getObject("a", of("a[b]", "1")).get("b"));
  }

  @Test
  public void nested_simple() {
    assertEquals("1", ((Map) getObject("a", of("a[b][c]", "1")).get("b")).get("c"));
  }

  @Test
  public void complex() {
    Map params = of("data[assignments][0][booth_uid]", "765285",
        "data[assignments][0][pers_uid]", "15814",
        "data[sess_uid]", "201154");
    Map assignment = Map();
    assignment.put("booth_uid", "765285");
    assignment.put("pers_uid", "15814");
    Map assignments = Map();
    assignments.put("0", assignment);
    Map expected = Map();
    expected.put("sess_uid", "201154");
    expected.put("assignments", assignments);
    assertEquals(expected, getObject("data", params));
  }

  @Test
  public void to_object() {
    Map a = Map();
    a.put("apple", "apple");
    A ret = RequestToObject.object(a, A.class, new ArrayList<Pair<Converter, Class<?>>>());
    assertNotNull(ret);
    assertEquals("apple", ret.apple);
  }

  @Test
  public void to_object_nested() {
    Converter b_converter = new ABeanutilsBeanDelegateConverter() {
      @Override
      public Object convert(Class type, Object value) {
        BNested b = new BNested();
        delegeConverter.copyProperties(b, value);
        return b;
      }
    };
    Converter c_converter = new ABeanutilsBeanDelegateConverter() {
      @Override
      public Object convert(Class type, Object value) {
        CNested b = new CNested();
        delegeConverter.copyProperties(b, value);
        return b;
      }
    };
    ArrayList converters = new ArrayList();
    converters.add(Pair.of(b_converter, BNested.class));
    converters.add(Pair.of(c_converter, CNested.class));

    Map a = Map();
    a.put("apple", "apple");
    a.put("b", ImmutableMap.of("fruit", "barack", "c", ImmutableMap.of("fruit", "33")));

    A ret = RequestToObject.object(a, A.class, converters);

    assertNotNull(ret);
    assertEquals("apple", ret.apple);
    assertEquals("barack", ret.b.fruit);
    assertEquals(33, ret.b.c.fruit);
  }

  public static class A {
    String apple = "alma";
    BNested b = new BNested();

    public String getApple() {
      return apple;
    }

    public void setApple(String apple) {
      this.apple = apple;
    }

    public BNested getB() {
      return b;
    }

    public void setB(BNested b) {
      this.b = b;
    }
  }

  public static class BNested {
    CNested c = new CNested();
    String fruit = "korte";

    public String getFruit() {
      return fruit;
    }

    public void setFruit(String fruit) {
      this.fruit = fruit;
    }

    public CNested getC() {
      return c;
    }

    public void setC(CNested c) {
      this.c = c;
    }
  }

  public static class CNested {
    int fruit = 11;

    public int getFruit() {
      return fruit;
    }

    public void setFruit(int fruit) {
      this.fruit = fruit;
    }

  }
}
