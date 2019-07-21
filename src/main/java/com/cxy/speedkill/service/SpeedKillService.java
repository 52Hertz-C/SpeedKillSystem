package com.cxy.speedkill.service;

import com.cxy.speedkill.common.ResultStatus;
import com.cxy.speedkill.dao.SpeedKillDao;
import com.cxy.speedkill.domain.SpeedKillUser;
import com.cxy.speedkill.exception.GlobalException;
import com.cxy.speedkill.redis.RedisService;
import com.cxy.speedkill.redis.SpeedKillUserKey;
import com.cxy.speedkill.utils.MD5Util;
import com.cxy.speedkill.utils.UUIDUtil;
import com.cxy.speedkill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: cxy
 * @Date: 2019/7/19
 * @Description:
 */
@Service
public class SpeedKillService {

    private static final String COOKIE_NAME_TOKEN = "taken";

    @Autowired
    SpeedKillDao speedKillDao;

    @Autowired
    RedisService redisService;

    public SpeedKillUser getPassword(long mobile){
        SpeedKillUser speedKillUser = speedKillDao.getPassword(mobile);
        return speedKillUser;
    }

    public Boolean login(HttpServletResponse response,LoginVo loginVo){
        if(loginVo==null){
            throw new GlobalException(ResultStatus.SYSTEM_ERROR);
        }
        String password = loginVo.getPassword();
        String mobile = loginVo.getMobile();

        //判断手机号码是否存在
        SpeedKillUser speedKillUser = getPassword(Long.parseLong(mobile));
        if(speedKillUser==null){
            throw new GlobalException(ResultStatus.MOBILE_NOT_EXIST);
        }

        //校验密码
        String dbPass = speedKillUser.getPassword();
        String dbSalt = speedKillUser.getSalt();

        String calcPass = MD5Util.formPassToDBPass(password,dbSalt);
        if(!calcPass.equals(dbPass)){
            throw new GlobalException(ResultStatus.PASSWORD_ERROR);
        }

        //生成cookie 将session返回游览器 分布式session
        String token= UUIDUtil.uuid();
        addCookie(response, token, speedKillUser);
        return true;
    }

    private void addCookie(HttpServletResponse response, String token, SpeedKillUser user) {
        redisService.set(SpeedKillUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        //设置有效期
        cookie.setMaxAge(SpeedKillUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}