package eu.qualityontime;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class StringSubstitutorTest {

  @Test
  public void test() {
    assertEquals("alma", StrSubstitutor.replace("${apple}", ImmutableMap.of("apple", "alma")));
    assertEquals("1", StrSubstitutor.replace("${apple}", ImmutableMap.of("apple", 1)));
  }

}
