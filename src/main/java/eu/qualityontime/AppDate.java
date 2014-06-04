package eu.qualityontime;

import static eu.qualityontime.AppPreconditions.checkNotNull;
import static eu.qualityontime.DateRange.range;
import static org.joda.time.Days.daysBetween;
import static org.joda.time.Duration.standardMinutes;

import java.sql.Timestamp;
import java.text.*;
import java.util.*;

import com.google.common.base.Objects;
import org.apache.commons.lang3.*;
import org.joda.time.*;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.collect.Range;

import eu.qualityontime.type.EWeekday;

public class AppDate {
  final static long MINUTE = 1000 * 60;
  final static long HOUR = 1000 * 60 * 60;

  public final static String FORMAT_SLASH = "dd/MM/yyyy";
  public final static String FORMAT_SLASH_LONG = "dd/MM/yyyy HH:mm";
  public final static String FORMAT_TIME = "HH:mm";
  public final static String FORMAT_DASH = "dd-MM-yyyy";
  public final static String FORMAT_ORACLE = "yyyy-MM-dd";
  public final static String FORMAT_ORACLE_MEDI = "yyyy-MM-dd HH:mm";
  public final static String FORMAT_ORACLE_LONG = "yyyy-MM-dd HH:mm:ss";
  public final static Date DATE_MAX = AppDate.parse("31/12/2500", FORMAT_SLASH);
  public final static Date DATE_MIN = AppDate.parse("01/01/1950", FORMAT_SLASH);

  /**
   * synonim to parse
   */
  public static Date date(String s) {
    return parse(s);
  }

  public static Date parse(String s) {
    return parse(s, FORMAT_DASH, FORMAT_SLASH, FORMAT_SLASH_LONG, FORMAT_ORACLE, FORMAT_ORACLE_MEDI, FORMAT_ORACLE_LONG);
  }

  public static Date parse(String s, String... format) {
    if (null == s) {
      return null;
    }
    try {
      return parseDateStrictly(s, format);
    } catch (ParseException e) {
      return null;
    }
  }

  /**
   * Why do we need this method instead of commons-lang DateUtil.parseDateStrictly equivalent?
   * Becouse EP has refused to upgrade JBoss (jboss is nt supported anymore). And using
   * this method it requires `commons-lang-2.5 (we are using 2.6). There is one sily EP specific SAR file
   * in the JBoss which has `commons-lang-2.3`. And when deploying on Jboss the application finds the other
   * DateUtil from 2.3. And of course method not found error. I do not want to owngrade the system becouse
   * it can have other side effects plus if we really move to Tomcat this problem is totally eliminated.
   * Details: http://www.eu-foundry.eu.parl.union.eu/issue-tracking/browse/ALSASUPPORT-273
   */
  private static Date parseDateStrictly(String str, String[] parsePatterns) throws ParseException {
    return parseDateWithLeniency(str, parsePatterns, false);
  }

  private static Date parseDateWithLeniency(String str, String[] parsePatterns,
      boolean lenient) throws ParseException {
    if (str == null || parsePatterns == null) {
      throw new IllegalArgumentException("Date and Patterns must not be null");
    }

    SimpleDateFormat parser = new SimpleDateFormat();
    parser.setLenient(lenient);
    ParsePosition pos = new ParsePosition(0);
    for (int i = 0; i < parsePatterns.length; i++) {

      String pattern = parsePatterns[i];

      // LANG-530 - need to make sure 'ZZ' output doesn't get passed to SimpleDateFormat
      if (parsePatterns[i].endsWith("ZZ")) {
        pattern = pattern.substring(0, pattern.length() - 1);
      }

      parser.applyPattern(pattern);
      pos.setIndex(0);

      String str2 = str;
      // LANG-530 - need to make sure 'ZZ' output doesn't hit SimpleDateFormat as it will ParseException
      if (parsePatterns[i].endsWith("ZZ")) {
        int signIdx = indexOfSignChars(str2, 0);
        while (signIdx >= 0) {
          str2 = reformatTimezone(str2, signIdx);
          signIdx = indexOfSignChars(str2, ++signIdx);
        }
      }

      Date date = parser.parse(str2, pos);
      if (date != null && pos.getIndex() == str2.length()) {
        return date;
      }
    }
    throw new ParseException("Unable to parse the date: " + str, -1);
  }

