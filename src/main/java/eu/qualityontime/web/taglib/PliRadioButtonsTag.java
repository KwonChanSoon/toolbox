package eu.qualityontime.web.taglib;

public class PliRadioButtonsTag extends PliAbstractCheckedElementTag {

  @Override
  protected String getInputType() {
    return "radio";
  }
}
