package com.novots.itsm.ldap.test;

import java.util.HashMap;
import java.util.Map;

import javax.naming.directory.SearchControls;

public class Param {

	Map<String,String> map = new HashMap<String,String>();
	private String baseou;
	private String filter;
	private String attrs;
	private String pageSize;
	private Integer scope;
	private String find;
	
	public Param(String[] args){
		int i = 4;
		for(;i < args.length;i++){
			String arg = args[i];
			System.out.println("arg:" + arg);
			if(!arg.contains("=")){
				continue;
			}
			int x = arg.indexOf("=");
			String k = arg.substring(0,x);
			String v = arg.substring(x+1);
			if(k.startsWith("-")){
				k = k.substring(1);
			}
			map.put(k,v);
		}
		System.out.println(map);
		baseou = map.get("ou");
		filter = map.get("filter");
		attrs = map.get("attrs");
		pageSize = map.get("pageSize");
		find = map.get("find");
		String s  = map.get("scope");
		if(s==null){
			s = "subtree";
		}
		if(s.equals("onelevel")){
			scope = SearchControls.ONELEVEL_SCOPE;
		}else if(s.equals("object")){
			scope = SearchControls.OBJECT_SCOPE;
		}else{
			scope = SearchControls.SUBTREE_SCOPE;
		}
	}
	
	public String getOu() {
		return baseou;
	}
	public String getFilter() {
		if(isEmpty(filter)){
			filter = "(&(objectClass=user)(objectCategory=person)(!(userAccountControl:1.2.840.113556.1.4.803:=2))(mail=*))";
		}
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String[] getAttrs() {
		if(isEmpty(attrs)){
			attrs = "mail,userPrincipalName";
		}
		return attrs.split(",");
	}
	public Integer getPageSize() {
		int res = 100;
		try{
			if(!isEmpty(pageSize)){
				res = Integer.parseInt(pageSize);
			}
		}catch(Exception e){}
		return res;
	}
	
	public boolean isEmpty(String str){
		return str==null || str.trim().equals("");
	}

	public Integer getScope() {
		return scope;
	}

	public String getFind() {
		return find;
	}

}
