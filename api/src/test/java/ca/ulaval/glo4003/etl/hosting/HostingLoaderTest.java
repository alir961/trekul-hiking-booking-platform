package ca.ulaval.glo4003.etl.hosting;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.hosting.Hosting;
import ca.ulaval.glo4003.hosting.HostingRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HostingLoaderTest {
  private static final Hosting ANY_HOSTING = mock(Hosting.class);
  private HostingRepository hostingRepositoryMock;
  private HostingLoader hostingLoader;

  @BeforeEach
  void setUp() {
    this.hostingRepositoryMock = mock(HostingRepository.class);
    this.hostingLoader = new HostingLoader(this.hostingRepositoryMock);
  }

  @Test
  void whenLoading_thenSaveAllHosting() {
    this.hostingLoader.load(List.of(ANY_HOSTING));

    verify(this.hostingRepositoryMock).save(ANY_HOSTING);
  }
}
