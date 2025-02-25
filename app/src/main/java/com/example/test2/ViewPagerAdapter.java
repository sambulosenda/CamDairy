package com.example.test2;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ViewPagerAdapter extends PagerAdapter{
	//Interface List
    private List<View> views;
    
    public ViewPagerAdapter (List<View> views){
        this.views = views;
    }

    //Destruction arg1 position interface
    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(views.get(arg1));        
    }

    @Override
    public void finishUpdate(View arg0) {
        // TODO Auto-generated method stub
        
    }

    //Get the current number of the interface
    @Override
    public int getCount() {
        if (views != null)
        {
            return views.size();
        }
        
        return 0;
    }
    

    //Arg1 position initialization interface
    @Override
    public Object instantiateItem(View arg0, int arg1) {
        
        ((ViewPager) arg0).addView(views.get(arg1), 0);
        
        return views.get(arg1);
    }

    //Determine whether to generate the interface by an object
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Parcelable saveState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
        // TODO Auto-generated method stub
        
    }

}
