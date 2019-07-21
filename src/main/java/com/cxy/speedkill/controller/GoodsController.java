package com.cxy.speedkill.controller;

import com.cxy.speedkill.domain.SpeedKillUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: cxy
 * @Date: 2019/7/20
 * @Description: 商品
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @RequestMapping("/toGoods")
    public String toGoods(Model model, SpeedKillUser speedKillUser){
        model.addAttribute("user",speedKillUser);
        return "goods/goods_list";
    }
}