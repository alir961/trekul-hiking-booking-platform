package ca.ulaval.glo4003.user.response;

import java.time.LocalDate;

public record ProfileResponse(
    String name,
    LocalDate birthDay,
    String gender,
    String idul,
    String email,
    int age,
    String credits) {}
