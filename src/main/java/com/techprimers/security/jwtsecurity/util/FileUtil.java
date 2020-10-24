package com.techprimers.security.jwtsecurity.util;
/*==========================================================================
 * Copyright (C) Wit2Cloud Co.,Ltd
 * All Rights Reserved.
 * Created By 开源学社
 ==========================================================================*/

import com.alibaba.fastjson.JSON;
import com.techprimers.security.jwtsecurity.model.JwtUser;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * @author John Goo
 * @version 1.0
 * @ClassName: FileUtil
 * @Desc: TODO
 * @history v1.0
 */
public class FileUtil {



    public static String generateUUIDfileName(String originFileName){
        if(originFileName ==null){
            throw new RuntimeException("{FileUtil-->createNewFileName(String originFileName)}==>UUID文件名称异常,源文件名称不能为空！");
        }
        String uuid = UUID.randomUUID().toString().replace("-","");
        originFileName = originFileName.replace(".","~~");
        String[] array = originFileName.split("~~");
        int len = array.length;
        String fileType = null;
        if(len <= 1){
            fileType = "";
        }else{
            fileType = "."+array[len-1];
        }
        return uuid+fileType;
    }


    public static String generateUUIDfileName(FileTypeEnum fileType){
        if(fileType ==null){
            throw new RuntimeException("{FileUtil-->createNewFileName(String originFileName)}==>UUID文件名称异常,文件类型不能为null！");
        }
        StringBuffer buff = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-","");
        buff.append(uuid);
        if(!FileTypeEnum.EMPTY.equals(fileType)){
            buff.append(".");
            buff.append(fileType.toString().toLowerCase());
        }
        return buff.toString();
    }
    public static String extractFileName(String filePath){
        if(filePath ==null){
            throw new RuntimeException("{FileUtil-->extractFileName(String filePath)}==>从文件路径filePath不能为null！");
        }
        int from = filePath.lastIndexOf("/")+1;
        String fileName = filePath.substring(from);
        return fileName;
    }

    public static final String _HTTP_CONTENT_TYPE = "text/html;charset=utf-8";
    public static void print(HttpServletResponse response,Object data){
        response.setContentType(_HTTP_CONTENT_TYPE);
        PrintWriter out = null;
        try {

            String value = JSON.toJSONString(data);
            out = response.getWriter();
            out.write(value);
            // 强制将缓冲区中的数据发送出去,不必等到缓冲区满
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException("系统发生错误！");
        } finally {
            if (out != null) {
                out.close();
            }
        }

    }

    public static void main(String[] args) {
        //String fileName = generateUUIDfileName(FileTypeEnum.DOC);
        //System.out.println(fileName);
        //String fileName = generateUUIDfileName("test.png");
       // System.out.println(fileName);
        //
        //String filePath = "10086/activity/part/292777a804e04d07b59628b00f84c974.png";
        String filePath = "292777a804e04d07b59628b00f84c974.png";
        int from = filePath.lastIndexOf("/")+1;
        String fileName = filePath.substring(from);

        System.out.println(fileName);







    }
}
