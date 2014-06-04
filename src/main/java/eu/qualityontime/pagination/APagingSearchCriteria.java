package eu.qualityontime.pagination;

public abstract class APagingSearchCriteria {
  private PagingCriteria pagingCriteria;

  public PagingCriteria getPagingCriteria() {
    return pagingCriteria;
  }

  public void setPagingCriteria(PagingCriteria pagingCriteria) {
    this.pagingCriteria = pagingCriteria;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((pagingCriteria == null) ? 0 : pagingCriteria.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    APagingSearchCriteria other = (APagingSearchCriteria) obj;
    if (pagingCriteria == null) {
      if (other.pagingCriteria != null)
        return false;
    }
    else if (!pagingCriteria.equals(other.pagingCriteria))
      return false;
    return true;
  }

}
