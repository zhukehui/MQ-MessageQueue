package atkehui;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author eternity
 * @create 2019-10-23 23:26
 */
public class JMSConsumer_Topic {
    public static final String DEFAULT_BROKER_BIND_URL = "tcp://192.168.234.188:61616";
    public static final String TOPIC_NAME = "Topic01";

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(TOPIC_NAME);

        MessageConsumer messageConsumer = session.createConsumer(topic);

//先启动订阅再启动生产，不然发送的消息是废消息
//多个订阅的话，每个订阅都会收到相同的一份消息

        messageConsumer.setMessageListener((message) -> {
            if (message != null && message instanceof TextMessage){
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("***收到topic："+textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
