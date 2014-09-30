package eu.qualityontime.functionals;

import static eu.qualityontime.AppCollections.isEmpty;

import java.util.Set;

import com.google.common.base.*;
import com.google.common.collect.*;

/**
 * Use togather with Lombok @ExtensionMethod
 */
public class FunctionalSetExtensions {

  public static <T> Set<T> reject(Set<T> in, T val, T... vals) {
    ImmutableList.Builder<T> b = new ImmutableList.Builder<T>();
    b.add(val);
    if (!isEmpty(vals)) {
      b.addAll(ImmutableList.copyOf(vals));
    }
    Predicate<T> p = Predicates.in(b.build());
    return Sets.filter(in, Predicates.not(p));
  }
}
