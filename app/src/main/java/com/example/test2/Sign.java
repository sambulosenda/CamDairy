package com.example.test2;

import java.util.ArrayList;
import java.util.List;





import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Sign extends Activity{
	
	private Button signIn;
	private Button logIn;
	private Intent intent;
	
	private CheckBox autoLogin;  
	private SharedPreferences sharedPreferences;
	
	private EditText username;
	private EditText userpass;
	
	private String name;
	private String password;
	
	int i = 1;
	int j;
	boolean get = false;
	
	private List<Account> list = new ArrayList<Account>();
	private Account account = new Account();
	
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        ActivityCollector.addActivity(this);        
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        sharedPreferences = this.getSharedPreferences("userInfo",Context.MODE_WORLD_READABLE);
        if (sharedPreferences.getBoolean("AUTO_ISCHECK", false)) {
           Intent intent2 = new Intent();
           intent2.setClass(Sign.this, NeworMemory.class);
           startActivity(intent2);
        } else {
           setContentView(R.layout.sign);
           initView();
        }
	}  
	
	public void initView() {
        
        DataBase.user user0 = new DataBase.user();
        user0.setname("jia");
        user0.setpassword("123");
        
        DataBase.list_user.add(user0);
        
        logIn = (Button)findViewById(R.id.login_button);
        signIn = (Button)findViewById(R.id.signin_button); 
        
        intent = new Intent(Sign.this, SignIn.class);
       
        buttonListener();
        
        }  
	
	// listener for buttons
	public void buttonListener() {
			
		 	
		logIn.setOnClickListener(new OnClickListener() {

			    @Override
				public void onClick(View v) {
					username=(EditText)findViewById(R.id.username_edit);
	                userpass=(EditText)findViewById(R.id.password_edit);
	                
	                autoLogin = (CheckBox) findViewById(R.id.autologin);
					name = username.getText().toString();
					password = userpass.getText().toString();
					
					//if username and password are both right, user log in successfully 
					for(j=0;j<DataBase.list_user.size();j++){
						if (name.equals(DataBase.list_user.get(j).getname())) {
							get = true;
							if(password.equals(DataBase.list_user.get(j).getpassword())){
								Editor editor = sharedPreferences.edit();
							    editor.putString("userName", name);
							    if (autoLogin.isChecked()) {
							    //log in automatically 
							    editor.putString("password", password);
								editor.putBoolean("AUTO_ISCHECK", true).commit();
								}	
							    editor.commit();
						        Intent intent = new Intent();
							    intent.putExtra("name", name);
							    intent.putExtra("password", password);
							    
							    if(DataBase.list_user.get(j).isOn==false){
							    	intent.setClass(Sign.this, NeworMemory.class);
							    }
							    else{
							    	int p=findUser(name);
							    	int p_voyage=DataBase.list_user.get(p).voyage_list.size()-1;
							    	String str=DataBase.list_user.get(p).voyage_list.get(p_voyage).name;
							    	intent.putExtra("pos_user", p);
							    	intent.putExtra("name_voyage", str);
							    	intent.setClass(Sign.this, MainActivity.class);
							    }
							    DataBase.isLogin=true;
							    startActivity(intent);// change to new or memory page
							    Sign.this.finish();
						    } 
						//if not, show a warning
					    else{
							Toast.makeText(Sign.this, "Name or Password is not right, please try again.",
									Toast.LENGTH_LONG).show();
						}
					}
				    
					}
					
					if(get==false)
					Toast.makeText(Sign.this, "User does not exit, please sign in! ", Toast.LENGTH_LONG).show();
				}
			});
		
		signIn.setOnClickListener(new  OnClickListener(){  

	           @Override  
	           public void onClick(View arg0) {  
	               // TODO Auto-generated method stub
	        	   i=0;
	               Sign.this.startActivity(intent); //change to sign in page
	               Sign.this.finish();   
	           }  
	        });
			
		}
	
	int findUser(String n){
		int usr_pos=-1;
		for(int i=0;i<DataBase.list_user.size();i++){
			System.out.println("into loop="+i);
			if(DataBase.list_user.get(i).getname().equals(n)){
				usr_pos=i;
			}
			
		}
		return usr_pos;
	}
	
}
