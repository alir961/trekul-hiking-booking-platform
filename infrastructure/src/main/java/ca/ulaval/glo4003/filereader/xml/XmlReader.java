package ca.ulaval.glo4003.filereader.xml;

import ca.ulaval.glo4003.filereader.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class XmlReader implements FileReader<XmlContent> {
  private final String elementTag;

  public XmlReader(String elementTag) {
    this.elementTag = elementTag;
  }

  @Override
  public XmlContent readFile(Reader reader) {
    BufferedReader bufferedReader = new BufferedReader(reader);
    StringBuilder xmlContent = new StringBuilder();
    String line;

    try {
      while ((line = bufferedReader.readLine()) != null) {
        xmlContent.append(line);
      }
      return new XmlContent(xmlContent.toString(), this.elementTag);
    } catch (IOException e) {
      throw new RuntimeException("Error reading XML input", e);
    }
  }
}
