package com.cxy.speedkill.access;

import com.cxy.speedkill.domain.SpeedKillUser;

public class UserContext {
	
	private static ThreadLocal<SpeedKillUser> userHolder = new ThreadLocal<SpeedKillUser>();
	
	public static void setUser(SpeedKillUser user) {
		userHolder.set(user);
	}
	
	public static SpeedKillUser getUser() {
		return userHolder.get();
	}

	public static void removeUser() {
		userHolder.remove();
	}

}
