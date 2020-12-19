package com.techprimers.security.jwtsecurity.util;
/*==========================================================================
 * Copyright (C) Wit2Cloud Co.,Ltd
 * All Rights Reserved.
 * Created By 开源学社
 ==========================================================================*/

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author John Goo
 * @version 1.0
 * @ClassName: PropUtil
 * @Desc: TODO
 * @history v1.0
 */
public class PropUtil {

    public static Properties properties;

    static {
        properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream in = PropUtil.class.getClassLoader().getResourceAsStream("wsecurity.properties");
        // 使用properties对象加载输入流
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key){
        //获取key对应的value值
        return properties.getProperty(key);
    }

    public static void main(String[] args) {
        String key = get("dev.jwt.token.key");
        System.out.println(key);
    }


}
