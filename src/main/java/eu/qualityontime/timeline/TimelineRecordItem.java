package eu.qualityontime.timeline;

import java.util.*;

public class TimelineRecordItem {

  private List<Timelineable> temporalObjects;

  private String code;
  private String label;

  public List<Timelineable> getTemporalObjects() {
    return temporalObjects;
  }

  public void setTemporalObjects(List<Timelineable> temporalObjects) {
    this.temporalObjects = temporalObjects;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public void addTemporalObject(Timelineable timelinable) {
    if (temporalObjects == null) {
      temporalObjects = new ArrayList<Timelineable>();
    }
    temporalObjects.add(timelinable);
  }

  public void addTemporalObjects(List<Timelineable> timelinables) {
    if (temporalObjects == null) {
      temporalObjects = new ArrayList<Timelineable>();
    }
    temporalObjects.addAll(timelinables);
  }

  public static TimelineRecordItem of(String code, String label) {
    TimelineRecordItem item = new TimelineRecordItem();
    item.setCode(code);
    item.setLabel(label);
    return item;
  }

  public TimelineRecordItem() {
  }

}
