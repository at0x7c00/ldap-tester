package com.novots.itsm.ldap.test;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class LdapConnection {
	
	public static int DEFAULT_READ_TIME_OUT= 30*1000;//30秒
	private String url;//连接LDAP服务器的url，例如：ldap://192.168.3.129:389
	private String user;//连接LDAP服务器的user，对应于DN，例如：cn=Administrator,cn=Users,dc=test,dc=com
	private String password;//连接LDAP服务器的密码
	private Control[] control;
	private int readTimeout;
	
	
	/**
	 * @param url eg: ldap://101.0.0.1:389
	 * @param user DN
	 * @param password 域账号密码
	 * @param readTimeout 超时毫秒数，默认可以使用LdapConnection.DEFAULT_READ_TIME_OUT,在LDAP进行数据同步的时候建议使用同步周期
	 */
	public LdapConnection(String url,String user,String password,int readTimeout){
		this.url = url;
		this.user = user;
		this.password = password;
		this.readTimeout = readTimeout;
	}
	//连接LDAP服务器，成功后返回LdapContext对象
	public LdapContext getLdapContext(){
		Hashtable<String,String> hashtable = new Hashtable<String,String>();
		hashtable.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		hashtable.put(Context.PROVIDER_URL, url);//URL
		hashtable.put(Context.SECURITY_AUTHENTICATION, "simple");
		
		hashtable.put(Context.SECURITY_PRINCIPAL, user);//用户名
		hashtable.put(Context.SECURITY_CREDENTIALS, password);//密码
		
		//20秒连接超时
		hashtable.put("com.sun.jndi.ldap.connect.timeout", String.valueOf(20*1000));
		
		LdapContext ctx = null;
		try {
			ctx = new InitialLdapContext(hashtable,null);
		} catch (NamingException e) {
			e.printStackTrace();
			System.out.println("域服务器同步时连接异常，请检查域服务器连接URL，绑定识别名和密码是否正确");
		}
		return ctx;
	}
	//域用户登录验证
	public boolean  isValid(){
		LdapContext ctx = getLdapContext();
		return null!=ctx;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	
}