  private static int indexOfSignChars(String str, int startPos) {
    int idx = StringUtils.indexOf(str, '+', startPos);
    if (idx < 0) {
      idx = StringUtils.indexOf(str, '-', startPos);
    }
    return idx;
  }

  private static String reformatTimezone(String str, int signIdx) {
    String str2 = str;
    if (signIdx >= 0 &&
        signIdx + 5 < str.length() &&
        Character.isDigit(str.charAt(signIdx + 1)) &&
        Character.isDigit(str.charAt(signIdx + 2)) &&
        str.charAt(signIdx + 3) == ':' &&
        Character.isDigit(str.charAt(signIdx + 4)) &&
        Character.isDigit(str.charAt(signIdx + 5))) {
      str2 = str.substring(0, signIdx + 3) + str.substring(signIdx + 4);
    }
    return str2;
  }

  public static Date defaultIfNull(Date date, Date def) {
    return (Date) ObjectUtils.defaultIfNull(date, def);
  }

  public static String formatIfNotNull(Object date, String format) {
    if (null == date || !(date instanceof Date)) {
      return "";
    }
    return formatIfNotNull((Date) date, format);
  }

  public static String formatIfNotNull(Date date, Locale locale) {
    if (null == date) {
      return "";
    }
    return DateFormat.getDateInstance(DateFormat.DEFAULT, locale).format(date);
  }

  public static String formatIfNotNull(Date date, String format) {
    if (null == date) {
      return "";
    }
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    return sdf.format(date);
  }

  public static boolean between(Date d, Date from, Date to) {
    Preconditions.checkNotNull(d);
    Preconditions.checkNotNull(from);
    Preconditions.checkNotNull(to);
    return betweenCloseClose(new Interval(new DateTime(from), new DateTime(to)), d);
  }

  public static boolean betweenCloseClose(Interval interval, Date d) {
    return interval.contains(new DateTime(d)) || d.getTime() == interval.getEndMillis();
  }

  public static boolean betweenOpenEnd(Date d, Date from, Date to) {
    AppPreconditions.checkNotNull(d);
    AppPreconditions.checkNotNull(from);
    AppPreconditions.checkNotNull(to);
    return new Interval(new DateTime(from), new DateTime(to)).contains(new DateTime(d));
  }

  public static boolean between(Date d, DateRange range) {
    AppPreconditions.checkNotNull(range);
    return between(d, range.getFrom(), range.getTo());
  }

  public static boolean betweenOpenEnd(Date d, DateRange range) {
    AppPreconditions.checkNotNull(range);
    return betweenOpenEnd(d, range.getFrom(), range.getTo());
  }

  public static String formatToDb(Date d) {
    return formatIfNotNull(d, FORMAT_ORACLE);
  }

  public static String formatToDbLong(Date d) {
    return formatIfNotNull(d, FORMAT_ORACLE_LONG);
  }

  public static String formatForViewDate(Date d) {
    return formatIfNotNull(d, FORMAT_SLASH);
  }

  public static String formatForViewDateTime(Date d) {
    return formatIfNotNull(d, FORMAT_SLASH_LONG);
  }

  public static String formatForViewTime(Date d) {
    return formatIfNotNull(d, FORMAT_TIME);
  }

  public static String formatForView(DateRange range) {
    if (range == null) {
      return "N/A";
    }
    return formatForViewDateTime(range.getFrom()) + " - " + formatForViewTime(range.getTo());
  }

  public static boolean isMidnight(Date d) {
    AppPreconditions.checkNotNull(d);
    return new DateMidnight(d).isEqual(new DateTime(d));
  }

  public static Date closestMidnightIfPossible(Date d, int gapInMinutes) {
    AppPreconditions.checkNotNull(d);
    DateTime dt = new DateTime(d);
    DateMidnight currentMidnight = new DateMidnight(d);
    Duration dur = new Duration(currentMidnight, dt);
    if (gapInMinutes >= dur.getStandardMinutes()) {
      return currentMidnight.toDate();
    }
    DateMidnight nextMidnight = currentMidnight.plusDays(1);
    Duration durToNext = new Duration(dt, nextMidnight);
    if (gapInMinutes >= durToNext.getStandardMinutes()) {
      return nextMidnight.toDate();
    }
    return d;
  }

