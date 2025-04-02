package ca.ulaval.glo4003.hosting.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import ca.ulaval.glo4003.traveler.user.UserRepository;
import ca.ulaval.glo4003.trip.TripTravelerFetcher;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TripTravelerFetcherFactoryTest {

  private UserRepository userRepositoryMock;
  private TripTravelerFetcherFactory tripTravelerFetcherFactory;

  @BeforeEach
  void setUp() {
    this.userRepositoryMock = mock(UserRepository.class);

    this.tripTravelerFetcherFactory = new TripTravelerFetcherFactory();
  }

  @Test
  void whenCreatingTripTravelerFetcher_thenTripTravelerFetcherWithCorrectFieldsIsReturned() {
    TripTravelerFetcher tripTravelerFetcher =
        this.tripTravelerFetcherFactory.create(this.userRepositoryMock);

    assertEquals(Collections.emptySet(), tripTravelerFetcher.fetchUpdatedUsers());
  }
}
