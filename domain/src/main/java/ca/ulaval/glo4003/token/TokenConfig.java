package ca.ulaval.glo4003.token;

import java.time.Duration;

public record TokenConfig(Duration loginTokenDuration, Duration guest2FATokenDuration) {}
