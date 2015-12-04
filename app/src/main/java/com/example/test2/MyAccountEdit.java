package com.example.test2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MyAccountEdit extends Activity{
	private ListView contentListView;
	private ArrayAdapter<String> contentListAdapter;  

	private SlidingLayout slidingLayout;
	private ListView menuListView;
	private ArrayAdapter<String> menuListAdapter;  
	private String[] menuItems = {"Main Page","My account", "My voyages", "System setting","Find more" }; 
	private String[] contentItems = { "Name:      Jie", "Sex:      M", "Age:      22",  
			"City:      Paris", "Mail:      jie.chen@enst.fr"}; 
	
	//private String[] contentItems;
	//private String[] contents = { "Jie", "M", "22",  
					//"Paris", "Mail"}; 

	private Button menuButton;
	private Button save;

	
	private TextView name;
	private TextView sex;
	private TextView city;
	private TextView age;
	private TextView email;
	
	private EditText editName;
	private EditText editAge;
	private EditText editCity;
	private EditText editSex;
	private EditText editEmail;
	
	private String name2;
	private String age2;
	private String city2;
	private String sex2;
	private String email2;
	
	private Intent intent;
	
	private int user_pos;
	private String current_voyage;

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_myaccountedit);
		ActivityCollector.addActivity(this);
		
		Intent intent3 = getIntent();
		user_pos=intent3.getIntExtra("p_user", -1);
		current_voyage=intent3.getStringExtra("name_voyage");
		
		//DataBase.user user1 = new DataBase.user();
		//user1.setname("xiao");
		
		//DataBase.list_user.add(user1);

        save = (Button)findViewById(R.id.save);

		
		intent = new Intent(MyAccountEdit.this,MyAccountActivity.class);
		
		
		//save all changes and change to myaccount pâge when press the button
		save.setOnClickListener(new  OnClickListener(){  

	           //change personal informations
			   @Override  
	           public void onClick(View arg0) { 
	        	   editAge=(EditText)findViewById(R.id.age_edit);
	        	   age2 = editAge.getText().toString();
	        	   
	        	   editCity=(EditText)findViewById(R.id.city_edit);
	        	   city2 = editCity.getText().toString();
	        	   
	        	   editSex=(EditText)findViewById(R.id.sex_edit);
	        	   sex2 = editSex.getText().toString();
	        	   
	        	   editEmail=(EditText)findViewById(R.id.email_edit);
	        	   email2 = editEmail.getText().toString();
	        	   
	        	   DataBase.list_user.get(user_pos).setage(age2);
	        	   DataBase.list_user.get(user_pos).setcity(city2);
	        	   DataBase.list_user.get(user_pos).setsex(sex2);
	        	   DataBase.list_user.get(user_pos).setemail(email2);
	        	   
	        	   
	        	   intent.putExtra("p_user",user_pos);
	        	   intent.putExtra("name_voyage", current_voyage);
	               MyAccountEdit.this.startActivity(intent); //跳转activity   
	               MyAccountEdit.this.finish();   
	           }  
		  });
	
				
				//**************** Content ListView ***********************************
				/*contentListView = (ListView) findViewById(R.id.mycontent);  
				SimpleAdapter adapter; 
				ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
				
				Map<String, Object> map= new HashMap<String, Object>();  
				
				map.put("image", R.drawable.head); //放入图标资源  
				map.put("name", "haha"); //放入计数器  
				arrayList.add(map);
				
				adapter = new SimpleAdapter(this,arrayList, R.layout.myaccount,  
						new String[] {"image", map.get("name")},  
						new int[]{R.id.head, R.id.username_edit}); 
				
				contentListView.setAdapter(adapter);*/
				
				
				//contentListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  
						//contentItems);  
				//contentListView.setAdapter(contentListAdapter);  

				
			
				
				name = (TextView) this.findViewById(R.id.username_edit);
		        name.setText(DataBase.list_user.get(user_pos).getname());  
		        
		        age = (TextView) this.findViewById(R.id.age_edit);  
		        age.setText(DataBase.list_user.get(user_pos).getage());
		        
		        city = (TextView) this.findViewById(R.id.city_edit);
		        city.setText(DataBase.list_user.get(user_pos).getcity());
		        
		        sex = (TextView) this.findViewById(R.id.sex_edit);
		        sex.setText(DataBase.list_user.get(user_pos).getsex());
		        
		        email = (TextView) this.findViewById(R.id.email_edit);
		        email.setText(DataBase.list_user.get(user_pos).getemail());
		        
		        
		        
		        
				
				//***********************************************************************

				menuButton = (Button) findViewById(R.id.menuButton_myaccount);
				menuButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent1 = new Intent(MyAccountEdit.this,MainActivity.class);
						
						intent1.putExtra("name_voyage", current_voyage);
						intent1.putExtra("p_user",user_pos);
						startActivity(intent1);
					}
				});
				//*****************end sliding menu****************************************
		
	}

	
	
	
}



