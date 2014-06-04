package ep.pericles.timeline;

import static ep.pericles.AppPreconditions.checkNotNull;

import java.util.*;

import ep.pericles.*;

public class TimelineRecordFactory {

  public static TimelineRecord createTimelineRecord(Timeline timeline, List<? extends Timelineable> temporals) {
    checkNotNull(timeline, temporals);
    checkNotNull(timeline.getItems());

    TimelineRecord record = new TimelineRecord();

    if (!temporals.isEmpty()) {
      int idx = 0;
      for (TimelineItem item : timeline.getItems()) {
        checkNotNull(item);
        TimelineRecordItem recordItem = new TimelineRecordItem();
        List<Timelineable> timelineables = getAllTemporalsForItem(item, temporals);
        if (!timelineables.isEmpty()) {
          recordItem.setTemporalObjects(timelineables);
          record.putRecordItem(idx, recordItem);
        }
        idx++;
      }
    }
    return record;
  }

  protected static List<Timelineable> getAllTemporalsForItem(TimelineItem item, List<? extends Timelineable> temporals) {
    List<Timelineable> matching = new ArrayList<Timelineable>();
    DateRange itemRange = DateRange.of(item.getStart(), item.getEnd());

    for (Timelineable t : temporals) {
      checkNotNull(t);
      DateRange tRange = DateRange.of(t.getStartDate(), t.getEndDate());

      if (AppDate.overlaps(tRange, itemRange)) {
        matching.add(t);
      }
    }

    return matching;
  }

}
