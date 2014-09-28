package eu.qualityontime.diff;

public class ToStringPrintVisitor extends PrintVisitor {
  private StringBuilder sb = new StringBuilder();

  @Override
  protected void printPostfix() {
    sb.append("\n");
  }

  @Override
  protected void print(String string) {
    sb.append(string);
  }

  @Override
  protected void printPrefix() {
    sb.append(indent());
  }

  public String getString() {
    return sb.toString();
  }

}
