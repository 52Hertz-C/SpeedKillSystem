package com.cxy.speedkill.controller;

import com.cxy.speedkill.domain.User;
import com.cxy.speedkill.redis.RedisService;
import com.cxy.speedkill.redis.UserKey;
import com.cxy.speedkill.common.Result;
import com.cxy.speedkill.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auther: cxy
 * @Date: 2019/7/19
 * @Description:
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    DemoService demoService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","cxy");
        return "hello";
    }

    @RequestMapping("/dbGet")
    @ResponseBody
    public Result<User> dbGet(){
        User user = demoService.dbGet(1);
        return Result.success(user);
    }

    @RequestMapping("/redisGet")
    @ResponseBody
    public Result<String> redisGet(){
        String v1 = redisService.get(UserKey.getById,"1",String.class);
        return Result.success(v1);
    }

    @RequestMapping("/redisSet")
    @ResponseBody
    public Result<Boolean> redisSet(){
        User user = new User();
        user.setId(1);
        user.setName("1111");
        redisService.set(UserKey.getById,""+1,user);
        return Result.success(true);
    }

}