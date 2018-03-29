package com.novots.itsm.ldap.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;

public class LdapUtil {

	public List<Map<String,Object>> doQuery(LdapConnection conn,String[] attributes,
			String name,int scope,String filter,int pageSize
			){
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		LdapContext ctx = conn.getLdapContext();
		if(ctx==null){
			return result;
		}
		SearchControls control = new SearchControls();
		control.setSearchScope(scope);
		control.setTimeLimit(conn.getReadTimeout());
		control.setReturningAttributes(attributes);
		
		try {
			ctx.setRequestControls(new Control[] { new PagedResultsControl(pageSize, false) });
		} catch (NamingException | IOException e1) {
			e1.printStackTrace();
		}
		
		byte[] cookie = null;
		try {
			do {
				//System.out.println("name=" + name+",filter=" + filter + ",control=" + control);
				NamingEnumeration<SearchResult> results = ctx.search(name, filter, control);
				String y = "";
				for(String att : attributes){
					y += att + ",";
				}
				System.out.println("LDAP search params:scope=" + scope+",timeLimit=" + control.getTimeLimit() + ",attributes:" + y + ",pageSize=" + pageSize +",");
				System.out.println("filter="+ filter+",name=" + name);
				int count = 0;
				while (results != null && results.hasMoreElements()) {
					SearchResult entry = (SearchResult) results.next();
					processResult(result,attributes,entry.getAttributes(),entry.getNameInNamespace());
				}
				Control[] controls = ctx.getResponseControls();
				if (controls != null) {
					for (int i = 0; i < controls.length; i++) {
						if (controls[i] instanceof PagedResultsResponseControl) {
							PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
							cookie = prrc.getCookie();
						}
					}
				} else {
					System.out.println("No controls were sent from the server");
					cookie = null;
				}
				ctx.setRequestControls(new Control[] { new PagedResultsControl(pageSize, cookie, Control.CRITICAL) });
				
			} while (cookie!=null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private void processResult(List<Map<String,Object>> result,String[] attributes,Attributes atts,String namespace){
		Map<String,Object> map = new HashMap<String,Object>();
		for(String attribute : attributes){
			map.put(attribute,atts.get(attribute));
		}
		map.put("_namespace", namespace);
		result.add(map);
	}
	
	
	private List<String> queryMemberOfGroup(LdapConnection conn,String name,int scope,String filter,int pageSize){
		List<String> members = new ArrayList<String>();
		List<Map<String,Object>> queryRes = doQuery(conn, new String[]{"member"}, name, scope, filter, pageSize);
		for(Map<String, Object> map : queryRes){
			for(Map.Entry<String, Object> entry : map.entrySet()){
				Attribute att =  (Attribute) entry.getValue();
				for(int i = 0;i<att.size();i++){
					try {
						members.add(att.get(i).toString());
					} catch (NamingException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return members;
	}
	
	
	public List<Map<String, Object>> query(LdapConnection conn,String name,String[] attributes,int scope,String filter,int pageSize){
		if(name.toLowerCase().startsWith("cn=")){
			List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
			for(String member : queryMemberOfGroup(conn,name,scope,"(objectClass=group)",pageSize)){
				res.addAll(doQuery(conn, attributes, member, scope, filter, pageSize));
 			}
			return res;
		}else{
			return  doQuery(conn,attributes,name,scope,filter,pageSize);
		}
	}
	
}
