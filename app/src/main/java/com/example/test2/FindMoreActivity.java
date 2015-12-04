package com.example.test2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;



public class FindMoreActivity extends Activity {
	
	private ListView contentListView;  
	private ArrayAdapter<String> contentListAdapter;  

	private SlidingLayout slidingLayout;
	private ListView menuListView;
	private ArrayAdapter<String> menuListAdapter;  
	private String[] menuItems = {"Main Page","My account", "My voyages", "System setting","Find more" }; 
	private String[] contentItems = { "Suisse", "Paysbas", "Espagne",  
			"Allmagne", "Itali"}; 

	private Button menuButton;
	private int user_pos;
	private String current_voyage;
	

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_findmore);
		ActivityCollector.addActivity(this);
		
		Intent intent = getIntent();
		user_pos=intent.getIntExtra("p_user", user_pos);
		current_voyage=intent.getStringExtra("name_voyage");
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
				Intent intent0 = new Intent(FindMoreActivity.this,MainActivity.class);
				intent0.putExtra("classname", "FindMoreActivity");
				intent0.putExtra("name_voyage", current_voyage);
				intent0.putExtra("p_user",user_pos);
				startActivity(intent0);
				
				break;
			case 1:  //my account
				Intent intent1 = new Intent(FindMoreActivity.this,MyAccountActivity.class);
				intent1.putExtra("classname", "FindMoreActivity");
				intent1.putExtra("name_voyage", current_voyage);
				intent1.putExtra("p_user",user_pos);
				startActivity(intent1);
				break;
			case 2: //my voyage
				Intent intent2 = new Intent(FindMoreActivity.this,MyVoyageActivity.class);
				intent2.putExtra("classname", "FindMoreActivity");
				intent2.putExtra("name_voyage", current_voyage);
				intent2.putExtra("p_user",user_pos);
				startActivity(intent2);
				break;
			case 3: //system setting
				Intent intent3 = new Intent(FindMoreActivity.this,SystemSettingActivity.class);
				intent3.putExtra("classname", "FindMoreActivity");
				intent3.putExtra("name_voyage", current_voyage);
				intent3.putExtra("p_user",user_pos);
				startActivity(intent3);
				break;
			case 4: // find more
				Intent intent4 = new Intent(FindMoreActivity.this,FindMoreActivity.class);
				intent4.putExtra("classname", "FindMoreActivity");
				intent4.putExtra("name_voyage", current_voyage);
				intent4.putExtra("p_user",user_pos);
				startActivity(intent4);
				break;
			}
			
			
			}
		});
		
		//**************** Content ListView ***********************************
		contentListView = (ListView) findViewById(R.id.contentList);  
		contentListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  
				contentItems);  
		contentListView.setAdapter(contentListAdapter);  
		contentListView.setOnItemClickListener(new OnItemClickListener() {  
	        @Override  
	        public void onItemClick(AdapterView<?> adapterView, View view, int position,  
	            long id) {  
	        	switch (position){
				case 0: //language
					Intent intent0 = new Intent(FindMoreActivity.this,SuisseActivity.class);
					intent0.putExtra("classname", "FindMoreActivity");
					intent0.putExtra("name_voyage", current_voyage);
					intent0.putExtra("p_user",user_pos);
					startActivity(intent0);
					break;
				case 1:  //personnel
					Intent intent1 = new Intent(FindMoreActivity.this,MyAccountActivity.class);
					startActivity(intent1);
					break;
				case 2: //clean
					Intent intent2 = new Intent(FindMoreActivity.this,MyVoyageActivity.class);
					startActivity(intent2);
					break;
				case 3: //suggestion
					Intent intent3 = new Intent(FindMoreActivity.this,SystemSettingActivity.class);
					startActivity(intent3);
					break;
				case 4: // about us
					Intent intent4 = new Intent(FindMoreActivity.this,AboutusActivity.class);
					startActivity(intent4);
					break;
				}
	        }  
	    });  
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
