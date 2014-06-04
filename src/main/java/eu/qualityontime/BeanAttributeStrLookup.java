package eu.qualityontime;

import org.apache.commons.lang3.text.StrLookup;

public class BeanAttributeStrLookup extends StrLookup<Object> {
  private final Object target;

  public BeanAttributeStrLookup(Object target) {
    this.target = target;
  }

  @Override
  public String lookup(String key) {
    Object val = AppReflection.getField(target, key);
    return null == val ? null : String.valueOf(val);
  }

  public Object getTarget() {
    return target;
  }

}
