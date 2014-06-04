package eu.qualityontime;

import static eu.qualityontime.AppCollections.List;
import static eu.qualityontime.test.AssertThrowable.assertThrowable;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import com.google.common.base.Function;
import com.google.common.collect.*;

import eu.qualityontime.type.Procedure;

public class AppCollectionsTest {
  @Test
  public void test_array_instanceof() {
    Assert.assertTrue(new Long[] {} instanceof Object[]);
  }

  @Test
  public void group_by() {
    List<A> l = List();
    l.add(new A(1, "alma"));
    l.add(new A(1, "korte"));
    l.add(new A(2, "szilva"));
    ListMultimap<Integer, A> r = AppCollections.groupBy(l, new Function<A, Integer>() {
      @Override
      public Integer apply(A input) {
        return input.num;
      }
    });
    assertNotNull(r);
    assertEquals(2, r.keySet().size());
    assertEquals(2, r.get(1).size());
    assertEquals(1, r.get(2).size());
    assertNotNull(r.get(3));
    assertEquals(0, r.get(3).size());
  }

  @Test
  public void group_by_nums() {
    List<Integer> l = List(1, 2, 3, 4, 5);
    ListMultimap<Integer, Integer> r = AppCollections.groupBy(l, new Function<Integer, Integer>() {
      @Override
      public Integer apply(Integer input) {
        return input % 2;
      }
    });
    assertNotNull(r);
    assertEquals(2, r.keySet().size());
    assertEquals(2, r.get(0).size());
    assertEquals(3, r.get(1).size());
    assertEquals(0, r.get(3).size());
  }

  static class A {
    Integer num;
    String val;

    public A(Integer num, String val) {
      this.num = num;
      this.val = val;
    }
  }

  @Test
  public void test_toMap() {
    Map<Integer, A> r = AppCollections.toMap(List(new A(1, "1"), new A(2, "2"), new A(3, "3")), new Function<A, Integer>() {
      @Override
      public Integer apply(A input) {
        return input.num;
      }
    });
    assertEquals("1", r.get(1).val);
  }

  @Test
  public void test_maxBy() {
    Integer r = AppCollections.maxBy(List(1, 2, 100, 4, 5), new Function<Integer, Integer>() {
      @Override
      public Integer apply(Integer input) {
        return input;
      }
    });
    assertEquals(new Integer(100), r);
  }

  @Test
  public void test_transforValue_Multimap() {
    ListMultimap<String, Integer> in = ArrayListMultimap.create();
    in.put("odd", 1);
    in.put("odd", 3);
    in.put("even", 2);
    ListMultimap<String, String> expected = ArrayListMultimap.create();
    expected.put("odd", "1");
    expected.put("odd", "3");
    expected.put("even", "2");
    ListMultimap<String, String> res = AppCollections.transformValues(in, new Function<Integer, String>() {
      @Override
      public String apply(Integer input) {
        return "" + input;
      }
    });
    assertEquals(expected, res);
  }

  @Test
  public void customUniqSet() {
    A a = new A(1, "alma");
    A b = new A(1, "korte");
    Set<A> plain = new HashSet<AppCollectionsTest.A>();
    plain.add(a);
    plain.add(b);
    assertEquals(2, plain.size());
    Set<A> customUniq = AppCollections.customUniqSet(plain, new Function<A, Object>() {
      @Override
      public Object apply(A input) {
        return input.num;
      }
    });
    assertEquals(1, customUniq.size());
  }

  @Test
  public void testToSubLists() {
    List<Integer> l20 = createList(20);

    List<List<Integer>> sl20 = AppCollections.toSubLists(l20, 10);
    assertNotNull(sl20);
    assertEquals(2, sl20.size());
    assertNotNull(sl20.get(0));
    assertNotNull(sl20.get(1));
    assertTrue(sl20.get(0).size() == 10);
    assertTrue(sl20.get(1).size() == 10);

    assertTrue(sl20.get(0).get(0) == 0);
    assertTrue(sl20.get(0).get(9) == 9);

    assertTrue(sl20.get(1).get(0) == 10);
    assertTrue(sl20.get(1).get(9) == 19);

    List<Integer> l21 = createList(21);

    List<List<Integer>> sl21 = AppCollections.toSubLists(l21, 10);
    assertNotNull(sl21);
    assertEquals(3, sl21.size());
    assertNotNull(sl21.get(0));
    assertNotNull(sl21.get(1));
    assertNotNull(sl21.get(2));

    assertTrue(sl21.get(0).size() == 10);
    assertTrue(sl21.get(1).size() == 10);
    assertTrue(sl21.get(2).size() == 1);

    assertTrue(sl21.get(0).get(0) == 0);
    assertTrue(sl21.get(0).get(9) == 9);

    assertTrue(sl21.get(1).get(0) == 10);
    assertTrue(sl21.get(1).get(9) == 19);

    assertTrue(sl21.get(2).get(0) == 20);

  }

  @Test
  public void testSortValues() {
    ListMultimap<String, String> map = ArrayListMultimap.create();
    map.putAll("FR", List("EN", "FR", "DE"));
    map.putAll("DE", List("HU", "FR", "DE"));

    ListMultimap<String, String> expectedMap = ArrayListMultimap.create();
    expectedMap.putAll("FR", List("DE", "EN", "FR"));
    expectedMap.putAll("DE", List("DE", "FR", "HU"));

    ListMultimap<String, String> sortedMap = AppCollections.sortValues(map, new Comparator<String>() {
      @Override
      public int compare(String o1, String o2) {
        return o1.compareTo(o2);
      }
    });

    assertNotNull(sortedMap);
    assertEquals(expectedMap, sortedMap);

    sortedMap = AppCollections.sortValues(map);
    assertNotNull(sortedMap);
    assertEquals(expectedMap, sortedMap);

  }

  private List<Integer> createList(int size) {
    List<Integer> l = List();
    for (int i = 0; i < size; i++) {
      l.add(i);
    }
    return l;
  }

  @Test
  public void between() throws Exception {
    List<Integer> in = ImmutableList.of(1, 2, 3, 4, 5);
    assertEquals(ImmutableList.of(3), ImmutableList.copyOf(AppCollections.between(in, 2, 3)));
  }

  @Test
  public void immutablemap_creation() throws Exception {
    assertNotNull(AppCollections.IMap());
    assertNotNull(AppCollections.IMap("1", 1));
    assertThrowable(IllegalArgumentException.class, "even nr of arguments only", new Procedure() {
      @Override
      public void execute() throws Exception {
        AppCollections.IMap("a", 1, "b");
      }
    });
    AppCollections.IMap("a", 1, 2, 3);//!!
  }
}
