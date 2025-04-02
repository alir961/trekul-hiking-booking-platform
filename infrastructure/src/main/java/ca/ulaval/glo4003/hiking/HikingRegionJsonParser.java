package ca.ulaval.glo4003.hiking;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

public class HikingRegionJsonParser implements HikingRegionParser {
  @Override
  public Collection<HikingRegion> parse(Reader content) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode arrayNode = mapper.readTree(content);
    Collection<HikingRegion> regions = new ArrayList<>();
    for (final JsonNode objNode : arrayNode) {
      String key = objNode.get("key").asText();
      regions.add(HikingRegion.valueOf(key));
    }

    return regions;
  }

  public String getFileExtension() {
    return "json";
  }
}
