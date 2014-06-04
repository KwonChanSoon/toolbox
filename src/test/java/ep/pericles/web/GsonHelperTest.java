package ep.pericles.web;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.Map;

import org.junit.Test;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;

public class GsonHelperTest {

  @Test
  public void map_from_json_simple() {
    Type mapType = new TypeToken<Map<String, Integer>>() {}.getType();
    Map<?, ?> r = GsonHelper.fromJson("{'sess_uid':12}", mapType);
    assertNotNull(r);
    assertEquals(12, r.get("sess_uid"));
  }

  @Test
  public void map_from_json_subobject() {
    Type mapType = new TypeToken<Map<String, Map<String, String>>>() {}.getType();
    Map<String, Map<String, String>> r = GsonHelper.fromJson("{ data : { field1 : 'value1', field2 : 'value2'}}", mapType);
    assertNotNull(r);
    assertNotNull(r.get("data"));
    assertNotNull(r.get("data").get("field1"));
  }

  @Test
  public void map_from_json_mixed_subobject() {
    String json = "{sess_uid:12, data : { field1 : 'value1', field2 : 'value2'}}";
    JsonElement je = new JsonParser().parse(json);
    assertNotNull(je);
    //System.out.println(je);
    Map<?, ?> r = GsonHelper.fromJsonToMap(json);
    assertNotNull(r);
    //System.out.println(r);
  }

  @Test
  public void map_from_json_withArray() {
    String json = "{sess_uid:12, assignments:[{booth_uid:23, pers_uid:34},{}]}";
    JsonElement je = new JsonParser().parse(json);
    assertNotNull(je);
    //System.out.println(je);
    Map<?, ?> r = GsonHelper.fromJsonToMap(json);
    assertNotNull(r);
    //System.out.println(r);
  }

  @Test
  public void Gson_Circular_reference() {
    AA a = new AA();
    BB b = new BB();
    a.bb = b;
    b.aa = a;
    String actual = GsonHelper.toJson(a);
    assertNotNull(actual);
  }

  public static class AA {
    public String aa = "apple";
    public BB bb;
  }

  public static class BB {
    public String bb = "barack";
    public transient AA aa;
  }
}
