package eu.qualityontime.type;

public enum EWeekday {
  MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6), SUNDAY(7);
  private final int order;

  private EWeekday(int order) {
    this.order = order;
  }

  public int getOrder() {
    return order;
  }

}
