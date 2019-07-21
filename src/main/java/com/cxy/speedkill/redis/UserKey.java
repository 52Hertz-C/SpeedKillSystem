package com.cxy.speedkill.redis;

/**
 * @Auther: cxy
 * @Date: 2019/7/19
 * @Description:
 */

public class UserKey extends BasePrefix {

    private UserKey(String prefix) {
        super( prefix);
    }

    public static UserKey getById = new UserKey("id") ;

    public static UserKey getByName = new UserKey("name") ;

}