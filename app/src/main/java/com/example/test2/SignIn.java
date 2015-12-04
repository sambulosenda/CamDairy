package com.example.test2;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;

public class SignIn extends Activity{

	private Button signIn;
	private Intent intent;
	
	private DataBase.user account = new DataBase.user();
	
	private EditText useremail;
	private EditText username;
	private EditText userpass;
	
	private String email;
	private String name;
	private String password;
	
	int i;
	
	
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.sign_in);  
        ActivityCollector.addActivity(this);
        
        signIn = (Button)findViewById(R.id.signin_button);  
        
        intent = new Intent(SignIn.this, Sign.class);
        
        signIn.setOnClickListener(new  OnClickListener(){  

           @Override  
           public void onClick(View arg0) {  
        	   useremail=(EditText)findViewById(R.id.email_edit);
       	   username=(EditText)findViewById(R.id.username_edit);
               userpass=(EditText)findViewById(R.id.password_edit);
			   
               email = useremail.getText().toString();
               name = username.getText().toString();
			   password = userpass.getText().toString();
			   
			   DataBase.user user0 = new DataBase.user();
		       user0.setname("haha");
		       user0.setpassword("123");
		        
		       DataBase.list_user.add(user0);
				
			   for(i=0;i<DataBase.list_user.size();i++){
					
			   if (name.equals(DataBase.list_user.get(i).getname())) {
					Toast.makeText(SignIn.this, "This name is already used.",
							Toast.LENGTH_LONG).show();
					break;
					} 
			     }
		       
			   //if username and email are both new to database, sign in successfully 
			   if(i==DataBase.list_user.size()){
				   account.setname(name);
				   account.setemail(email);
				   account.setpassword(password);
				   
				   DataBase.list_user.add(account);
				   
	               SignIn.this.startActivity(intent); //change to log in page 
	               SignIn.this.finish();
			   }
			      
           }  
          
        });
	}
}
