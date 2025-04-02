package ca.ulaval.glo4003.notification;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SmtpAuthenticator extends Authenticator {
  private final EmailSmtpConfig emailSmtpConfig;

  public SmtpAuthenticator(EmailSmtpConfig emailSmtpConfig) {
    this.emailSmtpConfig = emailSmtpConfig;
  }

  @Override
  protected PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(
        this.emailSmtpConfig.username(), this.emailSmtpConfig.password());
  }
}
