package ca.ulaval.glo4003.money;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class MoneyTest {
  private static final String A_MONEY_STRING = "70.63";
  private static final Money A_MONEY = new Money(A_MONEY_STRING);

  @Test
  void givenSameObject_whenCallingEquals_thenObjectIsEqual() {
    assertEquals(A_MONEY, A_MONEY);
  }

  @Test
  void givenAnotherObjectWithSameProperties_whenCallingEquals_thenObjectIsEqual() {
    Money sameMoney = new Money(A_MONEY_STRING);

    assertEquals(A_MONEY, sameMoney);
  }

  @Test
  void givenAnotherObjectWithDifferentProperties_whenCallingEquals_thenObjectIsNotEqual() {
    Money differentMoney = new Money("104" + A_MONEY_STRING);
    assertNotEquals(A_MONEY, differentMoney);
  }

  @Test
  void givenAnotherMoney_whenAddingMoney_thenNewMoneyIsAdditionOfBoth() {
    Money moneyOf10 = new Money("10");
    Money moneyOf25 = new Money("25");
    Money moneyOf35 = new Money("35");

    Money result = moneyOf10.add(moneyOf25);

    assertEquals(moneyOf35, result);
  }

  @Test
  void givenCollectionOfMoney_whenAddingMoney_thenTotalOfAllIsReturned() {
    List<Money> multipleMoney = new ArrayList<>();
    Money moneyOf10 = new Money("10");
    multipleMoney.add(moneyOf10);
    multipleMoney.add(moneyOf10);

    Money expectedIs20 = new Money("20");

    Money zeroMoney = new Money();
    Money total = zeroMoney.add(multipleMoney);

    assertEquals(expectedIs20, total);
  }

  @Test
  void givenMultiplierOf2_whenMultiplyingMoney_thenNewMoneyIs2TimesOldMoney() {
    Money simpleMoney = new Money("0.5");
    Money doubleMoney = new Money("1");

    Money result = simpleMoney.multiply(2);

    assertEquals(doubleMoney, result);
  }

  @Test
  void givenMoneyWithMultipleDecimals_whenCallingToString_thenStringShows2Decimals() {
    Money multiDigitsMoney = new Money("0.05980395");
    String expectedString = "0.06";

    assertEquals(expectedString, multiDigitsMoney.toString());
  }

  @Test
  void givenMoneyIsZero_whenCheckIfStrictlyPositive_thenReturnFalse() {
    Money zeroMoney = new Money();

    assertFalse(zeroMoney.isStrictlyPositive());
  }

  @Test
  void givenMoneyIsNegative_whenCheckIfStrictlyPositive_thenReturnFalse() {
    Money negativeMoney = new Money("-0.3");

    assertFalse(negativeMoney.isStrictlyPositive());
  }

  @Test
  void givenMoneyIsPositive_whenCheckIfStrictlyPositive_thenReturnTrue() {
    Money positiveMoney = new Money("0.2");

    assertTrue(positiveMoney.isStrictlyPositive());
  }
}
