package com.cxy.speedkill.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息中心用戶存储关系表
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SpeedKillMessageUser implements Serializable {

    private Long id ;

    private Long userId ;

    private Long messageId ;

    private String goodId ;

    private Date orderId;

}
