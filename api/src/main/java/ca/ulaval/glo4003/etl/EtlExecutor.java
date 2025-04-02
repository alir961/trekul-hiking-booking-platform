package ca.ulaval.glo4003.etl;

import ca.ulaval.glo4003.etl.exception.ExtractException;

public interface EtlExecutor {
  void execute() throws ExtractException;
}
