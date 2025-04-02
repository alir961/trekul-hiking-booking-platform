package ca.ulaval.glo4003.user.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record CreateUserRequest(
    @JsonProperty(required = true) String firstname,
    @JsonProperty(required = true) String lastname,
    @JsonProperty(required = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate birthDay,
    @JsonProperty(required = true)
        @Schema(
            description =
                "Gender of the user in the following format: GENDER_MAN, GENDER_WOMAN, GENDER_NON_BINARY, GENDER_OTHER, GENDER_UNSPECIFIED",
            example = "GENDER_MAN")
        String gender,
    @JsonProperty(required = true) String idul,
    @JsonProperty(required = true) String email,
    @JsonProperty(required = true) String password) {}
