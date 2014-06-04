package ep.pericles.timeline;

public enum ETimelineUnit {
  QUARTER_HOUR(1, 96),
  HALF_HOUR(2, 48),
  ONE_HOUR(3, 24),
  HALF_DAY(4, 2),
  WC_SLOT(5, 3);

  private int type;
  private int unitsPerDay;

  private ETimelineUnit(int aType, int someUnitsPerDay) {
    type = aType;
    unitsPerDay = someUnitsPerDay;
  }

  public int getValue() {
    return type;
  }

  public int getUnitsPerDays() {
    return unitsPerDay;
  }

  public static ETimelineUnit getLargestUnit() {
    ETimelineUnit largest = null;
    int minUnitPerDays = 1000;

    for (ETimelineUnit unit : ETimelineUnit.values()) {
      if (largest == null || unit.getUnitsPerDays() < minUnitPerDays) {
        largest = unit;
        minUnitPerDays = unit.getUnitsPerDays();
      }
    }
    return largest;
  }

}
