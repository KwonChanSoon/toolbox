package ep.pericles.generator;

import static com.google.common.collect.ImmutableMap.of;

import java.util.*;

import org.apache.commons.lang3.text.WordUtils;

import ep.pericles.AppStrings;

class GeneratedSource {
  public GeneratedSource(String baseClass, boolean generateSetter) {
    this.baseClass = baseClass;
    this.name = baseClass + "Builder";
    this.generateSetter = generateSetter;
  }

  private boolean generateSetter = false;
  private String baseClass;
  private String name;
  private List<SimpleProperty> properties = new ArrayList<SimpleProperty>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<SimpleProperty> getProperties() {
    return properties;
  }

  public void setProperties(List<SimpleProperty> properties) {
    this.properties = properties;
  }

  public String getSource() {
    StringBuilder sb = new StringBuilder();
    buildClass(sb);
    return sb.toString();
  }

  private void buildClass(StringBuilder sb) {
    sb.append("class " + name + "{\n");
    buildProperties(sb);
    buildBuilderMethods(sb);
    buildBuilder(sb);
    sb.append("}\n");
  }

  private void buildBuilder(StringBuilder sb) {
    String template = "  public ${baseClass} build(){\n" +
        "    ${baseClass} inst = new ${baseClass}();\n" +
        "${assignments}\n    return inst;\n" +
        "  }\n";
    sb.append(AppStrings.replace(template, of("baseClass", baseClass, "assignments", buildBuilderAssignments())));
  }

  private String buildBuilderAssignments() {
    String ret = "";
    for (SimpleProperty p : properties) {
      String template = generateSetter ? "    inst.set${method}(${name});\n" : "    inst.${name} = ${name};\n";
      ret += AppStrings.replace(template, of("method", WordUtils.capitalize(p.getName()), "name", p.getName()));
    }
    return ret;
  }

  private void buildBuilderMethods(StringBuilder sb) {
    for (SimpleProperty p : properties) {
      sb.append("  public ").append(getName()).append(" ");
      sb.append(p.getName()).append("(");
      sb.append(p.getType()).append(" ").append(p.getName()).append("){\n");
      sb.append("    this.").append(p.getName()).append("=").append(p.getName()).append(";\n");
      sb.append("    return this;\n  }\n");
    }

  }

  private void buildProperties(StringBuilder sb) {
    for (SimpleProperty p : properties) {
      sb.append("  ").append("private ").append(p.getType()).append(" ").append(p.getName()).append(";\n");
    }
  }
}
