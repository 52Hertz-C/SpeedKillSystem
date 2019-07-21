package com.cxy.speedkill.redis;

public class SpeedKillKey extends BasePrefix{

	private SpeedKillKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static SpeedKillKey isGoodsOver = new SpeedKillKey(0, "go");
	public static SpeedKillKey getMiaoshaPath = new SpeedKillKey(60, "mp");
	public static SpeedKillKey getMiaoshaVerifyCode = new SpeedKillKey(300, "vc");
	public static SpeedKillKey getMiaoshaVerifyCodeRegister = new SpeedKillKey(300, "register");

}
