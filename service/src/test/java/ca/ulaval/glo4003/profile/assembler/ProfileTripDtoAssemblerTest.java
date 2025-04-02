package ca.ulaval.glo4003.profile.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.distance.Distance;
import ca.ulaval.glo4003.hiking.Hiking;
import ca.ulaval.glo4003.hiking.HikingDifficulty;
import ca.ulaval.glo4003.hiking.HikingLocation;
import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.invoice.TripInvoice;
import ca.ulaval.glo4003.invoice.assembler.InvoiceDtoAssembler;
import ca.ulaval.glo4003.invoice.dto.InvoiceDto;
import ca.ulaval.glo4003.profile.dto.ProfileTripDto;
import ca.ulaval.glo4003.reservation.Reservation;
import ca.ulaval.glo4003.reservation.assembler.CreatedReservationDtoAssembler;
import ca.ulaval.glo4003.reservation.dto.CreatedReservationDto;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.Trip;
import ca.ulaval.glo4003.trip.TripID;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProfileTripDtoAssemblerTest {
  private static final TravelerID ANY_TRAVELER_ID = new TravelerID("00013");
  private static final TripID ANY_TRIP_ID = TripID.generate();
  private static final LocalDate ANY_DATE = LocalDate.now();
  private static final HikingRegion ANY_REGION = HikingRegion.GASPESIE;
  private static final HikingDifficulty ANY_HIKING_DIFFICULTY = HikingDifficulty.HARD;
  private static final Distance ANY_HIKING_DISTANCE = new Distance("103");
  private static final HikingLocation ANY_HIKING_LOCATION =
      new HikingLocation(ANY_REGION, "Any location name");
  private static final Hiking ANY_HIKING =
      new Hiking(ANY_HIKING_LOCATION, ANY_HIKING_DIFFICULTY, ANY_HIKING_DISTANCE);
  private static final InvoiceDto INVOICE_DTO_DUMMY = mock(InvoiceDto.class);
  private static final Reservation RESERVATION_DUMMY = mock(Reservation.class);
  private static final CreatedReservationDto CREATED_RESERVATION_DTO_DUMMY =
      mock(CreatedReservationDto.class);
  private TripInvoice tripInvoiceMock;
  private Trip tripStub;
  private ProfileTripDtoAssembler assembler;

  @BeforeEach
  void setUp() {
    this.tripInvoiceMock = mock(TripInvoice.class);

    InvoiceDtoAssembler invoiceDtoAssemblerStub = mock(InvoiceDtoAssembler.class);
    when(invoiceDtoAssemblerStub.assemble(this.tripInvoiceMock, ANY_TRAVELER_ID))
        .thenReturn(INVOICE_DTO_DUMMY);

    when(RESERVATION_DUMMY.getInvoice()).thenReturn(this.tripInvoiceMock);
    CreatedReservationDtoAssembler createdReservationDtoAssembler =
        mock(CreatedReservationDtoAssembler.class);
    when(createdReservationDtoAssembler.assemble(RESERVATION_DUMMY))
        .thenReturn(CREATED_RESERVATION_DTO_DUMMY);

    this.tripStub = mock(Trip.class);
    when(this.tripStub.getHiking()).thenReturn(ANY_HIKING);
    when(this.tripStub.getId()).thenReturn(ANY_TRIP_ID);
    when(this.tripStub.getDate()).thenReturn(ANY_DATE);
    this.assembler =
        new ProfileTripDtoAssembler(invoiceDtoAssemblerStub, createdReservationDtoAssembler);
  }

  @Test
  void givenTripList_whenAssembling_thenSearchTripResponseAssembleCorrectly() {
    ProfileTripDto result =
        this.assembler.assemble(this.tripStub, RESERVATION_DUMMY, ANY_TRAVELER_ID);

    assertEquals(ANY_HIKING, result.hiking());
    assertSame(INVOICE_DTO_DUMMY, result.invoice());
    assertEquals(ANY_DATE, result.date());
    assertEquals(ANY_TRIP_ID, result.tripID());
    assertSame(CREATED_RESERVATION_DTO_DUMMY, result.reservation());
  }
}
