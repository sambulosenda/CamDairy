package com.example.test2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * Activity: in this activity, we can choose to start a new voyage or look at the previous voyage in the list
 * 
 * if there is one voyage which is not finished, we will not get into this activity again when the user log in the application
 * @author Administrator
 *
 */
public class NeworMemory extends Activity{
private Button buttonNewVoyage,buttonMyMemory;
private String user_name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_or_memory);
		ActivityCollector.addActivity(this);
		
		
		buttonNewVoyage = (Button)findViewById(R.id.buttonnewvoyage);
		buttonMyMemory = (Button)findViewById(R.id.buttonmymemory);
		
		ButtonListener buttonListener = new ButtonListener();
		buttonNewVoyage.setOnClickListener(buttonListener);
		buttonMyMemory.setOnClickListener(buttonListener);	
		Intent intent = getIntent();
		user_name=intent.getStringExtra("name");

}
	
	class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			if(v==buttonNewVoyage){
				System.out.println("I want to record my new voyage!");
				intent.setClass(NeworMemory.this, NewVoyage.class);
				intent.putExtra("name", user_name);
				System.out.println("user_name in new or memory="+user_name);
				NeworMemory.this.startActivity(intent);
			}
			else if(v==buttonMyMemory){
				System.out.println("I want to reviwe my memory...");
				intent.setClass(NeworMemory.this, MyVoyageActivity.class);
				int p=findUser(user_name);
				intent.putExtra("classname","NeworMemory");
				intent.putExtra("p_user",p);
				//intent.putExtra("name_voyage",);
				NeworMemory.this.startActivity(intent);
			}
		}
		
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

