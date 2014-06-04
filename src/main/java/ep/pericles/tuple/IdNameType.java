package ep.pericles.tuple;

public class IdNameType<T> extends IdName<T> {
  public final String type;
  
  public static <T> IdNameType<T> of(T id, String name, String type) {
    return new IdNameType<T>(id, name, type);
  }

  public IdNameType(T id, String name, String type) {
    super(id, name);
    this.type = type;
  }

  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return "IdNameType [id=" + id + ", name=" + name + ", type=" + type + "]";
  }

}
