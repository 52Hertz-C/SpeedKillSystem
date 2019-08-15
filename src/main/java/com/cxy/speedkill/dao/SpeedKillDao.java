package com.cxy.speedkill.dao;

import com.cxy.speedkill.domain.SpeedKillUser;
import org.apache.ibatis.annotations.*;

/**
 * @Auther: cxy
 * @Date: 2019/7/19
 * @Description: 登陆
 */
@Mapper
public interface SpeedKillDao {

    @Select("select * from miaosha_user where nickname = #{nickname}")
    public SpeedKillUser getByNickname(@Param("nickname") String nickname ) ;

    @Select("select * from miaosha_user where id = #{id}")
    public SpeedKillUser getPassword(@Param("id") long id ) ;

    @Update("update miaosha_user set password = #{password} where id = #{id}")
    public void update(SpeedKillUser toBeUpdate);

    @Insert("insert into miaosha_user (id, nickname, password, salt, head, register_date, last_login_date) value (#{id},#{nickname},#{password},#{salt},#{head},#{registerDate},#{lastLoginDate}) ")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public void insertMiaoShaUser(SpeedKillUser speedKillUser);
}
