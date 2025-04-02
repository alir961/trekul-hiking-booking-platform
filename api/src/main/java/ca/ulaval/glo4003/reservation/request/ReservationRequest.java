package ca.ulaval.glo4003.reservation.request;

import ca.ulaval.glo4003.guest.request.GuestRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ReservationRequest(
    @JsonProperty(required = true) String transportType,
    String stop,
    @JsonProperty(required = true) List<GuestRequest> guests) {}
