package com.example.test2;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/**
 * Activity: when we have a photo, we can make audio records or write a note in this activity
 * @author Administrator
 *
 */
public class RecordActivity extends Activity {

	private ImageView btn_finish;
	private ImageView finalpicture;
	private Uri imageUri;
	private Date date;
	
	//********************
	private MediaRecorder mediaRecorder=null;
	private MediaPlayer mediaPlayer = null;
	private ImageView recordStart;
	private ImageView button_replay;
	private ImageView button_delete;
	private EditText add_words;
	private static final String TAG="RECORD"; 
	private boolean isrecord = false;
	private boolean ison = false;
	private String path="0";
	private String name;
	private int audioid;
	private int num = 0;
	private ListView listview;
	private AudioDataSource audiodatalist;
	private ArrayAdapter<String> adapter;
	private TextView minView;
	private TextView secView;
	private MyTimeTask timetask;
	List<DataBase.Record.Voice> new_voicelist;
	List<String>new_voicelistname;
	private Animation anim;
	private int pos__day;
	private int pos__record;
	private int pos__user;
	private int pos__voyage;
	
	//**********************

	//private static final int FINISH_PHOTO = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);

		ActivityCollector.addActivity(this);

		//**********************************************

		//start recording
		recordStart = (ImageView)findViewById(R.id.button_recordnow);
		button_replay=(ImageView)findViewById(R.id.button_replay);
		button_delete= (ImageView)findViewById(R.id.button_delete);
		add_words=(EditText)findViewById(R.id.addwords);
		anim = AnimationUtils.loadAnimation(this, R.anim.shake);
		//changetowrite=(Button)findViewById(R.id.button_changetowrite);
		
		
		recordStart.setOnLongClickListener(new ButtonLongListener());
		recordStart.setOnTouchListener(new TouchListener());
		
		button_delete.setOnClickListener(new ButtonListener());
		button_replay.setOnClickListener(new ButtonListener());
		
		//changetowrite.setOnClickListener(new ButtonListener());
		
		minView=(TextView)findViewById(R.id.min);
		secView=(TextView)findViewById(R.id.sec);
		timetask=new MyTimeTask();
		minView.setText(timetask.getMin()+":");
		secView.setText(timetask.getSec());
		
		listview=(ListView)findViewById(R.id.record_list);
		
		audiodatalist = new AudioDataSource();
		new_voicelistname = new ArrayList<String>();
		
		Log.d("FinishphotoActivity","oncreate()");
		Intent intent= getIntent();
		Uri imageUri= intent.getData();
		//String posday=intent.getStringExtra("pos_d");
		//String posrec=intent.getStringExtra("pos_r");
		//System.out.println(posday+";"+posrec);
		pos__day=Integer.parseInt(intent.getStringExtra("pos_d"));
		pos__record=Integer.parseInt(intent.getStringExtra("pos_r"));
		pos__user=intent.getIntExtra("pos_u", -1);
		pos__voyage=intent.getIntExtra("pos_v", -1);
		
		
		if(DataBase.list_user.get(pos__user).voyage_list.get(pos__voyage).list_day.get(pos__day).list_record.get(pos__record).text!="")add_words.setText(DataBase.list_user.get(pos__user).voyage_list.get(pos__voyage).list_day.get(pos__day).list_record.get(pos__record).text);
		for(int i=0;i<DataBase.list_user.get(pos__user).voyage_list.get(pos__voyage).list_day.get(pos__day).list_record.get(pos__record).list_voice.size();i++){
			new_voicelistname.add(DataBase.list_user.get(pos__user).voyage_list.get(pos__voyage).list_day.get(pos__day).list_record.get(pos__record).list_voice.get(i).name);
			
		}
		//adapter = new ArrayAdapter<String>(RecordActivity.this,R.layout.list_text,audiodatalist.getAudiolistname());
		adapter = new ArrayAdapter<String>(RecordActivity.this,R.layout.list_text,new_voicelistname);
		listview.setAdapter(adapter);
		listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listview.setOnItemClickListener(new ItemListener());
		
		
		new_voicelist=new ArrayList<DataBase.Record.Voice>();
		
		//**********************************************
		
		
		//Log.d("Recordact","oncreate");

		//addrecord = (Button) findViewById(R.id.addrecord);
		finalpicture = (ImageView) findViewById(R.id.picture_final);
		btn_finish=(ImageView) findViewById(R.id.button_finish);
	
