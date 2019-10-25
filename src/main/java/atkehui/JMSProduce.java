package atkehui;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;


/**
 * @author eternity
 * @create 2019-10-23 20:00
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
public class JMSProduce {
    public static final String MQ_BROKER_URL= "tcp://192.168.234.188:61616";
    public static final String QUEUE_NAME  = "myqueue";

    public static void main(String[] args) throws JMSException {
        //1.先通过ActiveMQConnectionFactory获得mq工厂
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(MQ_BROKER_URL);

        //2.获得连接connection
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();//启动ActiveMQ服务

        //3、通过connection获得session
        //3.1第一个参数叫事务，默认用false
        //3.2第二个参数叫签收，默认自动签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4.通过session创建目的地
        Queue queue = session.createQueue(QUEUE_NAME);

        //5.通过session创建消息生产者
        MessageProducer messageProducer = session.createProducer(queue);

        for (int i = 1; i <= 5; i++) {
            //6.编写发送的消息(提问卡msg)
            TextMessage textMessage = session.createTextMessage("---提问msg：" + i);
            //7.messageProducer开始发送消息到MQ
            messageProducer.send(textMessage);
        }
            //8.释放资源
            messageProducer.close();
            session.close();
            connection.close();
            System.out.println("---MessageProducer  send发送消息成功 ！！！");

    }
}
