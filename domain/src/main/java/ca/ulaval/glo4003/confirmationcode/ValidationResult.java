package ca.ulaval.glo4003.confirmationcode;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ValidationResult(boolean canBoard, String message, int validFor) {}
