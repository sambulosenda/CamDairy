package com.example.test2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
/**
 * Activity: to notice that the name of the voyage is empty
 * @author Administrator
 *
 */
public class NoticeEmptyname extends Activity{
	LinearLayout note;
	private int u_pos;
	@Override
	protected void onCreate(Bundle saveInstanceState){
		super.onCreate(saveInstanceState);
		setContentView(R.layout.notice_emptyname);
		ActivityCollector.addActivity(this);
		
		Intent intent=getIntent();
		u_pos=intent.getIntExtra("p_user",-1);
		note=(LinearLayout)findViewById(R.id.note);
		note.setOnClickListener(new ButtonListener());
		
	}
	
	class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			if(v==note){
				intent.setClass(NoticeEmptyname.this, NewVoyage.class);
				NoticeEmptyname.this.startActivity(intent);
			}
			intent.setClass(NoticeEmptyname.this, NewVoyage.class);
			String str=DataBase.list_user.get(u_pos).getname();
			intent.putExtra("name", str);
			NoticeEmptyname.this.startActivity(intent);
			NoticeEmptyname.this.finish();
		}
		
		
	}
	
	class TouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			if(v==note){
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					intent.setClass(NoticeEmptyname.this, NewVoyage.class);
					NoticeEmptyname.this.startActivity(intent);
				}
			}
			return false;
		}
		
	}
}
