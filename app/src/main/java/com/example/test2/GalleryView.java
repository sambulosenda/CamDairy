package com.example.test2;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryView extends Gallery 
{
	private Camera mCamera = new Camera();
	private int mMaxRotationAngle = 45;		// the maxmiun rotation angle
	private int mMaxZoom = -120;
	private int mCoveflowCenter;

	public GalleryView(Context context) 
	{
		super(context);
		this.setStaticTransformationsEnabled(true);
	}

	public GalleryView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		this.setStaticTransformationsEnabled(true);
	}

	public GalleryView(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
		this.setStaticTransformationsEnabled(true);
	}

	public int getMaxRotationAngle() 
	{
		return mMaxRotationAngle;
	}

	public void setMaxRotationAngle(int maxRotationAngle) 
	{
		mMaxRotationAngle = maxRotationAngle;
	}

	public int getMaxZoom() 
	{
		return mMaxZoom;
	}

	public void setMaxZoom(int maxZoom) 
	{
		mMaxZoom = maxZoom;
	}

	/** get the x of the centre of Gallery */
	private int getCenterOfCoverflow() 
	{
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
	}

	/** get the x of the centre of View */
	private static int getCenterOfView(View view) {
		return view.getLeft() + view.getWidth() / 2;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) 
	{
		mCoveflowCenter = getCenterOfCoverflow();
		super.onSizeChanged(w, h, oldw, oldh);
	} 

	@Override
	protected boolean getChildStaticTransformation(View child, Transformation trans) 
	{
		final int childCenter = getCenterOfView(child);
		final int childWidth = child.getWidth();
		int rotationAngle = 0;

		trans.clear();
		trans.setTransformationType(Transformation.TYPE_BOTH);		// alpha and matrix both change

		if (childCenter == mCoveflowCenter) 
		{	// childView in the middle
			transformImageBitmap((ImageView) child, trans, 0);	
		} 
		else 
		{		// childView in both sides
			rotationAngle = (int) ( ( (float) (mCoveflowCenter - childCenter) / childWidth ) * mMaxRotationAngle );
			if (Math.abs(rotationAngle) > mMaxRotationAngle) 
			{
				rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle : mMaxRotationAngle;
			}
			transformImageBitmap((ImageView) child, trans, rotationAngle);
		}

		return true;
	}

	private void transformImageBitmap(ImageView child, Transformation trans, int rotationAngle) 
	{
		mCamera.save();
		
		final Matrix imageMatrix = trans.getMatrix();
		final int imageHeight = child.getLayoutParams().height;
		final int imageWidth = child.getLayoutParams().width;
		final int rotation = Math.abs(rotationAngle);

		// In the Z-axis moving forward camera perspective, the actual effect is an enlarged image; if you move the Y-axis, the picture moves up and down; X-axis corresponding to the picture and move around.
		mCamera.translate(0.0f, 0.0f, -20.0f);

		// As the angle of the view gets less, zoom in
		if (rotation < mMaxRotationAngle)
		{
			float zoomAmount = (float) (mMaxZoom + (rotation * 1.0));
			mCamera.translate(0.0f, 0.0f, zoomAmount);
		}

		mCamera.rotateY(rotationAngle);		//rotationAngle is positive, the rotation along the y axis within; is negative, the rotation along the y axis outside
		
		mCamera.getMatrix(imageMatrix);
		imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
		imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
		
		mCamera.restore();
	}
}