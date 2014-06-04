package ep.pericles.clipboard;

import static ep.pericles.AppObjects.defaultIfNull;

import java.util.*;
import java.util.Map.Entry;

import com.google.common.base.*;
import com.google.common.collect.*;

import ep.pericles.*;
import ep.pericles.tuple.ImmutablePair;

public class Clipboard {
  private long lastModified = System.currentTimeMillis();
  private Multimap<EItemCategory, ClipboardEntry> items = LinkedListMultimap.create();

  public static enum EItemCategory {
    INTERPRETER,
    TEAM
  }

  public static class ClipboardEntry extends ImmutablePair<Long, Clipboardable> {
    public ClipboardEntry(Long left, Clipboardable right) {
      super(left, right);
    }
  }

  private static SeqGenerator generator = new SeqGenerator();
  private static Function<ClipboardEntry, Clipboardable> contentExtractor = new Function<Clipboard.ClipboardEntry, Clipboardable>() {
    @Override
    public Clipboardable apply(ClipboardEntry input) {
      return input._2();
    }
  };

  private void refreshLastModified() {
    this.lastModified = System.currentTimeMillis();
  }

  public Multimap<EItemCategory, ClipboardEntry> getItems() {
    return items;
  }

  public Collection<ClipboardEntry> getItems(EItemCategory category) {
    return items.get(category);
  }

  public Collection<Clipboardable> get(EItemCategory category) {
    return ImmutableList.copyOf(Iterables.transform(items.get(category), contentExtractor));
  }

  public void addItem(EItemCategory category, Clipboardable item) {
    add(category, item);
    refreshLastModified();
  }

  public void add(EItemCategory category, Clipboardable item) {
    items.put(category, new ClipboardEntry(generator.next(), item));
    refreshLastModified();
  }

  public void addItems(EItemCategory category, Collection<? extends Clipboardable> items) {
    for (Clipboardable item : items) {
      addItem(category, item);
    }
    refreshLastModified();
  }

  public void removeItems(Collection<Long> clipboard_ids) {
    for (Long id : clipboard_ids) {
      removeItem(id);
    }
    refreshLastModified();
  }

  public void removeItem(final long entryId) {
    Iterable<Entry<EItemCategory, ClipboardEntry>> deletables = Iterables.filter(items.entries(),
        new Predicate<Entry<EItemCategory, ClipboardEntry>>() {
          @Override
          public boolean apply(Entry<EItemCategory, ClipboardEntry> input) {
            return entryId == input.getValue()._1().longValue();
          }
        });
    for (Entry<EItemCategory, ClipboardEntry> e : ImmutableList.copyOf(deletables)) {
      items.remove(e.getKey(), e.getValue());
    }
    refreshLastModified();
  }

  public void remove(final EItemCategory category, final Clipboardable item) {
    AppPreconditions.checkNotNull(category, item);
    Iterable<Entry<EItemCategory, ClipboardEntry>> deletables = Iterables.filter(items.entries(),
        new Predicate<Entry<EItemCategory, ClipboardEntry>>() {
          @Override
          public boolean apply(Entry<EItemCategory, ClipboardEntry> input) {
            return category == input.getKey() && Objects.equal(item, input.getValue()._2());
          }
        });
    for (Entry<EItemCategory, ClipboardEntry> e : ImmutableList.copyOf(deletables)) {
      items.remove(e.getKey(), e.getValue());
    }
    refreshLastModified();
  }

  public void clearAllItems(EItemCategory category) {
    items.removeAll(category);
    refreshLastModified();
  }

  public void clearAllItems() {
    items.clear();
    refreshLastModified();
  }

  public boolean isEmpty(EItemCategory cat) {
    return AppCollections.isEmpty(items.get(cat));
  }

  public <T extends Clipboardable> List<T> get(EItemCategory cat, Class<T> claz) {
    Collection<? extends Clipboardable> r = get(cat);
    ImmutableList.Builder<T> builder = new ImmutableList.Builder<T>();
    for (Object o : r) {
      T v = claz.cast(o);
      builder.add(v);
    }
    List<T> ret = builder.build();
    return ret;
  }

  public Collection<ClipboardEntry> getItems(EItemCategory category, Long last_time_requested) {
    AppPreconditions.checkNotNull(category);
    last_time_requested = defaultIfNull(last_time_requested, 0L);
    if (hasChanged(last_time_requested)) {
      return items.get(category);
    }
    return ImmutableList.of();
  }

  public boolean hasChanged(Long last_refresh_date) {
    last_refresh_date = defaultIfNull(last_refresh_date, 0L);
    return last_refresh_date < lastModified;
  }

  public long getLastModified() {
    return lastModified;
  }

}
