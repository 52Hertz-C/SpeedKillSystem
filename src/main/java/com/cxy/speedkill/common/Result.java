package com.cxy.speedkill.common;

/**
 * @Auther: cxy
 * @Date: 2019/7/19
 * @Description:
 */
public class Result<T> {
    String code;
    String desc;
    T user;

    protected Result(T value) {
        this.code = "0000";
        this.desc = "Success";
        this.user = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public T getUser() {
        return user;
    }

    public void setUser(T user) {
        this.user = user;
    }

    public static <T> Result<T> success(T value){

        return new Result<T>(value);
    }
}