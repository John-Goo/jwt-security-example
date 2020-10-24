package com.techprimers.security.jwtsecurity.security;


import org.apache.commons.codec.binary.Hex;
import java.security.MessageDigest;
import java.util.Random;

/**
 * @author John Goo
 * @version 1.0
 * @ClassName: MD5Util
 * @Desc: TODO
 * @history v1.0
 */
public class MD5Util {


    public static String salt(){
        Random r = new Random();
        StringBuilder buff = new StringBuilder(16);
        buff.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = buff.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                // 填充补位
                buff.append("&");
            }
        }
        return buff.toString();
    }




    /**
     * 生成含有随机盐的密码
     */
    public static String md5HexWithSalt(String srcStr) {
        String salt = salt();
        srcStr = generateMD5Hex(srcStr + salt);
        System.out.println(">>加盐字符串："+srcStr);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = srcStr.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = srcStr.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }

    public static String md5Hex(String srcStr){
        return generateMD5Hex(srcStr);
    }


    public static String[] extractSalt(String md5SaltStr){
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5SaltStr.charAt(i);
            cs1[i / 3 * 2 + 1] = md5SaltStr.charAt(i + 2);
            cs2[i / 3] = md5SaltStr.charAt(i + 1);
        }
        // [0]=salt,[1]=md5str
        String[] _md5Salt = new String[]{new String(cs1),new String(cs2)};
        return _md5Salt;
    }


    /**
     * 获取十六进制字符串形式的MD5摘要
     */
    public static String generateMD5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            return null;
        }
    }
    public static void main(String[] args) {
        // 加密+加盐
        String password1 = md5HexWithSalt("admin");
        System.out.println("结果：" + password1 + "   长度："+ password1.length());
        // 解码
        System.out.println("============================");
        String[] destStr = extractSalt(password1);
        System.out.println("{salt}-->"+destStr[1]);
        System.out.println("{md5str}-->"+destStr[0]);

    }
}


