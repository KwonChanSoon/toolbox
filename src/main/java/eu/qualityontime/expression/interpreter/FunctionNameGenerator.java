package eu.qualityontime.expression.interpreter;

/**
 * Naping pattern is `xyz_fn1_2` where
 * `zyx`: common prefix
 * `fn1`: `fn` is the current function name and `1` is indicating the first call of function
 * `2`: parameter index start from 0.
 */
class FunctionNameGenerator {
  private final String paramPrefix;
  private final String name;
  private final Integer currentCount;
  private int counter = 0;

  public FunctionNameGenerator(String paramPrefix, String name, Integer currentCount) {
    this.paramPrefix = paramPrefix;
    this.name = name;
    this.currentCount = currentCount;
  }

  public String get() {
    return paramPrefix + name + currentCount + "_" + (counter++);
  }

}
