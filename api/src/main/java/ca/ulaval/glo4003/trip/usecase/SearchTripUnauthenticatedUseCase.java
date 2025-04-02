package ca.ulaval.glo4003.trip.usecase;

import ca.ulaval.glo4003.guest.GuestTripService;
import ca.ulaval.glo4003.profile.assembler.ProfileTripResponseAssembler;
import ca.ulaval.glo4003.profile.dto.ProfileTripDto;
import ca.ulaval.glo4003.profile.response.ProfileTripResponse;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.TripID;
import jakarta.inject.Inject;

public class SearchTripUnauthenticatedUseCase {
  private final GuestTripService guestTripService;
  private final ProfileTripResponseAssembler profileTripResponseAssembler;

  @Inject
  public SearchTripUnauthenticatedUseCase(
      GuestTripService guestTripService,
      ProfileTripResponseAssembler profileTripResponseAssembler) {
    this.guestTripService = guestTripService;
    this.profileTripResponseAssembler = profileTripResponseAssembler;
  }

  public ProfileTripResponse searchTripUnauthenticated(TripID tripId, TravelerID travelerId) {
    ProfileTripDto unauthenticatedTrip =
        this.guestTripService.searchTripByIdAndTravelerId(tripId, travelerId);

    return this.profileTripResponseAssembler.assemble(unauthenticatedTrip);
  }
}
