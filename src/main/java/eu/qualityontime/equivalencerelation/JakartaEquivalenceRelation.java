package eu.qualityontime.equivalencerelation;

import static eu.qualityontime.AppStrings.rs;
import static eu.qualityontime.commons.JakartaPropertyUtils.getPropertyDescriptors;

import java.beans.PropertyDescriptor;
import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.*;

import eu.qualityontime.*;
import eu.qualityontime.commons.JakartaPropertyUtils;
import eu.qualityontime.functionals.FIterable;

public class JakartaEquivalenceRelation extends ReportingEquivalenceRelation {
  private Set<Pair<Integer, Integer>> registry = new HashSet<Pair<Integer, Integer>>();

  public JakartaEquivalenceRelation() {
  }

  public JakartaEquivalenceRelation(String title) {
    super(title);
  }

  @Override
  public boolean _areEquals(Object o1, Object o2) {
    if (o1 == o2) {
      return true;
    }
    if (null == o1 || null == o2) {
      return false;
    }
    if (isPrimitive(o1) || isPrimitive(o2)) {
      return equalsPrimitive(o1, o2);
    }
    return beanEquals(o1, o2);
  }

  static Predicate<PropertyDescriptor> class_attribute = AppPredicates.propEq("name", "class");
  static Predicate<PropertyDescriptor> no_class_attribute = Predicates.not(class_attribute);
  static Function<PropertyDescriptor, String> byName = AppFunction.byProp("name");

  private boolean beanEquals(Object o1, Object o2) {
    register(o1, o2);
    if (isArray(o1)) {
      return equalsArrays(o1, o2);
    }
    if (isList(o1)) {
      return equalsList(o1, o2);
    }
    if (isSet(o1)) {
      throw new RuntimeException("Set equivalence is not supported");
    }
    if (isMap(o1)) {
      return equalsMap(o1, o2);
    }
    //plaing object
    Map<String, PropertyDescriptor> pd1 = FIterable.from(getPropertyDescriptors(o1)).filter(no_class_attribute).asMap(byName);
    Map<String, PropertyDescriptor> pd2 = FIterable.from(getPropertyDescriptors(o2)).filter(no_class_attribute).asMap(byName);
    if (pd1.size() != pd2.size()) {
      addMessage("size diff");
      return false;
    }
    if (!(pd1.keySet().equals(pd2.keySet()))) {
      addMessage("nr of bean properties diff");
      return false;
    }
    for (String prop : pd1.keySet()) {
      Object p1 = JakartaPropertyUtils.getProperty(o1, prop);
      Object p2 = JakartaPropertyUtils.getProperty(o2, prop);
      addMessage(rs("[{}:`{}` vs `{}`]", prop, simplify(p1), simplify(p2)));
      if (isRegistered(p1, p2)) {
        continue;
      }
      if (!areEquals(p1, p2)) {
        return false;
      }
    }
    return true;
  }

  private boolean isMap(Object o1) {
    return o1 instanceof Map;
  }

  private boolean isSet(Object o1) {
    return o1 instanceof Set;
  }

  private boolean isList(Object o1) {
    return o1 instanceof List;
  }

  private Object simplify(Object p1) {
    if (isPrimitive(p1)) {
      return p1;
    }
    return "<complex>";
  }

  private boolean isRegistered(Object o1, Object o2) {
    Pair<Integer, Integer> pos = Pair.of(System.identityHashCode(o1), System.identityHashCode(o2));
    return registry.contains(pos);
  }

  private void register(Object o1, Object o2) {
    Pair<Integer, Integer> pos = Pair.of(System.identityHashCode(o1), System.identityHashCode(o2));
    registry.add(pos);
  }

  private boolean equalsPrimitive(Object o1, Object o2) {
    return o1.equals(o2);
  }

  private boolean isPrimitive(Object o1) {
    return o1 == null
        || o1.getClass().isPrimitive()
        || o1 instanceof Boolean
        || java.lang.Number.class.isAssignableFrom(o1.getClass())
        || java.lang.String.class.isAssignableFrom(o1.getClass())
        || java.lang.Object.class.equals(o1.getClass())
        || o1 instanceof Date;
  }

  private boolean equalsMap(Object o1, Object o2) {
    addMessage("MAP");
    Map<?, ?> a = (Map<?, ?>) o1;
    Map<?, ?> b = (Map<?, ?>) o2;
    if (a.size() != b.size()) {
      addMessage("size diff");
      return false;
    }
    for (Object k : a.keySet()) {
      Object va = a.get(k);
      Object vb = b.get(k);
      addMessage(rs("[{}:`{}` vs `{}`]", k, va, vb));
      boolean r = areEquals(va, vb);
      if (!r) {
        return false;
      }
    }
    return true;
  }

  private boolean equalsList(Object o1, Object o2) {
    addMessage("LIST");
    List<?> l1 = (List<?>) o1;
    List<?> l2 = (List<?>) o2;
    if (l1.size() != l2.size()) {
      addMessage("size diff");
      return false;
    }
    for (int i = 0; i < l1.size(); i++) {
      addMessage(i + ".");
      boolean r = areEquals(l1.get(i), l2.get(i));
      if (!r) {
        return false;
      }
    }
    return true;
  }

  private boolean isArray(Object o1) {
    return o1.getClass().isArray();
  }

  private boolean equalsArrays(Object o1, Object o2) {
    addMessage("ARRAY");
    Object[] oa1 = (Object[]) o1;
    Object[] oa2 = (Object[]) o2;
    if (oa1.length != oa2.length) {
      addMessage("Length diff");
      return false;
    }
    for (int i = 0; i < oa1.length; i++) {
      addMessage(i + ".");
      boolean r = areEquals(oa1[i], oa2[i]);
      if (!r) {
        return false;
      }
    }
    return true;
  }

}
