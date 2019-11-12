import broker.MyBrokerConnection;
import broker.MyBrokerService;

import javax.jms.*;

@SuppressWarnings("Duplicates")

/*
   Multiple producers and multiple consumers
   P2P Queue
 */

public class JMSQueueAsyncMultiple {
    public static void main(String[] args) throws Exception {

        // Start the broker service server
        MyBrokerService.getInstance().start();

        try {
            // Create the connection and get the session
            Session session = MyBrokerConnection.getConnection()
                    .createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the queue chanel (p2p)
            Queue queue = session.createQueue("chanelQueue");

            // Create the consumers
            for (int i = 1; i <= 4; i++) {
                MessageConsumer consumer = session.createConsumer(queue);
                consumer.setMessageListener(new ConsumerMessageListener("Consumer " + i));
            }

            // Start the connection
            MyBrokerConnection.getConnection().start();

            // Create the producers
            for (int i = 1; i <= 10; i++) {
                MessageProducer producer = session.createProducer(queue);
                Message message = session.createTextMessage("Message from producer " + i);
                producer.send(message);
            }

            // Close the session
            Thread.sleep(20000);
            session.close();

        } finally {
            // Close the connection
            if (MyBrokerConnection.getConnection() != null) {
                MyBrokerConnection.getConnection().close();
            }

            // Stop the broker service server
            MyBrokerService.getInstance().stop();
        }

    }
}
