package com.techprimers.security.jwtsecurity.config;
/*==========================================================================
 * Copyright (C) Wit2Cloud Co.,Ltd
 * All Rights Reserved.
 * Created By 开源学社
 ==========================================================================*/

import com.techprimers.security.jwtsecurity.util.WSyncDataHelper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author John Goo
 * @version 1.0
 * @ClassName: WConfig
 * @Desc: TODO
 * @history v1.0
 */
@Configuration
@Data
public class WSyncDataConfig extends WSyncDataConfigAbstract{

    @Value("${sync.pdUrl}")
    private String pdUrl;
    @Value("${sync.token}")
    private String token;

    @Override
    public String pdUrl() {
        return this.pdUrl;
    }

    @Override
    public String token() {
        return this.token;
    }

}
