package ep.pericles;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.*;
import org.joda.time.format.*;

public class DurationHumanizer {
  static DurationHumanizer instance = new DurationHumanizer(AppCollections.Map(
      "default", Pair.of("((\\d+)h)?((\\d+)m)?", new PeriodFormatterBuilder()
          .appendHours().appendSuffix("h")
          .appendMinutes().appendSuffix("m")
          )
      , "hh:mm", Pair.of("(\\d\\d):(\\d\\d)", new PeriodFormatterBuilder()
          .appendHours()
          .appendSeparator(":")
          .appendMinutes()
          )
      , "minutes", Pair.of("(\\d+)", new PeriodFormatterBuilder()
          .appendMinutes()
          )));

  public static boolean isValid(String string) {
    return instance.valid(string);
  }

  public static long parseInMillis(String string) {
    return instance.getMillis(string);
  }

  public static long parseInMinutes(String string) {
    return instance.getMillis(string) / 1000 / 60;
  }

  public static String formatMillis(long millis) {
    ReadablePeriod period = new Period(millis);
    return instance.getFormatter("default").print(period);
  }

  public static String formatMinutes(long minutes) {
    ReadablePeriod period = new Period(minutes * 60 * 1000);
    return instance.getFormatter("default").print(period);
  }

  public PeriodFormatter getFormatter(String key) {
    return parsers.get(key).getRight().toFormatter();
  }

  private Map<?, Pair<String, PeriodFormatterBuilder>> parsers;

  public DurationHumanizer(Map<?, Pair<String, PeriodFormatterBuilder>> parsers) {
    this.parsers = parsers;
  }

  public boolean valid(String string) {
    if (StringUtils.isBlank(string)) {
      return false;
    }
    string = normalize(string);
    for (Pair<String, PeriodFormatterBuilder> pat : parsers.values()) {
      if (string.matches(pat.getLeft())) {
        return true;
      }
    }
    return false;
  }

  public long getMillis(String string) {
    if (!valid(string)) {
      throw new InvalidFormat();
    }
    ReadablePeriod period = parse(string);
    return period.toMutablePeriod().toDurationFrom(new DateTime()).getMillis();
  }

  public PeriodParser getParser(String string) {
    for (Pair<String, PeriodFormatterBuilder> e : parsers.values()) {
      if (string.matches(e.getKey())) {
        return e.getValue().toParser();
      }
    }
    throw new InvalidFormat();
  }

  protected String normalize(String string) {
    return StringUtils.replaceChars(string, " ", "");
  }

  protected ReadablePeriod parse(String string) {
    string = normalize(string);
    PeriodParser parser = getParser(string);
    MutablePeriod period = new MutablePeriod();
    parser.parseInto(period, string, 0, Locale.getDefault());
    return period;
  }

  public static class InvalidFormat extends RuntimeException {
  }

}
