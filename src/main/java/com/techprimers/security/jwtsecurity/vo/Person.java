package com.techprimers.security.jwtsecurity.vo;
/*==========================================================================
 * Copyright (C) Wit2Cloud Co.,Ltd
 * All Rights Reserved.
 * Created By 开源学社
 ==========================================================================*/

import lombok.Data;

/**
 * @author John Goo
 * @version 1.0
 * @ClassName: Person
 * @Desc: TODO
 * @history v1.0
 */
@Data
public class Person {

    private String idCard;
    private String name;
    private int age;
    private String addr;

    public Person() {
    }

    public Person(String idCard, String name, int age, String addr) {
        this.idCard = idCard;
        this.name = name;
        this.age = age;
        this.addr = addr;
    }

    @Override
    public String toString() {
        return "Person{" +
                "idCard='" + idCard + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", addr='" + addr + '\'' +
                '}';
    }
}
