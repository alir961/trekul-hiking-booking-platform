package ca.ulaval.glo4003.notification.language;

import ca.ulaval.glo4003.hiking.HikingDifficulty;
import ca.ulaval.glo4003.hiking.HikingRegion;
import ca.ulaval.glo4003.invoice.BillableItems;
import ca.ulaval.glo4003.transport.TransportType;
import java.util.HashMap;
import java.util.Map;

public class FrenchDescriptions {
  private Map<HikingDifficulty, String> hikingDifficultyDescriptions;
  private Map<BillableItems, String> billableItemDescriptions;
  private Map<TransportType, String> transportTypeDescriptions;
  private Map<HikingRegion, String> hikingRegionDescriptions;

  public FrenchDescriptions() {
    this.hikingDifficultyDescriptions = new HashMap<>();
    this.billableItemDescriptions = new HashMap<>();
    this.transportTypeDescriptions = new HashMap<>();
    this.hikingRegionDescriptions = new HashMap<>();
    this.putHikingDifficultyDescriptions();
    this.putBillableItemDescriptions();
    this.putTransportTypeDescriptions();
    this.putHikingRegionDescriptions();
  }

  public String getHikingDifficultyDescription(HikingDifficulty difficulty) {
    return this.hikingDifficultyDescriptions.get(difficulty);
  }

  public String getBillableItemDescription(BillableItems item) {
    return this.billableItemDescriptions.get(item);
  }

  public String getTransportTypeDescription(TransportType type) {
    return this.transportTypeDescriptions.get(type);
  }

  public String getHikingRegionDescription(HikingRegion region) {
    return this.hikingRegionDescriptions.get(region);
  }

  private void putHikingDifficultyDescriptions() {
    this.hikingDifficultyDescriptions.put(HikingDifficulty.EASY, "Facile");
    this.hikingDifficultyDescriptions.put(HikingDifficulty.INTERMEDIATE, "Intermédiaire");
    this.hikingDifficultyDescriptions.put(HikingDifficulty.HARD, "Difficile");
  }

  private void putBillableItemDescriptions() {
    this.billableItemDescriptions.put(BillableItems.RESERVATION, "Réservation");
    this.billableItemDescriptions.put(BillableItems.CREDIT, "Crédit");
    this.billableItemDescriptions.put(BillableItems.TRANSPORT, "Transport");
    this.billableItemDescriptions.put(BillableItems.TRIP, "Voyage");
    this.billableItemDescriptions.put(BillableItems.HOSTING, "Séjour");
  }

  private void putTransportTypeDescriptions() {
    this.transportTypeDescriptions.put(TransportType.NONE, "Transport autonome");
    this.transportTypeDescriptions.put(TransportType.CARPOOL, "Covoiturage");
    this.transportTypeDescriptions.put(TransportType.ORGANIZED_BUS, "Autobus organisé");
  }

  private void putHikingRegionDescriptions() {
    this.hikingRegionDescriptions.put(HikingRegion.CHARLEVOIX, "Charlevoix");
    this.hikingRegionDescriptions.put(HikingRegion.SAGLAC, "Saguenay–Lac-Saint-Jean");
    this.hikingRegionDescriptions.put(HikingRegion.GASPESIE, "Gaspésie");
  }
}
