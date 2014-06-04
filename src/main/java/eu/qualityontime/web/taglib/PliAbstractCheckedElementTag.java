package eu.qualityontime.web.taglib;

import java.util.*;

import javax.servlet.jsp.JspException;

import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.tags.form.*;

import eu.qualityontime.AppReflection;

public abstract class PliAbstractCheckedElementTag extends AbstractMultiCheckedElementTag {

  private String cssLabelClass;
  private String itemTooltip;

  public String getCssLabelClass() {
    return cssLabelClass;
  }

  public void setCssLabelClass(String cssLabelClass) {
    this.cssLabelClass = cssLabelClass;
  }

  public String getItemTooltip() {
    return itemTooltip;
  }

  public void setItemTooltip(String itemTooltip) {
    this.itemTooltip = itemTooltip;
  }

  /**
   * Renders the '<code>input type="radio"</code>' element with the configured {@link #setItems(Object)} values. Marks the element as checked if the
   * value matches the bound value.
   */
  @SuppressWarnings("rawtypes")
  @Override
  protected int writeTagContent(TagWriter tagWriter) throws JspException {
    Object items = getItems();
    Object itemsObject = (items instanceof String ? evaluate("items", items) : items);

    String itemValue = getItemValue();
    String itemLabel = getItemLabel();
    String itemTooltip = getItemTooltip();
    String valueProperty =
        (itemValue != null ? ObjectUtils.getDisplayString(evaluate("itemValue", itemValue)) : null);
    String labelProperty =
        (itemLabel != null ? ObjectUtils.getDisplayString(evaluate("itemLabel", itemLabel)) : null);
    String tooltipProperty =
        (itemTooltip != null ? ObjectUtils.getDisplayString(evaluate("itemTooltip", itemTooltip)) : null);

    Class<?> boundType = getBindStatus().getValueType();
    if (itemsObject == null && boundType != null && boundType.isEnum()) {
      itemsObject = boundType.getEnumConstants();
    }

    if (itemsObject == null) {
      throw new IllegalArgumentException("Attribute 'items' is required and must be a Collection, an Array or a Map");
    }

    if (itemsObject.getClass().isArray()) {
      Object[] itemsArray = (Object[]) itemsObject;
      for (int i = 0; i < itemsArray.length; i++) {
        Object item = itemsArray[i];
        writeObjectEntry(tagWriter, valueProperty, labelProperty, tooltipProperty, item, i);
      }
    }
    else if (itemsObject instanceof Collection) {
      final Collection optionCollection = (Collection) itemsObject;
      int itemIndex = 0;
      for (Iterator it = optionCollection.iterator(); it.hasNext(); itemIndex++) {
        Object item = it.next();
        writeObjectEntry(tagWriter, valueProperty, labelProperty, tooltipProperty, item, itemIndex);
      }
    }
    else if (itemsObject instanceof Map) {
      final Map optionMap = (Map) itemsObject;
      int itemIndex = 0;
      for (Iterator it = optionMap.entrySet().iterator(); it.hasNext(); itemIndex++) {
        Map.Entry entry = (Map.Entry) it.next();
        writeMapEntry(tagWriter, valueProperty, labelProperty, tooltipProperty, entry, itemIndex);
      }
    }
    else {
      throw new IllegalArgumentException("Attribute 'items' must be an array, a Collection or a Map");
    }

    return SKIP_BODY;
  }

  private void writeObjectEntry(TagWriter tagWriter, String valueProperty,
      String labelProperty, String tooltipProperty, Object item, int itemIndex) throws JspException {

    Object renderValue;
    if (valueProperty != null) {
      renderValue = AppReflection.getPropOrField(item, valueProperty);
    }
    else if (item instanceof Enum) {
      renderValue = ((Enum<?>) item).name();
    }
    else {
      renderValue = item;
    }

    Object renderLabel = (labelProperty != null ? AppReflection.getPropOrField(item, labelProperty) : item);
    Object renderTooltip = (tooltipProperty != null ? AppReflection.getPropOrField(item, tooltipProperty) : null);
    writeElementTag(tagWriter, item, renderValue, renderLabel, renderTooltip, itemIndex);
  }

  private void writeMapEntry(TagWriter tagWriter, String valueProperty,
      String labelProperty, String tooltipProperty, Map.Entry<?, ?> entry, int itemIndex) throws JspException {

    Object mapKey = entry.getKey();
    Object mapValue = entry.getValue();
    Object renderValue = (valueProperty != null ?
        AppReflection.getPropOrField(mapKey, valueProperty) : mapKey.toString());
    Object renderLabel = (labelProperty != null ?
        AppReflection.getPropOrField(mapValue, labelProperty) : mapValue.toString());
    Object renderTooltip = (tooltipProperty != null ?
        AppReflection.getPropOrField(mapValue, tooltipProperty) : null);
    writeElementTag(tagWriter, mapKey, renderValue, renderLabel, renderTooltip, itemIndex);
  }

  private void writeElementTag(TagWriter tagWriter, Object item, Object value, Object label, Object tooltip, int itemIndex)
      throws JspException {

    if (itemIndex > 0) {
      Object resolvedDelimiter = evaluate("delimiter", getDelimiter());
      if (resolvedDelimiter != null) {
        tagWriter.appendValue(resolvedDelimiter.toString());
      }
    }
    String id = resolveId();
    tagWriter.startTag("label");
    tagWriter.writeAttribute("for", id);
    writeOptionalAttribute(tagWriter, "class", getCssLabelClass());
    writeOptionalAttribute(tagWriter, "title", convertToDisplayString(tooltip));
    tagWriter.startTag("input");
    writeOptionalAttribute(tagWriter, "id", id);
    writeOptionalAttribute(tagWriter, "name", getName());
    writeOptionalAttributes(tagWriter);
    tagWriter.writeAttribute("type", getInputType());
    renderFromValue(item, value, tagWriter);
    tagWriter.endTag();
    tagWriter.appendValue(convertToDisplayString(label));
    tagWriter.endTag();
  }
}
