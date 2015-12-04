package com.example.test2;

import com.example.test2.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class AboutusActivity extends Activity {
	
	private ListView contentListView;  
	private ArrayAdapter<String> contentListAdapter;  

	private SlidingLayout slidingLayout;
	private ListView menuListView;
	private ArrayAdapter<String> menuListAdapter;  
	private String[] menuItems = {"Main Page","My account", "My voyages", "System setting","Find more" }; 
	private String[] contentItems = { "Xiao DENG", "Yingpo HUANG", "Jia MA",  
			"Yushan SUN"}; 

	private Button menuButton;
	private String current_voyage;
	private int user_pos;

	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_aboutus);
		ActivityCollector.addActivity(this);
		
		Log.d("About us","oncreat");
		Intent intent=getIntent();
		current_voyage=intent.getStringExtra("name_voyage");
		user_pos=intent.getIntExtra("p_user",-1);
		
		
		Log.d("About us","after get");
				
				//**************** Content ListView ***********************************
				contentListView = (ListView) findViewById(R.id.contentList);  
				contentListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  
						contentItems);  
				contentListView.setAdapter(contentListAdapter);  

				//slidingLayout.setScrollEvent(contentListView);  
				//***********************************************************************

				menuButton = (Button) findViewById(R.id.backtomainpage);
				menuButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
					Intent intent1 = new Intent(AboutusActivity.this,MainActivity.class);
			
					intent1.putExtra("name_voyage", current_voyage);
					intent1.putExtra("p_user",user_pos);
					startActivity(intent1);
					}
				});
				
				//*****************end sliding menu****************************************
		
	}

}

