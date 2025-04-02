package ca.ulaval.glo4003.hosting;

import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.hosting.assembler.HostingDtoAssembler;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripID;
import ca.ulaval.glo4003.trip.TripRepository;
import ca.ulaval.glo4003.trip.exception.TripNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HostingServiceTest {
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private static final HikingRegion ANY_REGION = HikingRegion.GASPESIE;
  private HostingRepository hostingRepository;
  private TripRepository tripRepository;
  private Trip existingTripMock;
  private Hosting hostingMock;
  private HostingDtoAssembler hostingDtoAssembler;
  private HostingSearchFilterer hostingSearchFilterer;
  private HostingService hostingService;

  @BeforeEach
  void setUp() {
    this.hostingRepository = mock(HostingRepository.class);
    this.tripRepository = mock(TripRepository.class);
    this.existingTripMock = mock(Trip.class);
    this.hostingMock = mock(Hosting.class);
    this.hostingDtoAssembler = mock(HostingDtoAssembler.class);
    this.hostingSearchFilterer = mock(HostingSearchFilterer.class);
    this.hostingService =
        new HostingService(
            this.hostingRepository,
            this.tripRepository,
            this.hostingSearchFilterer,
            this.hostingDtoAssembler);
  }

  @Test
  void givenAnAbsentTripID_whenSearchingCompatibleHosts_thenTripNotFoundException() {
    when(this.tripRepository.get(ANY_TRIP_ID)).thenReturn(Optional.empty());

    Assertions.assertThrows(
        TripNotFoundException.class, () -> this.hostingService.searchCompatibleHosts(ANY_TRIP_ID));
  }

  @Test
  void givenAnExistingTripID_whenSearchingCompatibleHosts_thenGetAllHosts() {
    when(this.tripRepository.get(ANY_TRIP_ID))
        .thenReturn(Optional.ofNullable(this.existingTripMock));
    when(this.existingTripMock.getRegion()).thenReturn(ANY_REGION);

    this.hostingService.searchCompatibleHosts(ANY_TRIP_ID);

    verify(this.hostingRepository).getAll();
  }

  @Test
  void givenAnExistingTripID_whenSearchingCompatibleHosts_thenFiltererFiltersCompatibleHosts() {
    when(this.tripRepository.get(ANY_TRIP_ID))
        .thenReturn(Optional.ofNullable(this.existingTripMock));
    when(this.existingTripMock.getRegion()).thenReturn(ANY_REGION);
    when(this.hostingRepository.getAll()).thenReturn(List.of(this.hostingMock));

    this.hostingService.searchCompatibleHosts(ANY_TRIP_ID);

    verify(this.hostingSearchFilterer)
        .filterSearch(List.of(this.hostingMock), this.existingTripMock);
  }
}
