package ca.ulaval.glo4003.transport.organizedbus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrganizedBusStopJsonParser implements OrganizedBusStopParser {
  private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

  @Override
  public Collection<TransportStop> parse(Reader content) throws IOException {
    List<TransportStop> stops = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();
    JsonNode arrayNode = mapper.readTree(content);

    for (final JsonNode objNode : arrayNode) {
      TransportStop busStop = this.parseBusStop(objNode);
      stops.add(busStop);
    }

    return stops;
  }

  @Override
  public String getFileExtension() {
    return "json";
  }

  private static TransportStop parseBusStop(JsonNode objNode) {
    String locationValue = objNode.get("location").asText();
    String departureValue = objNode.get("departure_time").asText();
    TransportStopLocation location = new TransportStopLocation(locationValue);
    LocalTime localTime = LocalTime.parse(departureValue, TIME_FORMAT);
    LocalDateTime departureTime = LocalDateTime.of(LocalDate.now(), localTime);
    return new TransportStop(location, departureTime);
  }
}
