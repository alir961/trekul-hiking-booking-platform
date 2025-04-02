package ca.ulaval.glo4003.payment.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record PaymentRequest(
    @JsonProperty(required = true) String cardNumber,
    @JsonProperty(required = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @Schema(pattern = "MMYY")
        String expirationDate,
    @JsonProperty(required = true) Integer ccv,
    @JsonProperty(required = true) String cardHolderName,
    @JsonProperty(required = true) Double payment) {}
