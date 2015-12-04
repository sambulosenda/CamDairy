package com.example.test2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.test2.DairyActivity.ItemListener;
import com.example.test2.DataBase.Record;

import android.R.string;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.sql.Date;


public class Voyage1Activity extends Activity{
	//public String imagePath = Environment.getExternalStorageDirectory().toString()+ "/1.jpg";
	public String imagePath = new String();
	private ImageView finalpicture;
	//List<Record> list_record=DataBase.Day.class
	private DataBase.Voyage data_voyage;
	private DataBase.Day data_day;
	private DataBase.Record data_record;
	private List<Uri> photo_uri;
	private SimpleDateFormat dateFormat;
	private SimpleDateFormat dateFormatday;
	private Date date;
	private List<String> dataString;
	private List<String> dataText;
	private List<Integer> what;//0=photo 1=diary
	private List<Integer> list_day;
	private List<Integer> list_record;
	private int id_user;
	private int diary_num;
	private ListView lv;
	private int listid;
	private int lastid;
	private int which;
	private int pos;//pos_voyage
	private String path="0";
	private String voice_name;
	private MediaPlayer  mediaPlayer = null;
	private static final String TAG="RECORD"; 
	private boolean ison = false;
	@Override  
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.list_view);  
		
		ActivityCollector.addActivity(this);
		
		listid=-1;
		lastid=-1;
		which=-1;
		lv = (ListView)findViewById(R.id.h_list_view); 
		Intent intent = getIntent();
		pos = intent.getIntExtra("voyage_pos", -1);
		id_user=intent.getIntExtra("p_user", -1);
		dataString = new ArrayList<String>();
		dataText= new ArrayList<String>();
		photo_uri=new ArrayList<Uri>();
		what = new ArrayList<Integer>();
		list_day = new ArrayList<Integer>();
		list_record = new ArrayList<Integer>();
		if(DataBase.list_user.get(id_user).voyage_list.size()!=0){
			Log.d("djfakj", String.valueOf(pos));
			System.out.println("voyage list size="+DataBase.list_user.get(id_user).voyage_list.size());
			data_voyage = DataBase.list_user.get(id_user).voyage_list.get(pos);
			if(data_voyage.list_day.size()!=0){
				diary_num=0;
				for(int i=0;i<data_voyage.list_day.size();i++){
					
					Log.d("for", String.valueOf(i));
					System.out.println("list_day pos="+i);
					data_day = data_voyage.list_day.get(i);
					dateFormat = new SimpleDateFormat(
							"yyyy/MM/dd HH:mm:ss");
					for(int j=0;j<data_day.list_record.size();j++){
						Log.d("forj", String.valueOf(j));
	
						data_record = data_day.list_record.get(j);
						
						dataString.add(dateFormat.format(data_record.date));
						dataText.add(data_record.text);
						what.add(0);
						list_day.add(i);
						list_record.add(j);
						Log.d("forjdata", data_record.photo.toString());
						
						photo_uri.add(data_record.photo);
						Log.d("forjpath", "ggjhg");
					}
					if(data_day.list_record.size()!=0){
						if(data_day.text.equals("")==false || data_day.voice.path!="0"){
							if(data_day.text.equals("")==false)System.out.println("aaaaaaaaaaaaaaaaaa");
							if(data_day.voice.path!="0")System.out.println("BBBBBBBBBBBBBBBB");
							dateFormatday= new SimpleDateFormat(
									"yyyy/MM/dd ");
							dataString.add(dateFormatday.format(data_day.date));
							String d="Diary: ";
							d+=data_day.text;
							dataText.add(d);
							what.add(1);
							list_day.add(i);
							list_record.add(-1);
							//dataText.add(data_day.text);this day has photos and a diary
							photo_uri.add(Uri.parse("file:///storage/emulated/0/Voyagephoto/diary.png"));
							System.out.println("this day has some photos and diary");
							diary_num++;
						}
						
					}
					else{
						diary_num++;
						System.out.println("this day has no photo but a diary");
						dateFormatday= new SimpleDateFormat(
								"yyyy/MM/dd ");
						dataString.add(dateFormatday.format(data_day.date));
						String d="Diary: ";
						d+=data_day.text;
						dataText.add(d);
						what.add(1);
						list_day.add(i);
						list_record.add(-1);
						//dataText.add(data_day.text);this day has no photo but a diary
						photo_uri.add(Uri.parse("file:///storage/emulated/0/Voyagephoto/diary.png"));
						System.out.println("this day has no photo but a diary");
						
					}
				}
				System.out.println("diarynum="+diary_num);
				setViews();
			
			}
			else {
				//photo_uri.add(Uri.parse("No Voyage!"));
				System.out.println("No days!!!!!!!!!!");
			}
		}
		else{
			System.out.println("No voyage!!!!!!!!!!!!!");
		}
		 
	}  
	
	
		
	
	
	private void setViews() {         
		Log.d("forjdataddfe", photo_uri.get(0).toString());

		//lv = (ListView)findViewById(R.id.h_list_view);  
		SimpleAdapter adapter = new ImageSimpleAdapter(this, getDatas() ,R.layout.message_list  
				, new String[]{"icon","title","shortContent"}, new int[]{R.id.ml_icon,R.id.ml_title,R.id.ml_short_content});  
		lv.setAdapter(adapter);  
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv.setOnItemClickListener(new OnItemClickListener(){  
			@Override  
			public void onItemClick(AdapterView<?> av, View v, int position, long id) {  
								
				//Toast.makeText(Voyage1Activity.this, position, Toast.LENGTH_SHORT).show();
				System.out.println("now pos="+listid+" last pos="+lastid);
				listid=lv.getCheckedItemPosition();
				System.out.println("now pos="+listid+" last pos="+lastid);
				if(listid!=-1){
					if(listid!=lastid)which=0;
					System.out.println("which="+which);
					if(what.get(listid)==0){//choose a photo
						System.out.println("1");
						int ss=DataBase.list_user.get(id_user).voyage_list.get(pos).list_day.get(list_day.get(listid)).list_record.get(list_record.get(listid)).list_voice.size();	
						
								System.out.println("2");
								if(mediaPlayer!=null){
									stopplay();
								}
								path=DataBase.list_user.get(id_user).voyage_list.get(pos).list_day.get(list_day.get(listid)).list_record.get(list_record.get(listid)).list_voice.get(which).path;
								voice_name=DataBase.list_user.get(id_user).voyage_list.get(pos).list_day.get(list_day.get(listid)).list_record.get(list_record.get(listid)).list_voice.get(which).name;
								replay();
								which++;
								Toast.makeText(Voyage1Activity.this, voice_name, Toast.LENGTH_SHORT).show();
							
						
						
						if(which==ss){
							Toast.makeText(Voyage1Activity.this, "No more voice...", Toast.LENGTH_SHORT).show();
							which=0;
							System.out.println("3");
						}
						
						
					}
					else if(what.get(listid)==1){//choose a diary
						System.out.println("list_day pos="+list_day.get(listid));
						path=DataBase.list_user.get(id_user).voyage_list.get(pos).list_day.get(list_day.get(listid)).voice.path;
						if(path!="0"){
							System.out.println("5");
							if(mediaPlayer!=null){
								stopplay();
							}
							voice_name=DataBase.list_user.get(id_user).voyage_list.get(pos).list_day.get(list_day.get(listid)).voice.name;
							System.out.println(voice_name);
							replay();
							Toast.makeText(Voyage1Activity.this, voice_name, Toast.LENGTH_SHORT).show();
							
						}
					}
				}
				
				lastid=listid;
				
			}  
		});  
	}  
	private List<Map<String,Object>> getDatas() {  
		//Log.d("forjdataddfe", photo_uri.get(0).toString());
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();  
		//input photo + diary
		for (int i=0; i<photo_uri.size(); i++) {  
			Map<String,Object> map = new HashMap<String,Object>();  
			Bitmap bitmap = null;
			
			try {
				bitmap = BitmapFactory.decodeStream
						(getContentResolver().openInputStream(photo_uri.get(i)));
				
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("get Datas loop="+i);
			String datestr=dataString.get(i);
			System.out.println("get Datas loop="+i);
			String textstr=dataText.get(i);
			System.out.println("get Datas loop="+i);
			map.put("icon", bitmap);
			Log.d("forjdataddfe", imagePath);
			map.put("title", datestr);  
			map.put("shortContent", textstr);  
			list.add(map);  
		}  

	 
		
		/* Map<String,Object> map = new HashMap<String,Object>();  
        map.put("icon", BitmapFactory.decodeFile(imagePath));  
        map.put("title", "Voyage photo 1");  
        map.put("shortContent", "my short content ");  
        list.add(map);  
        Map<String,Object> map1 = new HashMap<String,Object>();  
        map1.put("icon", BitmapFactory.decodeFile(imagePath));  
        map1.put("title", "Voyage photo 2");  
        map1.put("shortContent", "my short content ");  
        list.add(map1);  
		Map<String,Object> map = new HashMap<String,Object>();  
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(photo_uri.get(0)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("icon", bitmap);
		Log.d("forjdataddfe", imagePath);
		map.put("title", "My Title ");  
		map.put("shortContent", "my short content ");  
		list.add(map);  
*/
		return list;  
	}  
	
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
	
}