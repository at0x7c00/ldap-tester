package com.novots.itsm.ldap.test;

import java.util.List;
import java.util.Map;

import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapContext;

public class LdapTest {

	public static void main(String[] args) {
		if(args.length==0 || ("help").equals(args[0])){
			printHelp();
			return;
		}
		if("pwd".equals(args[0])){
			String pwd = EncodeUtil.decode(args[1],10);
			System.out.println(pwd);
			return;
		}
		if("login".equals(args[0])){
			if(testLogin(args[1],args[2], args[3])){
				System.out.println("Login success!");
			}else{
				System.out.println("Login failed!");
			}
			return;
		}
		Param p = new Param(args);
		test(args[0], args[1], args[2], args[3],p.getFilter(),p.getAttrs(),p.getPageSize(),p.getScope());
	}
	
	private static void printHelp() {
		System.out.println("Useage:");
		System.out.println("\tpwd <encryped_pwd>");
		System.out.println("\tlogin <ldap_server:port> <user_id> <user_password>");
		System.out.println("\t<ldap_server:port> <user_id> <user_password> <base_ou> [-filter=<filter>] [-attrs=<attrs>] [-pageSize=<page_size>]");
	}

	public static void test(String url,String dn,String pwd,String baseOu,String filter,String[] attrs,int pageSize,int scope){
		System.out.println("pwd:"+pwd);
		LdapConnection conn = new LdapConnection(url,dn,pwd,36000);
		LdapUtil ldapUtil = new LdapUtil();
		List<Map<String,Object>> res =  ldapUtil.doQuery(conn, attrs, baseOu,scope, filter, pageSize);
		for(Map<String,Object> map : res){
			System.out.println(map);
		}
	}
	
	public static boolean testLogin(String url,String dn,String pwd){
		LdapConnection conn = new LdapConnection(url,dn,pwd,36000);
		LdapContext ctx = conn.getLdapContext();
		return ctx!=null;
	}
	
}
