# LDAP Tester

Click [here](https://github.com/xooxle/ldap-tester/raw/master/versions/v2.0/ldaptester.jar) to download.

Useage:
```bash
Useage:
	pwd <encryped_pwd>
	login <ldap_server:port> <user_id> <user_password>
	<ldap_server:port> <user_id> <user_password> <base_ou> [-filter=<filter>] [-attrs=<attrs>] [-pageSize=<page_size>] [-scope=[onelevel|object|sub]] [-find=<find>]
```

# Optional parameter default value
<b>filter</b>
default value : (&(objectClass=user)(objectCategory=person)(!(userAccountControl:1.2.840.113556.1.4.803:=2))(mail=*))

<b>pageSize</b>
default value: 100

<b>attrs</b>
default value:mail,userPrincipalName

<b>scope</b>
default value : subtree. Avaliable values:onelevel,object and subtree.

<b>find</b>
you can find by userPrincipalName with -find=your_account.

# Examples

## Find all AD users
```bash
java -jar ldaptester.jar ldap://17*.2*.2*.**:389 itsm-test pwd*** OU=***有限公司,OU=***,DC=***,DC=***,DC=*** -filter="(&(objectClass=user)(objectCategory=person)(!(
userAccountControl:1.2.840.113556.1.4.803:=2))(mail=*))" -pageSize=100 -atts=mail,userPrincipalName
```
You can use pipleline to save result to file system.



## Login test
```bash
java -jar login ldaptester.jar ldap://192.168.1.200:389 test changeit
```
If login success,while display:"Login success!". 



## find
since 2.0

you can find by userPrincipalName with -find=your_account.


