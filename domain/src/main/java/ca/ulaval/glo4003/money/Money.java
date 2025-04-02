package ca.ulaval.glo4003.money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;

public class Money {
  private static final int DECIMALS = 2;
  private final BigDecimal value;

  public Money() {
    this.value = new BigDecimal("0");
  }

  public Money(String value) {
    this.value = new BigDecimal(value.replace(",", "."));
  }

  private Money(BigDecimal value) {
    this.value = value.setScale(DECIMALS, RoundingMode.CEILING);
  }

  public Money(Double value) {
    this.value = BigDecimal.valueOf(value).setScale(DECIMALS, RoundingMode.CEILING);
  }

  public Money add(Money other) {
    return new Money(this.value.add(other.value));
  }

  public Money add(Collection<Money> multiples) {
    Money total = this;
    for (Money money : multiples) {
      total = total.add(money);
    }

    return total;
  }

  public Money subtract(Money other) {
    return new Money(this.value.subtract(other.value));
  }

  public boolean isLowerOrEqualThan(Money other) {
    int compareResult = this.value.compareTo(other.value);
    return compareResult <= 0;
  }

  public boolean isGreaterOrEqualThan(Money other) {
    return this.value.compareTo(other.value) >= 0;
  }

  public Money multiply(int multiplier) {
    return this.multiply(new BigDecimal(multiplier));
  }

  public Money multiply(BigDecimal multiplier) {
    return new Money(this.value.multiply(multiplier));
  }

  @Override
  public String toString() {
    return this.value.setScale(DECIMALS, RoundingMode.CEILING).toString();
  }

  @Override
  public int hashCode() {
    return this.value.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    boolean equal = false;

    if (o == this) {
      equal = true;
    } else if (o instanceof Money otherMoney) {
      equal = this.toString().equals(otherMoney.toString());
    }

    return equal;
  }

  public boolean isStrictlyPositive() {
    return this.value.compareTo(new BigDecimal(0)) > 0;
  }

  public Money asNegative() {
    if (this.isStrictlyPositive()) {
      return new Money(this.value.negate());
    } else {
      return new Money(this.value);
    }
  }

  public Money asPositive() {
    if (!(this.isStrictlyPositive())) {
      return new Money(this.value.negate());
    } else {
      return new Money(this.value);
    }
  }
}
