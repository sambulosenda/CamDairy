package com.example.test2;
/**
 * class used to store all the data needed
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;




import android.app.Activity;
import android.net.Uri;
import android.support.v4.util.Pair;

public class DataBase {

	public static List<user> list_user = new ArrayList<user>(); // record of all user
	public static boolean isLogin = false;
	
	
	static class Record {

		Date date;// current time
		Pair gps;
		
		Uri photo;// class Photo for recording
		static class Voice{
			String path="0";
			String name;
			int duree;
		}
		List<Voice> list_voice = new ArrayList<Voice>();
		// class Voice for the current photo
		String text;// comments for the current photo
		
	}


	static class Day {
		List<Record> list_record= new ArrayList<Record>();// list for all the photo in one day
		Date date;
		Voice voice= new Voice();// class Voice for the current day
		static class Voice{
			String path="0";
			String name;
			int duree;
		}
		String text; // comments for the current day
	}

	
	static class Voyage {

			String name;//name of voyage
			List<Day> list_day = new ArrayList<Day>();;//list for everyday record
			List<Date> date = new ArrayList<Date>();//2 dates for the first and the last day
			String text;//comments for the whole voyage
			String voice;// class Voice for the whole voyage
			boolean status;//status check for the current voyage
		//	FootPrint footprint = new FootPrint(); //record of path(with map)
						 
			List<Pair> list_gps =  new ArrayList<Pair>();
			
		
		}


	public static class user {
		
		List<Voyage> voyage_list =  new ArrayList<Voyage>();;//list of existing record of voyage
		private String name;//username
		private String password;
		private String email;
		
		private String city;
		private String age;
		private String sex;
		
		public static boolean isOn = false;
		
		public String getname(){
			return name;
		}
		
		public String getpassword(){
			return password;
		}
		
		public String getemail(){
			return email;
		}
		
		public String getcity(){
			return city;
		}
		
		public String getage(){
			return age;
		}
		
		public String getsex(){
			return sex;
		}
		
		public void setname(String n){
			name = n;
		}
		
		public void setpassword(String p){
			password = p;
		}
		
		public void setemail(String e){
			email = e;
		}
		

		public void setcity(String n){
			city = n;
		}
		
		public void setage(String p){
			age = p;
		}
		
		public void setsex(String e){
			sex = e;
		}
		
	} 
	

	
}
