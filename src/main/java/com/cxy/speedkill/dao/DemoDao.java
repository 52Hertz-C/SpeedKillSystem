package com.cxy.speedkill.dao;

import com.cxy.speedkill.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Auther: cxy
 * @Date: 2019/7/19
 * @Description:
 */
@Mapper
public interface DemoDao {

    @Select("select * from user where id=#{id}")
    public abstract User dbGet(@Param("id") int id);
}