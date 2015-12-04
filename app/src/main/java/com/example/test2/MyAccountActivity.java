package com.example.test2;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.content.Intent;
import android.content.IntentFilter;


public class MyAccountActivity extends Activity {

	private ListView contentListView;
	private ArrayAdapter<String> contentListAdapter;  

	private SlidingLayout slidingLayout;
	private ListView menuListView;
	private ArrayAdapter<String> menuListAdapter;  
	private String[] menuItems = {"Main Page","My account", "My voyages", "System setting","Find more" }; 
	//private String[] contentItems = { "Name:      Jie", "Sex:      M", "Age:      22",  
			//"City:      Paris", "Mail:      jie.chen@enst.fr"}; 
	
	//private String[] contentItems;
	//private String[] contents = { "Jie", "M", "22",  
					//"Paris", "Mail"}; 

	private Button menuButton;
	private Button edit;
	private Button logOut;
	
	private TextView name;
	private TextView sex;
	private TextView city;
	private TextView age;
	private TextView email;
	
	Intent intent;
	Intent intent2;
	
	private int user_pos;
	private String current_voyage;
	
	
	
	

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_myaccount);
		ActivityCollector.addActivity(this);
		
		
		Intent intent3 = getIntent();
		user_pos=intent3.getIntExtra("p_user", user_pos);
		current_voyage=intent3.getStringExtra("name_voyage");
		
		edit = (Button)findViewById(R.id.edit);
		logOut= (Button)findViewById(R.id.log_out);
			
	    intent = new Intent(MyAccountActivity.this,MyAccountEdit.class);
		intent2 = new Intent(MyAccountActivity.this,Sign.class);
		
		
		//change to edit page when press the button
		edit.setOnClickListener(new  OnClickListener(){  

	           @Override  
	           public void onClick(View arg0) { 
	        	   intent.putExtra("p_user", user_pos);
	               intent.putExtra("name_voyage", current_voyage);
	               MyAccountActivity.this.startActivity(intent); //Ìø×ªactivity 
	               MyAccountActivity.this.finish();   
	           }  
	        });
		
		//log out and change to log in page
		logOut.setOnClickListener(new  OnClickListener(){  
			   public void onClick(View arg0) { 
				   
				   DataBase.isLogin=false;
				   MyAccountActivity.this.startActivity(intent2); //Ìø×ªactivity   
	               MyAccountActivity.this.finish();   
	           }  
		  });
				
		//************ sliding menu*****************************//
				slidingLayout = (SlidingLayout) findViewById(R.id.slidingLayout);

				//************** Menu ListView **************************************
				menuListView = (ListView) findViewById(R.id.MenuList);  
				menuListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  
						menuItems);  
				menuListView.setAdapter(menuListAdapter);  

				menuListView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						//String menuitem = menuItems[position];
						//Log.d("menulist",String.valueOf(position));
						//Toast.makeText(MainActivity.this, menuitem,Toast.LENGTH_SHORT).show();
					switch (position){
					case 0: //main page
						Intent intent0 = new Intent(MyAccountActivity.this,MainActivity.class);
						intent0.putExtra("classname", "MyAccountActivity");
						intent0.putExtra("name_voyage", current_voyage);
						intent0.putExtra("p_user",user_pos);
						startActivity(intent0);
						
						break;
					case 1:  //my account
						Intent intent1 = new Intent(MyAccountActivity.this,MyAccountActivity.class);
						intent1.putExtra("classname", "MyAccountActivity");
						intent1.putExtra("name_voyage", current_voyage);
						intent1.putExtra("p_user",user_pos);
						startActivity(intent1);
						break;
					case 2: //my voyage
						Intent intent2 = new Intent(MyAccountActivity.this,MyVoyageActivity.class);
						intent2.putExtra("classname", "MyAccountActivity");
						intent2.putExtra("name_voyage", current_voyage);
						intent2.putExtra("p_user",user_pos);
						startActivity(intent2);
						break;
					case 3: //system setting
						Intent intent3 = new Intent(MyAccountActivity.this,SystemSettingActivity.class);
						intent3.putExtra("classname", "MyAccountActivity");
						intent3.putExtra("name_voyage", current_voyage);
						intent3.putExtra("p_user",user_pos);
						startActivity(intent3);
						break;
					case 4: // find more
						Intent intent4 = new Intent(MyAccountActivity.this,FindMoreActivity.class);
						intent4.putExtra("classname", "MyAccountActivity");
						intent4.putExtra("name_voyage", current_voyage);
						intent4.putExtra("p_user",user_pos);
						startActivity(intent4);
						break;
					}
					
					
					}
				});
				
				//**************** Content ListView ***********************************

				slidingLayout.setScrollEvent(menuListView);
			
				System.out.println(user_pos);
				
				name = (TextView) this.findViewById(R.id.username_edit);// search name and load it
		        name.setText(DataBase.list_user.get(user_pos).getname());  
		        
		        age = (TextView) this.findViewById(R.id.age_edit);// search age and load it
		        age.setText(DataBase.list_user.get(user_pos).getage());
		        
		        city = (TextView) this.findViewById(R.id.city_edit);
		        city.setText(DataBase.list_user.get(user_pos).getcity());
		        
		        sex = (TextView) this.findViewById(R.id.sex_edit);
		        sex.setText(DataBase.list_user.get(user_pos).getsex());
		        
		        email = (TextView) this.findViewById(R.id.email_edit);
		        email.setText(DataBase.list_user.get(user_pos).getemail());
		        
		        
		        
		        
				
				//***********************************************************************

				menuButton = (Button) findViewById(R.id.menuButton);
				menuButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Clicking on the menu on the left to achieve the layout display, click again to hide the left side of the layout of the function
						if (slidingLayout.isLeftLayoutVisible()) {
							slidingLayout.scrollToRightLayout();
						} else {
							slidingLayout.scrollToLeftLayout();
						}
					}
				});
				//*****************end sliding menu****************************************
		
	}

	
	
	
}
