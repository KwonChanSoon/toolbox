package eu.qualityontime.itext.table;

public class TPhrase {
  private String text;
  private String font;

  public TPhrase() {
    this(null, null);
  }

  public TPhrase(String text) {
    this(text, null);
  }

  public TPhrase(String text, String font) {
    this.text = text;
    this.font = font;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getFont() {
    return font;
  }

  public void setFont(String font) {
    this.font = font;
  }
}
