package com.example.test2;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;



public class MyVoyageActivity extends Activity {
	
	private ListView contentListView;  
	private ArrayAdapter<String> contentListAdapter;  

	private SlidingLayout slidingLayout;
	private ListView menuListView;
	private ArrayAdapter<String> menuListAdapter;  
	private String[] menuItems = {"Main Page","My account", "My voyages", "System setting","Find more" }; 
	private String[] contentItems = { "Voyage 1", "Voyage 2", "Voyage 3",  
			"Voyage 4", "Voyage 5"}; 

	private Button menuButton;
	private int pos_user;
	private String current_voyage;

	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_myvoyage);
		ActivityCollector.addActivity(this);
		
		Intent intentpos = getIntent();
		String class_name=intentpos.getStringExtra("classname");
		if(class_name.equals("NeworMemory")){
			current_voyage="";
			
		}
		else{
			current_voyage=intentpos.getStringExtra("voyage_name");
		}
		pos_user= intentpos.getIntExtra("p_user",-1);
		
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
				Intent intent0 = new Intent(MyVoyageActivity.this,MainActivity.class);
				intent0.putExtra("classname", "MyVoyageActivity");
				intent0.putExtra("name_voyage", current_voyage);
				intent0.putExtra("p_user",pos_user);
				startActivity(intent0);
				
				break;
			case 1:  //my account
				Intent intent1 = new Intent(MyVoyageActivity.this,MyAccountActivity.class);
				intent1.putExtra("classname", "MyVoyageActivity");
				intent1.putExtra("name_voyage", current_voyage);
				intent1.putExtra("p_user",pos_user);
				startActivity(intent1);
				break;
			case 2: //my voyage
				Intent intent2 = new Intent(MyVoyageActivity.this,MyVoyageActivity.class);
				intent2.putExtra("classname", "MyVoyageActivity");
				intent2.putExtra("name_voyage", current_voyage);
				intent2.putExtra("p_user",pos_user);
				startActivity(intent2);
				break;
			case 3: //system setting
				Intent intent3 = new Intent(MyVoyageActivity.this,SystemSettingActivity.class);
				intent3.putExtra("classname", "MyVoyageActivity");
				intent3.putExtra("name_voyage", current_voyage);
				intent3.putExtra("p_user",pos_user);
				startActivity(intent3);
				break;
			case 4: // find more
				Intent intent4 = new Intent(MyVoyageActivity.this,FindMoreActivity.class);
				intent4.putExtra("classname", "MyVoyageActivity");
				intent4.putExtra("name_voyage", current_voyage);
				intent4.putExtra("p_user",pos_user);
				startActivity(intent4);
				break;
			}
			
			
			}
		});
		
		//**************** Content ListView ***********************************
		
		/**
		 * the list will show all the photos that we take or choose from the album and the note as well
		 * the diary will also be shown on the list
		 *  
		 * if the voyage is empty or there is no voyage, one picture will shown on the list
		 */
		contentListView = (ListView) findViewById(R.id.contentList);  
		
		//List<DataBase.Voyage> local_list = DataBase.list_user.get(0).voyage_list;

		List<String> all_voyage_name = new ArrayList<String>();
		String nam= new String();
		Log.d("before loop","aaaaaaaa");
		Log.d("try", String.valueOf(DataBase.list_user.size()));
		for (int i=0;i<DataBase.list_user.get(pos_user).voyage_list.size();i++){
			Log.d("enter loop","aaaaaaaa");
			nam = DataBase.list_user.get(pos_user).voyage_list.get(i).name;
			if (DataBase.list_user.get(pos_user).voyage_list.get(i).status==true){
				nam = nam +"   (On the way...)";
			}
			else{
				nam = nam +"   (Finished)";
			}
			
			all_voyage_name.add(nam);
		}
		
		contentListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  
				all_voyage_name);  
		contentListAdapter.notifyDataSetChanged();
		contentListView.setAdapter(contentListAdapter);  
		
		contentListView.setOnItemClickListener(new OnItemClickListener() {  
	        @Override  
	        public void onItemClick(AdapterView<?> adapterView, View view, int position,  
	            long id) {  
	        	
	        	
					Intent intent = new Intent(MyVoyageActivity.this,Voyage1Activity.class);
					intent.putExtra("voyage_pos", position);
					intent.putExtra("p_user",pos_user);
					startActivity(intent);
					
				}
	        }  
	    );  
		slidingLayout.setScrollEvent(contentListView);  
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
