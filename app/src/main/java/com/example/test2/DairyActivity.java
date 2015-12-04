package com.example.test2;
/**
 * the Activity : in this activity, we can make one audio record and write one diary
 */

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
public class DairyActivity extends Activity {
	private MediaRecorder mediaRecorder=null;
	private MediaPlayer mediaPlayer = null;
	private ImageView recordStart;
	private ImageView button_replay;
	private ImageView button_delete,button_finish;
	private Button changetowrite;
	private Button changetorecord;
	private Button savediary;
	private EditText add_words;
	private static final String TAG="RECORD"; 
	private boolean isrecord = false;
	private boolean ison = false;
	private String path;
	private String name;
	private int audioid;
	private int num = 0;
	private ListView listview;
	private AudioDataSource audiodatalist;
	private ArrayAdapter<String> adapter;
	private TextView minView;
	private TextView secView;
	private EditText add_dairy;
	private MyTimeTask timetask;
	private Animation anim;
	private Date current_date;
	private DataBase.Record.Voice voice;
	List<String>new_voicelistname;
	private int pos_user;
	private int pos_voyage;

	@Override
	protected void onCreate(Bundle saveInstanceState){
		super.onCreate(saveInstanceState);
		setContentView(R.layout.activity_dairy);
	
		ActivityCollector.addActivity(this);
		
		/**
		 * to get the id of the user and the id of its current voyage
		 */
		Intent intent = getIntent();
		pos_user=intent.getIntExtra("pos_uu", -1);
		pos_voyage=intent.getIntExtra("pos_vv", -1);

		path="0";
		Log.d("Dairyactivity","oncreate");
		//start recording
		recordStart = (ImageView)findViewById(R.id.button_recordnow);
		button_replay=(ImageView)findViewById(R.id.button_replay);
		button_delete= (ImageView)findViewById(R.id.button_delete);
		button_finish=(ImageView)findViewById(R.id.button_finish);
		add_dairy=(EditText)findViewById(R.id.adddiary);
		
		
		anim = AnimationUtils.loadAnimation(this, R.anim.shake);
		
		current_date = new Date(System.currentTimeMillis());
		new_voicelistname = new ArrayList<String>();
		if(pos_user!=-1 && pos_voyage!=-1){
			int ps_day=findDay(current_date,DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day);
			if(ps_day!=-1){
				if(DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day.get(ps_day).voice!=null){
					new_voicelistname.add(DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day.get(ps_day).voice.name);
					voice = new DataBase.Record.Voice();
					voice.path=DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day.get(ps_day).voice.path;
					voice.name=DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day.get(ps_day).voice.name;
					voice.duree=DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day.get(ps_day).voice.duree;
				}
				if(DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day.get(ps_day).text!="")add_dairy.setText(DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day.get(ps_day).text);
			}
		}
			
		
		
		
		//add_words=(EditText)findViewById(R.id.addwords);
		/**
		 * when we click the finish button, the record and the diary will be put into the data base.
		 * and the activity will turn to the MainActivity
		 */
		button_finish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				button_finish.startAnimation(anim);
				System.out.println("button finish diary");
				if(pos_user!=-1 && pos_voyage!=-1){
					if(voice!=null){
						
						int position_day=findDay(current_date,DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day);
						if(position_day!=-1){
							
							DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day.get(position_day).voice.path=voice.path;
							DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day.get(position_day).voice.duree=voice.duree;
							DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day.get(position_day).voice.name=voice.name;
							if(add_dairy.getText().toString()!="")DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day.get(position_day).text=add_dairy.getText().toString();
							else DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day.get(position_day).text="";
								//String pp=day.list_record.get(i).list_voice.get(0).path;
								//Log.d("testhahahahahah",pp);
							
						}
						else{
							DataBase.Day newday = new DataBase.Day();
							newday.date=current_date;
							newday.voice.path=voice.path;
							newday.voice.duree=voice.duree;
							newday.voice.name=voice.name;
							if(add_dairy.getText().toString()!="")newday.text=add_dairy.getText().toString();
							else newday.text="";
							DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day.add(newday);
	
						}
					}
					else{
						if(add_dairy.getText().toString()!=""){
							int position_day=findDay(current_date,DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day);
							if(position_day!=-1){
								DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day.get(position_day).text=add_dairy.getText().toString();
								DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day.get(position_day).voice=null;
							}
							else{
								DataBase.Day newday = new DataBase.Day();
								newday.date=current_date;
								newday.text=add_dairy.getText().toString();
								newday.voice=null;
								DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day.add(newday);
								int k=DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day.size()-1;
								System.out.println("text="+DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day.get(k).text);
							}
						}
					}
				}
				
				
				System.out.println("user="+DataBase.list_user.get(pos_user).getname());
				System.out.println("voyage="+DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).name);
				System.out.println("day="+DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day.size());
				//ActivityCollector.finishAll();

			
				Intent intent = new Intent(DairyActivity.this,MainActivity.class);
				String nn=DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).name;
				intent.putExtra("name_voyage", nn);
				intent.putExtra("p_user",pos_user);
				startActivity(intent); 	
				DairyActivity.this.finish();

			}
			});
		
		recordStart.setOnLongClickListener(new ButtonLongListener());
		recordStart.setOnTouchListener(new TouchListener());
		
		button_delete.setOnClickListener(new ButtonListener());
		button_replay.setOnClickListener(new ButtonListener());
		
		minView=(TextView)findViewById(R.id.min);
		secView=(TextView)findViewById(R.id.sec);
		timetask=new MyTimeTask();
		minView.setText(timetask.getMin()+":");
		secView.setText(timetask.getSec());
		
		listview=(ListView)findViewById(R.id.record_list);
		
		audiodatalist = new AudioDataSource();
		adapter = new ArrayAdapter<String>(DairyActivity.this,R.layout.list_text,new_voicelistname);
		listview.setAdapter(adapter);
		listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listview.setOnItemClickListener(new ItemListener());
		
	}
	
	@Override
	protected void onDestroy() {
	super.onDestroy();
	ActivityCollector.removeActivity(this);
	}
	
	/**
	 * the action of choosing one record
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
			int pps_day=findDay(current_date,DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day);
			if(pps_day!=-1)path=DataBase.list_user.get(pos_user).voyage_list.get(pos_voyage).list_day.get(pps_day).voice.path;
			Toast.makeText(DairyActivity.this, "" + position, Toast.LENGTH_SHORT).show();
			
		}
		
	}
	
	/**
	 * we press the button and hold on in order to make a record
	 *
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
	 * if we release the button then we can stop recording
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
					
					
					voice = new DataBase.Record.Voice();
					voice.path=path;
					voice.duree=(int)timetask.returnduree();
					String vname=name;
					vname+="          ";//10 spaces
					vname+=String.valueOf((voice.duree / 60));
					vname+=":";
					int vsec=(int)(voice.duree % 60);
					vname+=(vsec < 10? "0" + vsec :String.valueOf(vsec));  
					voice.name=vname;
					new_voicelistname.clear();
					new_voicelistname.add(vname);
					adapter.notifyDataSetChanged();
					
				}
			}
			return false;
		}
		
	}
	
	/**
	 * the button for replaying the record and if the record is playing, then click again to stop replaying
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
	 * the function of recording
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
	 * the function of stop recording
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
	 * the function of replaying
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
	 * the function of stopping playing the record
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
	 * the function of delete one record
	 */
	public void delete(){
		if(path.equals("0")==false){
			File file = new File(path);
			if(file.exists())file.delete();
			//audiodatalist.deleteAudioData(audioid);
			voice=null;
			new_voicelistname.clear();
			adapter.notifyDataSetChanged();
			path="0";
		}
	}
	
	
	/**
	 * the class in order for timekeeping
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
		
		/**
		 * start timekeeping
		 */
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
		
		/**
		 * update the time
		 */
		public void updatetime(){
			totalsec += 1;
			sec=(int) (totalsec % 60);
			min=(int) (totalsec / 60);
			minView.setText(timetask.getMin()+":");
			secView.setText(timetask.getSec());
		}
		/**
		 * change the form of display
		 * @return
		 */
		public CharSequence getSec(){  
	        int s = sec;
	        return s < 10? "0" + s :String.valueOf(sec);  
	    }  
		
		public CharSequence getMin(){  
	        return String.valueOf((int)(totalsec / 60));  
	    } 
	}
	
	/**
	 * the function to find the id of one certain day in the list of voyage 
	 * if the day does not exist, then return -1
	 * 
	 * @param d
	 * @param l
	 * @return
	 */
	int findDay(Date d, List<DataBase.Day> l){
		int pos=-1;
		//DataBase.Day day=new DataBase.Day();
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(d);
		Calendar cal2 = Calendar.getInstance();
		System.out.println("before loop");
		System.out.println("listsize="+l.size());
		for(int i=0; i<l.size();i++){
			System.out.println("looping="+i);
			cal2.setTime(l.get(i).date);
			System.out.println("find day at ="+i);
			if(cal1.get(Calendar.DAY_OF_YEAR)==cal2.get(Calendar.DAY_OF_YEAR)){
				pos=i;
				System.out.println("find day at ="+i);
			}
			
		}
		return pos;
		
	}

}









