package com.cxy.speedkill.redis;

public interface KeyPrefix {

    public int expireSeconds() ;

    public String getPrefix() ;

}
