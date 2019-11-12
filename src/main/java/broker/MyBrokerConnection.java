package broker;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

public class MyBrokerConnection {
    private static Connection connection = null;

    public static Connection getConnection() throws JMSException {
        if (connection == null) {
            ConnectionFactory connectionFactory =
                    new ActiveMQConnectionFactory(MyBrokerService.getUrl());
            connection = connectionFactory.createConnection();

            return connection;
        }

        return connection;
    }
}
