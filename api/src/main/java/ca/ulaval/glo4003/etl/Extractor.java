package ca.ulaval.glo4003.etl;

import ca.ulaval.glo4003.etl.exception.ExtractException;
import java.util.Collection;

public interface Extractor<T> {
  Collection<T> extract() throws ExtractException;
}
