package com.cxy.speedkill.service;

import com.cxy.speedkill.dao.DemoDao;
import com.cxy.speedkill.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: cxy
 * @Date: 2019/7/19
 * @Description:
 */
@Service
public class DemoService {

    @Autowired
    DemoDao demoDao;

    public User dbGet(int id){
        return demoDao.dbGet(id);
    }
}