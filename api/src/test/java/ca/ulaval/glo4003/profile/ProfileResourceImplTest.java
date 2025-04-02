package ca.ulaval.glo4003.profile;

import static ca.ulaval.glo4003.reservation.ReservationStatus.CONFIRMED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.hiking.response.HikingResponse;
import ca.ulaval.glo4003.hosting.reservation.HostingReservation;
import ca.ulaval.glo4003.hosting.response.HostingResponse;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.invoice.response.InvoiceShortResponse;
import ca.ulaval.glo4003.profile.assembler.ProfileTripResponseAssembler;
import ca.ulaval.glo4003.profile.dto.ProfileTripDto;
import ca.ulaval.glo4003.profile.response.ProfileTripResponse;
import ca.ulaval.glo4003.reservation.ReservationStatus;
import ca.ulaval.glo4003.reservation.dto.CreatedReservationDto;
import ca.ulaval.glo4003.reservation.response.ReservationStatusResponse;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.TripID;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProfileResourceImplTest {
  private static final ReservationStatus ANY_RESERVATION_STATUS = CONFIRMED;
  private static final TravelerID ANY_AUTHENTICATED_USER_ID = new TravelerID("123");
  private ProfileTripsService profileTripsServiceMock;
  private ProfileTripResponseAssembler profileTripResponseAssemblerMock;
  private ProfileResourceImpl profileResource;

  @BeforeEach
  void setUp() {
    this.profileTripsServiceMock = mock(ProfileTripsService.class);
    this.profileTripResponseAssemblerMock = mock(ProfileTripResponseAssembler.class);
    this.profileResource =
        new ProfileResourceImpl(
            this.profileTripsServiceMock, this.profileTripResponseAssemblerMock);
  }

  @Test
  void whenSearchProfileTrips_thenSearchTripsOfAuthenticatedUser() {
    this.profileResource.searchProfileTrips(
        ANY_AUTHENTICATED_USER_ID, ANY_RESERVATION_STATUS.toString());

    verify(this.profileTripsServiceMock)
        .searchTrips(ANY_AUTHENTICATED_USER_ID, ANY_RESERVATION_STATUS);
  }

  @Test
  void givenAuthenticatedUser_whenSearchProfileTrips_thenReturnAssembledTripsResponse() {
    List<ProfileTripResponse> assembledTripsResponse = this.givenAuthenticatedUserSearchesTrips();

    Response returnedResponse =
        this.profileResource.searchProfileTrips(
            ANY_AUTHENTICATED_USER_ID, ANY_RESERVATION_STATUS.toString());

    assertTrue(returnedResponse.hasEntity());
    assertEquals(assembledTripsResponse, returnedResponse.getEntity());
  }

  @Test
  void givenAuthenticatedUser_whenSearchProfileTrips_thenReturnHttpOk() {
    this.givenAuthenticatedUserSearchesTrips();

    Response returnedResponse =
        this.profileResource.searchProfileTrips(
            ANY_AUTHENTICATED_USER_ID, ANY_RESERVATION_STATUS.toString());

    assertEquals(HttpStatus.OK_200, returnedResponse.getStatus());
  }

  private List<ProfileTripResponse> givenAuthenticatedUserSearchesTrips() {
    List<ProfileTripResponse> profileTripResponses = this.createProfileTripResponses();
    List<ProfileTripDto> trips = this.createProfileTripDtoList();
    when(this.profileTripsServiceMock.searchTrips(
            ANY_AUTHENTICATED_USER_ID, ANY_RESERVATION_STATUS))
        .thenReturn(trips);
    when(this.profileTripResponseAssemblerMock.assemble(trips)).thenReturn(profileTripResponses);
    return profileTripResponses;
  }

  private List<ProfileTripDto> createProfileTripDtoList() {
    List<ProfileTripDto> profileTripDtoList = new ArrayList<>();
    Hiking anyHiking = mock(Hiking.class);
    InvoiceDto anyInvoiceDto = mock(InvoiceDto.class);
    CreatedReservationDto anyCreatedReservationDto = mock(CreatedReservationDto.class);
    TripID anyTripID = TripID.generate();
    HostingReservation anyHostingReservation = mock(HostingReservation.class);
    ProfileTripDto anyProfileTripDto =
        new ProfileTripDto(
            anyHiking,
            anyTripID,
            LocalDate.now(),
            anyInvoiceDto,
            anyCreatedReservationDto,
            anyHostingReservation);
    profileTripDtoList.add(anyProfileTripDto);
    return profileTripDtoList;
  }

  private List<ProfileTripResponse> createProfileTripResponses() {
    List<ProfileTripResponse> profileTripResponses = new ArrayList<>();
    String anyId = "id";
    String anyDate = "1999-10-10";
    HikingResponse anyHikingResponse = mock(HikingResponse.class);
    InvoiceShortResponse invoiceShortResponse = mock(InvoiceShortResponse.class);
    ReservationStatusResponse reservationResponse = mock(ReservationStatusResponse.class);
    HostingResponse hostingResponse = mock(HostingResponse.class);
    ProfileTripResponse profileTripResponse =
        new ProfileTripResponse(
            anyId,
            anyHikingResponse,
            anyDate,
            invoiceShortResponse,
            reservationResponse,
            hostingResponse);
    profileTripResponses.add(profileTripResponse);
    return profileTripResponses;
  }
}
