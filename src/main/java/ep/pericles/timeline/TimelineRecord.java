package ep.pericles.timeline;

import static ep.pericles.AppCollections.Map;

import java.util.Map;

public class TimelineRecord {
  Map<Integer, TimelineRecordItem> map = Map();

  public void putRecordItem(Integer idx, TimelineRecordItem item) {
    map.put(idx, item);
  }

  public Map<Integer, TimelineRecordItem> getItemByPos() {
    return map;
  }

  public TimelineRecord() {
  }

}
