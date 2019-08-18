package com.cxy.speedkill.service;

import com.cxy.speedkill.common.MessageStatus;
import com.cxy.speedkill.common.ResultStatus;
import com.cxy.speedkill.common.SnowflakeIdWorker;
import com.cxy.speedkill.dao.SpeedKillDao;
import com.cxy.speedkill.domain.SpeedKillUser;
import com.cxy.speedkill.exception.GlobalException;
import com.cxy.speedkill.rabbitmq.MQSender;
import com.cxy.speedkill.redis.RedisService;
import com.cxy.speedkill.redis.SpeedKillKey;
import com.cxy.speedkill.redis.SpeedKillUserKey;
import com.cxy.speedkill.utils.MD5Util;
import com.cxy.speedkill.utils.UUIDUtil;
import com.cxy.speedkill.vo.LoginVo;
import com.cxy.speedkill.vo.SpeedKillMessageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Random;

/**
 * @Auther: cxy
 * @Date: 2019/7/19
 * @Description: 用户登陆服务
 */
@Service
public class SpeedKillUserService {
    private static final Logger logger = LoggerFactory.getLogger(SpeedKillUserService.class);

    public static final String COOKIE_NAME_TOKEN = "taken";

    @Autowired
    SpeedKillDao speedKillDao;

    @Autowired
    RedisService redisService;

    @Autowired
    private MQSender sender ;

    /**
     * 根据手机号获取密码
     * @param mobile
     * @return
     */
    public SpeedKillUser getPassword(long mobile){
        SpeedKillUser speedKillUser = speedKillDao.getPassword(mobile);
        return speedKillUser;
    }

    /**
     * 登陆
     * @param response
     * @param loginVo
     * @return
     */
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

    /**
     * 获取token
     * @param response
     * @param token
     * @return
     */
    public SpeedKillUser getToken(HttpServletResponse response, String token){
        if(StringUtils.isEmpty(token)){
            return null;
        }
        SpeedKillUser speedKillUser = redisService.get(SpeedKillUserKey.token,token,SpeedKillUser.class);

        //延长有效期
        if(speedKillUser!=null){
            addCookie(response,token,speedKillUser);
        }

        return speedKillUser;
    }

    /**
     * 生成验证码
     * @return
     */
    public BufferedImage createVerifyCodeRegister() {
        int width = 100;
        int height = 40;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(SpeedKillKey.getMiaoshaVerifyCodeRegister,"regitser",rnd);
        //输出图片
        return image;
    }

    /**
     * 对脚本语言处理
     * @param exp
     * @return
     */
    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            Integer catch1 = (Integer)engine.eval(exp);
            return catch1.intValue();
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * + - *
     */
    private static char[] ops = new char[] {'+', '-', '*'};

    /**
     * 获取验证码内容
     * @param rdm
     * @return
     */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    /**
     * 注册时用的验证码
     * @param verifyCode
     * @return
     */
    public boolean checkVerifyCodeRegister(int verifyCode) {
        Integer codeOld = redisService.get(SpeedKillKey.getMiaoshaVerifyCodeRegister,"regitser", Integer.class);
        if(codeOld == null || codeOld != verifyCode ) {
            return false;
        }
        redisService.delete(SpeedKillKey.getMiaoshaVerifyCode, "regitser");
        return true;
    }

    /**
     * 注册
     * @param response
     * @param mobile
     * @param passWord
     * @param salt
     * @return
     */
    public boolean register(HttpServletResponse response , String mobile , String passWord , String salt) {
        SpeedKillUser speedKillUser =  new SpeedKillUser();
        speedKillUser.setId(Long.parseLong(mobile));
        String DBPassWord =  MD5Util.formPassToDBPass(passWord, salt);
        speedKillUser.setPassword(DBPassWord);
        speedKillUser.setRegisterDate(new Date());
        speedKillUser.setSalt(salt);
        speedKillUser.setNickname(mobile);
        try {
            speedKillDao.insertMiaoShaUser(speedKillUser);
            SpeedKillUser user = speedKillDao.getByNickname(speedKillUser.getNickname());
            if(user == null){
                return false;
            }

//            SpeedKillMessageVo vo = new SpeedKillMessageVo();
//            vo.setContent("尊敬的用户你好，你已经成功注册！");
//            vo.setCreateTime(new Date());
//            vo.setMessageId(SnowflakeIdWorker.getOrderId(0,0));
//            vo.setSendType(0);
//            vo.setStatus(0);
//            vo.setMessageType(MessageStatus.messageType.system_message.ordinal());
//            vo.setUserId(speedKillUser.getId());
//            vo.setMessageHead(MessageStatus.ContentEnum.system_message_register_head.getMessage());
//            sender.sendRegisterMessage(vo);

            //生成cookie 将session返回游览器 分布式session
            String token= UUIDUtil.uuid();
            addCookie(response, token, user);
        } catch (Exception e) {
            logger.error("注册失败",e);
            return false;
        }
        return true;
    }
}