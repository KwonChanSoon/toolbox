package eu.qualityontime.spring.propertyediors;

import java.beans.PropertyEditorSupport;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableSet;

public class CommaDelimitedStringToIntegerEditor extends PropertyEditorSupport {
  @Override
  public void setAsText(String text) {
    if (StringUtils.isBlank(text)) {
      setValue(ImmutableSet.of());
      return;
    }
    Set<Integer> tags = new HashSet<Integer>();
    String[] stringTags = text.split(",");
    for (int i = 0; i < stringTags.length; i++) {
      Integer v = Integer.valueOf(stringTags[i]);
      tags.add(v);
    }
    setValue(tags);
  }
}
