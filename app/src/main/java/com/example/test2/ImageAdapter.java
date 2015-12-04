package com.example.test2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImageAdapter extends BaseAdapter 
{
	private ImageView[] mImages;		

	private Context mContext;
	public List<Map<String, Object>> list;
	

	public Integer[] imgs = { R.drawable.rs01, R.drawable.rs02, R.drawable.rs03,
							  R.drawable.rs04, R.drawable.rs05};
	public String[] titles = { "Swisse01", "Swisse02", "Swisse03", "Swisse04", "Swisse05", "Swisse06", "Swisse07"};

	public ImageAdapter(Context c) 
	{
		this.mContext = c;
		list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < imgs.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("image", imgs[i]);
			list.add(map);
		}
		mImages = new ImageView[list.size()];
	}

	/** Reflex reflection*/
	public boolean createReflectedImages() 
	{
		final int reflectionGap = 4;
		final int Height = 200;
		int index = 0;
		for (Map<String, Object> map : list) {
			Integer id = (Integer) map.get("image");
			//  get the original picture
			Bitmap originalImage = BitmapFactory.decodeResource(mContext.getResources(), id);	
			int width = originalImage.getWidth();
			int height = originalImage.getHeight();
			float scale = Height / (float)height;
			
			Matrix sMatrix = new Matrix();
			sMatrix.postScale(scale, scale);
			Bitmap miniBitmap = Bitmap.createBitmap(originalImage, 0, 0,
					originalImage.getWidth(), originalImage.getHeight(), sMatrix, true);
			
			// If the original image data to save memory
			originalImage.recycle();

			int mwidth = miniBitmap.getWidth();
			int mheight = miniBitmap.getHeight();
			Matrix matrix = new Matrix();
			matrix.preScale(1, -1);			// Image matrix transformations (reflection from the lower part of the top)
			Bitmap reflectionImage = Bitmap.createBitmap(miniBitmap, 0, mheight/2, mwidth, mheight/2, matrix, false);	// Bottom half interception picture
			Bitmap bitmapWithReflection = Bitmap.createBitmap(mwidth, (mheight + mheight / 2), Config.ARGB_8888);		// Create the reflection image (height picture 3/2)

			Canvas canvas = new Canvas(bitmapWithReflection);	// Draw the reflection images (picture + pitch + reflection)
			canvas.drawBitmap(miniBitmap, 0, 0, null);		// Draw picture
			Paint paint = new Paint();
			canvas.drawRect(0, mheight, mwidth, mheight + reflectionGap, paint);		// Draw original pitch and reflection
			canvas.drawBitmap(reflectionImage, 0, mheight + reflectionGap, null);	// Draw the reflection of FIG.

			paint = new Paint();
			LinearGradient shader = new LinearGradient(0, miniBitmap.getHeight(), 0, bitmapWithReflection.getHeight()
					+ reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
			paint.setShader(shader);	// Linear gradient
			paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));		// Reflection mask effect
			canvas.drawRect(0, mheight, mwidth, bitmapWithReflection.getHeight() + reflectionGap, paint);		// Draw the reflection of the shadow effect

			ImageView imageView = new ImageView(mContext);
			imageView.setImageBitmap(bitmapWithReflection);		// Set the reflection image
			imageView.setLayoutParams(new GalleryView.LayoutParams((int)(width * scale),
					(int)(mheight * 3 / 2.0 + reflectionGap)));
			imageView.setScaleType(ScaleType.MATRIX);
			mImages[index++] = imageView;
		}
		return true;
	}

	@Override
	public int getCount() {
		return imgs.length;
	}

	@Override
	public Object getItem(int position) {
		return mImages[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return mImages[position];		// Show the reflection image (current acquisition focus)
	}

	public float getScale(boolean focused, int offset) {
		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
	}

}
