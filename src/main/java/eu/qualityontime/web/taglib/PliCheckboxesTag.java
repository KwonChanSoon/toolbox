package eu.qualityontime.web.taglib;

import javax.servlet.jsp.JspException;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.tags.form.TagWriter;

public class PliCheckboxesTag extends PliAbstractCheckedElementTag {
  @Override
  protected int writeTagContent(TagWriter tagWriter) throws JspException {
    super.writeTagContent(tagWriter);

    if (!isDisabled()) {
      // Write out the 'field was present' marker.
      tagWriter.startTag("input");
      tagWriter.writeAttribute("type", "hidden");
      String name = WebDataBinder.DEFAULT_FIELD_MARKER_PREFIX + getName();
      tagWriter.writeAttribute("name", name);
      tagWriter.writeAttribute("value", processFieldValue(name, "on", getInputType()));
      tagWriter.endTag();
    }

    return SKIP_BODY;
  }

  @Override
  protected String getInputType() {
    return "checkbox";
  }
}
