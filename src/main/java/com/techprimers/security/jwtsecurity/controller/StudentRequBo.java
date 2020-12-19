package com.techprimers.security.jwtsecurity.controller;
/*==========================================================================
 * Copyright (C) Wit2Cloud Co.,Ltd
 * All Rights Reserved.
 * Created By 开源学社
 ==========================================================================*/

/**
 * @author John Goo
 * @version 1.0
 * @ClassName: ReadRequestBo
 * @Desc: TODO
 * @history v1.0
 */
public class StudentRequBo {
    private String name;
    private String idCard;
    private String addr;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @Override
    public String toString() {
        return "StudentRequBo{" +
                "name='" + name + '\'' +
                ", idCard='" + idCard + '\'' +
                ", addr='" + addr + '\'' +
                '}';
    }
}
