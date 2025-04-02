package ca.ulaval.glo4003.tax;

import ca.ulaval.glo4003.money.Money;
import java.math.BigDecimal;

public class QuebecTaxStrategy implements TaxStrategy {
  private static final String TYPE_TPS = "TPS";
  private static final String TYPE_TVQ = "TVQ";
  private static final BigDecimal TPS = new BigDecimal("0.05");
  private static final BigDecimal TVQ = new BigDecimal("0.09975");

  @Override
  public Taxes calculateTax(Money priceWithoutTax) {
    Taxes taxes = new Taxes(priceWithoutTax);
    taxes.addTaxe(TYPE_TPS, priceWithoutTax.multiply(TPS));
    taxes.addTaxe(TYPE_TVQ, priceWithoutTax.multiply(TVQ));

    return taxes;
  }
}
