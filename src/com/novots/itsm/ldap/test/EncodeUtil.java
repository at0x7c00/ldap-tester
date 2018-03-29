package com.novots.itsm.ldap.test;


public class EncodeUtil {

	/**
	 * 加密
	 * @param str 待加密字符
	 * @param salt 混淆值
	 * @return
	 */
	public static String encode(String str,int salt){
		byte[] bytes = str.getBytes();
		byte[] resbyte = new byte[bytes.length];
		int i  = 0;
		for(int b : bytes){
			b = b^salt;
			resbyte[i++]=(byte)b;
		}
		return new String(resbyte);
	}
	
	/**
	 * 解密
	 * @param str 待解密字符
	 * @param salt 混淆值，和加密时的值一直
	 * @return
	 */
	public static String decode(String str,int salt){
		byte[] bytes = str.getBytes();
		byte[] resbyte = new byte[bytes.length];
		int i  = 0;
		for(int b : bytes){
			b = b^salt;
			resbyte[i++]=(byte)b;
		}
		return new String(resbyte);
	}
	
}
