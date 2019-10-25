package atkehui;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author eternity
 * @create 2019-10-23 23:09

 * JMS = java Message Service
 *
 * JMS开发的基本步骤
 * 1：创建一个connection factory
 * 2：通过connection factory来创建JMS connection
 * 3：启动JMS connection
 * 4：通过connection创建JMS session
 * 5：创建JMS destination
 * 6：创建JMS producer或者创建JMS message并设置destination
 * 7：创建JMS consumer或者是注册一个JMS message listener
 * 8：发送或者接受JMS message(s)
 * 9：关闭所有的JMS资源
 * (connection, session, producer, consumer等)
 */
public class JMSProduce_Topic
{
    public static final String DEFAULT_BROKER_BIND_URL = "tcp://192.168.234.188:61616";
    public static final String TOPIC_NAME = "Topic01";

    public static void main(String[] args) throws JMSException {

        //创建一个connection factory
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
        //通过connection factory来创建JMS connection
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();//启动ActiveMQ服务

        //3、通过connection获得session
        //3.1第一个参数叫事务，默认用false
        //3.2第二个参数叫签收，默认自动签收
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        //通过session创建主题
        Topic topic = session.createTopic(TOPIC_NAME);
        //5.通过session创建消息生产者
        MessageProducer messageProducer = session.createProducer(topic);

        for (int i = 0; i < 5; i++) {
            TextMessage textMessage = session.createTextMessage("topicMsg***" + i);
            messageProducer.send(textMessage); //7.messageProducer开始发送消息到MQ
        }
        //8.释放资源
        messageProducer.close();
        session.close();
        connection.close();
        System.out.println("---********主题消息发送到MQ完成");


    }
}
