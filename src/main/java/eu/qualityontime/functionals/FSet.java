package eu.qualityontime.functionals;

import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.*;

public class FSet<V> extends ForwardingSet<V> {
  private Set<V> delegate;

  public FSet(Set<V> in) {
    delegate = in;
  }

  public static <K> FSet<K> FSet(Iterable<K> in) {
    if (null == in)
      return FSet(ImmutableSet.<K> of());
    if (in instanceof FSet)
      return (FSet<K>) in;
    if (in instanceof Set)
      return new FSet<K>((Set<K>) in);
    return new FSet<K>(ImmutableSet.copyOf(in));
  }

  @Override
  protected Set<V> delegate() {
    return delegate;
  }

  public <O> FSet<O> map(F1<V, O> f) {
    return FSet(Iterables.transform(delegate(), FAdapters.toGuava(f)));
  }

  public FSet<V> filter(Predicate<? super V> p) {
    return FSet(Sets.filter(delegate(), p));
  }

  public FSet<V> substract(Set<V> subtrahend) {
    return FSet(Sets.difference(delegate(), subtrahend));
  }
}
