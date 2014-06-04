package eu.qualityontime;

public class AppBoolean {
  public static Boolean bool(String in) {
    AppPreconditions.checkValueSet(in, AppCollections.List("Y", "N"));
    return "Y".equals(in);
  }

  public static String bool(Boolean in) {
    return in ? "Y" : "N";
  }
}
