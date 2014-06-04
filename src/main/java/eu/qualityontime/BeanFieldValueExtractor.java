package eu.qualityontime;

import java.lang.reflect.Field;

import org.springframework.util.*;

import com.google.common.base.Function;

public class BeanFieldValueExtractor<T> implements Function<Object, T> {
  private final String attr;

  public static <T> BeanFieldValueExtractor<T> byField(String attr) {
    return new BeanFieldValueExtractor<T>(attr);
  }

  public static <T> BeanFieldValueExtractor<T> byField(String attr, Class<T> claz) {
    return new BeanFieldValueExtractor<T>(attr);
  }

  public BeanFieldValueExtractor(String attr) {
    this.attr = attr;
  }

  @SuppressWarnings("unchecked")
  @Override
  public T apply(Object input) {
    return (T) getField(input, attr);
  }

  /**
   * Extracted from ReflectionTestUtil if Spring
   */
  private static Object getField(Object target, String name) {
    Assert.notNull(target, "Target object must not be null");
    Field field = ReflectionUtils.findField(target.getClass(), name);
    Assert.notNull(field, "Could not find field [" + name + "] on target [" + target + "]");

    ReflectionUtils.makeAccessible(field);
    return ReflectionUtils.getField(field, target);
  }

}
