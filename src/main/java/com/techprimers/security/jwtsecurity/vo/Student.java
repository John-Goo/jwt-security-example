package com.techprimers.security.jwtsecurity.vo;
/*==========================================================================
 * Copyright (C) Wit2Cloud Co.,Ltd
 * All Rights Reserved.
 * Created By 开源学社
 ==========================================================================*/

import com.sun.corba.se.pept.transport.ContactInfo;
import lombok.Data;

import javax.annotation.sql.DataSourceDefinition;

/**
 * @author John Goo
 * @version 1.0
 * @ClassName: Student
 * @Desc: TODO
 * @history v1.0
 */

@Data
public class Student {
    private String idCard;
    private String name;
    private int age;
    private String addr;

    @Override
    public String toString() {
        return "Student{" +
                "idCard='" + idCard + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", addr='" + addr + '\'' +
                '}';
    }
}
