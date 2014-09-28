package eu.qualityontime.diff;

import static ep.pericles.AppCollections.IList;
import static ep.pericles.AppDate.date;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.*;

import lombok.experimental.ExtensionMethod;

import org.junit.*;
import org.junit.runner.RunWith;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import ep.pericles.AppPredicates;
import ep.pericles.util.diff.DiffNode.State;
import ep.pericles.util.diff.DiffSampleMother.A;
import ep.pericles.util.diff.DiffSampleMother.B;
import ep.pericles.util.diff.DiffSampleMother.C;
import ep.pericles.util.diff.DiffSampleMother.Parent;

@ExtensionMethod({ DiffExtension.class, CollectionExtension.class })
@RunWith(HierarchicalContextRunner.class)
public class ObjectDifferTest {
  ObjectDiffer differ = new ObjectDiffer();

  public class Primitives {
    @Test
    public void nulls_are_same() throws Exception {
      DiffNode diff = differ.compare(null, null);
      assertFalse(diff.isChanged());
    }

    @Test
    public void ref_equals_are_same() throws Exception {
      Object o = new Object();
      DiffNode diff = differ.compare(o, o);
      assertFalse(diff.isChanged());
    }

    @Test
    public void same_primitive() throws Exception {
      DiffNode diff = differ.compare(1, 1);
      assertFalse(diff.isChanged());
    }

    @Test
    public void different_primitive() throws Exception {
      DiffNode diff = differ.compare(1, 2);
      assertTrue(diff.isChanged());
    }

    @Test
    public void primitive_added() throws Exception {
      DiffNode diff = differ.compare(1, null);
      assertTrue(diff.isAdded());
    }

  }

  public class Beans {
    @Test
    public void bean_eq_with_null_attributes() throws Exception {
      A working = new A();
      A base = new A();
      DiffNode diff = differ.compare(working, base);
      assertFalse(diff.isChanged());
    }

    @Test
    public void bean_eq_with_non_null_attributes() throws Exception {
      A working = new A(1, "1", new B());
      A base = new A(1, "1", new B());
      DiffNode diff = differ.compare(working, base);
      assertFalse(diff.isChanged());
    }

    @Test
    public void bean_neq() throws Exception {
      A working = new A(1, "3", new B());
      A base = new A(2, "3", new B());
      DiffNode diff = differ.compare(working, base);
      diff.assertChanged();
      diff.assertPathChanged("/i");
    }

    @Test
    public void bean_removed() throws Exception {
      DiffNode diff = differ.compare(null, 1);
      assertTrue(diff.isRemoved());
    }

    @Test
    public void complex_diff_in_depth() throws Exception {
      A working = new A(1, "3", new B("a"));
      A base = new A(1, "3", new B("b"));
      DiffNode diff = differ.compare(working, base);
      assertTrue(diff.isChanged());
      diff.assertPathChanged("/b.name");
    }

  }

  public class IndexeListDiffStrategy {
    @Test
    public void list_same() throws Exception {
      List<Integer> working = IList(1);
      List<Integer> base = IList(1);
      DiffNode diff = differ.compare(working, base);
      diff.assertUnchanged();
    }

    @Test
    public void list_different() throws Exception {
      List<Integer> working = IList(1);
      List<Integer> base = IList(2);
      DiffNode diff = differ.compare(working, base);
      assertChanged(diff);
      diff.assertPathChanged("/[0]");

    }

    @Test
    public void list_different_removal() throws Exception {
      List<Integer> working = IList(1);
      List<Integer> base = IList(1, 2);
      DiffNode diff = differ.compare(working, base);
      diff.assertPathRemoved("/[1]");
    }

    @Test
    public void list_different_additon() throws Exception {
      DiffNode diff = differ.compare(IList(1, 2), IList(1));
      diff.assertPathAdded("/[1]");
    }

    @Test
    public void list_same_complex_objects() throws Exception {
      DiffNode diff = differ.compare(IList(new B("b")), IList(new B("b")));
      diff.assertUnchanged();
    }

    @Test
    public void list_different_complex_objects() throws Exception {
      DiffNode diff = differ.compare(IList(new B("a")), IList(new B("b")));
      diff.assertChanged();
      diff.assertPathChanged("/[0].name");
    }
  }

  public class UnorderedCollectionListDiffStrategy {
    @Before
    public void before() {
      ComparisonStrategyResolver resolver = new ComparisonStrategyResolver();
      resolver.setGlobalCollectionComparisonStrategy(new UnorderedCollectionComparisonStrategy());
      differ.setComparisonStrategyResolver(resolver);
    }

    @Test
    public void diff_order_should_be_same() throws Exception {
      DiffNode diff = differ.compare(IList(2, 1), IList(1, 2));
      diff.assertUnchanged();
    }

    @Test
    public void eliminated_differences() throws Exception {
      DiffNode diff = differ.compare(IList(2, 2, 1, 1, 2, 1), IList(1, 2));
      diff.assertUnchanged();
    }

