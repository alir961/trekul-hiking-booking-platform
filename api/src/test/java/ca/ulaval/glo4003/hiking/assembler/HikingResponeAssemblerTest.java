package ca.ulaval.glo4003.hiking.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo4003.distance.Distance;
import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.hiking.HikingDifficulty;
import ca.ulaval.glo4003.hiking.HikingLocation;
import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.hiking.response.HikingResponse;
import org.junit.jupiter.api.Test;

public class HikingResponeAssemblerTest {
  private static final String ANY_HIKING_NAME = "NAME";
  private static final HikingRegion ANY_HIKING_REGION = HikingRegion.GASPESIE;
  private static final HikingLocation ANY_HIKING_LOCATION =
      new HikingLocation(ANY_HIKING_REGION, ANY_HIKING_NAME);
  private static final HikingDifficulty ANY_HIKING_DIFFICULTY = HikingDifficulty.INTERMEDIATE;
  private static final Distance ANY_HIKING_DISTANCE = new Distance("123");
  private static final Hiking ANY_HIKING =
      new Hiking(ANY_HIKING_LOCATION, ANY_HIKING_DIFFICULTY, ANY_HIKING_DISTANCE);
  private final HikingResponseAssembler hikingResponseAssembler = new HikingResponseAssembler();

  @Test
  void whenAssembling_thenHikingResponseAssembleCorrectly() {
    HikingResponse assembledHikingResponse = this.hikingResponseAssembler.assemble(ANY_HIKING);

    assertEquals(ANY_HIKING.getDifficulty().toString(), assembledHikingResponse.difficulty());
    assertEquals(ANY_HIKING.getDistance().toString(), assembledHikingResponse.distance());
    assertEquals(ANY_HIKING.getRegion().toString(), assembledHikingResponse.region());
  }
}
