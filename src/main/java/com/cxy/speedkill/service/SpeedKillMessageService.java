package com.cxy.speedkill.service;

import com.cxy.speedkill.dao.SpeedKillMessageDao;
import com.cxy.speedkill.domain.SpeedKillMessageInfo;
import com.cxy.speedkill.domain.SpeedKillMessageUser;
import com.cxy.speedkill.vo.SpeedKillMessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SpeedKillMessageService {

    @Autowired
    private SpeedKillMessageDao messageDao;

    public List<SpeedKillMessageInfo> getmessageUserList(Long userId , Integer status ){
        return messageDao.listMiaoShaMessageByUserId(userId,status);
    }


    @Transactional(rollbackFor = Exception.class)
    public void insertMs(SpeedKillMessageVo miaoShaMessageVo){
        SpeedKillMessageUser mu = new SpeedKillMessageUser() ;
        mu.setUserId(miaoShaMessageVo.getUserId());
        mu.setMessageId(miaoShaMessageVo.getMessageId());
        messageDao.insertMiaoShaMessageUser(mu);
        SpeedKillMessageInfo miaoshaMessage = new SpeedKillMessageInfo();
        miaoshaMessage.setContent(miaoShaMessageVo.getContent());
//        miaoshaMessage.setCreateTime(new Date());
        miaoshaMessage.setStatus(miaoShaMessageVo.getStatus());
        miaoshaMessage.setMessageType(miaoShaMessageVo.getMessageType());
        miaoshaMessage.setSendType(miaoShaMessageVo.getSendType());
        miaoshaMessage.setMessageId(miaoShaMessageVo.getMessageId());
        miaoshaMessage.setCreateTime(new Date());
        miaoshaMessage.setMessageHead(miaoShaMessageVo.getMessageHead());
        messageDao.insertMiaoShaMessage(miaoshaMessage);
    }
}
