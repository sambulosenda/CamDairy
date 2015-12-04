package com.example.test2;

import java.io.Serializable;

public class Account implements Serializable{
	private String email;
	private String name;
	private String password;
	
	public void SerializableUser() {  
    }  
	
    public void SerializableUser(String name, String password, String email) {  
        this.name = name;  
        this.password = password;
        this.email=email;
    }  

	public String getemail(){
		return email;
	}
	
	public String getname(){
		return name;
	}
	
	public String getpassword(){
		return password;
	}
	
	public void setemail(String e){
		email=e;
	}
	
	public void setname(String e){
		name = e;
	}
	
	public void setpassword(String e){
		password = e;
	}
}


