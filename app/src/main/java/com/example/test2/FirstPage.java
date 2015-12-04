package com.example.test2;




import android.util.Log;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;


public class FirstPage extends Activity{
	
	boolean isFirstIn = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firstpage);
		ActivityCollector.addActivity(this);
		
		SharedPreferences preferences = getSharedPreferences("first_pref",
			    MODE_PRIVATE);
	    isFirstIn = preferences.getBoolean("isFirstIn", true);
			//first page to show our logo, stay for 2 seconds
	        new Handler().postDelayed(new Runnable() {
			    @Override
			    public void run() {
			     Intent intent = new Intent();
			     if (isFirstIn) {
			      // if it is the first time to open the app, start guide pages
			      intent = new Intent(FirstPage.this, Welcome.class);
			     } else {
			      // if not, change to log in page
			      intent = new Intent(FirstPage.this, Sign.class);
			     }
			     FirstPage.this.startActivity(intent);
			     FirstPage.this.finish();
			    }
			   }, 2000);
		
	}
}