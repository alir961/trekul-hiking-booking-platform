package ca.ulaval.glo4003.trip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.reservation.request.ReservationRequest;
import ca.ulaval.glo4003.reservation.response.ReservationResponse;
import ca.ulaval.glo4003.reservation.usecase.ReservationCreationUseCase;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.usecase.BlockTripUseCase;
import ca.ulaval.glo4003.trip.usecase.VerifyTripUseCase;
import jakarta.ws.rs.core.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TripAuthenticatedResourceImplTest {
  private static final ReservationRequest ANY_RESERVATION_REQUEST = mock(ReservationRequest.class);
  private static final ReservationResponse ANY_RESERVATION_RESPONSE =
      mock(ReservationResponse.class);
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private static final TravelerID ANY_TRAVELER_ID = new TravelerID("jjftgj");
  private VerifyTripUseCase verifyTripUseCaseMock;
  private BlockTripUseCase blockTripUseCaseMock;
  private ReservationCreationUseCase reservationCreationUseCaseMock;
  private TripAuthenticatedResourceImpl tripAuthenticatedResourceImpl;

  @BeforeEach
  void setUp() {
    this.blockTripUseCaseMock = mock(BlockTripUseCase.class);
    this.reservationCreationUseCaseMock = mock(ReservationCreationUseCase.class);
    this.verifyTripUseCaseMock = mock(VerifyTripUseCase.class);
    this.tripAuthenticatedResourceImpl =
        new TripAuthenticatedResourceImpl(
            this.blockTripUseCaseMock,
            this.reservationCreationUseCaseMock,
            this.verifyTripUseCaseMock);
  }

  @Test
  void givenAuthenticatedUser_whenBlockingTrip_thenReturnHttpOk() {
    Response returnedResponse = this.tripAuthenticatedResourceImpl.blockTrip(ANY_TRIP_ID);

    assertEquals(HttpStatus.NO_CONTENT_204, returnedResponse.getStatus());
  }

  @Test
  void givenAuthenticatedUser_whenCreateReservation_thenReturnAssembledReservationResponse() {
    when(this.reservationCreationUseCaseMock.createReservation(
            ANY_RESERVATION_REQUEST, ANY_TRAVELER_ID, ANY_TRIP_ID))
        .thenReturn(ANY_RESERVATION_RESPONSE);

    Response returnedResponse =
        this.tripAuthenticatedResourceImpl.createReservation(
            ANY_TRIP_ID, ANY_TRAVELER_ID, ANY_RESERVATION_REQUEST);

    assertTrue(returnedResponse.hasEntity());
    assertEquals(ANY_RESERVATION_RESPONSE, returnedResponse.getEntity());
  }

  @Test
  void givenAuthenticatedUser_whenCreateReservation_thenReturnHttpCreated() {
    when(this.reservationCreationUseCaseMock.createReservation(
            ANY_RESERVATION_REQUEST, ANY_TRAVELER_ID, ANY_TRIP_ID))
        .thenReturn(ANY_RESERVATION_RESPONSE);

    Response returnedResponse =
        this.tripAuthenticatedResourceImpl.createReservation(
            ANY_TRIP_ID, ANY_TRAVELER_ID, ANY_RESERVATION_REQUEST);

    assertEquals(HttpStatus.CREATED_201, returnedResponse.getStatus());
  }
}
