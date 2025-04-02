package ca.ulaval.glo4003.hiking;

import ca.ulaval.glo4003.distance.Distance;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class HikingCsvParser implements HikingParser {
  private static final int NUMBER_OF_EXPECTED_VALUES = 7;
  private static final String DELIMITER = ";";
  private static final int DISTANCE_INDEX = 2;
  private static final int DIFFICULTY_INDEX = 4;
  private static final int PARK_NAME_INDEX = 1;
  private static final int HIKING_NAME_INDEX = 0;
  private final Map<String, HikingDifficulty> difficultyMapper;

  public HikingCsvParser() {
    this.difficultyMapper = new HashMap<>();
    this.difficultyMapper.put("Facile", HikingDifficulty.EASY);
    this.difficultyMapper.put("Moyen", HikingDifficulty.INTERMEDIATE);
    this.difficultyMapper.put("Difficile", HikingDifficulty.HARD);
  }

  @Override
  public Collection<Hiking> parse(Reader reader, HikingRegion region) {
    List<Hiking> hikings = new ArrayList<>();
    List<List<String>> records = this.getRecords(new BufferedReader(reader));
    this.recordsToHikings(hikings, records, region);
    return hikings;
  }

  @Override
  public String getFileExtension() {
    return "csv";
  }

  private List<List<String>> getRecords(BufferedReader bufferedReader) {
    List<List<String>> records = new ArrayList<>();
    String line;
    try {
      while ((line = bufferedReader.readLine()) != null) {
        String[] values = line.split(DELIMITER);
        records.add(Arrays.asList(values));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return records;
  }

  private void recordsToHikings(
      List<Hiking> hikings, List<List<String>> records, HikingRegion region) {
    this.removeCsvHeader(records);

    records.forEach(
        record -> {
          if (record.size() >= NUMBER_OF_EXPECTED_VALUES) {
            HikingLocation location = this.getLocation(record, region);
            Distance distance = this.getDistance(record);
            HikingDifficulty difficulty = this.getDifficulty(record);

            Hiking hiking = new Hiking(location, difficulty, distance);
            hikings.add(hiking);
          } else {
            throw new RuntimeException();
          }
        });
  }

  private void removeCsvHeader(List<List<String>> records) {
    records.removeFirst();
  }

  private HikingLocation getLocation(List<String> record, HikingRegion region) {
    return new HikingLocation(region, record.get(PARK_NAME_INDEX));
  }

  private Distance getDistance(List<String> record) {
    return new Distance(record.get(DISTANCE_INDEX));
  }

  private HikingDifficulty getDifficulty(List<String> record) {
    return this.difficultyMapper.get(record.get(DIFFICULTY_INDEX));
  }
}
