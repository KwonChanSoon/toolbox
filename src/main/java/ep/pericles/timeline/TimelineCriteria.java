package ep.pericles.timeline;

import org.apache.commons.lang3.builder.*;

import ep.pericles.DateRange;

public class TimelineCriteria {
  private ETimelineUnit unit;
  protected DateRange range;

  public ETimelineUnit getUnit() {
    return unit;
  }

  public void setUnit(ETimelineUnit unit) {
    this.unit = unit;
  }

  public DateRange getRange() {
    return range;
  }

  public void setRange(DateRange range) {
    this.range = range;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }

}
