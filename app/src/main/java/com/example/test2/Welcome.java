package com.example.test2;


import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Welcome extends Activity implements OnClickListener, OnPageChangeListener{
	
	private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    
    //import guide pages
    private static final int[] pics = { R.drawable.a1,
            R.drawable.a2, R.drawable.a3};
    
    //small figures showing the current page
    private ImageView[] dots ;
    
    private Intent intent;
    private Button startButton; 
    
    private int currentIndex;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        
        ActivityCollector.addActivity(this);
        
        SharedPreferences preferences = getSharedPreferences(
      	      "first_pref", MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putBoolean("isFirstIn", false);
        editor.commit();
        
        views = new ArrayList<View>();
       
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        
        for(int i=0; i<pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setImageResource(pics[i]);
            views.add(iv);
        }
        vp = (ViewPager) findViewById(R.id.viewpager);
        vpAdapter = new ViewPagerAdapter(views);
        vp.setAdapter(vpAdapter);
        vp.setOnPageChangeListener(this);
        
        //show and change the picture of small figures
        initDots();
        
        //end guide pages when press the button start
        startButton = (Button)findViewById(R.id.startButton);  
        
        intent = new Intent(Welcome.this, Sign.class);
        startButton.setOnClickListener(new  OnClickListener(){  

           @Override  
           public void onClick(View arg0) {  
               Welcome.this.startActivity(intent); //go to the page of log in   
               Welcome.this.finish();   
           }  
          
        });
        
    }
    

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[pics.length];
        
        for (int i = 0; i < pics.length; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);
    }
    

    private void setCurView(int position)
    {
        if (position < 0 || position >= pics.length) {
            return;
        }

        vp.setCurrentItem(position);
    }


    private void setCurDot(int positon)
    {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }

        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = positon;
        
        if(currentIndex == pics.length-1)  
        {  
            startButton.setVisibility(View.VISIBLE); 
        }  
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onPageSelected(int arg0) {
        setCurDot(arg0);
    }

    @Override
    public void onClick(View v) {
        int position = (Integer)v.getTag();
        setCurView(position);
        setCurDot(position);
    }
    
    public void click() {
        setCurView(1);
        setCurDot(1);
    }
    
    private void turnPage() {
    	Intent intent = new Intent(Welcome.this, MainActivity.class);
    	Welcome.this.startActivity(intent);
    	Welcome.this.finish();
    	}


}
