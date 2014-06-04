package eu.qualityontime.itext.table;

import static eu.qualityontime.AppCollections.List;

import java.util.Collection;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TCell extends ATable {
  private Collection<TPhrase> phrases = List();
  private Integer colspan = 1;
  private Collection<TTable> tables = List();
  private Integer border;
  private Integer horizontalAlignment;
  private Integer verticalAlignment;
  private String backgroundColor;
  private Float fixedHeight;
  private String backgroundPicture;
  private String image;
  private Float padding;

  public void setTable(TTable table) {
    this.tables.add(table);
  }

  public Collection<TTable> getTables() {
    return tables;
  }

  public void setTables(Collection<TTable> tables) {
    this.tables = tables;
  }

  @Override
  public void done() {
    for (TTable t : tables) {
      t.done();
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public Integer getColspan() {
    return colspan;
  }

  public void setColspan(Integer colspan) {
    this.colspan = colspan;
  }

  public Integer getBorder() {
    return border;
  }

  public void setBorder(Integer border) {
    this.border = border;
  }

  public void setPhrase(TPhrase phrase) {
    phrases.add(phrase);
  }

  public Collection<TPhrase> getPhrases() {
    return phrases;
  }

  public void setPhrases(Collection<TPhrase> phrases) {
    this.phrases = phrases;
  }

  public Integer getHorizontalAlignment() {
    return horizontalAlignment;
  }

  public void setHorizontalAlignment(Integer horizontalAlignment) {
    this.horizontalAlignment = horizontalAlignment;
  }

  public Integer getVerticalAlignment() {
    return verticalAlignment;
  }

  public void setVerticalAlignment(Integer verticalAlignment) {
    this.verticalAlignment = verticalAlignment;
  }

  public String getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(String backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public Float getFixedHeight() {
    return fixedHeight;
  }

  public void setFixedHeight(Float fixedHeight) {
    this.fixedHeight = fixedHeight;
  }

  public String getBackgroundPicture() {
    return backgroundPicture;
  }

  public void setBackgroundPicture(String backgroundPicture) {
    this.backgroundPicture = backgroundPicture;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Float getPadding() {
    return padding;
  }

  public void setPadding(Float padding) {
    this.padding = padding;
  }
}
