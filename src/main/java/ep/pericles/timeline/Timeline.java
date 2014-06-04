package ep.pericles.timeline;

import static ep.pericles.AppDate.midnight;
import static ep.pericles.AppPreconditions.checkNotNull;

import java.util.*;

import ep.pericles.AppDate;

public class Timeline {

  private List<TimelineItem> items = null;

  public List<TimelineItem> getItems() {
    return items;
  }

  public void setItems(List<TimelineItem> items) {
    this.items = items;
    if (items != null) {
      for (TimelineItem item : items) {
        if (item != null) {
          item.setTimeline(this);
        }
      }
    }
  }

  public void addItem(TimelineItem item) {
    if (items == null) {
      items = new ArrayList<TimelineItem>();
    }
    items.add(item);
    item.setTimeline(this);
  }

  public List<TimelineItemGroup> getDailyGroups() {
    List<TimelineItemGroup> groups = new ArrayList<TimelineItemGroup>();
    checkNotNull(items);

    Date groupDay = null;
    TimelineItemGroup group = null;

    for (TimelineItem item : items) {
      checkNotNull(item);
      Date curDay = midnight(item.getStart());
      if (groupDay == null || !curDay.equals(groupDay)) {
        if (group != null) {
          groups.add(group);
        }
        group = new TimelineItemGroup();
        group.setCode(AppDate.formatIfNotNull(curDay, "dd/MM/yyyy"));
        groupDay = curDay;
      }
      group.addItem(item);
    }
    // Last one...
    if (group != null) {
      groups.add(group);
    }

    return groups;
  }

}
