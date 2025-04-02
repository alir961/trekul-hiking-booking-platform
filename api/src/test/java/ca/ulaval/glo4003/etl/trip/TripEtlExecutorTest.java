package ca.ulaval.glo4003.etl.trip;

import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.etl.Extractor;
import ca.ulaval.glo4003.etl.exception.ExtractException;
import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.transport.organizedbus.TransportStop;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TripEtlExecutorTest {
  private static final Collection<Hiking> ANY_EXTRACTED_HIKING = List.of(mock(Hiking.class));
  private static final Collection<TransportStop> ANY_EXTRACTED_STOPS =
      List.of(mock(TransportStop.class));
  private TripLoader tripLoaderMock;
  private TripEtlExecutor tripEtlExecutor;

  @BeforeEach
  void setUp() throws ExtractException {
    Extractor<Hiking> hikingExtractorMock = mock(Extractor.class);
    Extractor<TransportStop> busStopExtractorMock = mock(Extractor.class);
    this.tripLoaderMock = mock(TripLoader.class);

    when(hikingExtractorMock.extract()).thenReturn(ANY_EXTRACTED_HIKING);
    when(busStopExtractorMock.extract()).thenReturn(ANY_EXTRACTED_STOPS);

    this.tripEtlExecutor =
        new TripEtlExecutor(hikingExtractorMock, busStopExtractorMock, this.tripLoaderMock);
  }

  @Test
  void whenExecuting_thenLoadTripFromExtractors() throws ExtractException {
    this.tripEtlExecutor.execute();

    verify(this.tripLoaderMock).load(ANY_EXTRACTED_HIKING, ANY_EXTRACTED_STOPS);
  }
}
