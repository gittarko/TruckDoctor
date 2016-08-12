package com.juma.truckdoctor.js.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dong.he on 2016/8/5.
 */

public class EncryptUtils {

    /**
     * MD5加密
     * @param data 待加密字符串
     * @return
     */
    public static String getMD5(String data) {
        return getMD5(data.getBytes());
    }

    /**
     * MD5加密
     * @param data 待加密的字节数组
     * @return MD5密文
     */
    public static String getMD5(byte[] data) {
        return bytes2Hex(encryptMD5(data));
    }

    public static byte[] encryptMD5(byte[] data) {
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            return md.digest();
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * 一个byte转2个hex字符
     * @param src
     * @return
     */
    public static String bytes2Hex(byte[] src) {
        char[] res = new char[src.length * 2];
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        for(int i=0,j=0; i<src.length; i++) {
            res[j++] = hexDigits[src[i] >>> 4 & 0x0f];     //取高位 <= 15
            res[j++] = hexDigits[src[i] & 0x0f];           //取低位 <= 15
        }
        return new String(res);
    }
}
