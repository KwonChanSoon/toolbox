package eu.qualityontime;

public class AppRuntimeException extends RuntimeException {

  public AppRuntimeException() {
  }

  public AppRuntimeException(String message) {
    super(message);
  }

  public AppRuntimeException(Throwable cause) {
    super(cause);
  }

  public AppRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

}
