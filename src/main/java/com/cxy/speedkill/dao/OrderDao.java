package com.cxy.speedkill.dao;

import com.cxy.speedkill.domain.OrderInfo;
import com.cxy.speedkill.domain.SpeedKillOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Auther: cxy
 * @Date: 2019/8/4
 * @Description: 秒杀功能
 */
@Mapper
public interface OrderDao {
	
	@Select("select * from miaosha_order where user_id=#{user_id} and goods_id=#{goodsId}")
	public SpeedKillOrder getSpeedKillOrderByUserIdGoodsId(@Param("user_id") long userId, @Param("goodsId") long goodsId);

	@Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
			+ "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
	@SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
	public long insert(OrderInfo orderInfo);
	
	@Insert("insert into miaosha_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
	public int insertMiaoshaOrder(SpeedKillOrder miaoshaOrder);

	@Select("select * from order_info where id = #{orderId}")
	public OrderInfo getOrderById(@Param("orderId") long orderId);

	@Select("select * from order_info where status=#{status} and create_Date<=#{createDate}")
	public List<OrderInfo> selectOrderStatusByCreateTime(@Param("status") Integer status, @Param("createDate") String createDate);

	@Select("update order_info set status=0 where id=#{id}")
	public int closeOrderByOrderInfo();
}
