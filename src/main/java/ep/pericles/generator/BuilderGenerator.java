package ep.pericles.generator;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

/**
 * Generating Builder pattern based Builder object source code.
 * not full but always a good start.
 */
public class BuilderGenerator {
  private boolean generateSetter;

  public BuilderGenerator(boolean generateSetter) {
    this.generateSetter = generateSetter;
  }

  public BuilderGenerator() {
    this(false);
  }

  public String generate(Class<?> claz) {
    String name = claz.getSimpleName() + "Builder";
    GeneratedSource source = new GeneratedSource(name, true);
    PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(claz);
    for (PropertyDescriptor pd : props) {
      if ("class".equals(pd.getName())) {
        continue;
      }
      source.getProperties().add(new SimpleProperty(pd));
    }

    return source.getSource();
  }

  public String generate(String baseName, String propLines) {
    List<String> ps = ImmutableList.copyOf(Splitter.on(";").trimResults().omitEmptyStrings().split(propLines));
    return this.generate(baseName, ps);
  }

  public String generate(String baseName, List<String> propLines) {
    GeneratedSource source = new GeneratedSource(baseName, generateSetter);
    for (String s : propLines) {
      if (null == s.trim()) {
        continue;
      }
      source.getProperties().add(new SimpleProperty(s.trim()));
    }
    return source.getSource();
  }

  public static void main(String[] args) throws Exception {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("Class name:");
    String baseClass = in.readLine().trim();
    System.out.println("Attributes:");
    String attributes = "";
    for (String s = in.readLine(); s != null; s = in.readLine()) {
      attributes += s;
    }
    String source = new BuilderGenerator().generate(baseClass, attributes);
    System.out.println();
    System.out.println(source);
  }
}
