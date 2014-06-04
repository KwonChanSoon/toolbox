package eu.qualityontime.itext.table;

import static eu.qualityontime.AppCollections.*;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.base.Function;

public class TRow extends ATable {
  private List<TCell> cells = List();

  public void setCell(TCell c) {
    cells.add(c);
  }

  public List<TCell> getCells() {
    return cells;
  }

  public void setCells(List<TCell> cells) {
    this.cells = cells;
  }

  public int getNrOfCells() {
    return getCells().size();
  }

  public int sumOfColls() {
    return intSum(this.getCells(), new Function<TCell, Integer>() {
      public Integer apply(TCell input) {
        return input.getColspan();
      }
    });
  }

  @Override
  public void done() {
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
