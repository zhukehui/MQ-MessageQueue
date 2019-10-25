package atkehui;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


/**
 * @author eternity
 * @create 2019-10-23 20:52
 */
public class JMSConsumer {

    public static final String MQ_BROKER_URL = "tcp://192.168.234.188:61616";
    public static final String QUEUE_NAME = "myqueue";//和前面的必须要一致

    public static void main(String[] args) throws JMSException {
        //1,先通过ActiveMQConnectionFactory获得mq工厂
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(MQ_BROKER_URL);

        //2.获得连接Connection
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();//启动ActiveMQ服务

        //3.通过Connection获得session
        //3.1第一个参数叫事务默认为false
        //3.2第二个参数叫签收，默认自动签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4.通过session创建目的地
        Queue queue = session.createQueue(QUEUE_NAME);

        //5.通过session创建消息消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);
/*
        异步非阻塞方式(监听器onMessage())
        订阅者或接收者通过MessageConsumer的setMessageListener(MessageListener listener)注册一个消息监听器，
        当消息到达之后，系统自动调用监听器MessageListener的onMessage(Message message)方法。


        消息不能重复消费
        多个消费者的话：采用轮询的方式，一人一条执行
*/

        messageConsumer.setMessageListener((message) -> {
            if (message != null && message instanceof TextMessage){
                 TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println(textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        /*同步阻塞方式receive() ，订阅者或接收者调用MessageConsumer的receive()方法来接收消息，
        receive()将一直阻塞
        receive(long timeout) 按照给定的时间阻塞，到时间自动退出

        while (true){
//            TextMessage textMessage = (TextMessage) messageConsumer.receive();//等待，前面发送什么类型的，就接收什么类型
            TextMessage textMessage = (TextMessage) messageConsumer.receive(4*1000);//计时等待，前面发送什么类型的，就接收什么类型
            if (textMessage != null){
                System.out.println(textMessage.getText());
            }else {
                break;
            }
        }
        //释放资源
        messageConsumer.close();
        session.close();
        connection.close();

         */
    }
}
