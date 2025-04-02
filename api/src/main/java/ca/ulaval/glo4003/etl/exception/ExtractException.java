package ca.ulaval.glo4003.etl.exception;

import ca.ulaval.glo4003.exception.TrekULSystemException;

public class ExtractException extends TrekULSystemException {
  public ExtractException(Exception e) {
    super("ETL_EXTRACT_ERROR", e);
  }
}
