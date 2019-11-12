import broker.MyBrokerConnection;
import broker.MyBrokerService;

import javax.jms.*;

@SuppressWarnings("Duplicates")

/*
   One producer and one synchronous consumer
   P2P Queue
 */

public class JMSQueueSync {
    public static void main(String[] args) throws Exception {

        // Start the broker service server
        MyBrokerService.getInstance().start();

        try {
            // Create the connection and get the session
            Session session = MyBrokerConnection.getConnection()
                    .createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the queue chanel (p2p)
            Queue queue = session.createQueue("chanelQueue");

            // Create the producer & the consumer
            MessageProducer producer = session.createProducer(queue);
            MessageConsumer consumer = session.createConsumer(queue);

            // Produces the messages
            new Thread(() -> {
                try {
                    sendMessage("My first message", session, producer);
                    sendMessage("My second message", session, producer);

                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }).start();

            // Start the connection
            MyBrokerConnection.getConnection().start();

            // Consumes the messages
            new Thread(() -> {
                try {
                    getLastMessage(consumer);
                    getLastMessage(consumer);
                    getLastMessage(consumer);

                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }).start();

            // Close the session
            Thread.sleep(1000);
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

    private static void getLastMessage(MessageConsumer consumer) throws JMSException {
        TextMessage textMessage = (TextMessage) consumer.receive();

        System.out.println("Trying get message...");

        if (textMessage != null) {
            System.out.println(textMessage);
            System.out.println("Received: " + textMessage.getText());
        } else {
            System.out.println("No messages in the queue yet!");
        }

        System.out.println();
    }

    private static void sendMessage(String payload, Session session, MessageProducer producer) throws JMSException {
        Message message = session.createTextMessage(payload);
        producer.send(message);
    }
}
