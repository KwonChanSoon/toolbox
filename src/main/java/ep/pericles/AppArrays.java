package ep.pericles;

import java.util.Arrays;

public class AppArrays {

  public static String[] a(String... ss) {
    return (String[]) ss;
  }

  public static Object[] oa(Object... ss) {
    return (Object[]) ss;
  }
  
  public static long[] appendArray(long[] arr, long val) {
    if(arr == null) {
      arr = new long[0];
    }
    arr = Arrays.copyOf(arr, arr.length + 1);
    arr[arr.length - 1] = val;
    return arr;
  }  
}