  public static boolean isCoverWholeDay(Date currentDate, DateRange dateRange) {
    AppPreconditions.checkNotNull(currentDate);
    AppPreconditions.checkNotNull(dateRange);
    return new Interval(new DateTime(dateRange.getFrom()), new DateTime(dateRange.getTo())).contains(new Interval(
        new DateMidnight(currentDate), new DateMidnight(currentDate).plusDays(1)));
  }

  /**
   * Testing whether covering day fraction dateRanges are covering the whole day for currentDay.
   */
  public static boolean isCoverWholeDay(Date currentDate, Collection<DateRange> dateRanges) {
    AppPreconditions.checkNotNull(currentDate);
    AppPreconditions.checkNotNull(dateRanges);
    // at least one is covering
    for (DateRange r : dateRanges) {
      if (isCoverWholeDay(currentDate, r)) {
        return true;
      }
    }

    // joining if possible
    DateRange collector = join(dateRanges);
    if (null == collector) {
      return false;
    }

    if (isCoverWholeDay(currentDate, collector)) {
      return true;
    }
    return false;
  }

  private static Ordering<DateRange> RANGE_ORDERING = new Ordering<DateRange>() {
    @Override
    public int compare(DateRange o1, DateRange o2) {
      return o1.getFrom().compareTo(o2.getFrom());
    }
  };

  public static DateRange join(Collection<DateRange> dateRanges) {
    List<DateRange> sorted = RANGE_ORDERING.sortedCopy(ImmutableList.copyOf(dateRanges));
    DateRange collector = new DateRange(sorted.get(0));
    for (DateRange r : sorted) {
      if (between(r.getFrom(), collector)) {
        collector = join(collector, r);
      }
      else {
        // there is a gap not covered
        return null;
      }
    }
    return collector;
  }

  public static DateRange join(DateRange a, DateRange b) {
    if (contains(a, b)) {
      return a;
    }
    if (contains(b, a)) {
      return b;
    }
    if (between(b.getFrom(), a)) {
      return new DateRange(a.getFrom(), b.getTo());
    }
    if (between(a.getFrom(), b)) {
      return new DateRange(b.getFrom(), a.getTo());
    }
    return null;
  }

  public static DateRange completeDaysPeriod(DateRange p) {
    AppPreconditions.checkNotNull(p);
    Date start = midnight(p.getFrom());
    Date end = isMidnight(p.getTo()) && p.getTo().after(start) ? p.getTo() : tomorrowMidnight(p.getTo());
    DateRange adjusted = DateRange.of(start, end);
    return adjusted;
  }

  public static boolean isCoverWholeDay(Date currentDate, DateRange... dateRanges) {
    return isCoverWholeDay(currentDate, Arrays.asList(dateRanges));
  }

  public static boolean contains(DateRange container, DateRange contained) {
    AppPreconditions.checkNotNull(container);
    return new Interval(new DateTime(container.getFrom()), new DateTime(container.getTo())).contains(new Interval(
        new DateTime(contained.getFrom()), new DateTime(contained.getTo())));
  }

  /**
   * Checks whether 2 ranges overlap(strictly).
   * Ranges can be open ended (both sides) having a corresponding null boundary
   * 
   * @param range1
   * @param range2
   * @return true if there is a overlap between range1 and range2
   */
  public static boolean overlaps(DateRange range1, DateRange range2) {
    AppPreconditions.checkNotNull(range1, range2);
    Date f1 = range1.getFrom();
    Date t1 = range1.getTo();
    Date f2 = range2.getFrom();
    Date t2 = range2.getTo();

    return ((f1 == null || t2 == null || f1.before(t2)) && (t1 == null || f2 == null || t1.after(f2)));
  }

  public static boolean overlaps(DateRange dr, Range<LocalTime> range) {
    return overlapsAny(splitByDays(dr), range);
  }

  private static boolean overlapsAny(Collection<DateRange> dailyRanges, Range<LocalTime> range) {
    for (DateRange dr : dailyRanges) {
      LocalTime to = new LocalTime(dr.getTo());
      if (isMidnight(dr.getTo())) {
        to = new LocalTime(23, 59, 59);
      }
      if (Range.open(new LocalTime(dr.getFrom()), to).isConnected(range)) {
        return true;
      }
    }
    return false;
  }

