package com.example.test2;

import java.util.Iterator;







import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GpsActivity extends Activity
{
	private EditText editText;  
    private LocationManager lm;  
    private static final String TAG = "GpsActivity";  
    
    private ListView contentListView;  
	private ArrayAdapter<String> contentListAdapter;  

	private SlidingLayout slidingLayout;
	private ListView menuListView;
	private ArrayAdapter<String> menuListAdapter;  
	private String[] menuItems = {"Main Page","My account", "My voyages", "System setting","Find more" }; 
	private String[] contentItems = { "Language", "Personnel information", "Clean all date",  
			"Suggestion", "About us","My GPS"}; 

	private Button menuButton;
	private String current_voyage;
	private int user_pos;

  
    @Override  
    protected void onDestroy() {  
        // TODO Auto-generated method stub  
        super.onDestroy();  
        lm.removeUpdates(locationListener);  
    }  
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_gps);  
        ActivityCollector.addActivity(this);
  
        Intent intent7=getIntent();
		current_voyage=intent7.getStringExtra("name_voyage");
		user_pos=intent7.getIntExtra("p_user",-1);
        
        editText = (EditText) findViewById(R.id.editText);  
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
        
        
        menuButton = (Button) findViewById(R.id.backButton);
		menuButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			Intent intent1 = new Intent(GpsActivity.this,MainActivity.class);
	
			intent1.putExtra("name_voyage", current_voyage);
			intent1.putExtra("p_user",user_pos);
			startActivity(intent1);
			}
		});
        
        
  
        // GPS determine whether the normal start  
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {  
            Toast.makeText(this, "Please turn GPS navigation...", Toast.LENGTH_SHORT).show();  
            // Back On GPS navigation setup interface 
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
            startActivityForResult(intent, 0);  
            return;  
        }  
  
        // Set the query conditions to obtain location information 
        String bestProvider = lm.getBestProvider(getCriteria(), true);  
        // Obtain location information 
        // If you do not set the query requirements, getLastKnownLocation method parameters for the successor LocationManager.GPS_PROVIDER 
        Location location = lm.getLastKnownLocation(bestProvider);  
        updateView(location);  
        // Listening state 
        lm.addGpsStatusListener(listener);  
        // Binding monitor, there are four parameters 
        // Parameter 1, equipment: two kinds GPS_PROVIDER and NETWORK_PROVIDER  
        // Parameter 2, the location information update cycle, in milliseconds 
        // Parameter 3, position changes the minimum distance: When the position from the change exceeds this value, it will update the location information  
        // Parameter 4, monitor  
        // Note: Parameter 2 and 3, if parameter 3 is not 0, places parameter 3 prevail; Parameter 3 to 0, then by the time to regularly update; both to 0, then any time to refresh  
  
        // 1 second update once, or more than one meter minimum displacement updated;
        // Note: Update accuracy here is very low, it is recommended to start a service inside Thread, in the run in sleep (10000); then perform handler.sendMessage (), update the position 
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);  
      //************ sliding menu*****************************//
		slidingLayout = (SlidingLayout) findViewById(R.id.slidingLayout);

		//************** Menu ListView **************************************
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
				Intent intent0 = new Intent(GpsActivity.this,MainActivity.class);
				startActivity(intent0);
				
				break;
			case 1:  //my account
				Intent intent1 = new Intent(GpsActivity.this,MyAccountActivity.class);
				startActivity(intent1);
				break;
			case 2: //my voyage
				Intent intent2 = new Intent(GpsActivity.this,MyVoyageActivity.class);
				startActivity(intent2);
				break;
			case 3: //system setting
				Intent intent3 = new Intent(GpsActivity.this,SystemSettingActivity.class);
				startActivity(intent3);
				break;
			case 4: // find more
				Intent intent4 = new Intent(GpsActivity.this,FindMoreActivity.class);
				startActivity(intent4);
				break;
			}
			
			
			}
		});
    }  
  
    // Position monitor  
    private LocationListener locationListener = new LocationListener() {  
  
        /** 
         * Location information changes triggered 
         */  
        public void onLocationChanged(Location location) {  
            updateView(location);  
            Log.i(TAG, "Time：" + location.getTime());  
            Log.i(TAG, "Longitude:" + location.getLongitude());  
            Log.i(TAG, "Latitude:" + location.getLatitude());  
            Log.i(TAG, "Altitude：" + location.getAltitude());  
        }  
  
        /** 
         * GPS status changes triggered 
         */  
        public void onStatusChanged(String provider, int status, Bundle extras) {  
            switch (status) {  
            // GPS status is visible  
            case LocationProvider.AVAILABLE:  
                Log.i(TAG, "The current GPS state is visible");  
                break;  
            // GPS status is outside the service area  
            case LocationProvider.OUT_OF_SERVICE:  
                Log.i(TAG, "The current GPS status is outside service area state");  
                break;  
            // GPS status is suspended  
            case LocationProvider.TEMPORARILY_UNAVAILABLE:  
                Log.i(TAG, "The current GPS status is suspended status");  
                break;  
            }  
        }  
  
        /** 
         *When the GPS is on the trigger 
         */  
        public void onProviderEnabled(String provider) {  
            Location location = lm.getLastKnownLocation(provider);  
            updateView(location);  
        }  
  
        /** 
         * GPS trigger disabled 
         */  
        public void onProviderDisabled(String provider) {  
            updateView(null);  
        }  
  
    };  
  
    // Status monitor  
    GpsStatus.Listener listener = new GpsStatus.Listener() {  
        public void onGpsStatusChanged(int event) {  
            switch (event) {  
            // First Fix 
            case GpsStatus.GPS_EVENT_FIRST_FIX:  
                Log.i(TAG, "First Fix");  
                break;  
            // Satellite status change 
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:  
                Log.i(TAG, "Satellite status change");  
                // Get the current status  
                GpsStatus gpsStatus = lm.getGpsStatus(null);  
                // Gets the default maximum number of pieces of satellites 
                int maxSatellites = gpsStatus.getMaxSatellites();  
                // Create an iterator save all satellites 
                Iterator<GpsSatellite> iters = gpsStatus.getSatellites()  
                        .iterator();  
                int count = 0;  
                while (iters.hasNext() && count <= maxSatellites) {  
                    GpsSatellite s = iters.next();  
                    count++;  
                }  
                System.out.println("Searched：" + count + "Satellites");  
                break;  
            // Positioning start  
            case GpsStatus.GPS_EVENT_STARTED:  
                Log.i(TAG, "Positioning start");  
                break;  
            // End Location 
            case GpsStatus.GPS_EVENT_STOPPED:  
                Log.i(TAG, "End Location");  
                break;  
            }  
        };  
    };  
  
    /** 
     * Live Update text 
     *  
     * @param location 
     */  
    private void updateView(Location location) {  
        if (location != null) {  
            editText.setText("Device location information\n\nLongitude：");  
            editText.append(String.valueOf(location.getLongitude()));  
            editText.append("\nLatitude：");  
            editText.append(String.valueOf(location.getLatitude()));  
        } else {  
            // Empty the object EditText 
            editText.getEditableText().clear();  
        }  
    }  
  
    /** 
     * Return query condition 
     *  
     * @return 
     */  
    private Criteria getCriteria() {  
        Criteria criteria = new Criteria();  
        // Set positioning accuracy Criteria.ACCURACY_COARSE sketchy, Criteria.ACCURACY_FINE is relatively fine 
        criteria.setAccuracy(Criteria.ACCURACY_FINE);  
        // Set whether for speed 
        criteria.setSpeedRequired(false);  
        // Set whether to allow operators to charge  
        criteria.setCostAllowed(false);  
        // Whether setting requires location information 
        criteria.setBearingRequired(false);  
        // Set whether altitude information 
        criteria.setAltitudeRequired(false);  
        // Set the demand for power 
        criteria.setPowerRequirement(Criteria.POWER_LOW);  
        return criteria;  
    }  
}  