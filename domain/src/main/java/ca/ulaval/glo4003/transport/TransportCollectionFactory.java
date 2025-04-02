package ca.ulaval.glo4003.transport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransportCollectionFactory {
  public TransportCollection create() {
    Map<TransportType, List<ReservableTransport>> transportsMap =
        new HashMap<TransportType, List<ReservableTransport>>();
    transportsMap.put(TransportType.ORGANIZED_BUS, new ArrayList<>());
    transportsMap.put(TransportType.CARPOOL, new ArrayList<>());
    return new TransportCollection(transportsMap);
  }
}
