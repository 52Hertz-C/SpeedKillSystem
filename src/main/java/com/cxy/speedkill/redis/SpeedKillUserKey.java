package com.cxy.speedkill.redis;

public class SpeedKillUserKey extends  BasePrefix{
    public static final int TOKEN_EXPIRE = 3600 *24*2;
    public static SpeedKillUserKey token = new SpeedKillUserKey(TOKEN_EXPIRE,"tk") ;
    public static SpeedKillUserKey getByNickName = new SpeedKillUserKey(0, "nickName");

    public SpeedKillUserKey(int expireSeconds , String prefix) {
        super(expireSeconds,prefix);
    }
}
