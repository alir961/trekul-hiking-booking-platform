package ca.ulaval.glo4003.group.reponse;

import ca.ulaval.glo4003.guest.response.GuestResponse;
import java.util.List;

public record GroupResponse(List<GuestResponse> guests) {}
