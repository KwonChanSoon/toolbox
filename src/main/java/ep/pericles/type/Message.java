package ep.pericles.type;

import static com.google.common.base.Objects.equal;

import org.apache.commons.lang3.builder.*;

import com.google.common.base.Function;

public class Message {
  public static Function<String, Message> errorTransformer = new Function<String, Message>() {
    public Message apply(String input) {
      return new Message(EMessageType.ERROR, input);
    }
  };
  public static Function<String, Message> warningTransformer = new Function<String, Message>() {
    public Message apply(String input) {
      return new Message(EMessageType.WARNING, input);
    }
  };

  public static Function<Message, String> textExtractor = new Function<Message, String>() {
    public String apply(Message input) {
      return input.getText();
    }
  };

  private EMessageType type;
  private String title;
  private String text;

  public static Message of(EMessageType type, String text) {
    return new Message(type, text);
  }

  public static Message of(EMessageType type, String title, String text) {
    return new Message(type, title, text);
  }

  public Message() {
  }

  public Message(EMessageType type, String text) {
    this(type, null, text);
  }

  public Message(EMessageType type, String title, String text) {
    this.type = type;
    this.title = title;
    this.text = text;
  }

  public EMessageType getType() {
    return type;
  }

  public String getText() {
    return text;
  }

  protected void setType(EMessageType type) {
    this.type = type;
  }

  protected void setText(String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }

  @Override
  public boolean equals(Object obj) {
    if (null == obj || !(obj instanceof Message)) {
      return false;
    }
    Message that = (Message) obj;
    return this.type.equals(that.type) && this.text.equals(that.text) && equal(this.title, that.title);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

}
