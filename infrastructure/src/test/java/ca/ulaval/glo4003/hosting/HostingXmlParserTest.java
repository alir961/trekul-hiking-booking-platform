package ca.ulaval.glo4003.hosting;

import static ca.ulaval.glo4003.hiking.HikingRegion.SAGLAC;
import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo4003.money.Money;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HostingXmlParserTest {
  private static final HostingID AN_EXPECTED_ID = new HostingID(123);
  private static final String AN_EXPECTED_NAME = "AUBERGE DU LAC-COMMANDANT";
  private static final int AN_EXPECTED_CAPACITY = 10;
  private static final int AN_EXPECTED_MONTH_OF_PERIOD_START = 6;
  private static final int AN_EXPECTED_MONTH_OF_PERIOD_END = 9;

  private HostingXmlParser parser;

  @BeforeEach
  public void setUp() {
    this.parser = new HostingXmlParser();
  }

  @Test
  public void givenValidXML_whenParseHost_thenReturnsList() {
    String validXml =
        """
        <ETABLISSEMENTS>
            <ETABLISSEMENT>
                <ETBL_ID>123</ETBL_ID>
                <ETBL_NOM_EN><![CDATA[AUBERGE DU LAC-COMMANDANT]]></ETBL_NOM_EN>
                <TERRITOIRES>
                    <TERRITOIRE>
                        <TERR_ZONES>
                            <TERR_ZONE>
                                <TERR_ZONE_GENRE_EN>Area</TERR_ZONE_GENRE_EN>
                                <TERR_ZONE_NOM_EN>Saguenay_Lac_Saint_Jean</TERR_ZONE_NOM_EN>
                            </TERR_ZONE>
                        </TERR_ZONES>
                    </TERRITOIRE>
                </TERRITOIRES>
                <PERIODES_EXPLOITATION>
                    <PERIODE_EXPLOITATION>
                        <PER_EXPL_JJMM_DEBUT>01-06</PER_EXPL_JJMM_DEBUT>
                        <PER_EXPL_JJMM_FIN>01-09</PER_EXPL_JJMM_FIN>
                    </PERIODE_EXPLOITATION>
                </PERIODES_EXPLOITATION>
                <CARACTERISTIQUES>
                    <CARACTERISTIQUE>
                        <CARACT_NOM_EN>Units</CARACT_NOM_EN>
                            <CARACT_ATTRIBUTS>
                            <CARACT_ATTRIBUT>
                                <CARACT_ATTRB_NOM_EN>room~</CARACT_ATTRB_NOM_EN>
                                <CARACT_ATTRB_VAL>4</CARACT_ATTRB_VAL>
                            </CARACT_ATTRIBUT>
                            <CARACT_ATTRIBUT>
                                <CARACT_ATTRB_NOM_EN>suite~</CARACT_ATTRB_NOM_EN>
                                <CARACT_ATTRB_VAL>6</CARACT_ATTRB_VAL>
                            </CARACT_ATTRIBUT>
                            </CARACT_ATTRIBUTS>
                    </CARACTERISTIQUE>
                    <CARACTERISTIQUE>
                            <CARACT_NOM_EN>Rates</CARACT_NOM_EN>
                            <CARACT_ATTRIBUTS>
                             <CARACT_ATTRIBUT>
                                <CARACT_ATTRB_NOM_EN><Maximum for the least expensive unit></CARACT_ATTRB_NOM_EN>
                                <CARACT_ATTRB_VAL>149</CARACT_ATTRB_VAL>
                             </CARACT_ATTRIBUT>
                             <CARACT_ATTRIBUT>
                                <CARACT_ATTRB_NOM_EN><Maximum for the most expensive unit></CARACT_ATTRB_NOM_EN>
                                <CARACT_ATTRB_VAL>238</CARACT_ATTRB_VAL>
                             </CARACT_ATTRIBUT>
                            </CARACT_ATTRIBUTS>
                    </CARACTERISTIQUE>
                </CARACTERISTIQUES>
            </ETABLISSEMENT>
        </ETABLISSEMENTS>
        """;

    Reader reader = new StringReader(validXml);
    List<Hosting> hosts = this.parser.parse(reader);

    assertEquals(1, hosts.size());
    Hosting host = hosts.getFirst();
    assertEquals(AN_EXPECTED_ID, host.getID());
    assertEquals(AN_EXPECTED_NAME, host.getName());
    assertEquals(AN_EXPECTED_CAPACITY, host.getMaxCapacity());
    assertEquals(SAGLAC, host.getRegion());

    Map<LocalDate, LocalDate> availablePeriod = host.getAvailablePeriod();
    availablePeriod.forEach(
        (start, end) -> {
          assertEquals(LocalDate.of(now().getYear(), AN_EXPECTED_MONTH_OF_PERIOD_START, 1), start);
          assertEquals(LocalDate.of(now().getYear(), AN_EXPECTED_MONTH_OF_PERIOD_END, 1), end);
        });
    Map<Money, Money> priceRange = host.getPriceRange();
    priceRange.forEach(
        (min, max) -> {
          assertEquals(new Money("149"), min);
          assertEquals(new Money("238"), max);
        });
  }

  @Test
  public void givenInvalidRegion_whenParse_thenReturnsEmptyHosts() {
    String invalidRegionXml =
        """
            <ETABLISSEMENTS>
                <ETABLISSEMENT>
                    <ETBL_ID>123</ETBL_ID>
                    <ETBL_NOM_EN><![CDATA[AUBERGE DU LAC-COMMANDANT]]></ETBL_NOM_EN>
                    <TERRITOIRES>
                        <TERRITOIRE>
                            <TERR_ZONES>
                                <TERR_ZONE>
                                    <TERR_ZONE_GENRE_EN>Area</TERR_ZONE_GENRE_EN>
                                    <TERR_ZONE_NOM_EN>Invalid Region</TERR_ZONE_NOM_EN>
                                </TERR_ZONE>
                            </TERR_ZONES>
                        </TERRITOIRE>
                    </TERRITOIRES>
                    <PERIODES_EXPLOITATION>
                        <PERIODE_EXPLOITATION>
                            <PER_EXPL_JJMM_DEBUT>01-06</PER_EXPL_JJMM_DEBUT>
                            <PER_EXPL_JJMM_FIN>01-09</PER_EXPL_JJMM_FIN>
                        </PERIODE_EXPLOITATION>
                    </PERIODES_EXPLOITATION>
                    <CARACTERISTIQUES>
                        <CARACTERISTIQUE>
                            <CARACT_NOM_EN>Units</CARACT_NOM_EN>
                                <CARACT_ATTRIBUT>
                                    <CARACT_ATTRB_NOM_EN>room~</CARACT_ATTRB_NOM_EN>
                                    <CARACT_ATTRB_VAL>4</CARACT_ATTRB_VAL>
                                </CARACT_ATTRIBUT>
                        </CARACTERISTIQUE>
                        <CARACTERISTIQUE>
                            <CARACT_NOM_EN>Rates</CARACT_NOM_EN>
                            <CARACT_ATTRIBUTS>
                             <CARACT_ATTRIBUT>
                                <CARACT_ATTRB_NOM_EN><Maximum for the least expensive unit></CARACT_ATTRB_NOM_EN>
                                <CARACT_ATTRB_VAL>149</CARACT_ATTRB_VAL>
                             </CARACT_ATTRIBUT>
                            </CARACT_ATTRIBUTS>
                    </CARACTERISTIQUE>
                    </CARACTERISTIQUES>
                </ETABLISSEMENT>
            </ETABLISSEMENTS>
            """;

    Reader reader = new StringReader(invalidRegionXml);
    List<Hosting> hosts = this.parser.parse(reader);
    assertTrue(hosts.isEmpty());
  }

  @Test
  public void givenInvalidCapacityParsing_whenParse_thenReturnsEmptyHosts() {
    String invalidCapacityXml =
        """
            <ETABLISSEMENTS>
                <ETABLISSEMENT>
                    <ETBL_ID>123</ETBL_ID>
                    <ETBL_NOM_EN><![CDATA[AUBERGE DU LAC-COMMANDANT]]></ETBL_NOM_EN>
                    <TERRITOIRES>
                        <TERRITOIRE>
                            <TERR_ZONES>
                                <TERR_ZONE>
                                    <TERR_ZONE_GENRE_EN>Area</TERR_ZONE_GENRE_EN>
                                    <TERR_ZONE_NOM_EN>Saguenay_Lac_Saint_Jean</TERR_ZONE_NOM_EN>
                                </TERR_ZONE>
                            </TERR_ZONES>
                        </TERRITOIRE>
                    </TERRITOIRES>
                    <PERIODES_EXPLOITATION>
                        <PERIODE_EXPLOITATION>
                            <PER_EXPL_JJMM_DEBUT>01-06</PER_EXPL_JJMM_DEBUT>
                            <PER_EXPL_JJMM_FIN>01-09</PER_EXPL_JJMM_FIN>
                        </PERIODE_EXPLOITATION>
                    </PERIODES_EXPLOITATION>
                    <CARACTERISTIQUES>
                        <CARACTERISTIQUE>
                            <CARACT_NOM_EN>Rates</CARACT_NOM_EN>
                            <CARACT_ATTRIBUTS>
                             <CARACT_ATTRIBUT>
                                <CARACT_ATTRB_NOM_EN><Maximum for the least expensive unit></CARACT_ATTRB_NOM_EN>
                                <CARACT_ATTRB_VAL>149</CARACT_ATTRB_VAL>
                             </CARACT_ATTRIBUT>
                            </CARACT_ATTRIBUTS>
                    </CARACTERISTIQUE>
                    </CARACTERISTIQUES>
                </ETABLISSEMENT>
            </ETABLISSEMENTS>
            """;

    Reader reader = new StringReader(invalidCapacityXml);
    List<Hosting> hosts = this.parser.parse(reader);
    assertTrue(hosts.isEmpty());
  }

  @Test
  public void givenMissingOrInvalidExploitationPeriods_whenParse_thenReturnsEmptyHosts() {
    String missingExploitationXml =
        """
            <ETABLISSEMENTS>
                <ETABLISSEMENT>
                    <ETBL_ID>123</ETBL_ID>
                    <ETBL_NOM_EN><![CDATA[AUBERGE DU LAC-COMMANDANT]]></ETBL_NOM_EN>
                    <TERRITOIRES>
                        <TERRITOIRE>
                            <TERR_ZONES>
                                <TERR_ZONE>
                                    <TERR_ZONE_GENRE_EN>Area</TERR_ZONE_GENRE_EN>
                                    <TERR_ZONE_NOM_EN>Saguenay_Lac_Saint_Jean</TERR_ZONE_NOM_EN>
                                </TERR_ZONE>
                            </TERR_ZONES>
                        </TERRITOIRE>
                    </TERRITOIRES>
                    <CARACTERISTIQUES>
                        <CARACTERISTIQUE>
                            <CARACT_NOM_EN>Units</CARACT_NOM_EN>
                                <CARACT_ATTRIBUT>
                                    <CARACT_ATTRB_NOM_EN>room~</CARACT_ATTRB_NOM_EN>
                                    <CARACT_ATTRB_VAL>4</CARACT_ATTRB_VAL>
                                </CARACT_ATTRIBUT>
                        </CARACTERISTIQUE>
                        <CARACTERISTIQUE>
                            <CARACT_NOM_EN>Rates</CARACT_NOM_EN>
                            <CARACT_ATTRIBUTS>
                             <CARACT_ATTRIBUT>
                                <CARACT_ATTRB_NOM_EN><Maximum for the least expensive unit></CARACT_ATTRB_NOM_EN>
                                <CARACT_ATTRB_VAL>149</CARACT_ATTRB_VAL>
                             </CARACT_ATTRIBUT>
                            </CARACT_ATTRIBUTS>
                    </CARACTERISTIQUE>
                    </CARACTERISTIQUES>
                </ETABLISSEMENT>
            </ETABLISSEMENTS>
            """;

    Reader reader = new StringReader(missingExploitationXml);
    List<Hosting> hosts = this.parser.parse(reader);
    assertTrue(hosts.isEmpty());
  }

  @Test
  public void givenInvalidXmlFile_whenParse_thenReturnsEmptyHosts() {
    String invalidXmlFile = """
        <INVALIDXML>
        </INVALIDXML>
        """;
    Reader reader = new StringReader(invalidXmlFile);

    List<Hosting> hosts = this.parser.parse(reader);
    assertTrue(hosts.isEmpty());
  }
}