  private static Collection<DateRange> splitByDays(DateRange range) {
    if (isSameDay(range.getFrom(), range.getTo())) {
      return ImmutableList.of(range);
    }
    List<DateRange> res = new ArrayList<DateRange>();
    DateTime lastMN = new DateMidnight(range.getTo()).toDateTime();
    DateTime beforeFirstMN = new DateMidnight(range.getFrom()).toDateTime();
    DateTime currentStart = lastMN.toDateTime();
    DateTime currentEnd = new DateTime(range.getTo());
    do {
      DateRange r = range(currentStart.toDate(), currentEnd.toDate());
      res.add(r);
      currentEnd = currentStart;
      currentStart = currentStart.minusDays(1);
    } while (!beforeFirstMN.isEqual(currentStart));
    res.add(range(range.getFrom(), currentEnd.toDate()));
    return ImmutableList.copyOf(res);
  }

  public static boolean isSameDay(Date d1, Date d2) {
    return new DateMidnight(d1).isEqual(new DateMidnight(d2));
  }

  /**
   * @return Monday at midnigh of the week the iven date is belonging to
   */
  public static Date thisMonday(Date ref) {
    return thisWeekday(EWeekday.MONDAY, ref);
  }

  public static Date thisTuesday(Date ref) {
    return new LocalDate(ref).withDayOfWeek(DateTimeConstants.TUESDAY).toDate();
  }

  public static Date thisWednesday(Date ref) {
    return new LocalDate(ref).withDayOfWeek(DateTimeConstants.WEDNESDAY).toDate();
  }

  public static Date thisThursday(Date ref) {
    return new LocalDate(ref).withDayOfWeek(DateTimeConstants.THURSDAY).toDate();
  }

  public static Date thisFriday(Date ref) {
    return new LocalDate(ref).withDayOfWeek(DateTimeConstants.FRIDAY).toDate();
  }

  public static Date thisSunday(Date ref) {
    return new LocalDate(ref).withDayOfWeek(DateTimeConstants.SUNDAY).toDate();
  }

  public static Date thisWeekday(EWeekday weekday, Date ref) {
    return new LocalDate(ref).withDayOfWeek(weekday.getOrder()).toDate();
  }

  public static LocalDate thisWeekday(EWeekday weekday, LocalDate ref) {
    return ref.withDayOfWeek(weekday.getOrder());
  }

  /**
   * Return the day of the year
   * 
   * @param ref
   *          of the given date
   * @return
   */
  public static int day(Date ref) {
    return new LocalDate(ref).getDayOfYear();
  }

  /**
   * Return the day of the month
   * 
   * @param ref of the given date
   * @return
   */
  public static int dayOfMonth(Date ref) {
    return new LocalDate(ref).getDayOfMonth();
  }

  public static int week(Date ref) {
    return new LocalDate(ref).getWeekOfWeekyear();
  }

  public static Integer month(Date d) {
    return new LocalDate(d).getMonthOfYear();
  }

  public static Date mondayOf(int year, int week) {
    LocalDate ld = new LocalDate().withYear(year).withWeekOfWeekyear(week).withDayOfWeek(DateTimeConstants.MONDAY);
    return ld.toDate();
  }

  public static Integer year(Date d) {
    return new LocalDate(d).getYear();
  }

  public static Date year(Date d, Integer year) {
    return new LocalDate(d).withYear(year).toDate();
  }

  public static Date tomorrowMidnight(Date d) {
    return new DateMidnight(d).plusDays(1).toDate();
  }

  public static Date midnight(Date d) {
    return new DateMidnight(d).toDate();
  }

  public static Date lastMinuteOfDay(Date d) {
    return new DateTime(d).withMillisOfDay(0).plusDays(1).minus(standardMinutes(1L)).toDate();
  }

  public static String dayNameOfWeek(Date d) {
    return new DateMidnight(d).dayOfWeek().getAsText(Locale.ENGLISH);
  }

  public static Date week_ago(Date ref) {
    return week_ago(ref, 1);
  }

  public static Date week_ago(Date ref, int nrOfWeekAgo) {
    return new DateMidnight(ref).minusDays(nrOfWeekAgo * 7).toDate();
  }

  public static Date next_week(Date ref) {
    return next_week(ref, 1);
  }

  public static Date next_week(Date ref, int nrOfWeek) {
    return new DateMidnight(ref).plusDays(nrOfWeek * 7).toDate();
  }

