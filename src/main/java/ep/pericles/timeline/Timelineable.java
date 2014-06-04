package ep.pericles.timeline;

import java.util.Date;

/**
 * Interface for entities which can be referred in a TimelineRecordItem
 * 
 * @author epolkowski
 * 
 */
public interface Timelineable {

  public Date getStartDate();

  public Date getEndDate();

}
