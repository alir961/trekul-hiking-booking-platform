package ca.ulaval.glo4003.guest.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GuestRequest(
    @JsonProperty(required = true) String firstname,
    @JsonProperty(required = true) String lastname,
    @JsonProperty(required = true) String idul,
    @JsonProperty(required = true) String email) {}
