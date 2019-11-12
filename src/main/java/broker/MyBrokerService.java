package broker;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

import java.net.URI;

public class MyBrokerService {
    private static BrokerService instance = null;
    private static String url = "tcp://localhost:61616";

    public static BrokerService getInstance() throws Exception {
        if (instance == null) {
            instance = BrokerFactory.createBroker(new URI("broker:(" + url + ")"));
            return instance;
        }

        return instance;
    }

    public static String getUrl() {
        return url;
    }
}
