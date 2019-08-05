package com.cxy.speedkill.controller;

import com.cxy.speedkill.domain.SpeedKillUser;
import com.cxy.speedkill.redis.GoodsKey;
import com.cxy.speedkill.redis.RedisService;
import com.cxy.speedkill.service.GoodsService;
import com.cxy.speedkill.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Auther: cxy
 * @Date: 2019/7/20
 * @Description: 商品
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;


    @Autowired
    ThymeleafViewResolver viewResolver;

    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping("/toGoods")
    public String toGoods(Model model, SpeedKillUser speedKillUser){
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsList);
        model.addAttribute("user",speedKillUser);
        return "goods/goods_list";
    }

    @RequestMapping(value="/toGoodsDetails/{goodsId}")
//    @ResponseBody
    public String toGoodsDetails(HttpServletRequest request, HttpServletResponse response, Model model, SpeedKillUser speedKillUser,
                          @PathVariable("goodsId")long goodsId) {
        model.addAttribute("user", speedKillUser);

        //取缓存
//        String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
////        if(!StringUtils.isEmpty(html)) {
////            return html;
////        }
        //手动渲染
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods/goods_detail";

//        SpringWebContext ctx = new SpringWebContext(request,response,
//                request.getServletContext(),request.getLocale(), model.asMap(), applicationContext );
//        html = viewResolver.getTemplateEngine().process("goods/goods_detail", ctx);
//        if(!StringUtils.isEmpty(html)) {
//            redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
//        }
//        return html;
    }
}