package ca.ulaval.glo4003.hiking.assembler;

import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.hiking.response.HikingResponse;

public class HikingResponseAssembler {
  public HikingResponse assemble(Hiking hiking) {
    return new HikingResponse(
        hiking.getRegion().toString(),
        hiking.getDifficulty().toString(),
        hiking.getDistance().toString());
  }
}