    @Test
    public void same_size_different() throws Exception {
      DiffNode diff = differ.compare(IList(1, 2, 3), IList(1, 2, 4));
      diff.assertChanged();
      diff.assertPathAdded("/<ADDED>", 3);
      diff.assertPathRemoved("/<REMOVED>", 4);
    }

    @Test
    public void different_size_different() throws Exception {
      DiffNode diff = differ.compare(IList(1, 2, 3), IList(1, 2, 4, 3));
      diff.assertChanged();
      diff.assertPathRemoved("/<REMOVED>", 4);
    }
  }

  public class CustomKeyCollectionDiffStrategy {
    @Before
    public void before() {
      ComparisonStrategyResolver resolver = new ComparisonStrategyResolver();
      resolver.setGlobalCollectionComparisonStrategy(new CustomKeyCollectionComparisonStrategy(new Key("i")));
      differ.setComparisonStrategyResolver(resolver);
    }

    @Test
    public void same_should_be_unchanged() throws Exception {
      DiffNode diff = differ.compare(IList(new A(1, "a")), IList(new A(1, "a")));
      diff.assertUnchanged();
    }

    @Test
    public void different_should_differnet() throws Exception {
      DiffNode diff = differ.compare(IList(new A(1, "a")), IList(new A(1, "b")));
      diff.assertChanged();
      diff.assertPathChanged("/{i=1}.s");
    }

    @Test
    public void identify_matching_pairs_independenty_from_order() throws Exception {
      DiffNode diff = differ.compare(
          IList(new A(1, "a"), new A(3, "a"), new A(2, "a"))
          , IList(new A(3, "a"), new A(1, "b"), new A(2, "a"))
          );
      diff.assertChanged();
      diff.assertPathChanged("/{i=1}.s");
    }

    @Test
    public void detect_addition_and_emoval_based_on_key() throws Exception {
      DiffNode diff = differ.compare(
          IList(new A(1, "a"), new A(4, "a"), new A(2, "a"))
          , IList(new A(1, "a"), new A(2, "a"), new A(5, "a"))
          );
      diff.assertChanged();
      diff.assertPathAdded("/<ADDED>", new A(4, "a"));
      diff.assertPathRemoved("/<REMOVED>", new A(5, "a"));
    }
  }

  public class ComplexExample {
    private Object base = new C("name1", BigDecimal.ONE, date("2014-09-12 14:45")
        , IList(1L, 2L, 3L)
        , IList(new B("b1"), new B("b2"))
        , IList(new A(1, "a1"), new A(2, "a2"), new A(3, "a3"))
        );
    private Object working_same = new C("name1", BigDecimal.ONE, date("2014-09-12 14:45")
        , IList(1L, 2L, 3L)
        , IList(new B("b1"), new B("b2"))
        , IList(new A(1, "a1"), new A(2, "a2"), new A(3, "a3"))
        );
    private Object working_leaf_attr_change = new C("name1", BigDecimal.ONE, date("2014-09-12 14:45")
        , IList(1L, 2L, 3L)
        , IList(new B("b1"), new B("b2_changed"))
        , IList(new A(1, "a1"), new A(2, "a2"), new A(3, "a3"))
        );
    private Object working_list_added = new C("name2", BigDecimal.ONE, date("2014-09-13 14:45")
        , IList(1L, 2L, 3L, 4L)
        , IList(new B("b1_changed"))
        , IList(new A(1, "a1"), new A(2, "a2"), new A(3, "a3"), new A(4, "a4"))
        );

    @Test
    public void no_change() throws Exception {
      DiffNode diff = differ.compare(working_same, base);
      diff.assertUnchanged();
    }

    @Test
    public void lief_change() throws Exception {
      DiffNode diff = differ.compare(working_leaf_attr_change, base);
      diff.assertChanged();
      diff.assertPathChanged("/babSet[1].name", "b2", "b2_changed");
    }

    @Test
    public void collection_modified_and_attribute_changed() throws Exception {
      DiffNode diff = differ.compare(working_list_added, base);
      diff.assertChanged();
      diff.assertPathChanged("/babSet[0].name", "b1", "b1_changed");
      diff.assertPathChanged("/valueDate");
      diff.assertPathChanged("/name");
      diff.assertPathAdded("/assSet[3]");
      diff.assertPathAdded("/termRefs[3]", 4L);
    }
  }

  public class ConfigurableCollectionDiffStrategy {
    private C working = new C();
    private C base = new C();

    @Before
    public void before() {
      Map<String, CollectionComparisonStrategy> strategiesByPath = new HashMap<String, CollectionComparisonStrategy>();
      strategiesByPath.put("/termRefs", new IndexedComparisonStrategy());
      strategiesByPath.put("/babSet", new UnorderedCollectionComparisonStrategy());
      strategiesByPath.put("/assSet", new CustomKeyCollectionComparisonStrategy(new Key("i")));
      ComparisonStrategyResolver resolver = new ComparisonStrategyResolver(strategiesByPath);
      differ.setComparisonStrategyResolver(resolver);
    }