		btn_finish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_finish.startAnimation(anim);
				System.out.println("newvoicelist size="+new_voicelist.size());
				/*if(new_voicelist.size()!=0){
			
					for(int j=0;j<new_voicelist.size();j++){
						DataBase.list_user.get(0).voyage_list.get(0).list_day.get(pos__day).list_record.get(pos__record).list_voice.add(new_voicelist.get(j));
						//String pp=day.list_record.get(i).list_voice.get(0).path;
						//Log.d("testhahahahahah",pp);
					}
					
				}*/
				int ss=DataBase.list_user.get(pos__user).voyage_list.get(pos__voyage).list_day.get(pos__day).list_record.get(pos__record).list_voice.size();
				System.out.println("size!!!!!!!!!!!!!!!="+ss);
				String photo_comment = add_words.getText().toString();
				DataBase.list_user.get(pos__user).voyage_list.get(pos__voyage).list_day.get(pos__day).list_record.get(pos__record).text=photo_comment;
				
				
				
				
				new_voicelist.clear();
				new_voicelistname.clear();
				//ActivityCollector.finishAll();
				

				
				Intent intent = new Intent(RecordActivity.this,MainActivity.class);
				//intent.setDataAndType(imageUri, "image/*");  
				//intent.putExtra("scale", true);
				//intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				String nn=DataBase.list_user.get(pos__user).voyage_list.get(pos__voyage).name;
				intent.putExtra("name_voyage", nn);
				intent.putExtra("p_user",pos__user);
				startActivity(intent); 
				RecordActivity.this.finish();

			}
		});
		
		
		
		//date = Date.valueOf(intent.getStringExtra("recorddate"));
		
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

	//******************************
	/**
	 * to detect which record is chosen
	 * @author Administrator
	 *
	 */
	class ItemListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
		 
			int i=listview.getCheckedItemPosition();
			audioid=listview.getCheckedItemPosition();
			//path=audiodatalist.audiolist.get(audioid);
			path=DataBase.list_user.get(pos__user).voyage_list.get(pos__voyage).list_day.get(pos__day).list_record.get(pos__record).list_voice.get(audioid).path;
			Toast.makeText(RecordActivity.this, "" + position, Toast.LENGTH_SHORT).show();
			
		}
		
	}
	
	/**
	 * we press the button and hold on, then the recording starts
	 * @author Administrator
	 *
	 */
	class ButtonLongListener implements OnLongClickListener{

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			if(v==recordStart){
				startRecord();
				isrecord=true;
				timetask.startTiming();
				minView.setText(timetask.getMin()+":");
				secView.setText(timetask.getSec());
			}
			return true;
		}


		
	}
	
	/**
	 * then when we release the button the recording will stop
	 * @author Administrator
	 *
	 */
	class TouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if(isrecord==true){
				if(event.getAction()==MotionEvent.ACTION_UP){
					stopRecord();
					isrecord=false;
					timetask.stopTiming();
					minView.setText(timetask.getMin()+":");
					secView.setText(timetask.getSec());
					audiodatalist.addAudioDuree(num, (int)timetask.returnduree());
					//audiodatalist.addAudioDuree(num, 1);
					audiodatalist.NameandDuree(name,num);
					
					
					DataBase.Record.Voice voice = new DataBase.Record.Voice();
					voice.path=path;
					voice.duree=(int)timetask.returnduree();
					String vname=name;
					vname+="          ";//10 spaces
					vname+=String.valueOf((voice.duree / 60));
					vname+=":";
					int vsec=(int)(voice.duree % 60);
					vname+=(vsec < 10? "0" + vsec :String.valueOf(vsec));  
					voice.name=vname;
					new_voicelist.add(voice);
					new_voicelistname.add(vname);
					DataBase.list_user.get(pos__user).voyage_list.get(pos__voyage).list_day.get(pos__day).list_record.get(pos__record).list_voice.add(voice);
					adapter.notifyDataSetChanged();
				}
			}
			return false;
		}
		
	}
	
	/**
	 * we can click the button to replay, stop playing and delete one record
	 * @author Administrator
	 *
	 */
	class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==button_replay){
				button_replay.startAnimation(anim);
				if(!ison){
					replay();
				}
				else if(!mediaPlayer.isPlaying()){
					stopplay();
					replay();	
				}
				else{
					stopplay();
					
					
				}
			}
			else if(v==button_delete){	
				button_delete.startAnimation(anim);
				delete();
			}
			
			
		}
		
	}
	
	/**
	 * function of starting a record
	 */
	public void startRecord(){
		int i=0;
		name="audio";
		File audioDir = new File(Environment.
				getExternalStorageDirectory().getAbsoluteFile(), "VoyageAudio");

		if (!audioDir.exists()) {
			audioDir.mkdir();
		}
		for(i=0;i<100;i++){
			path=Environment.getExternalStorageDirectory().getAbsolutePath();
			path += "/VoyageAudio";
			path += "/audio";
			path += i;
			path += ".amr";
			
			name="audio";
			name += i;
			num=i;
			System.out.println(num);
			File file=new File(path);
			if(file.exists()){
				continue;
			}
			else{
				break;
			}
		}
				
		try{
			mediaRecorder = new MediaRecorder();
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mediaRecorder.setOutputFile(path);
			mediaRecorder.prepare();  
			mediaRecorder.start();
		}catch (IllegalStateException e) {            
	            // TODO Auto-generated catch block
	            Log.i(TAG,e.getMessage());
	            e.printStackTrace();
        } catch (IOException e) {  
            Log.e(TAG, "prepare() failed");  
        }  
		System.out.println("recording...please waiting");
		String str = path+"total="+audiodatalist.getAudiolist().size();

		audiodatalist.addAudioData(path,name);
		adapter.notifyDataSetChanged();

	}
	
	/**
	 * function of stopping recording
	 */
	public void stopRecord(){
		if(mediaRecorder != null){
			System.out.println("stop recording!");
			mediaRecorder.stop();
			mediaRecorder.release();
			mediaRecorder=null;
			
		}
	}

	/**
	 * function of replaying one record
	 */
	public void replay(){
		
		if(path.equals("0")==false){
			mediaPlayer = new MediaPlayer();
			try{
				mediaPlayer.setDataSource(path);
				mediaPlayer.prepare();
				mediaPlayer.start();
			} catch (IOException e) {  
		        Log.e(TAG, "prepare() failed");  
		    } 
			ison=true;
			System.out.println("Replaying!");
		}
	}
	
	/**
	 * function of stopping replaying
	 */
	public void stopplay(){
		if(path.equals("0")==false){
			if(mediaPlayer!=null){
				mediaPlayer.stop();
				mediaPlayer.release();
				mediaPlayer=null;
				ison=false;
				System.out.println("stop playing!");
			}
		}
	}
	
	/**
	 * function of deleting one record
	 */
	public void delete(){
		if(path.equals("0")==false){
			File file = new File(path);
			System.out.println("find path in delete");
			if(file.exists())file.delete();
			System.out.println("delete file");
			//audiodatalist.deleteAudioData(audioid);
			DataBase.list_user.get(pos__user).voyage_list.get(pos__voyage).list_day.get(pos__day).list_record.get(pos__record).list_voice.remove(audioid);
			new_voicelistname.remove(audioid);
			System.out.println("delete file in database");
			adapter.notifyDataSetChanged();
			path="0";
		}
	}
	
	
	

	/**
	 * the class for the timekeeping
	 * @author Administrator
	 *
	 */
	class MyTimeTask {

		private int sec;
		private int min;
		private long totalsec;
		private long duree;
		private boolean isrecording;
		private Handler hander= new Handler(){
			@Override
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				switch(msg.what){
				case 1:
					if (isrecording) {  
	                    updatetime();
	                    
	                } 
					hander.sendEmptyMessageDelayed(1, 1000); 
					break;
				case 0:
					break;
				}
			}
		};
		
		
		public MyTimeTask(){
			sec=0;
			min=0;
			isrecording=false;
			initTime();
			
		}
		
		public void initTime(){
			sec=0;
			min=0;
			totalsec=0;
		}
		
		public void startTiming(){
			hander.removeMessages(1);
			hander.sendEmptyMessage(1);
			isrecording=true;
		}
		
		public void stopTiming(){
			hander.sendEmptyMessage(0);
			isrecording=false;
			duree=totalsec;
			totalsec=0;
			
		}
		
		public long returnduree(){
			return duree;
		}
		
		public void updatetime(){
			totalsec += 1;
			sec=(int) (totalsec % 60);
			min=(int) (totalsec / 60);
			minView.setText(timetask.getMin()+":");
			secView.setText(timetask.getSec());
		}
		
		public CharSequence getSec(){  
	        int s = sec;
	        return s < 10? "0" + s :String.valueOf(sec);  
	    }  
		
		public CharSequence getMin(){  
	        return String.valueOf((int)(totalsec / 60));  
	    } 
	}
	



	//********************************
	DataBase.Day findDay(Date d, List<DataBase.Day> l){

		DataBase.Day day=new DataBase.Day();
		for(int i=0; i<l.size();i++){
			if(d==l.get(i).date){
				day=l.get(i);
			}
			else day=null;
			
		}
		return day;
		
	}
	
}













