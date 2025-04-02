package ca.ulaval.glo4003.config.exception;

import ca.ulaval.glo4003.exception.TrekULSystemException;

public class ConfigurationException extends TrekULSystemException {

  public ConfigurationException(String message) {
    super("CONFIG_ERROR", message);
  }
}
