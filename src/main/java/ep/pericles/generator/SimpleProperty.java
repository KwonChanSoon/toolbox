package ep.pericles.generator;

import static com.google.common.base.CharMatcher.WHITESPACE;
import static com.google.common.collect.ImmutableList.copyOf;
import static java.util.Arrays.asList;

import java.beans.PropertyDescriptor;
import java.util.*;

import com.google.common.base.Splitter;

class SimpleProperty {
  //private PropertyDescriptor descriptor;
  private String name;
  private String type;

  public SimpleProperty(PropertyDescriptor descriptor) {
    super();
    this.name = descriptor.getName();
    this.type = descriptor.getPropertyType().getSimpleName();
  }

  /**
   * e.g:private List<ChildBean> others
   */
  public SimpleProperty(String prop) {
    List<String> tokens = copyOf(Splitter.on(WHITESPACE).omitEmptyStrings().trimResults().split(prop));
    List<String> effectiveTokens = effectiveTokens(tokens);
    this.type = effectiveTokens.get(0);
    this.name = effectiveTokens.get(1);
  }

  private List<String> effectiveTokens(List<String> tokens) {
    List<String> ret = new ArrayList<String>();
    for (String t : tokens) {
      if (asList("private", "public", "protected").contains(t)) {
        continue;
      }
      ret.add(t);
    }
    return copyOf(ret);
  }

  public String getType() {
    return type;
  }

  public String getName() {
    return name;
  }
}
