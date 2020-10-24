package com.techprimers.security.jwtsecurity.vo;
/*==========================================================================
 * Copyright (C) Wit2Cloud Co.,Ltd
 * All Rights Reserved.
 * Created By 开源学社
 ==========================================================================*/

import java.io.Serializable;

/**
 * @author John Goo
 * @version 1.0
 * @ClassName: Res
 * @Desc: TODO
 * @history v1.0
 */
public class Res implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer code;
    private Object data = "";
    private String message = "";

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
