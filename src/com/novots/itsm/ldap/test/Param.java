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
	
	public Param(String[] args){
		for(String arg : args){
			if(!arg.contains("=")){
				continue;
			}
			String[] kv = arg.split("=");
			String k = kv[0];
			if(k.startsWith("-")){
				k = k.substring(1);
			}
			map.put(kv[0], kv[1]);
		}
		baseou = map.get("ou");
		filter = map.get("filter");
		attrs = map.get("attrs");
		pageSize = map.get("pageSize");
		String s  = map.get("scope");
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
}
