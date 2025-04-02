package ca.ulaval.glo4003.register.dto;

import java.time.LocalDate;

public record UserDto(
    String firstname,
    String lastname,
    LocalDate birthDay,
    String gender,
    String idul,
    String email,
    String password) {}
