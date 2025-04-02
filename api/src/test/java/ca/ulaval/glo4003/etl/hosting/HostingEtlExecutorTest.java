package ca.ulaval.glo4003.etl.hosting;

import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.etl.Extractor;
import ca.ulaval.glo4003.etl.exception.ExtractException;
import ca.ulaval.glo4003.hosting.Hosting;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HostingEtlExecutorTest {
  private static final Collection<Hosting> ANY_EXTRACTED_HOSTINGS = List.of(mock(Hosting.class));
  private HostingLoader hostingLoaderMock;
  private HostingEtlExecutor hostingEtlExecutor;

  @BeforeEach
  void setUp() throws ExtractException {
    this.hostingLoaderMock = mock(HostingLoader.class);
    Extractor<Hosting> hostingExtractorMock = mock(Extractor.class);
    when(hostingExtractorMock.extract()).thenReturn(ANY_EXTRACTED_HOSTINGS);

    this.hostingEtlExecutor = new HostingEtlExecutor(hostingExtractorMock, this.hostingLoaderMock);
  }

  @Test
  void whenExecuting_thenLoadHostingFromExtractor() throws ExtractException {
    this.hostingEtlExecutor.execute();

    verify(this.hostingLoaderMock).load(ANY_EXTRACTED_HOSTINGS);
  }
}
