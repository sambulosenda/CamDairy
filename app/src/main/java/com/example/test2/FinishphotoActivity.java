package com.example.test2;

import java.io.FileNotFoundException;
import java.sql.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Activity : for confirming after croping the photo taken from camera or chosen from album
 */

public class FinishphotoActivity extends Activity {

	private ImageView next;
	private ImageView finalpicture;
	private Uri imageUri;
	private String position_day;
	private String position_record;
	private Animation anim;
	private int position_user;
	private int position_voyage;

	//private static final int FINISH_PHOTO = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finishphoto);

		ActivityCollector.addActivity(this);
		Log.d("oncreate","finish");
	
		next = (ImageView) findViewById(R.id.button_next);
		finalpicture = (ImageView) findViewById(R.id.picture_final);
		
		
		
		
		anim = AnimationUtils.loadAnimation(this, R.anim.shake);
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				next.startAnimation(anim);
				Intent intent = new Intent(FinishphotoActivity.this,RecordActivity.class);
				intent.setDataAndType(imageUri, "image/*");  
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				//intent.putExtra("recorddate", datestr);
				intent.putExtra("pos_d",position_day);
				intent.putExtra("pos_r", position_record);
				intent.putExtra("pos_u", position_user);
				intent.putExtra("pos_v", position_voyage);
				
				startActivity(intent); 
				FinishphotoActivity.this.finish();
			
			}
		});

 
		Intent intent= getIntent();
		imageUri= intent.getData();
		position_day=intent.getStringExtra("posday");
		position_record=intent.getStringExtra("posrecord");
		position_user=intent.getIntExtra("pos_uu", -1);
		position_voyage=intent.getIntExtra("pos_vv", -1);
		System.out.println("dayinFinish="+position_day);
		System.out.println("recinFinish="+position_record);
		//Log.d("imageUri we got in FinishphotoActivity",imageUri.toString());


		try {

			Bitmap bitmap = BitmapFactory.decodeStream
					(getContentResolver().openInputStream(imageUri));
			
			//bitmap is the newest token picture
			finalpicture.setImageBitmap(bitmap);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


	}

	
	@Override
	protected void onDestroy() {
	super.onDestroy();
	ActivityCollector.removeActivity(this);
	}
}




