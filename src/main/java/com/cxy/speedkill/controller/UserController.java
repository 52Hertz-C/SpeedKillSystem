package com.cxy.speedkill.controller;

import com.cxy.speedkill.common.Result;
import com.cxy.speedkill.domain.SpeedKillUser;
import com.cxy.speedkill.redis.GoodsKey;
import com.cxy.speedkill.redis.RedisService;
import com.cxy.speedkill.service.GoodsService;
import com.cxy.speedkill.service.SpeedKillService;
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
 * @Description: 用户
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    SpeedKillService speedKillService;


    @RequestMapping("/info")
    @ResponseBody
    public Result<SpeedKillUser> info(Model model, SpeedKillUser speedKillUser){
        return Result.success(speedKillUser);
    }
}