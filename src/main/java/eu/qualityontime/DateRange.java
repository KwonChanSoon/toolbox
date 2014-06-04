package eu.qualityontime;

import static com.google.common.collect.ImmutableList.copyOf;
import static eu.qualityontime.AppCollections.*;
import static eu.qualityontime.AppDate.*;

import java.util.*;

import org.joda.time.*;

import com.google.common.base.*;
import com.google.common.collect.*;

import eu.qualityontime.type.IRangeable;

public class DateRange implements IRangeable<Date> {
  public static enum EDateRangeFormat {
    HOURS("HH:mm"), MEDIUM("yyyy-MM-dd HH:mm"), LONG("yyyy-MM-dd HH:mm:ss");
    private final String dateFormat;

    private EDateRangeFormat(String format) {
      this.dateFormat = format;
    }

    public String format(DateRange d) {
      return formatIfNotNull(d.getFrom(), dateFormat) + " - " + formatIfNotNull(d.getTo(), dateFormat);
    }
  }

  private Date from;
  private Date to;
  public static final Ordering<DateRange> DATE_RANGE_ORDERING = new Ordering<DateRange>() {
    @Override
    public int compare(DateRange left, DateRange right) {
      if (left == right)
        return 0;
      int fromVal = left.from.compareTo(right.from);
      return 0 == fromVal ? left.to.compareTo(right.to) : fromVal;
    }
  };
  public final static DateRange NULL = new DateRange(null, null);

  public static DateRange range(Range<Date> range) {
    return range(range.lowerEndpoint(), range.upperEndpoint());
  }

  public static DateRange range(String date, String timeInterval) {
    Preconditions.checkNotNull(date);
    Preconditions.checkNotNull(timeInterval);
    String[] times = timeInterval.split("\\-");
    return range(AppDate.parse(date + " " + times[0]), AppDate.parse(date + " " + times[1]));
  }

  public static DateRange range(Date from, Date to) {
    return new DateRange(from, to);
  }

  public static DateRange of(Date from, Date to) {
    return new DateRange(from, to);
  }

  public DateRange(Date from, Date to) {
    this.from = AppDate.date(from);
    this.to = AppDate.date(to);
  }

  public DateRange(DateRange r) {
    this.from = r.from;
    this.to = r.to;
  }

  private DateRange(Interval interval) {
    if (interval != null) {
      this.from = interval.getStart().toDate();
      this.to = interval.getEnd().toDate();
    }
  }

  public Date getFrom() {
    return from;
  }

  public void setFrom(Date from) {
    this.from = from;
  }

  public Date getTo() {
    return to;
  }

  public void setTo(Date to) {
    this.to = to;
  }

  @Override
  public String toString() {
    return "Range(" + formatToDbLong(getFrom()) + "," + formatToDbLong(getTo()) + ")";
  }

  public boolean isEqual(DateRange other) {
    return this.from.equals(other.from) && this.to.equals(other.to);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(from, to);
  }

  @Override
  public boolean equals(Object obj) {
    if (null == obj || !(obj instanceof DateRange)) {
      return false;
    }
    DateRange that = (DateRange) obj;
    return (this == that) || (Objects.equal(this.from, that.from) && Objects.equal(this.to, that.to));
  }

  public String duration() {
    return AppDate.duration(this);
  }

  public static DateRange intersection(DateRange range1, DateRange range2) {
    if (!AppDate.overlaps(range1, range2)) {
      return null;
    }
    DateRange inter = new DateRange(range1);
    if (range1.startsBefore(range2)) {
      inter.setFrom(range2.getFrom());
    }
    if (range1.endsAfter(range2)) {
      inter.setTo(range2.getTo());
    }
    return inter;
  }

  public static Collection<DateRange> eliminateInclusizeRanges(final Collection<DateRange> ranges) {
    if (isEmpty(ranges) || 1 == ranges.size()) {
      return ranges;
    }
    final List<Range<Date>> rs = AppCollections.transformList(copyOf(ranges), new Function<DateRange, Range<Date>>() {
      @Override
      public Range<Date> apply(DateRange input) {
        return Range.closedOpen(input.getFrom(), input.getTo());
      }
    });
    List<Range<Date>> no_inclusons = copyOf(reject(rs, new Predicate<Range<Date>>() {
      @Override
      public boolean apply(final Range<Date> a) {
        return Iterables.any(rs, new Predicate<Range<Date>>() {
          @Override
          public boolean apply(Range<Date> b) {
            return !a.equals(b) && b.encloses(a);
          }
        });
      }
    }));

    List<DateRange> ret = transformList(no_inclusons, new Function<Range<Date>, DateRange>() {
      @Override
      public DateRange apply(Range<Date> input) {
        return range(input.lowerEndpoint(), input.upperEndpoint());
      }
    });
    return ret;
  }

  public boolean contains(DateRange range) {
    Interval thisInterval = toInterval();
    Interval anInterval = range.toInterval();
    if (thisInterval != null && anInterval != null) {
      return thisInterval.contains(anInterval);
    }
    return false;
  }

  private Interval toInterval() {
    if (from != null && to != null) {
      return new Interval(from.getTime(), to.getTime());
    }
    return null;
  }

  public boolean sameFrom(DateRange range) {
    return from != null && range != null && from.equals(range.getFrom());
  }

  public boolean sameTo(DateRange range) {
    return to != null && range != null && to.equals(range.getTo());
  }

  /**
   * Check whether this range starts before range2
   * If both ranges starts at the same time, return false
   * Handles open ended (null from) boundaries
   * 
   * @param range2
   * @return true if this range starts strictly before range2
   */
  public boolean startsBefore(DateRange range2) {
    AppPreconditions.checkNotNull(range2);
    return range2.getFrom() != null && (from == null || from.before(range2.getFrom()));
  }

  /**
   * Check whether this range ends after range2
   * If both ranges ends at the same time, return false
   * Handles open ended (null to) boundaries
   * 
   * @param range2 The range to compare to
   * @return true if this range ends strictly after range2
   */
  public boolean endsAfter(DateRange range2) {
    AppPreconditions.checkNotNull(range2);
    return range2.getTo() != null && (to == null || to.after(range2.getTo()));
  }

  public boolean abuts(DateRange range2) {
    return toInterval().abuts(range2.toInterval());
  }

  /**
   * Return true if this range is entirely before range 2
   * 
   * @param range2
   * @return
   */
  public boolean before(DateRange range2) {
    if (range2 != null) {
      return this.toInterval().isBefore(range2.toInterval());
    }
    return true;
  }

  /**
   * Return true if this range is entirely after range 2
   * 
   * @param range2
   * @return
   */
  public boolean after(DateRange range2) {
    if (range2 != null) {
      return this.toInterval().isAfter(range2.toInterval());
    }
    return true;
  }

  public DateRange gap(DateRange aRange) {
    Interval gap = this.toInterval().gap(aRange.toInterval());
    return gap != null ? new DateRange(gap) : null;
  }

  @Override
  public Date lowerEndpoint() {
    return getFrom();
  }

  @Override
  public Date upperEndpoint() {
    return getTo();
  }

  public Duration getDuration() {
    return new Duration(new DateTime(this.getFrom()), new DateTime(getTo()));
  }

  public String formatted() {
    return EDateRangeFormat.HOURS.format(this);
  }

  public boolean contains(Date d) {
    return Range.closedOpen(getFrom(), getTo()).contains(d);
  }

}
