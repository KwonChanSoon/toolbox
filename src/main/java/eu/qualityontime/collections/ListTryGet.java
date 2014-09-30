package eu.qualityontime.collections;

import java.util.List;

public class ListTryGet {
  public static <T> T tryGet(List<T> list, int index) {
    if (null == list)
      return null;
    if (index > list.size() - 1)
      return null;
    return list.get(index);
  }

}
