package eu.qualityontime.diff;

import static eu.qualityontime.AppCollections.IList;
import static eu.qualityontime.commons.JakartaPropertyUtils.getPropertyDescriptors;
import static eu.qualityontime.functionals.FCollections.FIterable;

import java.beans.PropertyDescriptor;
import java.util.*;

import lombok.Setter;
import lombok.experimental.ExtensionMethod;

import com.google.common.base.*;

import eu.qualityontime.*;
import eu.qualityontime.collections.ListTryGet;
import eu.qualityontime.commons.JakartaPropertyUtils;
import eu.qualityontime.diff.DiffNode.State;

@ExtensionMethod({ ListTryGet.class })
public class ObjectDiffer {
  @Setter
  private ComparisonStrategyResolver comparisonStrategyResolver = new ComparisonStrategyResolver();
  @Setter
  private List<String> ignoredAttributes = IList();

  public <T> DiffNode compare(T working, T base) {
    DiffNode root = DiffNode.RootNode();
    compare(root, working, base);
    return root;
  }

  <T> void compare(DiffNode current, T working, T base) {
    if (areNull(working, base)) {
      return;
    }
    if (areSame(working, base)) {
      return;
    }
    if (isAdded(working, base)) {
      current.setState(State.ADDED);
      return;
    }
    if (isRemoved(working, base)) {
      current.setState(State.REMOVED);
      return;
    }
    if (isPrimitive(working, base)) {
      if (!Objects.equal(working, base))
        current.setState(State.CHANGED);
      return;
    }
    if (!isList(working, base) && !sameType(working, base)) {
      current.setState(State.DIFFERENT_TYPE);
      return;
    }
    complexCompare(current, working, base);
  }

  private <T> void complexCompare(DiffNode current, T working, T base) {
    if (isList(working, base)) {
      compareList(current, (List<?>) working, (List<?>) base);
    }
    else
      compareBeans(current, working, base);
  }

  private void compareList(DiffNode current, List<?> working, List<?> base) {
    CollectionComparisonStrategy comparisonStrategy = comparisonStrategyResolver.collectionComparisonStrategy(current);
    comparisonStrategy.compare(this, current, working, base);
  }

  private <T> void compareBeans(DiffNode current, T working, T base) {
    Map<String, PropertyDescriptor> workingProperties = properties(working);
    Map<String, PropertyDescriptor> baseProperties = properties(base);
    Preconditions.checkArgument(workingProperties.keySet().size() == baseProperties.keySet().size(), "cannot happen");
    Set<String> beanProperties = workingProperties.keySet();
    for (String propertyName : beanProperties) {
      if (ignoredAttributes.contains(propertyName))
        continue;
      Object workingPropVal = JakartaPropertyUtils.getProperty(working, propertyName);
      Object basePropVal = JakartaPropertyUtils.getProperty(base, propertyName);
      DiffNode forField = current.addChild(DiffNode.SimpleNode(current, propertyName, new Instances(workingPropVal, basePropVal)));
      compare(forField, workingPropVal, basePropVal);
    }
  }

  private Map<String, PropertyDescriptor> properties(Object o) {
    return FIterable(getPropertyDescriptors(o)).filter(no_class_attribute).asMap(byName);
  }

  private static Predicate<PropertyDescriptor> class_attribute = AppPredicates.propEq("name", "class");
  private static Predicate<PropertyDescriptor> no_class_attribute = Predicates.not(class_attribute);
  private static Function<PropertyDescriptor, String> byName = AppFunction.byProp("name");

  private boolean sameType(Object working, Object base) {
    return working.getClass().equals(base.getClass());
  }

  private boolean areNull(Object working, Object base) {
    return null == working && null == base;
  }

  private boolean areSame(Object working, Object base) {
    return working == base;
  }

  private boolean isPrimitive(Object working, Object base) {
    return (null != working && isPrimitive(working)) || (null != base && isPrimitive(base));
  }

  private boolean isAdded(Object working, Object base) {
    return null != working && null == base;
  }

  private boolean isRemoved(Object working, Object base) {
    return null == working && null != base;
  }

  private boolean isList(Object working, Object base) {
    return working instanceof List || base instanceof List;
  }

  private boolean isPrimitive(Object o1) {
    return o1 == null
        || o1.getClass().isPrimitive()
        || o1 instanceof Boolean
        || java.lang.Number.class.isAssignableFrom(o1.getClass())
        || java.lang.String.class.isAssignableFrom(o1.getClass())
        || java.lang.Object.class.equals(o1.getClass())
        || o1 instanceof Date
        || o1 instanceof Enum<?>;
  }
}
