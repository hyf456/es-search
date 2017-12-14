package com.hivescm.escenter.cofig;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by DongChunfu on 2017/7/27
 * <p>
 * rabbit mq 配置类
 */
@Configurable
@Configuration
public class RabbitMQConfig {

	@Value("${spring.rabbitmq.host}")
	private String RABBITMQ_HOST;
	@Value("${spring.rabbitmq.port}")
	private int RABBITMQ_PORT;
	@Value("${spring.rabbitmq.username}")
	private String RABBITMQ_USERNAME;
	@Value("${spring.rabbitmq.password}")
	private String RABBITMQ_PASSWORD;

	@Value("${spring.rabbitmq.virtualhost}")
	private String RABBITMQ_VIRTUALHOST;
	@Value("${escenter.save.mq.exchange}")
	private String exchange;
	@Value("${escenter.save.mq.routekey}")
	private String routekey;
	@Value("${escenter.save.mq.queue}")
	private String esqueue;

	@Autowired
	private ConnectionFactory factory;

	/**
	 * rabbitMQ 生产者链接工厂配置
	 */
	@Bean
	public ConnectionFactory connectionFactory() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RABBITMQ_HOST);
		factory.setPort(RABBITMQ_PORT);
		factory.setUsername(RABBITMQ_USERNAME);
		factory.setPassword(RABBITMQ_PASSWORD);
		factory.setVirtualHost(RABBITMQ_VIRTUALHOST);
		factory.setAutomaticRecoveryEnabled(true);// 服务重启自动发现
		return factory;
	}

	/**
	 * rabbitMQ 消费者链接工厂配置
	 */
	@Bean
	public CachingConnectionFactory CachingConnectionFactory() {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(RABBITMQ_HOST,RABBITMQ_PORT);
		cachingConnectionFactory.setUsername(RABBITMQ_USERNAME);
		cachingConnectionFactory.setPassword(RABBITMQ_PASSWORD);
		return cachingConnectionFactory;
	}

	/**
	 * 显示声明信道，绑定交换器与队列关系
	 */
	@Bean
	public Channel rabbitMQProducerClient()
			throws Exception {
		final Connection connection = factory.newConnection();
		final Channel channel = connection.createChannel();
		final AMQP.Queue.BindOk bind = channel.queueBind(esqueue, exchange, routekey);
		return channel;
	}
}
