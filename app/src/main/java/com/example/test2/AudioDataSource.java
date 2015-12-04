package com.example.test2;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Environment;
/**
 * the class is for storing all the data of audio records
 * @author Administrator
 *
 */
public class AudioDataSource {
	List<String> audiolist; // the path name of the record
	List<String> audiolistname;// the name of the record
	HashMap<Integer,Integer> audiolistduree; // the duration of the record
	
	public AudioDataSource(){
		audiolist = new ArrayList<String>();
		audiolistname = new ArrayList<String>();
		audiolistduree = new HashMap <Integer,Integer>();
		String path=Environment.getExternalStorageDirectory().getAbsolutePath();
		path += "/VoyageAudio";
		path += "/audio";
		//can be optimizing!!!!
		int i=0;
		for(i=0;i<100;i++){
			path=Environment.getExternalStorageDirectory().getAbsolutePath();
			path += "/VoyageAudio";
			path += "/audio";
			path += i;
			path += ".amr";
			String name="audio";
			name += i;
						
			/*name+="          ";//10 spaces
			name+=String.valueOf((int)(audiolistduree.get(i) / 60));
			name+=":";
			int sec=(int)(audiolistduree.get(i) % 60);
			name+=(sec < 10? "0" + sec :String.valueOf(sec));  */
			
			/**
			 * initialization the record list
			 */
			
			File file=new File(path);
			if(file.exists()){
				audiolist.add(path);
				audiolistname.add(name);
				//audiolistduree.put(i,);
			}
			else{
				continue;
			}
		}
		
	}
	public void addAudioData(String path, String name){
		audiolist.add(path);
		audiolistname.add(name);
	}
	public void addAudioDuree(int n, int d){
		System.out.println("add duree");
		audiolistduree.put(n, d);
		System.out.println("add duree!!!!!");
	}
	/**
	 * delete one record by its id
	 * @param id
	 */
	public void deleteAudioData(int id){
		
		/*for(String str:this.audiolist){
			if(path==str)audiolist.remove(str);
		}*/
		audiolist.remove(id);
		audiolistname.remove(id);
	}
	public List<String> getAudiolist(){
		return audiolist;
	}
	public List<String> getAudiolistname(){
		return audiolistname;
	}
	public Map<Integer,Integer>getAudiolistduree(){
		return audiolistduree;
	}
	/**
	 * change the name of the record in order to display the duration in the list
	 * @param name
	 * @param num
	 */
	public void NameandDuree(String name,int num){
		String str;
		for(int i=0;i<audiolistname.size();i++){
			if(audiolistname.get(i)==name){
				str=audiolistname.get(i)+"          ";//10 spaces
				str+=String.valueOf((int)(audiolistduree.get(num) / 60));
				str+=":";
				int sec=(int)(audiolistduree.get(num) % 60);
				str+=(sec < 10? "0" + sec :String.valueOf(sec));  
				audiolistname.set(i, str);
			}
		}
		

	}

}
