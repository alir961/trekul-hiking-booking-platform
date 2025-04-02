package ca.ulaval.glo4003.hosting.reservation;

import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.hosting.HostingID;
import ca.ulaval.glo4003.hosting.HostingRepository;
import ca.ulaval.glo4003.hosting.exception.HostingNotFoundException;
import ca.ulaval.glo4003.hosting.reservation.assembler.CreatedHostingReservationDtoAssembler;
import ca.ulaval.glo4003.hosting.reservation.dto.CreatedHostingReservationDto;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripID;
import ca.ulaval.glo4003.trip.TripRepository;
import ca.ulaval.glo4003.trip.TripTravelerFetcher;
import ca.ulaval.glo4003.trip.exception.TripNotFoundException;

public class HostingReservationService {
  private final UserRepository userRepository;
  private final HostingRepository hostingRepository;
  private final TripRepository tripRepository;
  private final CreatedHostingReservationDtoAssembler hostingReservationDtoAssembler;
  private final TripTravelerFetcherFactory tripTravelerFetcherFactory;
  private final HostingNotificationSender hostingNotificationSender;

  public HostingReservationService(
      UserRepository userRepository,
      HostingRepository hostingRepository,
      TripRepository tripRepository,
      CreatedHostingReservationDtoAssembler hostingReservationDtoAssembler,
      HostingNotificationSender hostingNotificationSender,
      TripTravelerFetcherFactory tripTravelerFetcherFactory) {
    this.userRepository = userRepository;
    this.hostingRepository = hostingRepository;
    this.tripRepository = tripRepository;
    this.hostingReservationDtoAssembler = hostingReservationDtoAssembler;
    this.hostingNotificationSender = hostingNotificationSender;
    this.tripTravelerFetcherFactory = tripTravelerFetcherFactory;
  }

  public CreatedHostingReservationDto bookHosting(TripID tripID, HostingID hostingID) {
    HostingReservation hostingReservation = this.applyReservation(tripID, hostingID);

    return this.hostingReservationDtoAssembler.assemble(tripID, hostingReservation);
  }

  private HostingReservation applyReservation(TripID tripID, HostingID hostingID) {
    Hosting hosting =
        this.hostingRepository.get(hostingID).orElseThrow(HostingNotFoundException::new);
    Trip trip = this.tripRepository.get(tripID).orElseThrow(TripNotFoundException::new);

    TripTravelerFetcher fetcher = this.tripTravelerFetcherFactory.create(this.userRepository);

    this.userRepository.save(fetcher.fetchUpdatedUsers());

    this.hostingNotificationSender.notifyUsers(trip, hosting);

    HostingReservation hostingReservation = trip.reserveHosting(hosting, fetcher);
    this.tripRepository.save(trip);
    this.hostingRepository.save(hosting);

    return hostingReservation;
  }
}
