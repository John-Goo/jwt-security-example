package com.techprimers.security.jwtsecurity.config;
/*==========================================================================
 * Copyright (C) Wit2Cloud Co.,Ltd
 * All Rights Reserved.
 * Created By 开源学社
 ==========================================================================*/

import com.techprimers.security.jwtsecurity.util.WSyncDataHelper;

/**
 * @author John Goo
 * @version 1.0
 * @ClassName: WSyncDataConfigAbstract
 * @Desc: TODO
 * @history v1.0
 */
public abstract class WSyncDataConfigAbstract {

    public WSyncDataConfigAbstract(){
        WSyncDataHelper.setWConfig(this);
    }

    public abstract String pdUrl();
    public abstract String token();

}
