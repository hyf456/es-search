package com.hivescm.escenter.service;

import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.common.SaveESObject;
import com.hivescm.escenter.convert.ESSearchConvertor;
import com.hivescm.escenter.core.service.ESNestedSearchService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by dongchunfu on 2017/7/20.
 * <p>
 * es 依赖MQ服务
 */
@Configuration
@Component(value = "mqService")
public class MQService {
	private static final Logger LOGGER = LoggerFactory.getLogger(MQService.class);

	@Autowired
	private ESNestedSearchService esSearchService;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private Channel channel;

	/**
	 * 使用redis进行消息重发验证
	 *
	 * @param exchange
	 * @param routeKey
	 * @param msgContent
	 * @return
	 */
	public boolean sendMsg(String exchange, String routeKey, String msgContent) {
		boolean sendState = false;
		try {

			for (int i = 1; i < 4; i++) {
				// 启用确认功能
				channel.confirmSelect();
				channel.basicPublish(exchange, routeKey, MessageProperties.PERSISTENT_TEXT_PLAIN, msgContent.getBytes());
				sendState = channel.waitForConfirms();

				if (sendState) {
					LOGGER.info("index mq send success try :{} times", i);
					break;
				}
			}

		} catch (Exception ex) {
			LOGGER.error("index req mq send failed,exchange:" + exchange + "routeKey" + routeKey + "msgContent" + msgContent,
					ex);
		}
		return sendState;
	}

	@RabbitListener(queues = "common-es-save", containerFactory = "rabbitListenerContainerFactory")
	public void process(String msgContent) {
		LOGGER.debug("consume es save mq,message:{}.", msgContent);
		try {
			final SaveESObject obj = ESSearchConvertor.json2Object(msgContent, SaveESObject.class);
			final DataResult<Boolean> dataResult = esSearchService.save(obj);
		} catch (Throwable ex) {
			LOGGER.error("es save mq consume failed,message:" + msgContent, ex);
		}
	}
}
