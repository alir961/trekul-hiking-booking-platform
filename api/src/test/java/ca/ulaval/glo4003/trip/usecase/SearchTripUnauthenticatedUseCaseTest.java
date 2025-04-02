package ca.ulaval.glo4003.trip.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.guest.GuestTripService;
import ca.ulaval.glo4003.profile.assembler.ProfileTripResponseAssembler;
import ca.ulaval.glo4003.profile.dto.ProfileTripDto;
import ca.ulaval.glo4003.profile.response.ProfileTripResponse;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchTripUnauthenticatedUseCaseTest {
  private static final TripID TRIP_ID = TripID.generate();
  private static final TravelerID TRAVELER_ID = new TravelerID("traveler-id");
  private static final ProfileTripDto PROFILE_TRIP_DTO = mock(ProfileTripDto.class);
  private static final ProfileTripResponse PROFILE_TRIP_RESPONSE = mock(ProfileTripResponse.class);

  private GuestTripService guestTripService;
  private ProfileTripResponseAssembler responseAssembler;
  private SearchTripUnauthenticatedUseCase useCase;

  @BeforeEach
  void setUp() {
    this.guestTripService = mock(GuestTripService.class);
    this.responseAssembler = mock(ProfileTripResponseAssembler.class);

    this.useCase =
        new SearchTripUnauthenticatedUseCase(this.guestTripService, this.responseAssembler);
  }

  @Test
  void whenSearchTripUnauthenticated_thenGuestTripServiceIsCalled() {
    when(this.guestTripService.searchTripByIdAndTravelerId(TRIP_ID, TRAVELER_ID))
        .thenReturn(PROFILE_TRIP_DTO);

    this.useCase.searchTripUnauthenticated(TRIP_ID, TRAVELER_ID);

    verify(this.guestTripService).searchTripByIdAndTravelerId(TRIP_ID, TRAVELER_ID);
  }

  @Test
  void whenSearchTripUnauthenticated_thenResponseAssemblerIsCalled() {
    when(this.guestTripService.searchTripByIdAndTravelerId(TRIP_ID, TRAVELER_ID))
        .thenReturn(PROFILE_TRIP_DTO);
    when(this.responseAssembler.assemble(PROFILE_TRIP_DTO)).thenReturn(PROFILE_TRIP_RESPONSE);

    this.useCase.searchTripUnauthenticated(TRIP_ID, TRAVELER_ID);

    verify(this.responseAssembler).assemble(PROFILE_TRIP_DTO);
  }

  @Test
  void whenSearchTripUnauthenticated_thenReturnsResponse() {
    when(this.guestTripService.searchTripByIdAndTravelerId(TRIP_ID, TRAVELER_ID))
        .thenReturn(PROFILE_TRIP_DTO);
    when(this.responseAssembler.assemble(PROFILE_TRIP_DTO)).thenReturn(PROFILE_TRIP_RESPONSE);

    ProfileTripResponse actualResponse =
        this.useCase.searchTripUnauthenticated(TRIP_ID, TRAVELER_ID);

    assertEquals(PROFILE_TRIP_RESPONSE, actualResponse);
  }
}
