package com.example.test2;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/*
 * 
 * class used to manage all opened activities
 * 
 */

public class ActivityCollector {
	public static List<Activity> activities = new ArrayList<Activity>();
	public static void addActivity(Activity activity) {
		activities.add(activity);
	}
	public static void removeActivity(Activity activity) {
		activities.remove(activity);
	}
	/*
	 * finish all the activities, to quit the application
	 */
	public static void finishAll() {
		for (Activity activity : activities) {
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
	}
}