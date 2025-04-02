package ca.ulaval.glo4003.transport;

public enum TransportType {
  ORGANIZED_BUS("Organized bus by TrekUL. Many bus stops are available."),
  CARPOOL("Carpool is organized between users. Cost and availability may vary."),
  NONE("No organized transport. Always free.");

  private final String description;

  TransportType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return this.description;
  }
}
