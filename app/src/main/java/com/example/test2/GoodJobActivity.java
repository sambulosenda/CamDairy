package com.example.test2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;


/**
 * Activity: when we finish one voyage, we will get into this activity
 * @author Administrator
 *
 */
public class GoodJobActivity extends Activity{
	
	private Button ok;
	private int user_pos;
	private String current_voyage;
	
	
	@Override
	protected void onCreate(Bundle saveInstanceState){
		super.onCreate(saveInstanceState);
		setContentView(R.layout.goodjob);
		ActivityCollector.addActivity(this);
		
		ok=(Button)findViewById(R.id.okok);
		ok.setOnClickListener(new ButtonListener());
		Intent intent = getIntent();
		current_voyage=intent.getStringExtra("name_voyage");
		user_pos=intent.getIntExtra("p_user", -1);
		
	}
	
	class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			if(v==ok){
				intent.setClass(GoodJobActivity.this, NewVoyage.class);
				GoodJobActivity.this.startActivity(intent);
			}
			intent.setClass(GoodJobActivity.this, MainActivity.class);
			intent.putExtra("name_voyage", current_voyage);
			intent.putExtra("p_user", user_pos);
			GoodJobActivity.this.startActivity(intent);
			GoodJobActivity.this.finish();
		}
		
	}

}
