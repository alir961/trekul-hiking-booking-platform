package ca.ulaval.glo4003.tax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.ulaval.glo4003.money.Money;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class TaxesTest {
  private static final String ANY_TAX_TYPE = "any tax string";
  private static final String ANY_OTHER_TAX_TYPE = "any other tax string";
  private static final Money ZERO_MONEY = new Money();
  private Taxes taxes;

  @Test
  void givenNoTaxes_whenGettingTotal_thenReturnBaseAmount() {
    Money anyBaseAmount = new Money("60412");
    this.taxes = new Taxes(anyBaseAmount);

    assertEquals(anyBaseAmount, this.taxes.getTotal());
  }

  @Test
  void givenOneTax_whenGettingTotal_thenReturnAmountOfThatOneTaxPlusBaseAmount() {
    Money anyTaxMoney = new Money("5021");
    Money anyBaseAmount = new Money("601");
    this.taxes = new Taxes(anyBaseAmount);
    this.taxes.addTaxe(ANY_TAX_TYPE, anyTaxMoney);

    assertEquals(anyBaseAmount.add(anyTaxMoney), this.taxes.getTotal());
  }

  @Test
  void givenOneTax_whenGettingTaxes_thenThatTaxIsContained() {
    Money anyTaxMoney = new Money("1152");
    this.taxes = new Taxes(ZERO_MONEY);
    this.taxes.addTaxe(ANY_TAX_TYPE, anyTaxMoney);

    Map<String, Money> taxesTypes = this.taxes.getTaxes();

    assertTrue(taxesTypes.containsKey(ANY_TAX_TYPE));
    assertEquals(anyTaxMoney, taxesTypes.get(ANY_TAX_TYPE));
  }

  @Test
  void givenMultipleTaxes_whenGettingTotal_thenReturnTotalOfAllTaxes() {
    Money moneyOf10 = new Money("10");
    this.taxes = new Taxes(ZERO_MONEY);
    this.taxes.addTaxe(ANY_TAX_TYPE, moneyOf10);
    this.taxes.addTaxe(ANY_OTHER_TAX_TYPE, moneyOf10);

    Money expectedOf20 = new Money("20");
    assertEquals(expectedOf20, this.taxes.getTotal());
  }
}
