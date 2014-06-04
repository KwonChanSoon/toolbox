package ep.pericles.type;

/**
 * See http://commons.apache.org/proper/commons-collections/apidocs/src-html/org/apache/commons/collections4/Closure.html
 */
public interface Closure<T> {
  public void execute(T input);
}
