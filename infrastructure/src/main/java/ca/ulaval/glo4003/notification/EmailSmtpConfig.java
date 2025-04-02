package ca.ulaval.glo4003.notification;

public record EmailSmtpConfig(String host, String port, String username, String password) {}
