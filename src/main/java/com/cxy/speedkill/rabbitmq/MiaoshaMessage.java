package com.cxy.speedkill.rabbitmq;

import com.cxy.speedkill.domain.SpeedKillUser;

public class MiaoshaMessage {
	private SpeedKillUser user;
	private long goodsId;
	public SpeedKillUser getUser() {
		return user;
	}
	public void setUser(SpeedKillUser user) {
		this.user = user;
	}
	public long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}
}
