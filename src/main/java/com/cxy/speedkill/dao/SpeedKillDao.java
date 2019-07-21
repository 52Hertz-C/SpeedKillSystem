package com.cxy.speedkill.dao;

import com.cxy.speedkill.domain.SpeedKillUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Auther: cxy
 * @Date: 2019/7/19
 * @Description: 登陆
 */
@Mapper
public interface SpeedKillDao {

    @Select("select * from miaosha_user where id=#{id}")
    public SpeedKillUser getPassword(@Param("id")long id);
}
