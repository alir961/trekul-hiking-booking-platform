package ca.ulaval.glo4003.notification.types;

import static java.lang.String.format;

import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.notification.Notification;
import ca.ulaval.glo4003.trip.Trip;

public class HostingConfirmedNotificationFactory {
  private static final String HOSTING_CONFIRMED_TITLE = "Hébergement du voyage";

  public Notification build(Trip trip, Hosting hosting) {
    String body =
        format(
            "Voyage #%s%nDate: %s%nRégion: %s%nHébergement: %s%n",
            trip.getId(), trip.getDate(), trip.getRegion(), hosting.getName());
    return new Notification(HOSTING_CONFIRMED_TITLE, body);
  }
}