    @Test
    public void indexedResolver() throws Exception {
      working.setTermRefs(IList(1L));
      base.setTermRefs(IList(2L));
      DiffNode diff = differ.compare(working, base);
      diff.assertPathChanged("/termRefs[0]", 2L, 1L);
    }

    @Test
    public void unorderedResolver() throws Exception {
      working.setBabSet(IList(new B("a"), new B("b"), new B("c")));
      base.setBabSet(IList(new B("b"), new B("c"), new B("e")));
      DiffNode diff = differ.compare(working, base);
      diff.assertPathAdded("/babSet<ADDED>", new B("a"));
      diff.assertPathRemoved("/babSet<REMOVED>", new B("e"));
    }

    @Test
    public void customKeyResolver() throws Exception {
      working.setAssSet(IList(new A(1, "1"), new A(2, "2"), new A(3, "2")));
      base.setAssSet(IList(new A(2, "2changed"), new A(1, "1")));
      DiffNode diff = differ.compare(working, base);
      diff.assertPathAdded("/assSet<ADDED>", new A(3, "2"));
      diff.assertPathChanged("/assSet{i=2}.s", "2changed", "2");
      // other could be verified by I am lazy to write more and more assert :(
    }

    @Test
    public void matching_against_indexed_collection_path() throws Exception {
      Map<String, CollectionComparisonStrategy> strategiesByPath = new HashMap<String, CollectionComparisonStrategy>();
      strategiesByPath.put("/ccc", new IndexedComparisonStrategy());
      strategiesByPath.put("/ccc.termRefs", new UnorderedCollectionComparisonStrategy());
      ComparisonStrategyResolver resolver = new ComparisonStrategyResolver(strategiesByPath);
      differ.setComparisonStrategyResolver(resolver);

      Parent base = new Parent(IList(
          new C()
              .termRefs(IList(1L, 2L))
          ));
      Parent working = new Parent(IList(
          new C()
              .termRefs(IList(2L, 1L))
          ));
      DiffNode diff = differ.compare(working, base);

      diff.assertUnchanged();
    }
  }

  static void assertUnchanged(DiffNode diff) {
    assertFalse(diff.isChanged());
  }

  static void assertChanged(DiffNode diff) {
    assertTrue(diff.isChanged());
  }

}

class CollectionExtension {
  public static Iterable<DiffNode> filter(Iterable<DiffNode> iter, Predicate<? super DiffNode> predicate) {
    return Iterables.filter(iter, predicate);
  }

  public static DiffNode first(Iterable<DiffNode> iter) {
    return Iterables.get(iter, 0);
  }

}

@ExtensionMethod({ CollectionExtension.class })
class DiffExtension {
  public static void print(DiffNode diff) {
    PrintVisitor print = new PrintVisitor();
    diff.visit(print);
  }

  public static void assertUnchanged(DiffNode diff) {
    ObjectDifferTest.assertUnchanged(diff);
  }

  public static void assertChanged(DiffNode diff) {
    ObjectDifferTest.assertChanged(diff);
  }

  public static void assertPathChanged(DiffNode diff, String pathChanged) {
    for (DiffNode changed : diff.preorderIterable().filter(AppPredicates.propEq("state", State.CHANGED)))
      if (pathChanged.equals(changed.getPath()))
        return;
    fail("Path not found: " + pathChanged);
  }

  public static void assertPathChanged(DiffNode diff, String pathChanged, Object fromValue, Object toValue) {
    for (DiffNode changed : diff.preorderIterable().filter(AppPredicates.propEq("state", State.CHANGED))) {
      if (pathChanged.equals(changed.getPath())) {
        assertEquals(fromValue, changed.getInstances().getBase());
        assertEquals(toValue, changed.getInstances().getWorking());
        return;
      }
    }
    fail("Path not found: " + pathChanged);
  }

  public static void assertPathRemoved(DiffNode node, String path) {
    DiffNode changed = node.preorderIterable().filter(AppPredicates.propEq("state", State.REMOVED)).first();
    assertEquals(path, changed.getPath());
  }

  public static void assertPathRemoved(DiffNode node, String path, Object valueRemoved) {
    DiffNode changed = node.preorderIterable().filter(AppPredicates.propEq("state", State.REMOVED)).first();
    assertEquals(path, changed.getPath());
    assertEquals(valueRemoved, changed.getInstances().getRemoved());
  }

  public static void assertPathAdded(DiffNode node, String path) {
    for (DiffNode changed : node.preorderIterable().filter(AppPredicates.propEq("state", State.ADDED)))
      if (path.equals(changed.getPath()))
        return;
    fail("Path not found");
  }

  public static void assertPathAdded(DiffNode node, String path, Object addedValue) {
    for (DiffNode changed : node.preorderIterable().filter(AppPredicates.propEq("state", State.ADDED)))
      if (path.equals(changed.getPath())) {
        assertEquals(addedValue, changed.getInstances().getAdded());
        return;
      }
    fail("Path not found");
  }

}
