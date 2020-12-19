package com.techprimers.security.jwtsecurity.security;
/*==========================================================================
 * Copyright (C) Wit2Cloud Co.,Ltd
 * All Rights Reserved.
 * Created By 开源学社
 ==========================================================================*/

import net.coobird.thumbnailator.Thumbnails;

import java.io.*;

/**
 * @author John Goo
 * @version 1.0
 * @ClassName: GoogleFileExpress
 * @Desc: TODO
 * @history v1.0
 */
public class GoogleFileExpress {


    public static void main(String[] args) throws IOException {
        Thumbnails.of("F:\\demo\\p1.jpg")
                .scale(0.5)
                .toFile("F:\\demo\\p1-0.8.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
       // baos = (ByteArrayOutputStream) out;
        Thumbnails.of("F:\\demo\\p1.jpg")
                .scale(0.5)
                .toOutputStream(baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        int len = is.available();
        FileOutputStream fos = new FileOutputStream("F:\\demo\\b.jpg");
        byte[] b = new byte[1024];

        int length;
        while((length= is.read(b))>0){
            fos.write(b,0,length);
        }
        is.close();
        fos.close();

    }
}
