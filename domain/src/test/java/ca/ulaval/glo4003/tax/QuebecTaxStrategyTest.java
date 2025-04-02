package ca.ulaval.glo4003.tax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.ulaval.glo4003.money.Money;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class QuebecTaxStrategyTest {
  private static final String TEN_DOLLARS = "10.00";
  private static final String TEN_DOLLARS_AFTER_QUEBEC_TAX = "11.50";
  private static final String TAX_KEY_TPS = "TPS";
  private static final String TAX_KEY_TVQ = "TVQ";
  private TaxStrategy quebecTaxStrategy = new QuebecTaxStrategy();

  @Test
  void whenApplyingTaxes_thenPriceIsFifteenPercentOfPrevious() {
    Money costWithoutTaxes = new Money(TEN_DOLLARS);

    Money calculatedCostAfterTax = this.quebecTaxStrategy.calculateTax(costWithoutTaxes).getTotal();

    Money expectedCostAfterTax = new Money(TEN_DOLLARS_AFTER_QUEBEC_TAX);
    assertEquals(expectedCostAfterTax, calculatedCostAfterTax);
  }

  @Test
  void whenApplyingTaxes_thenTPSHasBeenAddedToTaxes() {
    Money costWithoutTaxes = new Money(TEN_DOLLARS);

    Map<String, Money> taxes = this.quebecTaxStrategy.calculateTax(costWithoutTaxes).getTaxes();

    assertTrue(taxes.containsKey(TAX_KEY_TPS));
  }

  @Test
  void whenApplyingTaxes_thenTVQHasBeenAddedToTaxes() {
    Money costWithoutTaxes = new Money(TEN_DOLLARS);

    Map<String, Money> taxes = this.quebecTaxStrategy.calculateTax(costWithoutTaxes).getTaxes();

    assertTrue(taxes.containsKey(TAX_KEY_TVQ));
  }
}
