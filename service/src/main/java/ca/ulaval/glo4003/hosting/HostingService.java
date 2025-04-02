package ca.ulaval.glo4003.hosting;

import ca.ulaval.glo4003.hosting.assembler.HostingDtoAssembler;
import ca.ulaval.glo4003.hosting.dto.HostingDto;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripID;
import ca.ulaval.glo4003.trip.TripRepository;
import ca.ulaval.glo4003.trip.exception.TripNotFoundException;
import java.util.List;

public class HostingService {
  private final HostingRepository hostingRepository;
  private final TripRepository tripRepository;
  private final HostingSearchFilterer hostingSearchFilterer;
  private final HostingDtoAssembler hostingDtoAssembler;

  public HostingService(
      HostingRepository hostingRepository,
      TripRepository tripRepository,
      HostingSearchFilterer hostingSearchFilterer,
      HostingDtoAssembler hostingDtoAssembler) {
    this.hostingRepository = hostingRepository;
    this.tripRepository = tripRepository;
    this.hostingSearchFilterer = hostingSearchFilterer;
    this.hostingDtoAssembler = hostingDtoAssembler;
  }

  public List<HostingDto> searchCompatibleHosts(TripID tripID) {
    Trip trip = this.tripRepository.get(tripID).orElseThrow(TripNotFoundException::new);
    List<Hosting> hostingList = this.hostingRepository.getAll();
    List<Hosting> filtered = this.hostingSearchFilterer.filterSearch(hostingList, trip);
    return this.hostingDtoAssembler.assemble(filtered);
  }
}
