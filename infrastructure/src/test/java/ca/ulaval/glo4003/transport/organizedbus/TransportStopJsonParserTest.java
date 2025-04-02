package ca.ulaval.glo4003.transport.organizedbus;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransportStopJsonParserTest {
  private static final String STOP_NAME = "VACHON";
  private static final Reader JSON_WITH_STOPS =
      new StringReader(
          "[\n"
              + "    {\n"
              + "        \"location\": \"VACHON\",\n"
              + "        \"departure_time\": \"19:00\"\n"
              + "    }\n"
              + "]");
  private static final Reader EMPTY_JSON_ARRAY = new StringReader("[]");
  private static final Reader INVALID_JSON = new StringReader("[");
  private OrganizedBusStopJsonParser busDepartureJsonParser;

  @BeforeEach
  void setUp() {
    this.busDepartureJsonParser = new OrganizedBusStopJsonParser();
  }

  @Test
  void givenJsonWithStop_whenParsing_thenReturnCorrectStop() throws IOException {
    Collection<TransportStop> result = this.busDepartureJsonParser.parse(JSON_WITH_STOPS);

    assertEquals(1, result.size());
    assertTrue(result.stream().toList().getFirst().isNamed(STOP_NAME));
  }

  @Test
  void givenEmptyJson_whenParsing_thenReturnNoStop() throws IOException {
    Collection<TransportStop> result = this.busDepartureJsonParser.parse(EMPTY_JSON_ARRAY);

    assertTrue(result.stream().toList().isEmpty());
  }

  @Test
  void givenInvalidJson_whenParsing_thenThrowIoException() {
    assertThrows(IOException.class, () -> this.busDepartureJsonParser.parse(INVALID_JSON));
  }
}
