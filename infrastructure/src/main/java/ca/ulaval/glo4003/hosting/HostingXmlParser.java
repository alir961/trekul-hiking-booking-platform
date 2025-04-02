package ca.ulaval.glo4003.hosting;

import static java.time.LocalDate.now;

import ca.ulaval.glo4003.filereader.xml.XmlContent;
import ca.ulaval.glo4003.filereader.xml.XmlReader;
import ca.ulaval.glo4003.filereader.xml.exception.XmlContentException;
import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.hiking.HikingRegionXmlExtractor;
import ca.ulaval.glo4003.hiking.exception.HikingRegionUnkown;
import ca.ulaval.glo4003.hosting.exception.HostingParsingException;
import ca.ulaval.glo4003.money.Money;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HostingXmlParser implements HostingParser {

  private static final String ID_TAG = "ETBL_ID";
  private static final String NAME_TAG = "ETBL_NOM_EN";
  private static final String HOSTINGS_TAG = "ETABLISSEMENTS";
  private static final String HOSTING_DETAILS_TAG = "ETABLISSEMENT";
  private static final String CHARACTERISTICS_TAG = "CARACTERISTIQUES";
  private static final String CHARACTERISTIC_DETAILS_TAG = "CARACTERISTIQUE";
  private static final String CHARACTERISTIC_NAME_TAG = "CARACT_NOM_EN";
  private static final String CHARACTERISTIC_ATTRIBUTE_TAG = "CARACT_ATTRIBUT";
  private static final String ATTRIBUTE_VALUE_TAG = "CARACT_ATTRB_VAL";
  private static final int CURRENT_YEAR = now().getYear();
  private final XmlReader xmlReader;
  private final HikingRegionXmlExtractor hikingRegionXmlExtractor;

  public HostingXmlParser() {
    this.xmlReader = new XmlReader(HOSTINGS_TAG);
    this.hikingRegionXmlExtractor = new HikingRegionXmlExtractor();
  }

  @Override
  public String getFileExtension() {
    return "xml";
  }

  @Override
  public List<Hosting> parse(Reader reader) {
    XmlContent establishments = this.xmlReader.readFile(reader);
    List<Hosting> hosts = new ArrayList<>();

    for (XmlContent establishment : establishments.extractSections(HOSTING_DETAILS_TAG)) {
      try {
        Hosting host = this.createHostFromParsing(establishment);
        hosts.add(host);
      } catch (HostingParsingException | XmlContentException | HikingRegionUnkown e) {
        continue;
      }
    }
    return hosts;
  }

  private Hosting createHostFromParsing(XmlContent establishment) {
    HostingID id = new HostingID(this.parseId(establishment));
    String name = this.parseName(establishment);
    HikingRegion region = this.hikingRegionXmlExtractor.extract(establishment);
    Map<Money, Money> priceRange = this.extractPriceRange(establishment);
    int maxCapacity = this.extractMaxCapacity(establishment);
    Map<LocalDate, LocalDate> availablePeriod = this.extractExploitationPeriod(establishment);

    return new Hosting(id, name, region, priceRange, maxCapacity, availablePeriod);
  }

  private Integer parseId(XmlContent establishment) {
    return establishment.extractField(ID_TAG).asInteger();
  }

  private String parseName(XmlContent establishment) {
    XmlContent nameField = establishment.extractField(NAME_TAG);
    String name = nameField.asString();
    return name.substring("<![CDATA[".length(), name.length() - "]]>".length());
  }

  private Map<Money, Money> extractPriceRange(XmlContent establishment) {
    XmlContent ratesSection = this.extractSectionByCharacteristic(establishment, "Rates");
    List<String> priceValues = this.extractPriceValues(ratesSection);
    Money lowerPrice = new Money(priceValues.get(0));
    Money higherPrice = new Money(priceValues.get(1));

    return Map.of(lowerPrice, higherPrice);
  }

  private List<String> extractPriceValues(XmlContent ratesSection) throws HostingParsingException {
    List<XmlContent> attributes = ratesSection.extractSections(CHARACTERISTIC_ATTRIBUTE_TAG);
    List<String> priceValues =
        attributes.stream()
            .flatMap(a -> a.extractSections(ATTRIBUTE_VALUE_TAG).stream())
            .map(XmlContent::asString)
            .toList();

    if (priceValues.size() < 2) {
      throw new HostingParsingException("Rates not found or incomplete in the establishment.");
    }
    return priceValues;
  }

  private int extractMaxCapacity(XmlContent establishment) {
    XmlContent unitsSection = this.extractSectionByCharacteristic(establishment, "Units");
    List<XmlContent> values = unitsSection.extractSections(ATTRIBUTE_VALUE_TAG);

    return values.stream().mapToInt(XmlContent::asInteger).sum();
  }

  private Map<LocalDate, LocalDate> extractExploitationPeriod(XmlContent establishment) {
    String currentYear = "-" + CURRENT_YEAR;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    LocalDate startDate =
        establishment.extractField("PER_EXPL_JJMM_DEBUT").asDate(dateFormatter, currentYear);
    LocalDate endDate =
        establishment.extractField("PER_EXPL_JJMM_FIN").asDate(dateFormatter, currentYear);

    return Map.of(startDate, endDate);
  }

  private XmlContent extractSectionByCharacteristic(XmlContent content, String characteristicName)
      throws HostingParsingException {
    return content
        .extractField(CHARACTERISTICS_TAG)
        .extractFirstSectionContaining(
            CHARACTERISTIC_DETAILS_TAG, CHARACTERISTIC_NAME_TAG, characteristicName);
  }
}
