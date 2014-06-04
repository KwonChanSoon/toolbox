package eu.qualityontime.timeline;

import java.util.Date;

public class TimelineItem {
  private Long id;
  private String code;
  private Date start;
  private Date end;
  protected Timeline timeline;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  // Do not use the getter naming convention
  // to prevent serialisation to be too large / cyclic ref 
  public Timeline timeline() {
    return timeline;
  }

  public void setTimeline(Timeline timeline) {
    this.timeline = timeline;
  }

  public static TimelineItem of(Long id, String code, Date start, Date end) {
    TimelineItem i = new TimelineItem();
    i.setId(id);
    i.setCode(code);
    i.setStart(start);
    i.setEnd(end);

    return i;

  }

}
