package com.cxy.speedkill.service;

import com.cxy.speedkill.dao.GoodsDao;
import com.cxy.speedkill.domain.SpeedKillGoods;
import com.cxy.speedkill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: cxy
 * @Date: 2019/7/21
 * @Description:
 */
@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;


    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public boolean reduceStock(GoodsVo goods) {
        SpeedKillGoods speedKillGoods = new SpeedKillGoods();
        speedKillGoods.setGoodsId(goods.getId());
        int ret = goodsDao.reduceStock(speedKillGoods);
        return ret > 0;
    }

}