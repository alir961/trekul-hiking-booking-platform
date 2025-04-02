package ca.ulaval.glo4003.filereader.xml;

import ca.ulaval.glo4003.filereader.xml.exception.XmlContentException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class XmlContent {
  private final String content;
  private final String sectionName;

  public XmlContent(String content, String sectionName) {
    this.content = content;
    this.sectionName = sectionName;
  }

  public List<XmlContent> extractSections(String fieldName) {
    List<XmlContent> sections = new ArrayList<>();
    String tagStart = "<" + fieldName + ">";
    String tagEnd = "</" + fieldName + ">";
    int startIndex = 0;

    while ((startIndex = this.content.indexOf(tagStart, startIndex)) != -1) {
      int endIndex = this.content.indexOf(tagEnd, startIndex);
      if (endIndex == -1) {
        break;
      }
      String section = this.content.substring(startIndex + tagStart.length(), endIndex);
      sections.add(new XmlContent(section, tagStart));
      startIndex = endIndex + 1;
    }

    return sections;
  }

  public XmlContent extractField(String fieldName) {
    String tagStart = "<" + fieldName + ">";
    String tagEnd = "</" + fieldName + ">";
    int startIndex = this.indexOfOrThrow(tagStart) + tagStart.length();
    int endIndex = this.indexOfOrThrow(tagEnd, startIndex);

    return new XmlContent(this.content.substring(startIndex, endIndex).trim(), fieldName);
  }

  public XmlContent extractFirstSectionContaining(
      String fieldName, String subTag, String tagValue) {
    List<XmlContent> items = this.extractSections(fieldName);
    String shouldContain = String.format("<%s>%s</%s>", subTag, tagValue, subTag);

    for (XmlContent item : items) {
      if (item.contains(shouldContain)) {
        return item;
      }
    }
    throw new XmlContentException(
        String.format(
            "Sub section '%s' with value '%s' not found in '%s'.", subTag, tagValue, fieldName));
  }

  public int asInteger() {
    return Integer.parseInt(this.content);
  }

  public String asString() {
    return this.content;
  }

  public LocalDate asDate(DateTimeFormatter dateFormatter, String year) {
    return LocalDate.parse(this.content + year, dateFormatter);
  }

  private boolean contains(String subContent) {
    return this.content.contains(subContent);
  }

  private int indexOfOrThrow(String toFind) {
    int index = this.content.indexOf(toFind);
    this.validateIndex(index, toFind);
    return index;
  }

  private int indexOfOrThrow(String toFind, int startIndex) {
    int index = this.content.indexOf(toFind, startIndex);
    this.validateIndex(index, toFind);
    return index;
  }

  private void validateIndex(int index, String toFind) throws XmlContentException {
    if (index == -1) {
      throw new XmlContentException(
          String.format("Tag '%s' not found in section '%s'.", toFind, this.sectionName));
    }
  }
}
