package com.ainian.wxapp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * MD5加密工具类
 * @author happyqing
 */
public class MD5Utils {

    private static final Log log = LogFactory.getLog(MD5Utils.class);

    /**
     * 字符串的MD5
     * @param
     * @return
     */
    public static String encode(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
            //System.out.println("result: " + buf.toString());// 32位的加密
            //System.out.println("result: " + buf.toString().substring(8, 24));// 16位的加密

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 文件的MD5
     * @param filePath
     * @return
     */
    public static String getFileMD5(String filePath){
        String value = null;
        FileInputStream in = null;
        try {
            File file = new File(filePath);
            in = new FileInputStream(file);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int c;
            while ((c = in.read(buffer)) != -1) {
                md5.update(buffer, 0, c);
            }
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16).toUpperCase();
        } catch (Exception e) {
            log.error(e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e);
                }
            }
        }
        return value;
    }

    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
    /**
     * 微信支付签名算法sign
     * @param parameters 参数集合
     * @return 返回md5签名
     */
    @SuppressWarnings("unchecked")
    public static String createWxPaySign(String signKey,SortedMap<String,String> parameters) {
        StringBuilder sb = new StringBuilder();       // 多线程访问的情况下需要用StringBuffer
        Set es = parameters.keySet();                 // 所有参与传参的key按照accsii排序（升序）
        for (Object set : es) {
            String k = set.toString();
            Object v = parameters.get(k);
            sb.append(k)
                    .append("=")
                    .append(v.toString())
                    .append("&");
        }
        sb.append("key=")
                .append(signKey);
        return str2MD5(sb.toString(), "utf-8").toUpperCase();
    }

    /** MD5加密
     * @param data 要加密的数据
     * @param encode 加密的编码
     * @return md5字符串
     */
    public static String str2MD5(String data, String encode) {
        String resultString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (encode == null || "".equals(encode))
                resultString = byteArrayToHexString(md.digest(data
                        .getBytes()));
            else {
                resultString = byteArrayToHexString(md.digest(data
                        .getBytes(encode)));
            }
        } catch (Exception exception) {
        }
        return resultString;
    }

    /** byte数组转换16进制字符串
     * @param b 要转换的byte数组
     * @return 16进制字符串
     */
    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    /** byte转换成16进制字符串
     * @param b 要转换的byte
     * @return byte对应的16进制字符串
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /** map集合参数转换为xml格式
     * @param dataMap 要转换的map对象
     * @return XML格式的字符串
     */
    private String map2XML(SortedMap<Object, Object> dataMap)
    {
        synchronized (MD5Utils.class)                             // 不是多线程访问可以不用加锁
        {
            StringBuilder strBuilder = new StringBuilder();
            Set<Object> objSet = dataMap.keySet();
            strBuilder.append("<xml>");
            for (Object key : objSet)
            {
                if (key == null)
                {
                    continue;
                }
                Object value = dataMap.get(key);
                strBuilder.append("<")
                        .append(key.toString())
                        .append(">")
                        .append(value)
                        .append("</")
                        .append(key.toString())
                        .append(">\n");
            }
            strBuilder.append("</xml>");
            return strBuilder.toString();
        }
    }

    /** 生成随机数
     * @param count 要生成的随机数位数
     * @return 随机数字符串
     */
    private String createNonceStr(int count){
        String[] nums = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        int maxIndex = nums.length - 1;
        int numIndex;
        StringBuilder builder = new StringBuilder(count);
        for (int i = 0; i < count; i++){
            numIndex = (int)(Math.random() * maxIndex);
            builder.append(nums[numIndex]);
        }
        return builder.toString();
    }



}