  public static String duration(DateRange dr) {
    long dif = dr.getTo().getTime() - dr.getFrom().getTime();
    long hourPart = dif / HOUR;
    long minutesPart = (dif % HOUR) / MINUTE;
    return String.format("%1$02d:%2$02d", hourPart, minutesPart);
  }

  public static Date addYear(Date d, Integer nbYears) {
    AppPreconditions.checkNotNull(d, nbYears);
    return new LocalDate(d).plusYears(nbYears).toDate();
  }
  
  public static Date addDays(Date d, Integer nbDays) {
    AppPreconditions.checkNotNull(d, nbDays);
    return new LocalDate(d).plusDays(nbDays).toDate();
  }
  
  public static Date addMinutes(Date d, Integer nbMinutes) {
    AppPreconditions.checkNotNull(d, nbMinutes);
    return new LocalDateTime(d).plusMinutes(nbMinutes).toDate();
  }

  public static Date date(Date d) {
    if (null == d) {
      return null;
    }
    if (d instanceof Date && d.getClass() != Date.class) {
      return new Date(d.getTime());
    }
    return d;
  }

  public static Timestamp timestamp(Date d) {
    if (null == d) {
      return null;
    }
    if (d instanceof Timestamp) {
      return (Timestamp) d;
    }
    return new Timestamp(d.getTime());
  }

  /**
   * Assuming closedOpen ranges.
   */
  public static Collection<DateRange> dinstinctRanges(Collection<DateRange> input) {
    Collection<Range<Date>> dateRanges = AppCollections.List(input.size());
    for (DateRange r : input) {
      dateRanges.add(Range.closedOpen(r.getFrom(), r.getTo()));
    }
    Collection<Range<Date>> dinstincts = AppRanges.dinstinctRanges(dateRanges);
    Collection<DateRange> res = AppCollections.List(dinstincts.size());
    for (Range<Date> r : dinstincts) {
      res.add(DateRange.range(r.lowerEndpoint(), r.upperEndpoint()));
    }
    return ImmutableList.copyOf(res);
  }

  /**
   * Minute to milisecond
   */
  public static long minute2ms(int in) {
    return 1000 * 60 * in;
  }

  /**
   * Second to milisecond
   */
  public static long second2ms(int in) {
    return 1000 * in;
  }

  public static boolean isMonday(Date d) {
    return Objects.equal(thisMonday(d), new DateMidnight(d).toDate());
  }

  public static boolean isThursday(Date d) {
    return Objects.equal(thisThursday(d), new DateMidnight(d).toDate());
  }

  public static boolean isFriday(Date d) {
    return Objects.equal(thisFriday(d), new DateMidnight(d).toDate());
  }

  public static boolean isWeekday(EWeekday weekday, Date d) {
    return Objects.equal(thisWeekday(weekday, d), new DateMidnight(d).toDate());
  }

  public static boolean isWeekday(EWeekday weekday, LocalDate d) {
    return Objects.equal(thisWeekday(weekday, d), d);
  }

  public static boolean between(Date d, Range<LocalTime> range) {
    DateTime inDt = new DateTime(d);
    LocalDate ld = new LocalDate(inDt);
    Range<DateTime> dtRange = Range.range(ld.toDateTime(range.lowerEndpoint()), range.lowerBoundType(),
        ld.toDateTime(range.upperEndpoint()), range.upperBoundType());
    return dtRange.contains(inDt);
  }

  public static Date prevYear(Date in) {
    return new DateMidnight(in).minusYears(1).toDate();
  }

  /**
   * Happy New Year!!! BUEK!!!
   */
  public static Date beginningOfYear(Date ref) {
    return new DateMidnight(ref).withDayOfYear(1).toDate();
  }

  /**
   * In practice this is the beginning of the next year
   */
  public static Date endOfYear(Date ref) {
    return new DateMidnight(ref).plusYears(1).withDayOfYear(1).toDate();
  }

  /**
   * Start testing equality and java.sql.Date/Timestamp and a java.util.Date
   * and you will understand why this function is needed.
   */
  public static boolean equals(Date d1, Date d2) {
    return new DateTime(d1).equals(new DateTime(d2));
  }
  
  public static int calendarDaysBetween(Date d1, Date d2) {
    checkNotNull(d1, d2);
    return daysBetween(new DateMidnight(d1), new DateMidnight(d2)).getDays();
  }
 
}
