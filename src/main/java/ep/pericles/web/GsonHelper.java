package ep.pericles.web;

import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;

import com.google.gson.*;

import ep.pericles.*;
import ep.pericles.visitor.ReflectiveFunctionVisitor;

public class GsonHelper {

  public static String toJson(Object o) {
    return toJsonWithDateFormat(o, AppDate.FORMAT_ORACLE_MEDI);
  }
  
  public static String toJson(Object o, Type type) {
    return toJsonWithDateFormat(o, AppDate.FORMAT_ORACLE_MEDI, type);
  }
  

  public static String toJsonWithDateFormat(Object o, String dateFormat) {
    return new GsonBuilder().setDateFormat(dateFormat).create().toJson(o);
  }

  public static String toJsonWithDateFormat(Object o, String dateFormat, Type type) {
    return new GsonBuilder().setDateFormat(dateFormat).create().toJson(o, type);
  }
  
  
  /**
   * Return the Json representation of <code>o</code> skipping the
   * fields <code>fields</code>
   * 
   * @param o The object to serialized
   * @param fieldsToSkip The fields to skip during serialization
   * @return The Json representation of o.
   */
  public static String toJson(Object o, final String... fieldsToSkip) {
    Gson gson = new GsonBuilder()
        .setExclusionStrategies(new ExclusionStrategy() {
          public boolean shouldSkipClass(Class<?> clazz) {
            return false;
          }

          public boolean shouldSkipField(FieldAttributes f) {
            if (fieldsToSkip != null && fieldsToSkip.length > 0) {
              for (int i = 0; i < fieldsToSkip.length; i++) {
                if (fieldsToSkip[i] != null && fieldsToSkip[i].equals(f.getName())) {
                  return true;
                }
              }
            }
            return false;
          }
        })
        .create();
    return gson.toJson(o);
  }

  /**
   * Return the Json representation of <code>o</code> skipping the
   * classes <code>classes</code>
   * 
   * @param o The object to serialize
   * @param classes The classes to skip
   * @return The Json representation of o
   */
  public static String toJson(Object o, final Class<?>... classes) {
    Gson gson = new GsonBuilder()
        .setExclusionStrategies(new SkipClassExclusionStrategy(classes))
        .create();
    return gson.toJson(o);
  }

  public static class SkipClassExclusionStrategy implements ExclusionStrategy {
    private final Class<?>[] classes;

    public SkipClassExclusionStrategy(Class<?>[] classes) {
      this.classes = classes;
    }

    public boolean shouldSkipClass(Class<?> clazz) {
      if (classes != null && classes.length > 0) {
        for (int i = 0; i < classes.length; i++) {
          if (classes[i] != null && classes[i].equals(clazz)) {
            return true;
          }
        }
      }
      return false;
    }

    public boolean shouldSkipField(FieldAttributes f) {
      return false;
    }
  }

  public static <T> T fromJson(String jsonString, Class<T> claz) {
    return new GsonBuilder().setDateFormat(AppDate.FORMAT_ORACLE_MEDI).create().fromJson(jsonString, claz);
  }

  public static <T> T fromJson(String jsonString, Type claz) {
    return new Gson().fromJson(jsonString, claz);
  }

  public static <K, V> Map<K, V> fromJsonToMap(String jsonString) {
    JsonElement je = new JsonParser().parse(jsonString);
    Map<K, V> r = AppObjects.cast(new JsonObject2MapConverter().visit(je));
    return r;
  }

  public static class JsonObject2MapConverter extends ReflectiveFunctionVisitor<Map<?, ?>> {

    public Map<?, ?> visitJsonObject(JsonObject obj) {
      Map<Object, Object> ret = new HashMap<Object, Object>();
      for (Entry<String, JsonElement> attr : obj.entrySet()) {
        ret.put(attr.getKey(), this.visit(attr.getValue()));
      }
      return ret;
    }

    public List<?> visitJsonArray(JsonArray obj) {
      List<Object> ret = new ArrayList<Object>();
      for (JsonElement e : obj) {
        ret.add(this.visit(e));
      }
      return ret;
    }

    public Map<?, ?> visitJsonNull(JsonNull obj) {
      return null;
    }

    public Object visitJsonPrimitive(JsonPrimitive obj) {
      if (obj.isBoolean())
        return obj.getAsBoolean();
      if (obj.isNumber())
        return obj.getAsBigDecimal();
      if (obj.isString())
        return obj.getAsString();
      throw new IllegalArgumentException("not supported type " + obj);
    }
  }

  public static String prettyPrint(Object o, Class<?>... classesExcluding) {
    Gson gson = new GsonBuilder().setPrettyPrinting().setExclusionStrategies(new SkipClassExclusionStrategy(classesExcluding)).create();
    String json = gson.toJson(o);
    return json;
  }
}
