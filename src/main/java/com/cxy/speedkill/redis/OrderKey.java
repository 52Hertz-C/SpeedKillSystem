package com.cxy.speedkill.redis;

/**
 * @Auther: cxy
 * @Date: 2019/7/19
 * @Description:
 */
public class OrderKey extends BasePrefix {

    public OrderKey( String prefix) {
        super( prefix);
    }


    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey("moug");
}