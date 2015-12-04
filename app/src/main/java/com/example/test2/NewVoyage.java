package com.example.test2;

import java.sql.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
/**
 * activity: to start a new voyage by entering its name
 * of the name is empty, one notice will show up.
 * @author Administrator
 *
 */
public class NewVoyage extends Activity{
	private ImageView buttonDone;
			EditText fillName;
			String nameOfVoyage;
	private Animation anim;
	private String user_name;
	private Date current_date;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_voyage);
		
		ActivityCollector.addActivity(this);
		
		buttonDone = (ImageView)findViewById(R.id.done);
		fillName = (EditText)findViewById(R.id.newname);
		buttonDone.setOnClickListener(new myButtonListener());
		anim = AnimationUtils.loadAnimation(this, R.anim.shake);
		Intent intent = getIntent();
		user_name=intent.getStringExtra("name");
		System.out.println("user_name in new="+user_name);
		current_date = new Date(System.currentTimeMillis());
	}
	

	/**
	 * if the name is not empty,we will go to MainActivity
	 * if the name is empty, one notice will show up
	 * @author Administrator
	 *
	 */
	class myButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==buttonDone)buttonDone.startAnimation(anim);
			Intent intent = new Intent();
			System.out.println("choose your name");
			if(v==buttonDone)nameOfVoyage=fillName.getText().toString();
			System.out.println(nameOfVoyage);
			//intent.setData(Uri.parse(nameOfVoyage));
			if(!nameOfVoyage.isEmpty()){
				intent.putExtra("name_voyage", nameOfVoyage);
				intent.setClass(NewVoyage.this, MainActivity.class);
				
				DataBase.Voyage v0 = new DataBase.Voyage();
				v0.status=true;
				System.out.println("before date");
				v0.name=nameOfVoyage;
				
				int user = findUser(user_name);
				
				System.out.println("after date");
				v0.date.add(current_date);
				System.out.println("before find user");
				int u_pos=findUser(user_name);
				System.out.println("user position!!!!!!!="+u_pos);
				if(u_pos!=-1){
					DataBase.list_user.get(u_pos).voyage_list.add(v0);
					DataBase.list_user.get(user).isOn=true;
					int ss=DataBase.list_user.get(u_pos).voyage_list.size()-1;
					intent.putExtra("p_user", u_pos);
					Log.d("come onnnnnnnnnnn",DataBase.list_user.get(u_pos).voyage_list.get(ss).name );
					
					NewVoyage.this.startActivity(intent);
				}
			}
			else{
				int u_pos=findUser(user_name);
				
				intent.setClass(NewVoyage.this, NoticeEmptyname.class);
				intent.putExtra("p_user", u_pos);
				NewVoyage.this.startActivity(intent);
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
