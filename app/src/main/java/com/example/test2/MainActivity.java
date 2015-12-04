package com.example.test2;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.example.test2.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.test2.ArcMenu.onMenuItemClickListener;
import com.example.test2.DataBase.Day;

import java.sql.Date;

/**
 * Activity : include all the functions of the "main page" 
 */

public class MainActivity extends Activity {
	public static final int TAKE_PHOTO = 1;
	public static final int CROP_PHOTO = 2;
	private static final int FINISH_PHOTO = 3;

	private ImageView picture;
	private Button endorstart;
	private Uri imageUri;
	private String fileName;
	private String path_uri;
	private String image_path;
	
	private SimpleDateFormat dateFormat;
	private Date date;
	
	private int pos_day;
	private int pos_record;

	private ListView contentListView;  
	private ArrayAdapter<String> contentListAdapter;  

	private SlidingLayout slidingLayout;
	private ListView menuListView;
	private ArrayAdapter<String> menuListAdapter;  
	private String[] menuItems = {"Main Page","My account", "My voyages", "System setting","Find more" }; 
	private String[] contentItems = { "Content Item 1", "Content Item 2", "Content Item 3",  
			"Content Item 4", "Content Item 5"}; 
	private Button menuButton;
	private String voyage_name;
	private int position_user;
	private int position_voyage;
	private int diary_num=0;
	
	
	private DataBase.Voyage data_voyage;
	private DataBase.Day data_day;
	private DataBase.Record data_record;
	private List<Uri> photo_uri=new ArrayList<Uri>();
	private SimpleDateFormat dateFormat1;
	private SimpleDateFormat dateFormatday;
	private List<String> dataString;
	private List<String> dataText;

	//******************************
	ArcMenu arcMenu;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActivityCollector.addActivity(this);
		
