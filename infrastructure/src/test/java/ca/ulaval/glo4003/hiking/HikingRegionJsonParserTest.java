package ca.ulaval.glo4003.hiking;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.*;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HikingRegionJsonParserTest {

  private static final Reader JSON_WITH_MULTIPLE_REGIONS =
      new StringReader(
          "[\n"
              + "    {\n"
              + "        \"id\": 1,\n"
              + "        \"key\": \"CHARLEVOIX\",\n"
              + "        \"fr_code\": \"Charlevoix\",\n"
              + "        \"en_code\": \"Charlevoix\"\n"
              + "    },\n"
              + "    {\n"
              + "        \"id\": 2,\n"
              + "        \"key\": \"GASPESIE\",\n"
              + "        \"fr_code\": \"Gaspésie\",\n"
              + "        \"en_code\": \"Gaspésie\"\n"
              + "    }\n"
              + "]");

  private static final Reader JSON_WITH_ONE_REGION =
      new StringReader(
          "[\n"
              + "    {\n"
              + "        \"id\": 1,\n"
              + "        \"key\": \"CHARLEVOIX\",\n"
              + "        \"fr_code\": \"Charlevoix\",\n"
              + "        \"en_code\": \"Charlevoix\"\n"
              + "    }\n"
              + "]");

  private static final Reader EMPTY_JSON_ARRAY = new StringReader("[]");
  private static final Reader INVALID_JSON = new StringReader("[");
  private HikingRegionJsonParser parser;

  @BeforeEach
  void setUp() {
    this.parser = new HikingRegionJsonParser();
  }

  @Test
  void givenJsonWithMultipleRegions_whenParsing_thenReturnAllPresentRegions() throws IOException {
    HikingRegion firstRegion = HikingRegion.CHARLEVOIX;
    HikingRegion secondRegion = HikingRegion.GASPESIE;

    Collection<HikingRegion> regions = this.parser.parse(JSON_WITH_MULTIPLE_REGIONS);

    assertTrue(regions.contains(firstRegion));
    assertTrue(regions.contains(secondRegion));
  }

  @Test
  void givenJsonWithOneRegion_whenParsing_thenReturnCorrectRegion() throws IOException {
    HikingRegion region = HikingRegion.CHARLEVOIX;

    Collection<HikingRegion> regions = this.parser.parse(JSON_WITH_ONE_REGION);

    assertTrue(regions.contains(region));
  }

  @Test
  void givenJsonWithNoRegions_whenParsing_thenReturnNoRegions() throws IOException {
    Collection<HikingRegion> regions = this.parser.parse(EMPTY_JSON_ARRAY);

    assertTrue(regions.isEmpty());
  }

  @Test
  void givenInvalidJson_whenParsing_thenThrowIoException() {
    assertThrows(IOException.class, () -> this.parser.parse(INVALID_JSON));
  }
}
