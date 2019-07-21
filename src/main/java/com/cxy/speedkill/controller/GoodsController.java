package com.cxy.speedkill.controller;

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
    public String toGoods(Model model){
        return "goods/goods_list";
    }
}