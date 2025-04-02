package ca.ulaval.glo4003.filereader.xml.exception;

import ca.ulaval.glo4003.exception.TrekULSystemException;

public class XmlContentException extends TrekULSystemException {
  public XmlContentException(String message) {
    super("XML_PARSING_ERROR", message);
  }
}
