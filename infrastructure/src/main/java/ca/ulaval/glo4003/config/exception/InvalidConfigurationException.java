package ca.ulaval.glo4003.config.exception;

import java.io.IOException;

public class InvalidConfigurationException extends ConfigurationException {

  public InvalidConfigurationException(IOException e) {
    super(e.getMessage());
  }
}
