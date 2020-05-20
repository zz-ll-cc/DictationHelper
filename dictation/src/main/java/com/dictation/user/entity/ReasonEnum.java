package com.dictation.user.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum ReasonEnum {

    QD(0,"签到",5),
    DT(1,"有效听写",5),
    STM(2,"学习10分钟",1),
    SHH(3,"学习30分钟",3),
    SAH(4,"学习60分钟",5),
    BQ(5,"补签",-2);

    private Integer code;

    private String reason;

    private Integer creditNum;


}
