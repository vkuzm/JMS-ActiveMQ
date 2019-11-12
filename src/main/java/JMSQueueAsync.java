import broker.MyBrokerConnection;
import broker.MyBrokerService;

import javax.jms.*;
import java.util.Scanner;

@SuppressWarnings("Duplicates")

/*
   One producer and one synchronous consumer
   P2P Queue
 */

public class JMSQueueAsync {
    public static void main(String[] args) throws Exception {

        // Start the broker service server
        MyBrokerService.getInstance().start();

        // Create the connection and get the session
        Session session = MyBrokerConnection.getConnection()
                .createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the queue chanel (p2p)
        Queue queue = session.createQueue("chanelQueue");

        // Create the producer & the consumer
        MessageProducer producer = session.createProducer(queue);
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new ConsumerMessageListener("Consumer"));

        // Start the connection
        MyBrokerConnection.getConnection().start();

        // Produces the messages
        new Thread(() -> {
            System.out.print("Enter a message: ");
            Scanner scanner = new Scanner(System.in);

            while (scanner.hasNext()) {
                try {
                    Message message = session.createTextMessage(scanner.nextLine());
                    producer.send(message);

                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
