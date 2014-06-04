package ep.pericles.functionals;

import static ep.pericles.AppCollections.IList;
import static ep.pericles.functionals.FIterable.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import com.google.common.base.*;
import com.google.common.collect.*;

import ep.pericles.AppFunction;

public class FunctionalsTest {

  @Test
  public void create() {
    Iterable<Integer> i = with(new ArrayList<Integer>());
    assertNotNull(i);
  }

  @Test
  public void asList() throws Exception {
    assertTrue("should be List", with(new HashSet<Integer>()).asList() instanceof List);
  }

  @Test
  public void map() throws Exception {
    assertEquals(ImmutableSet.of("1", "2"), with(ImmutableList.<Object> of(1, 2)).map(Functions.toStringFunction()).asSet());
  }

  @Test
  public void filter() throws Exception {
    assertEquals(ImmutableSet.of(1), with(ImmutableList.of(1, 2)).filter(new Predicate<Integer>() {
      @Override
      public boolean apply(Integer i) {
        return i.equals(1);
      }
    }).asSet());
  }

  @Test
  public void groupBy() throws Exception {
    Multimap<Integer, Integer> expected = ImmutableListMultimap.of(1, 1, 1, 3, 0, 2, 0, 4);
    Multimap<Integer, Integer> actual = with(ImmutableList.of(1, 2, 3, 4)).groupBy(new Function<Integer, Integer>() {
      @Override
      public Integer apply(Integer i) {
        return i % 2;
      }
    });
    assertEquals(expected, actual);
  }

  @Test
  public void sort() throws Exception {
    assertEquals(ImmutableList.of(1, 2, 3), with(ImmutableList.of(2, 1, 3)).sort(Ordering.<Integer> natural()).asList());
  }

  @SuppressWarnings("deprecation")
  @Test
  public void sortBy() throws Exception {
    Function<A, Integer> fieldExtractor = AppFunction.byProp("i");
    List<Integer> expected = FIterable(IList(new A(1), new A(2), new A(3))).pluck("i", Integer.class).asList();
    List<Integer> actual = FIterable(IList(new A(2), new A(3), new A(1))).sortBy(fieldExtractor).pluck("i", Integer.class).asList();
    assertEquals(expected.toArray(), actual.toArray());
  }

  public static class A {
    int i;

    public A(int i) {
      super();
      this.i = i;
    }

    public int getI() {
      return i;
    }

    public void setI(int i) {
      this.i = i;
    }

    @Override
    public String toString() {
      return "" + i;
    }

  }

  @Test
  public void flatMap() throws Exception {
    Function<Integer, List<Integer>> trans = new Function<Integer, List<Integer>>() {
      @Override
      public List<Integer> apply(Integer in) {
        return ImmutableList.of(in, in);
      }
    };
    List<Integer> actual = FIterable(ImmutableList.of(1, 2, 3)).flatMap(trans).asList();
    List<Integer> expected = ImmutableList.of(1, 1, 2, 2, 3, 3);
    assertEquals(expected, actual);
  }

  @Test
  public void sort_mutliple_attribute() throws Exception {
    final Function<Map<String, String>, String> attrA = AppFunction.byProp("a");
    final Function<Map<String, String>, String> attrB = AppFunction.byProp("b");
    Map<String, String> d = ImmutableMap.of("a", "a", "b", "a");
    Map<String, String> c = ImmutableMap.of("a", "b", "b", "a");
    Map<String, String> b = ImmutableMap.of("a", "a", "b", "b");
    Map<String, String> a = ImmutableMap.of("a", "b", "b", "c");

    List<Map<String, String>> in = ImmutableList.of(a, b, c, d);
    List<Map<String, String>> res = FIterable(in).sort(attrA, attrB).asList();
    List<Map<String, String>> expected = ImmutableList.of(d, b, c, a);
    assertEquals(expected, res);
  }

  @Test
  public void cast() throws Exception {
    FIterable<Object> i = FIterable.from(ImmutableList.<Object> of("a", "b"));
    //java compiler is failing without cast
    FIterable<String> is = i.cast(String.class);
    assertNotNull(is);
  }
}
