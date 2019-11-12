import broker.MyBrokerConnection;
import broker.MyBrokerService;

import javax.jms.*;
import java.util.Scanner;

@SuppressWarnings("Duplicates")

/*
   One producer and one synchronous consumer
   Topic Pub/Subs
 */

public class JMSTopicAsync {
    public static void main(String[] args) throws Exception {

        // Start the broker service server
        MyBrokerService.getInstance().start();

        // Create the connection and get the session
        Session session = MyBrokerConnection.getConnection()
                .createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the topic chanel (pub/subs)
        Topic topic = session.createTopic("chanelTopic");

        // Create the producer & the consumer
        MessageProducer producer = session.createProducer(topic);

        for (int i = 1; i <= 5; i++) {
            MessageConsumer consumer = session.createConsumer(topic);
            consumer.setMessageListener(new ConsumerMessageListener("Consumer " + i));
        }

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
