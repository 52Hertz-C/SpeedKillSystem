package com.cxy.speedkill.controller;

import com.cxy.speedkill.common.Result;
import com.cxy.speedkill.common.ResultGeekQ;
import com.cxy.speedkill.common.ResultStatus;
import com.cxy.speedkill.domain.SpeedKillUser;
import com.cxy.speedkill.service.SpeedKillService;
import com.cxy.speedkill.utils.ValidatorUtil;
import com.cxy.speedkill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: cxy
 * @Date: 2019/7/19
 * @Description: 登陆
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    SpeedKillService speedKillService;

    @RequestMapping("/toLogin")
    public String toLogin(LoginVo loginVo, Model model){
//        logger.info(loginVo.toString());
//        //未完成
//        RedisLua.vistorCount(COUNTLOGIN);
//        String count = RedisLua.getVistorCount(COUNTLOGIN).toString();
//        logger.info("访问网站的次数为:{}",count);
//        model.addAttribute("count",count);
        model.addAttribute("count",1);
        return "login/index";
    }

    @RequestMapping("/doLogin")
    @ResponseBody
    public ResultGeekQ<Boolean> doLogin(HttpServletResponse response, LoginVo loginVo){
        logger.info(loginVo.toString());

        Boolean result = speedKillService.login(response,loginVo);
        return ResultGeekQ.build("登陆成功");
    }


}