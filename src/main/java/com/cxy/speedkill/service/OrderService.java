package com.cxy.speedkill.service;

import com.cxy.speedkill.dao.OrderDao;
import com.cxy.speedkill.domain.OrderInfo;
import com.cxy.speedkill.domain.SpeedKillOrder;
import com.cxy.speedkill.domain.SpeedKillUser;
import com.cxy.speedkill.redis.OrderKey;
import com.cxy.speedkill.redis.RedisService;
import com.cxy.speedkill.utils.DateTimeUtils;
import com.cxy.speedkill.vo.GoodsVo;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.cxy.speedkill.common.ResultStatus.ORDER_NOT_EXIST;

/**
 * @Auther: cxy
 * @Date: 2019/8/4
 * @Description: 秒杀功能
 */
@Service
public class OrderService {
	
	@Autowired
	OrderDao orderDao;

	@Autowired
	private RedisService redisService ;
	
	public SpeedKillOrder getSpeedKillOrderByUserIdGoodsId(long userId, long goodsId) {
		return redisService.get(OrderKey.getMiaoshaOrderByUidGid,""+userId+"_"+goodsId,SpeedKillOrder.class);
	}

	public OrderInfo getOrderById(long orderId) {
		return orderDao.getOrderById(orderId);
	}

	@Transactional
	public OrderInfo createOrder(SpeedKillUser user, GoodsVo goods) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAddrId(0L);
		orderInfo.setGoodsCount(1);
		orderInfo.setGoodsId(goods.getId());
		orderInfo.setGoodsName(goods.getGoodsName());
		orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
		orderInfo.setOrderChannel(1);
		orderInfo.setStatus(0);
		orderInfo.setUserId(Long.valueOf(user.getNickname()));
		orderDao.insert(orderInfo);
		SpeedKillOrder miaoshaOrder = new SpeedKillOrder();
		miaoshaOrder.setGoodsId(goods.getId());
		miaoshaOrder.setOrderId(orderInfo.getId());
		miaoshaOrder.setUserId(Long.valueOf(user.getNickname()));
		orderDao.insertMiaoshaOrder(miaoshaOrder);
		redisService.set(OrderKey.getMiaoshaOrderByUidGid,""+user.getNickname()+"_"+goods.getId(),miaoshaOrder) ;
		return orderInfo;
	}

	public void closeOrder(int hour){
		Date closeDateTime = DateUtils.addHours(new Date(),-hour);
		List<OrderInfo> orderInfoList = orderDao.selectOrderStatusByCreateTime(Integer.valueOf(ORDER_NOT_EXIST.ordinal()), DateTimeUtils.dateToStr(closeDateTime));
		for (OrderInfo orderInfo:orderInfoList){
			System.out.println("orderinfo  infomation "+orderInfo.getGoodsName());
		}
	}

	
}
