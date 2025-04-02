package ca.ulaval.glo4003.config.exception;

public class ConfigurationNotFound extends ConfigurationException {

  public ConfigurationNotFound(String filepath) {
    super(String.format("Config file not found with path '%s'", filepath));
  }
}
