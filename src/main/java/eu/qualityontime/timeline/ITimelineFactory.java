package eu.qualityontime.timeline;

import java.util.Date;

public interface ITimelineFactory {

  public abstract Timeline newTimeline();

  public abstract TimelineItem newTimelineItem(Long id, String code, Date start, Date end);

}