package ca.ulaval.glo4003.hiking;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo4003.distance.Distance;
import java.io.StringReader;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HikingCsvParserTest {
  private static final HikingRegion ANY_HIKING_REGION = HikingRegion.GASPESIE;
  private static final HikingDifficulty ANY_HIKE_DIFFICULTY = HikingDifficulty.EASY;
  private static final String ANY_HIKE_DIFFICULTY_STRING = "Facile";
  private static final Distance ANY_HIKE_DISTANCE = new Distance("1.7");
  private static final String ANY_HIKE_DISTANCE_STRING = "1.7";
  private static final String HEADER_CSV_LINE =
      "Nom;Parc Associé;Distance (km);Durée (heures);Niveau de difficulté;"
          + "Description;Saison;;\n";
  private static final String SECOND_CSV_LINE =
      "La Chute-Sainte-Anne;Parc national de la Gaspésie;"
          + ANY_HIKE_DISTANCE_STRING
          + ";0.5;"
          + ANY_HIKE_DIFFICULTY_STRING
          + ";Description 1;Été;;\n";
  private static final String INVALID_CSV_LINE =
      "Parc national de la Gaspésie2.51.0;Facile;" + "Description 3;Hiver;;\n";
  private StringReader stringReader;
  private HikingCsvParser hikingsCsvParser;

  @BeforeEach
  void setUp() {
    this.hikingsCsvParser = new HikingCsvParser();
    this.stringReader = new StringReader(HEADER_CSV_LINE + SECOND_CSV_LINE);
  }

  @Test
  public void givenACsvFileWithAHike_whenParse_thenCollectionReturnedHasCorrectNumberOfElement() {
    Collection<Hiking> parsedText =
        this.hikingsCsvParser.parse(this.stringReader, ANY_HIKING_REGION);

    assertEquals(1, parsedText.size());
  }

  @Test
  public void givenACsvFileWithAHike_whenParse_thenHikeCharacteristicsAreCorrectlyParsed() {
    Collection<Hiking> parsedHikings =
        this.hikingsCsvParser.parse(this.stringReader, ANY_HIKING_REGION);
    Hiking hikeResult = parsedHikings.iterator().next();

    assertEquals(ANY_HIKING_REGION, hikeResult.getRegion());
    assertEquals(ANY_HIKE_DIFFICULTY, hikeResult.getDifficulty());
    assertEquals(ANY_HIKE_DISTANCE, hikeResult.getDistance());
  }

  @Test
  void givenCsvWithNoHikes_whenParsing_thenReturnNoHikes() {
    this.stringReader = new StringReader(HEADER_CSV_LINE);

    Collection<Hiking> parsedHikings =
        this.hikingsCsvParser.parse(this.stringReader, ANY_HIKING_REGION);

    assertFalse(parsedHikings.iterator().hasNext());
  }

  @Test
  void givenCsvWithInvalidData_whenParsing_thenReturnRuntimeError() {
    this.stringReader = new StringReader(HEADER_CSV_LINE + INVALID_CSV_LINE);

    assertThrows(
        RuntimeException.class,
        () -> this.hikingsCsvParser.parse(this.stringReader, ANY_HIKING_REGION));
  }
}
