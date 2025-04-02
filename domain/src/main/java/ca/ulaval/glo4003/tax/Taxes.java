package ca.ulaval.glo4003.tax;

import ca.ulaval.glo4003.money.Money;
import java.util.HashMap;
import java.util.Map;

public class Taxes {
  private final Map<String, Money> taxes = new HashMap<>();
  private final Money basePrice;

  public Taxes(Money basePrice) {
    this.basePrice = basePrice;
  }

  public void addTaxe(String type, Money tax) {
    this.taxes.put(type, tax);
  }

  public Map<String, Money> getTaxes() {
    return Map.copyOf(this.taxes);
  }

  public Money getTotal() {
    return this.basePrice.add(this.getTaxes().values());
  }
}
