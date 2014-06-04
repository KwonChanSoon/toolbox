package eu.qualityontime.expression.interpreter;

import java.util.*;

import com.google.common.base.*;

import eu.qualityontime.collections.DefaultValueMap;
import eu.qualityontime.visitor.ReflectiveFunctionVisitor;

/**
 * Double purpose class:
 * 1. By setting valueMapping attribute it is possible to change specific values.
 * 2. By extending visitors you could implemented type specific mappings.
 * 3. Even possible to combine both behaviour.
 */
public class ValueTypeMapping extends ReflectiveFunctionVisitor<Object> {
  private Function<Object, Object> defaultValue = Functions.identity();
  private Map<Object, Object> valueMapping = DefaultValueMap.decorate(new HashMap<Object, Object>(), defaultValue);

  public ValueTypeMapping() {
  }

  public ValueTypeMapping(Map<Object, Object> valueMapping) {
    super();
    setValueMapping(valueMapping);
  }

  public Object visitObject(Object o) {
    return valueMapping.get(o);
  }

  public void setValueMapping(Map<Object, Object> valueMapping) {
    this.valueMapping = DefaultValueMap.decorate(valueMapping, defaultValue);
  }

  @Override
  public Object visitNull(Object o) {
    return null;
  }
}