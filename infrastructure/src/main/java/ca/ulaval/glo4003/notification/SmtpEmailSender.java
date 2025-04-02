package ca.ulaval.glo4003.notification;

import static java.lang.System.getProperties;
import static org.slf4j.LoggerFactory.getLogger;

import ca.ulaval.glo4003.traveler.Contactable;
import com.sun.mail.smtp.SMTPSendFailedException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;

public class SmtpEmailSender implements EmailNotificationSender {

  private static final Logger LOGGER = getLogger(SmtpEmailSender.class);
  private static final int FORBIDDEN_EMAIL_CODE = 421;

  private final EmailSmtpConfig smtp;
  private Session session;

  public SmtpEmailSender(EmailSmtpConfig smtp) {
    this.smtp = smtp;
  }

  public void sendEmail(Collection<? extends Contactable> contacts, Notification notification) {
    contacts.forEach(contact -> this.sendEmail(contact, notification));
  }

  public void sendEmail(Contactable contactable, Notification notification) {
    try {
      MimeMessage message = this.defaultMessage();

      message.setSubject(notification.title(), "UTF-8");
      message.setText(notification.body(), "UTF-8");
      message.setSentDate(new Date());
      message.setRecipients(
          Message.RecipientType.TO, InternetAddress.parse(contactable.getEmail(), false));

      LOGGER.info("Sending email to {}", contactable.getEmail());
      LOGGER.debug("Message will be sent: {}", notification.body());
      Transport.send(message);
      LOGGER.info("Email sent to {}", contactable.getEmail());
    } catch (SMTPSendFailedException exception) {
      if (exception.getReturnCode() == FORBIDDEN_EMAIL_CODE) {
        LOGGER.warn("Email address is not authorized on {}", this.smtp.host());
      } else {
        LOGGER.error("Failed to send email", exception);
      }
    } catch (MessagingException | UnsupportedEncodingException exception) {
      LOGGER.error(exception.getMessage());
    }
  }

  private MimeMessage defaultMessage() throws MessagingException, UnsupportedEncodingException {
    if (this.session == null) {
      this.initializeSession();
    }
    MimeMessage message = new MimeMessage(this.session);

    message.addHeader("Content-type", "text/HTML; charset=UTF-8");
    message.addHeader("format", "flowed");
    message.addHeader("Content-Transfer-Encoding", "8bit");
    message.setFrom(new InternetAddress(this.smtp.username(), "NoReply-JD"));
    message.setReplyTo(InternetAddress.parse(this.smtp.username(), false));
    return message;
  }

  private void initializeSession() {
    Properties props = getProperties();

    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", this.smtp.host());
    props.put("mail.smtp.port", this.smtp.port());
    props.put("mail.smtp.ssl.trust", this.smtp.host());
    props.put("mail.smtp.ssl.protocols", "TLSv1.2");
    props.put("mail.smtp.timeout", "10000");

    this.session = Session.getInstance(props, new SmtpAuthenticator(this.smtp));
  }
}
