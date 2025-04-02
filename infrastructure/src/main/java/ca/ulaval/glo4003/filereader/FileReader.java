package ca.ulaval.glo4003.filereader;

import java.io.Reader;

public interface FileReader<T> {

  T readFile(Reader reader);
}
