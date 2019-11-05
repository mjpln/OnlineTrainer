package com.knowology.km.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.security.MessageDigest;
import java.sql.Clob;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * Project: lottery
 * @description: com.jjtx.common.util.StringUtil.java
 * @company: jjtx
 * @author：<a href="mailto:wenic705@sina.com.cn">villion</a>
 * @date: Oct 12, 2009
 * 字符处理工具类
 */
public class StringUtil {
    //~ Static fields/initializers =============================================

    /** The <code>Log</code> instance for this class. */
    private static Log log = LogFactory.getLog(StringUtil.class);

    //~ Methods ================================================================

    /**
     * Encode a string using algorithm specified in web.xml and return the
     * resulting encrypted password. If exception, the plain credentials
     * string is returned
     *
     * @param password Password or other credentials to use in authenticating
     *        this username
     * @param algorithm Algorithm used to do the digest
     *
     * @return encypted password based on the algorithm.
     */
    public static String encodePassword(String password, String algorithm) {
        byte[] unencodedPassword = password.getBytes();

        MessageDigest md = null;

        try {
            // first create an instance, given the provider
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
            log.error("Exception: " + e);

            return password;
        }

        md.reset();

        // call the update method one or more times
        // (useful when you don't know the size of your data, eg. stream)
        md.update(unencodedPassword);

        // now calculate the hash
        byte[] encodedPassword = md.digest();

        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < encodedPassword.length; i++) {
            if (((int) encodedPassword[i] & 0xff) < 0x10) {
                buf.append("0");
            }

            buf.append(Long.toString((int) encodedPassword[i] & 0xff, 16));
        }

        return buf.toString();
    }

    /**
     * Encode a string using Base64 encoding. Used when storing passwords
     * as cookies.
     *
     * This is weak encoding in that anyone can use the decodeString
     * routine to reverse the encoding.
     *
     * @param str
     * @return String
     * @throws IOException
     */
    public static String encodeString(String str)  {
    	
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        String encodedStr = new String(encoder.encodeBuffer(str.getBytes()));

        return (encodedStr.trim());
    }

    /**
     * Decode a string using Base64 encoding.
     *
     * @param str
     * @return String
     * @throws IOException
     */
    public static String decodeString(String str) throws IOException {
        sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
        String value = new String(dec.decodeBuffer(str));

        return (value);
    }
    /**
     * MD16.
     *
     * @param password
     * @param algorithm
     * @return String
     */
    public static String MD16(String password) {
    	if (password==null) password="";
        byte[] unencodedPassword = password.getBytes();

        MessageDigest md = null;

        try {
            // first create an instance, given the provider
            md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            log.error("Exception: " + e);

            return password;
        }

        md.reset();

        // call the update method one or more times
        // (useful when you don't know the size of your data, eg. stream)
        md.update(unencodedPassword);

        // now calculate the hash
        byte[] encodedPassword = md.digest();
        int i;  
        StringBuffer buf = new StringBuffer("");  
        for (int offset = 0; offset < encodedPassword.length; offset++){  
                i = encodedPassword[offset];  
                if (i < 0)  
                i += 256;  
                if (i < 16)  
                buf.append("0");  
                buf.append(Integer.toHexString(i));  
        }
        if (log.isDebugEnabled()){
        	log.debug("md buf="+buf.toString());
        }
       return buf.toString().substring(8, 24);
    }
    /**
     * 产生验证码.
     * @param size 随机数长度
     * @param conn 数据库连接
     * @return String 指定长度随机字符串
     */
    public static String getRandomString(int size){
     char[] c={'1','2','3','4','5','6','7','8','9','0'};
          Random random = new Random();  //初始化随机数产生器
          StringBuffer sb =new StringBuffer();
          for(int i=0;i<size;i++){
          sb.append(c[Math.abs(random.nextInt()) %c.length]);
          }
          return sb.toString();
    }
    /**
     * 检查字符串有值，既不为空，又不是空字串
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
    	if (log.isDebugEnabled()){
    		//log.debug("判断字符串============"+str);
    	}
    	if (str==null){
    		return true;
    	}
    	return "".equals(str.trim());
    }
    /**
     * 字符转码
     * @param str
     * @param fromCode
     * @param toCode
     * @return
     * @throws Exception
     */
    public String convertUnicode(String str,String fromCode,String toCode) throws Exception{
		return new String(str.getBytes(fromCode), toCode);
    }
    
    /**
     * 随即字串
     */
    private static Random randGen=null;
    private static char[] numbersAndLetters="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static Object sychLock=new Object();
    private static String kmkey = "MyEclipseKnowoLogy";
    /**
     * 随机字串生成器字符
     * @param length
     * @return
     */
    public static String getRandomLetterString(int length){
    	String ret=null;
    	if (length>0){
    		if (randGen==null){
				randGen=new Random();
			}
    		synchronized(sychLock){
    			StringBuffer randBuffer=new StringBuffer();
    			for (int i=0;i<length;i++){
    				randBuffer.append(numbersAndLetters[randGen.nextInt(numbersAndLetters.length)]);
    			}
    			ret=randBuffer.toString();
    		}
    	}
    	return ret;
    }
    /**
     * 随机字串生成器有符号
     * @param length
     * @return
     */
    public static String getRandomLetterChar(int length){
    	String ret=null;
    	if (length>0){
    		if (randGen==null){
				randGen=new Random();
			}
    		synchronized(sychLock){
    			char[] randBuffer=new char[length];
    			for (int i=0;i<length;i++){
    				randBuffer[i]=numbersAndLetters[randGen.nextInt(numbersAndLetters.length)];
    			}
    			ret=randBuffer.toString();
    		}
    	}
    	return ret;
    }
    /**
     * 
     * 方法名称： jiamiUserKey
     * 内容摘要：
     * 修改者名字	修改日期
     * 修改说明
     * @author kk 2013-5-22
     * @param account
     * @return String  
     * @throws
     *
     */
    public static String jiamiUserKey(String account){
    	String userkey = account+kmkey;
    	userkey = encodePassword(userkey,"MD5");
    	return userkey;
    	
    }
    public static void main(String[] args) {
		String str = "12345";
		try {
			System.out.println(encodeString(str));
			System.out.println(decodeString("MTExMTE="));
			System.out.println(encodePassword("6f298988829036f1136935ca8188728a", "MD5"));
			System.out.println(jiamiUserKey("12345"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    /***
     *  
     * @param clob
     * @return string
     * @throws Exception
     */ 
    static public String ClobToString(Clob clob) throws Exception {
        String reString = "";
        Reader is = clob.getCharacterStream();// 得到流
        BufferedReader br = new BufferedReader(is);
        String s = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (s != null) {// 执行循环将字符串全部取出,并赋值给StringBuffer由StringBuffer转成String
            sb.append(s);
            s = br.readLine();
        }        
        reString = sb.toString();
        return reString;
    }
}
