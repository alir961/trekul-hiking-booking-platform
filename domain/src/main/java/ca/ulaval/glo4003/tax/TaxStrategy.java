package ca.ulaval.glo4003.tax;

import ca.ulaval.glo4003.money.Money;

public interface TaxStrategy {
  Taxes calculateTax(Money priceWithoutTax);
}
