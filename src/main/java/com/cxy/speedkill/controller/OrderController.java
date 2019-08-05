package com.cxy.speedkill.controller;

import com.cxy.speedkill.common.ResultGeekQ;
import com.cxy.speedkill.domain.OrderInfo;
import com.cxy.speedkill.domain.SpeedKillUser;
import com.cxy.speedkill.redis.RedisService;
import com.cxy.speedkill.service.GoodsService;
import com.cxy.speedkill.service.OrderService;
import com.cxy.speedkill.service.SpeedKillUserService;
import com.cxy.speedkill.vo.GoodsVo;
import com.cxy.speedkill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.cxy.speedkill.common.ResultStatus.ORDER_NOT_EXIST;
import static com.cxy.speedkill.common.ResultStatus.SESSION_ERROR;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	SpeedKillUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	GoodsService goodsService;
	
    @RequestMapping("/detail")
    @ResponseBody
    public ResultGeekQ<OrderDetailVo> info(Model model, SpeedKillUser user,
										   @RequestParam("orderId") long orderId) {
		ResultGeekQ<OrderDetailVo> result = ResultGeekQ.build();
		if (user == null) {
			result.withError(SESSION_ERROR.getCode(), SESSION_ERROR.getMessage());
			return result;
		}
    	OrderInfo order = orderService.getOrderById(orderId);
    	if(order == null) {
			result.withError(ORDER_NOT_EXIST.getCode(), ORDER_NOT_EXIST.getMessage());
			return result;
    	}
    	long goodsId = order.getGoodsId();
    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	OrderDetailVo vo = new OrderDetailVo();
    	vo.setOrder(order);
    	vo.setGoods(goods);
    	result.setData(vo);
    	return result;
    }
    
}
