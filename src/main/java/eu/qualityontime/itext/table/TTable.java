package eu.qualityontime.itext.table;

import static eu.qualityontime.AppCollections.*;

import java.util.List;

import org.apache.commons.lang3.builder.*;

import com.google.common.base.Function;

public class TTable extends ATable {
  private Integer numberOfColumn;
  private Float totalWidth;
  private Integer headerRows;
  private Integer footerRows;
  private int[] widths;
  private Float widthPercentage;

  private List<TRow> rows = List();

  public Integer getNumberOfColumn() {
    return numberOfColumn;
  }

  public void setNumberOfColumn(Integer numberOfColumn) {
    this.numberOfColumn = numberOfColumn;
  }

  public void setRow(TRow row) {
    rows.add(row);
  }

  public List<TRow> getRows() {
    return rows;
  }

  public void setRows(List<TRow> rows) {
    this.rows = rows;
  }

  public int getMaxColOfRows() {
    return maxBy(rows, new Function<TRow, Integer>() {
      public Integer apply(TRow input) {
        return intSum(input.getCells(), new Function<TCell, Integer>() {
          public Integer apply(TCell input) {
            return input.getColspan();
          }
        });
      }
    });
  }

  /**
   * fillung all rows with empty cell to fill the max number of rows.
   */
  @Override
  public void done() {
    int maxCol = getMaxColOfRows();
    for (TRow r : getRows()) {
      int diff = maxCol - r.sumOfColls();
      while (diff != 0) {
        r.setCell(new TCell());
        diff--;
      }
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }

  public Float getTotalWidth() {
    return totalWidth;
  }

  public void setTotalWidth(Float totalWidth) {
    this.totalWidth = totalWidth;
  }

  public Integer getHeaderRows() {
    return headerRows;
  }

  public void setHeaderRows(Integer headerRows) {
    this.headerRows = headerRows;
  }

  public Integer getFooterRows() {
    return footerRows;
  }

  public void setFooterRows(Integer footerRows) {
    this.footerRows = footerRows;
  }

  public int[] getWidths() {
    return widths;
  }

  public void setWidths(int[] widths) {
    this.widths = widths;
  }

  public Float getWidthPercentage() {
    return widthPercentage;
  }

  public void setWidthPercentage(Float widthPercentage) {
    this.widthPercentage = widthPercentage;
  }
}