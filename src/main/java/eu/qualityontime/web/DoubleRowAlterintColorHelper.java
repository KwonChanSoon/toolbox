package eu.qualityontime.web;

import eu.qualityontime.tuple.Pair;

public class DoubleRowAlterintColorHelper {
  /**
   * Abstract color. Cannot be useodd or even as a phrase
   * because in the context of double row it make not sense.
   * 
   * @author otakacs
   */
  public enum AlteredColors {
    A, B;
  }

  public static AlteredColors doubleRowcolor(int rowId) {
    return (((rowId++) - 1) & 2) == 2 ? AlteredColors.A : AlteredColors.B;
  }

  public static <T> T doubleRowcolor(int rowId, T a, T b) {
    return (((rowId++) - 1) & 2) == 2 ? b : a;
  }

  public static <T> T doubleRowcolor(int rowId, Pair<T, T> pair) {
    return (((rowId++) - 1) & 2) == 2 ? pair.getRight() : pair.getLeft();
  }

}
