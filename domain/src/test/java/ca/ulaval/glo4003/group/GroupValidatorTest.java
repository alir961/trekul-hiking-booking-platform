package ca.ulaval.glo4003.group;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.group.exception.GroupTooBigException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupValidatorTest {

  private static final int ANY_MAXIMUM_GUEST_NUMBER = 5;
  private static final int ANY_GUEST_NUMBER_LESS_THAN_MAXIMUM = ANY_MAXIMUM_GUEST_NUMBER - 1;
  private static final int ANY_GUEST_NUMBER_MORE_THAN_MAXIMUM = ANY_MAXIMUM_GUEST_NUMBER + 3;
  private Group groupMock;
  private GroupConfig groupConfigMock;
  private GroupValidator groupValidator;

  @BeforeEach
  void setUp() {
    this.groupMock = mock(Group.class);
    this.groupConfigMock = mock(GroupConfig.class);
    when(this.groupConfigMock.getMaximumGroupSize()).thenReturn(ANY_MAXIMUM_GUEST_NUMBER);
    this.groupValidator = new GroupValidator(this.groupConfigMock);
  }

  @Test
  void givenNumberOfGuestsLessThanLimit_whenValidatingGroup_thenThrowNothing() {
    when(this.groupMock.calculateTotalTravelers()).thenReturn(ANY_GUEST_NUMBER_LESS_THAN_MAXIMUM);

    assertDoesNotThrow(() -> this.groupValidator.validate(this.groupMock));
  }

  @Test
  void givenNumberOfGuestsMoreThanLimit_whenValidatingGroup_thenGroupTooBigException() {
    when(this.groupMock.calculateTotalTravelers()).thenReturn(ANY_GUEST_NUMBER_MORE_THAN_MAXIMUM);

    assertThrows(GroupTooBigException.class, () -> this.groupValidator.validate(this.groupMock));
  }
}
