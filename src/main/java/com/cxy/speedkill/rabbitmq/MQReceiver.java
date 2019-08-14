package com.cxy.speedkill.rabbitmq;

import com.cxy.speedkill.domain.SpeedKillOrder;
import com.cxy.speedkill.domain.SpeedKillUser;
import com.cxy.speedkill.redis.RedisService;
import com.cxy.speedkill.service.GoodsService;
import com.cxy.speedkill.service.OrderService;
import com.cxy.speedkill.service.SpeedKillMessageService;
import com.cxy.speedkill.service.SpeedKillService;
import com.cxy.speedkill.vo.GoodsVo;
import com.cxy.speedkill.vo.SpeedKillMessageVo;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MQReceiver {

	private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

	@Autowired
	RedisService redisService;

	@Autowired
	GoodsService goodsService;

	@Autowired
	OrderService orderService;

	@Autowired
	SpeedKillService speedKillService;

	@Autowired
	SpeedKillMessageService messageService ;

	@RabbitListener(queues=MQConfig.MIAOSHA_QUEUE)
	public void receive(String message) {
		log.info("receive message:"+message);
		MiaoshaMessage mm  = RedisService.stringToBean(message, MiaoshaMessage.class);
		SpeedKillUser user = mm.getUser();
		long goodsId = mm.getGoodsId();

		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		int stock = goods.getStockCount();
		if(stock <= 0) {
			return;
		}
		//判断是否已经秒杀到了
		SpeedKillOrder order = orderService.getSpeedKillOrderByUserIdGoodsId(Long.valueOf(user.getNickname()), goodsId);
		if(order != null) {
			return;
		}
		//减库存 下订单 写入秒杀订单
		speedKillService.speedKill(user, goods);
	}


	@RabbitListener(queues=MQConfig.MIAOSHATEST)
	public void receiveMiaoShaMessage(Message message, Channel channel) throws IOException {
		log.info("接受到的消息为:{}",message);
		String messRegister = new String(message.getBody(), "UTF-8");
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
		SpeedKillMessageVo msm  = RedisService.stringToBean(messRegister, SpeedKillMessageVo.class);
		messageService.insertMs(msm);
		}
}