		endorstart=(Button)findViewById(R.id.endorstart);
		if(DataBase.list_user.get(position_user).isOn==true){
			endorstart.setText("End Voyage");
		}
		else{
			endorstart.setText("New Voyage");
		}
		/**
		 * when one voyage does not finish, then click the button for finishing it
		 * when there is no voyage at present, then click the button for a new voyage
		 */
		endorstart.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(DataBase.list_user.get(position_user).isOn==true){
					DataBase.list_user.get(position_user).isOn=false;
					DataBase.list_user.get(position_user).voyage_list.get(position_voyage).status=false;
					Intent intent = new Intent();
					intent.setClass(MainActivity.this,GoodJobActivity.class );
					intent.putExtra("name_voyage", voyage_name);
					intent.putExtra("p_user", position_user);
					startActivity(intent);
				}
				else{
					
					Intent intent = new Intent();
					intent.setClass(MainActivity.this,NewVoyage.class );
					String str=DataBase.list_user.get(position_user).getname();
					intent.putExtra("name", str);
					startActivity(intent);
				}
			}
			
		});
		Intent intentname = getIntent();
		voyage_name=intentname.getStringExtra("name_voyage");
		position_user=intentname.getIntExtra("p_user", -1);
		
		position_voyage=findVoyage(voyage_name,position_user);
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!oncreate");
		
		
		
		//*********************************** sliding menu*****************************************
		
		slidingLayout = (SlidingLayout) findViewById(R.id.slidingLayout);
		
		//************** Menu ListView *******************
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
				Intent intent0 = new Intent(MainActivity.this,MainActivity.class);
				intent0.putExtra("classname","MainActivity");
				intent0.putExtra("name_voyage", voyage_name);
				intent0.putExtra("p_user",position_user);
				startActivity(intent0);
				break;
			case 1:  //my account
				Intent intent1 = new Intent(MainActivity.this,MyAccountActivity.class);
				intent1.putExtra("classname","MainActivity");
				intent1.putExtra("name_voyage", voyage_name);
				intent1.putExtra("p_user",position_user);
			    
				startActivity(intent1);
				break;
			case 2: //my voyage
				Intent intent2 = new Intent(MainActivity.this,MyVoyageActivity.class);
				intent2.putExtra("classname","MainActivity");
				intent2.putExtra("name_voyage", voyage_name);
				intent2.putExtra("p_user",position_user);
				System.out.println("go into myvoyage!!!!!!!");
				startActivity(intent2);
				break;
			case 3: //system setting
				Intent intent3 = new Intent(MainActivity.this,SystemSettingActivity.class);
				intent3.putExtra("classname","MainActivity");
				intent3.putExtra("name_voyage", voyage_name);
				intent3.putExtra("p_user",position_user);
				startActivity(intent3);
				break;
			case 4: // find more
				Intent intent4 = new Intent(MainActivity.this,FindMoreActivity.class);
				intent4.putExtra("classname","MainActivity");
				intent4.putExtra("name_voyage", voyage_name);
				intent4.putExtra("p_user",position_user);
				startActivity(intent4);
				break;
			}
	
			}
		});
		//**************** Content ListView ***********************
		
		/**
		 * the list will show all the photos that we take or choose from the album and the note as well
		 * the diary will also be shown on the list
		 *  
		 * if the voyage is empty or there is no voyage, one picture will shown on the list
		 */
		contentListView = (ListView) findViewById(R.id.contentList);  
		/*contentListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  
				contentItems);  
		contentListView.setAdapter(contentListAdapter);  */
		
		slidingLayout.setScrollEvent(contentListView);  
		
		dataString = new ArrayList<String>();
		dataText= new ArrayList<String>();
		photo_uri=new ArrayList<Uri>();
		if(DataBase.list_user.get(position_user).voyage_list.size()!=0){
			Log.d("djfakj", String.valueOf(position_user));
			System.out.println("voyage list size="+DataBase.list_user.get(position_user).voyage_list.size());
			data_voyage = DataBase.list_user.get(position_user).voyage_list.get(position_voyage);
			if(data_voyage.list_day.size()!=0){
				diary_num=0;
				for(int i=0;i<data_voyage.list_day.size();i++){
					
					Log.d("for", String.valueOf(i));
					System.out.println("list_day pos="+i);
					data_day = data_voyage.list_day.get(i);
					dateFormat1 = new SimpleDateFormat(
							"yyyy/MM/dd HH:mm:ss");
					for(int j=0;j<data_day.list_record.size();j++){
						Log.d("forj", String.valueOf(j));
	
						data_record = data_day.list_record.get(j);
						
						dataString.add(dateFormat1.format(data_record.date));
						dataText.add(data_record.text);
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
							//dataText.add(data_day.text);this day has photos and a diary
							photo_uri.add(Uri.parse("No Voyage photo for diary!"));
							System.out.println("file:///storage/emulated/0/Voyagephoto/diary.png");
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
				setEmptyView();
			}
		}
		else{
			System.out.println("No voyage!!!!!!!!!!!!!");
			setEmptyView();
		}
			
		//***********************************************************************
	    
		
		
		/**
	     * the Menu button
	     */
		menuButton = (Button) findViewById(R.id.menuButton);
		menuButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (slidingLayout.isLeftLayoutVisible()) {
					slidingLayout.scrollToRightLayout();
				} else {
					slidingLayout.scrollToLeftLayout();
				}
			}
		});
		//*************************end sliding menu**********************************************

		
	
		/**
		 * the satellite menu
		 */
		arcMenu = new ArcMenu(this);
		arcMenu = (ArcMenu) findViewById(R.id.id_menu);
		arcMenu.setOnMenuItemClickListener(new onMenuItemClickListener() {

			@Override
			public void onclick(View view, int position) {
				// TODO Auto-generated method stub
				if(DataBase.list_user.get(position_user).isOn==true){
					if(view.getId()==101){// check the gps and our footprint
						
					}
					else if(view.getId()==102){// go to write a diary 
						Intent intent = new Intent(MainActivity.this,DairyActivity.class);
						position_voyage=findVoyage(voyage_name,position_user);
						intent.putExtra("pos_uu", position_user);
						intent.putExtra("pos_vv",position_voyage);
						startActivity(intent); 
						MainActivity.this.finish();
					}
					else if(view.getId()==103){//choose one photo in the album
	
	
						File appDir = new File(Environment.
								getExternalStorageDirectory(), "Voyagephoto");
						if (!appDir.exists()) {
							appDir.mkdir();
						}
	
						date = new Date(System.currentTimeMillis());
						dateFormat = new SimpleDateFormat(
								"yyyyMMdd_HHmmss");
						fileName = "";
						fileName = dateFormat.format(date) + ".jpg";
						
						
	
						image_path="";
						File outputImage=new File(appDir,fileName);
						image_path=outputImage.getAbsolutePath();
						imageUri = Uri.fromFile(outputImage);
	
						Intent intent = new Intent("android.intent.action.GET_CONTENT");
						intent.setType("image/*");
						intent.putExtra("crop", true);
						intent.putExtra("scale", true);
						intent.putExtra("aspectX", 3);
						intent.putExtra("aspectY", 4);
	
						intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
						startActivityForResult(intent, CROP_PHOTO);
					
					}
					else if(view.getId()==104){//to take photos
	
	
						File appDir = new File(Environment.
								getExternalStorageDirectory(), "Voyagephoto");
	
						if (!appDir.exists()) {
							appDir.mkdir();
						}
						//outputImage.createNewFile();
	
						date = new Date(System.currentTimeMillis()); 
						dateFormat = new SimpleDateFormat(
								"yyyyMMdd_HHmmss");
						fileName = "";
						fileName = dateFormat.format(date) + ".jpg";
	
						
						
					
						
						File outputImage=new File(appDir,fileName);
						image_path=outputImage.getAbsolutePath();
	
						imageUri = Uri.fromFile(outputImage);
						//Log.d("imageUri,oncreat",imageUri.toString());
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
						startActivityForResult(intent, TAKE_PHOTO); 
	
					
					}
				}
				else{
					Toast.makeText(MainActivity.this, "Sorry! You need a new voyage :(", Toast.LENGTH_SHORT).show();
				}
				
			}
			
		});

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		/**
		 * callback function of "taking photo" request,
		 * after taking a photo, go to "cropping photo" activity
		 */
		
		case TAKE_PHOTO:
			if (resultCode == RESULT_OK) {

				Log.d("take photo", "ok");
				// let's crop the photo
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");  //
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				/*
		        intent.putExtra("outputX", 640);  
		        intent.putExtra("outputY", 340); 
				 */
				intent.putExtra("aspectX", 3);
				intent.putExtra("aspectY", 4);

				Toast.makeText(MainActivity.this, "Cut photo", Toast.LENGTH_SHORT).show();

				startActivityForResult(intent, CROP_PHOTO); 
			}
			break;
			
			/**
			 * callback function of "cropping photo" request,
			 * after cropping a photo, go to "Finishphoto" activity
			 */
			
		case CROP_PHOTO:

			if (resultCode == RESULT_OK) {

				Log.d("After crop photo","case CROP_PHOTO:");
				
				
				try {
					Log.d("Crop photo", "ok");
					Bitmap bitmap = BitmapFactory.decodeStream
							(getContentResolver()
									.openInputStream(imageUri));

					//bitmap is the newest token picture
					//	picture.setImageBitmap(bitmap); 
					// input to the system album

					path_uri = MediaStore.Images.Media.insertImage(getContentResolver(),
							image_path,fileName, null);
					//Log.d("to insert image:image path absolut", path_uri);



					Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);  
					intentBc.setData(imageUri);       
					this.sendBroadcast(intentBc); 
					/*
					sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, 
							Uri.parse("file://"+ Environment.getExternalStorageDirectory()+ image_path)));       
					 */
					Log.d("Crop photo", "cancel");
					
					DataBase.Record r1 = new DataBase.Record();
					r1.date=date;
					r1.photo=imageUri;
			
					
					System.out.println("before find day");
					//DataBase.Day d = findDay(date,DataBase.list_user.get(0).voyage_list.get(0).list_day);
					
					if(position_user!=-1){
						position_voyage=findVoyage(voyage_name,position_user);
						if(position_voyage!=-1){
							int p=findDay(date,DataBase.list_user.get(position_user).voyage_list.get(position_voyage).list_day);
							System.out.println("After find day="+p);
							if(p!=-1){
								Log.d("have day","aaaaaaaaa");
								DataBase.list_user.get(position_user).voyage_list.get(position_voyage).list_day.get(p).list_record.add(r1);
								pos_record=DataBase.list_user.get(position_user).voyage_list.get(position_voyage).list_day.get(p).list_record.size()-1;
								pos_day=p;
							}
							else{
								DataBase.Day newday = new DataBase.Day();
								newday.date=date;
								newday.list_record.add(r1);
								newday.text="";
								DataBase.list_user.get(position_user).voyage_list.get(position_voyage).list_day.add(newday);
								pos_record=0;
								pos_day=DataBase.list_user.get(position_user).voyage_list.get(position_voyage).list_day.size()-1;
							}
						}
					}
					
					
					
					Intent intent = new Intent(MainActivity.this,FinishphotoActivity.class);
					intent.setDataAndType(imageUri, "image/*");  
					intent.putExtra("scale", true);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					intent.putExtra("posday", String.valueOf(pos_day));
					intent.putExtra("posrecord", String.valueOf(pos_record));
					intent.putExtra("pos_uu", position_user);
					intent.putExtra("pos_vv",position_voyage);
					//String datestring=dateFormat.format(date);
					System.out.println("dayinMain="+String.valueOf(pos_day));
					System.out.println("recinMain="+String.valueOf(pos_record));
					//Log.d("dateString", datestring);
					//intent.putExtra("day_record",dateFormat.format(date));
					startActivity(intent); 
					MainActivity.this.finish();


				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}  

			}
			else if(resultCode == RESULT_CANCELED ){
			}

			break;
		default:
			break;
		}
	}
	
	/*DataBase.Day findDay(Date d, List<DataBase.Day> l){

		DataBase.Day day=new DataBase.Day();
		for(int i=0; i<l.size();i++){
			if(d==l.get(i).date){
				day=l.get(i);
			}
			else day=null;
			
		}
		return day;
		
	}*/
	
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

	int findVoyage(String n, int p_usr){
		int p=-1;
		for(int i=0;i<DataBase.list_user.get(p_usr).voyage_list.size();i++){
			if(DataBase.list_user.get(p_usr).voyage_list.get(i).name.equals(n)){
				p=i;
			}
		}
		return p;
	}
	/**
	 * set the content of the content list when the voyage exist and not empty
	 */
	private void setViews() {         
		Log.d("forjdataddfe", photo_uri.get(0).toString());

		//contentListView = (ListView)findViewById(R.id.contentList);  
		SimpleAdapter adapter = new ImageSimpleAdapter(this, getDatas() ,R.layout.message_list  
				, new String[]{"icon","title","shortContent"}, new int[]{R.id.ml_icon,R.id.ml_title,R.id.ml_short_content});  
		contentListView.setAdapter(adapter);  
		contentListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		contentListView.setOnItemClickListener(new OnItemClickListener(){  
			@Override  
			public void onItemClick(AdapterView<?> av, View v, int position, long id) {  
							
				System.out.println("why!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+contentListView.getCheckedItemPosition());
				
			}  
		});  
	}  
	
	/**
	 * set content of the contentlist when there is no voyage or the voyage is empty
	 */
	private void setEmptyView(){
		SimpleAdapter adapter = new ImageSimpleAdapter(this, getEmptyDatas() ,R.layout.message_list  
				, new String[]{"icon","title","shortContent"}, new int[]{R.id.ml_icon,R.id.ml_title,R.id.ml_short_content});  
		contentListView.setAdapter(adapter);  
		contentListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		contentListView.setOnItemClickListener(new OnItemClickListener(){  
			@Override  
			public void onItemClick(AdapterView<?> av, View v, int position, long id) {  
							
				System.out.println("why!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+contentListView.getCheckedItemPosition());
				
			}  
		});  
		
	}
	
	/**
	 * the data in the content list
	 * @return
	 */
	private List<Map<String,Object>> getEmptyDatas() {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();  
		//input photo + diary
		
			Map<String,Object> map = new HashMap<String,Object>();  
			
			
			
			String datestr="Enrich your current voyage ;)";
			String textstr="";
			map.put("icon", R.drawable.deer);
			
			map.put("title", datestr);  
			map.put("shortContent", textstr);  
			list.add(map);  
		

	 
	
		return list;  
	}
	
	/**
	 * the date in the content list
	 * @return
	 */
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
			
			map.put("title", datestr);  
			map.put("shortContent", textstr);  
			list.add(map);  
		}  

	 
	
		return list;  
	}  
	
	
	/**
	 * callback function for "back" button
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK)
        {  
            exitBy2Click();		
        }
		return false;
	}
	/**
	 * double click to quit the application
	 */
	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // ready to quit
			Toast.makeText(this, "Press again to exit the program", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // cancel quit
				}
			}, 2000); // if there is no second click on "back" button within 2 seconds, cancel quitting  

		} else {
			//finish();
			//System.exit(0);
			
			/**
			 * quit the application
			 */
			ActivityCollector.finishAll();
			
		}
	}
	
}
