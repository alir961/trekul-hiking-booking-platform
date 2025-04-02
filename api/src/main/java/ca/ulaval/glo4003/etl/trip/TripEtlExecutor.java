package ca.ulaval.glo4003.etl.trip;

import ca.ulaval.glo4003.etl.EtlExecutor;
import ca.ulaval.glo4003.etl.Extractor;
import ca.ulaval.glo4003.etl.exception.ExtractException;
import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.transport.organizedbus.TransportStop;

public class TripEtlExecutor implements EtlExecutor {
  private final Extractor<Hiking> hikingExtractor;
  private final Extractor<TransportStop> busStopExtractor;
  private final TripLoader tripLoader;

  public TripEtlExecutor(
      Extractor<Hiking> hikingExtractor,
      Extractor<TransportStop> busStopExtractor,
      TripLoader tripLoader) {
    this.hikingExtractor = hikingExtractor;
    this.busStopExtractor = busStopExtractor;
    this.tripLoader = tripLoader;
  }

  public void execute() throws ExtractException {
    this.tripLoader.load(this.hikingExtractor.extract(), this.busStopExtractor.extract());
  }
}
