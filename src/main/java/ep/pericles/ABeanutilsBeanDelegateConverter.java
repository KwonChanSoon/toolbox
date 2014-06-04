package ep.pericles;

import org.apache.commons.beanutils.Converter;

import ep.pericles.commons.JakartaBeanutilsBean;

public abstract class ABeanutilsBeanDelegateConverter implements Converter {
  protected JakartaBeanutilsBean delegeConverter;

  public void setDelegeConverter(JakartaBeanutilsBean delegeConverter) {
    this.delegeConverter = delegeConverter;
  }
}
