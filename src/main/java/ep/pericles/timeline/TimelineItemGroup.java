package ep.pericles.timeline;

import java.util.*;

public class TimelineItemGroup {
  private String code;
  private List<TimelineItem> items;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public List<TimelineItem> getItems() {
    return items;
  }

  public void setItems(List<TimelineItem> items) {
    this.items = items;
  }

  public void addItem(TimelineItem item) {
    if (items == null) {
      items = new ArrayList<TimelineItem>();
    }
    items.add(item);
  }
}
