package ca.ulaval.glo4003.invoice;

import static ca.ulaval.glo4003.invoice.BillableItems.TRIP;

import ca.ulaval.glo4003.reservation.DurationCancellationPenalty;
import ca.ulaval.glo4003.tax.TaxStrategy;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.trip.Trip;
import java.util.Collection;

public class TripInvoiceFactory {
  private final TaxStrategy taxStrategy;

  public TripInvoiceFactory(TaxStrategy taxStrategy) {
    this.taxStrategy = taxStrategy;
  }

  public TripInvoice create(TravelerID userID, Trip trip, Collection<TravelerID> guests) {
    DurationCancellationPenalty durationCancellationPenalty = new DurationCancellationPenalty();
    TripInvoice tripInvoice =
        new TripInvoice(
            InvoiceID.generate(),
            userID,
            trip.getId(),
            this.taxStrategy,
            durationCancellationPenalty,
            guests);
    tripInvoice.saveItem(TRIP, trip.getCost());

    return tripInvoice;
  }
}
