package ca.ulaval.glo4003.confirmationcode;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.confirmationcode.exception.ConfirmationCodeNotFoundException;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.User;
import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripID;
import ca.ulaval.glo4003.trip.TripRepository;
import ca.ulaval.glo4003.trip.exception.TripNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConfirmationCodeVerificationServiceTest {
  private static final TravelerID ANY_USER_ID = new TravelerID("ANY_USER_ID");
  private static final String ANY_CODE = "ANY_CODE";
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private Trip anyTripMock;
  private User anyUserMock;
  private TripRepository tripRepositoryMock;
  private UserRepository userRepositoryMock;
  private ConfirmationCodeVerificationService service;

  @BeforeEach
  void setUp() {
    this.tripRepositoryMock = mock(TripRepository.class);
    this.userRepositoryMock = mock(UserRepository.class);

    this.service =
        new ConfirmationCodeVerificationService(this.tripRepositoryMock, this.userRepositoryMock);
    this.anyTripMock = mock(Trip.class);
    this.anyUserMock = mock(User.class);
    when(this.tripRepositoryMock.get(ANY_TRIP_ID)).thenReturn(Optional.of(this.anyTripMock));
    when(this.anyTripMock.retrieveReservedUsers()).thenReturn(List.of(ANY_USER_ID));
    when(this.anyUserMock.hasConfirmationCode(ANY_CODE)).thenReturn(true);
    when(this.anyTripMock.getId()).thenReturn(ANY_TRIP_ID);
  }

  @Test
  void givenTripNotInRepository_whenVerifyingCode_thenThrowTripNotFoundException() {
    when(this.tripRepositoryMock.get(ANY_TRIP_ID)).thenReturn(Optional.empty());

    assertThrows(
        TripNotFoundException.class,
        () -> this.service.verifyConfirmationCode(ANY_TRIP_ID, ANY_CODE));
  }

  @Test
  void
      givenConfirmationCodeNotFound_whenVerifyingCode_thenThrowConfirmationCodeNotFoundException() {
    when(this.tripRepositoryMock.get(ANY_TRIP_ID)).thenReturn(Optional.of(this.anyTripMock));

    assertThrows(
        ConfirmationCodeNotFoundException.class,
        () -> this.service.verifyConfirmationCode(ANY_TRIP_ID, ANY_CODE));
  }

  @Test
  void givenUserHasConfirmationCode_whenVerifyingCode_thenValidateInvoiceForTrip() {
    List<TravelerID> listWithUser = List.of(ANY_USER_ID);
    when(this.tripRepositoryMock.get(ANY_TRIP_ID)).thenReturn(Optional.of(this.anyTripMock));
    when(this.userRepositoryMock.getBunch(listWithUser)).thenReturn(List.of(this.anyUserMock));
    when(this.anyTripMock.retrieveReservedUsers()).thenReturn(listWithUser);
    when(this.anyUserMock.hasConfirmationCode(ANY_CODE)).thenReturn(true);

    this.service.verifyConfirmationCode(ANY_TRIP_ID, ANY_CODE);

    verify(this.anyUserMock).validateTripInvoiceForTrip(ANY_TRIP_ID);
  }
}
