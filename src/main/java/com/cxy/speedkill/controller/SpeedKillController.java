package com.cxy.speedkill.controller;

import com.cxy.speedkill.common.ResultGeekQ;
import com.cxy.speedkill.common.ResultStatus;
import com.cxy.speedkill.domain.OrderInfo;
import com.cxy.speedkill.domain.SpeedKillOrder;
import com.cxy.speedkill.domain.SpeedKillUser;
import com.cxy.speedkill.service.GoodsService;
import com.cxy.speedkill.service.OrderService;
import com.cxy.speedkill.service.SpeedKillService;
import com.cxy.speedkill.service.SpeedKillUserService;
import com.cxy.speedkill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: cxy
 * @Date: 2019/8/4
 * @Description: 秒杀功能
 */
@Controller
@RequestMapping("speedKill")
public class SpeedKillController {
    private static Logger logger = LoggerFactory.getLogger(SpeedKillController.class);

    @Autowired
    SpeedKillUserService speedKillUserService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SpeedKillService speedKillService;

    /**
     * QPS:1306
     * 5000 * 10
     * get　post get 幂等　从服务端获取数据　不会产生影响　　post 对服务端产生变化
     */
    @RequestMapping("/doSpeedKill")
    public String doSpeedKill(Model model, SpeedKillUser user, @RequestParam("goodsId")long goodsId) {
        ResultGeekQ<Integer> result = ResultGeekQ.build();

        if (user == null) {
            return "login/login";
        }

        //判断库存
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stockCount = goodsVo.getStockCount();
        if(stockCount<=0){
            model.addAttribute("errMsg",ResultStatus.MIAO_SHA_OVER.getMessage());
            return "speedKill/speedKill_fail";
        }

        //判断是否重复秒杀
        SpeedKillOrder speedKillOrder = orderService.getSpeedKillOrderByUserIdGoodsId(user.getId(),goodsId);

        //减库存、下订单、写入秒杀订单
        OrderInfo orderInfo = speedKillService.speedKill(user,goodsVo);
        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",goodsVo);
        return "order/order_detail";

    }

}