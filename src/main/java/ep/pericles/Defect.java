package ep.pericles;

/**
 * When throwing this it is always a developer issue;
 */
public class Defect extends RuntimeException {

  public Defect() {
    super();
  }

  public Defect(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public Defect(String arg0) {
    super(arg0);
  }

  public Defect(Throwable arg0) {
    super(arg0);
  }

}
