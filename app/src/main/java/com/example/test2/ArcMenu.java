package com.example.test2;

import com.example.test2.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

/**
 * The class for creating a satellite menu
 * @author Administrator
 *
 */

public class ArcMenu extends ViewGroup implements OnClickListener {
	
	public static final int TAKE_PHOTO = 1;
	public static final int CROP_PHOTO = 2;
	private static final int FINISH_PHOTO = 3;
	private Button takePhoto,dairy,chooseFromAlbum;
	private ImageView picture;
	private Uri imageUri;
	private String fileName;
	private String path_uri;
	private String image_path;
	

	private Position mPosition = Position.RIGHT_BOTTOM;
	private int mRadius;// radius of the menu

	private Status mCurrStatus = Status.CLOSE;// current situation

	private View mCButton;// the main button of the menu

	private onMenuItemClickListener mMenuItemClickListener;

	public static final int POS_LEFT_TOP = 0;
	public static final int POS_LEFT_BOTTOM = 1;
	public static final int POS_RIGHT_TOP = 2;
	public static final int POS_RIGHT_BOTTOM = 3;

	public enum Status {
		OPEN, CLOSE
	}

	/** position of the menu */
	public enum Position {
		LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
	}

	/**
	 * click on the menu item
	 * @author Administrator
	 *
	 */
	public interface onMenuItemClickListener {
		void onclick(View view, int position);
	}

	public void setOnMenuItemClickListener(
			onMenuItemClickListener mMenuItemClickListener) {
		this.mMenuItemClickListener = mMenuItemClickListener;
	}

	public ArcMenu(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public ArcMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public ArcMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				100, getResources().getDisplayMetrics());

		// in order to get values of the class
		TypedArray array = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.AreMenu, defStyle, 0);
		int pos = array.getInt(R.styleable.AreMenu_position, POS_RIGHT_BOTTOM);
		switch (pos) {
		case POS_LEFT_TOP:
			mPosition = Position.LEFT_TOP;
			break;
		case POS_LEFT_BOTTOM:
			mPosition = Position.LEFT_BOTTOM;
			break;
		case POS_RIGHT_TOP:
			mPosition = Position.RIGHT_TOP;
			break;
		case POS_RIGHT_BOTTOM:
			mPosition = Position.RIGHT_BOTTOM;
			break;
		}
		mRadius = (int) array.getDimension(R.styleable.AreMenu_radius,
				TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80,
						getResources().getDisplayMetrics()));//100 change radius of child menu
		System.out.println("postion=" + mPosition);
		System.out.println("Radius=" + mRadius);
		array.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			layoutCButton();
			int count = getChildCount();
			for (int i = 0; i < count - 1; i++) {
				View child = getChildAt(i + 1);
				child.setVisibility(View.GONE);
				child.setId(100+i+1);
				int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) //child menu's layout design automatically by count
						* i));
				int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2)
						* i));

				int cWidth = child.getMeasuredWidth();
				int cHeight = child.getMeasuredHeight();

				// if the position of the menu is at left-bottom or right-bottom
				if (mPosition == Position.LEFT_BOTTOM
						|| mPosition == Position.RIGHT_BOTTOM) {
					ct = getMeasuredHeight() - cHeight - ct;
				}
				// if the position of the menu is at left-top or right-bottom
				if (mPosition == Position.RIGHT_TOP
						|| mPosition == Position.RIGHT_BOTTOM) {
					cl = getMeasuredWidth() - cWidth - cl;
				}
				child.layout(cl, ct, cl + cWidth, ct + cHeight);

			}

		}

	}

	/**
	 * get the position of the main button
	 */
	private void layoutCButton() {
		mCButton = getChildAt(0);
		mCButton.setOnClickListener(this);
		int l = 0;
		int t = 0;
		int width = mCButton.getMeasuredWidth();
		int height = mCButton.getMeasuredHeight();
		switch (mPosition) {
		case LEFT_TOP:
			l = 0;
			t = 0;
			break;
		case LEFT_BOTTOM:
			l = 0;
			t = getMeasuredHeight() - height;
			break;
		case RIGHT_TOP:
			l = getMeasuredWidth() - width;
			t = 0;
			break;
		case RIGHT_BOTTOM:
			l = getMeasuredWidth() - width;
			t = getMeasuredHeight() - height;
			break;
		}
		mCButton.layout(l, t, l + width, t + width);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		rotateCButton(v, 0f, 360f, 300);// v, at first 0f,start 360f&#xfffd;&#xfffd;end 300 time
		System.out.println("click on the mainmenu");
		System.out.println(getChildCount());

		toggleMenu(300);
	}

	/**
	 *  change the menu
	 * @param duration
	 */
	private void toggleMenu(int duration) {
		// TODO Auto-generated method stub
		// add translation and rotation animation for the menu item
		int count = getChildCount();
		for (int i = 0; i < count - 1; i++) {
			final View childView = getChildAt(i + 1);
			childView.setVisibility(View.VISIBLE);
			// end 0,0
			// start
			int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));
			int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));

			int xflag = 1;
			int yflag = 1;
			if (mPosition == Position.LEFT_TOP
					|| mPosition == Position.LEFT_BOTTOM) {
				xflag = -1;
			}
			if (mPosition == Position.LEFT_TOP
					|| mPosition == Position.RIGHT_TOP) {
				yflag = -1;
			}
			AnimationSet animationSet = new AnimationSet(true);
			Animation traAnimation = null;
			// to open
			if (mCurrStatus == Status.CLOSE) {
				traAnimation = new TranslateAnimation(xflag * cl, 0,
						yflag * ct, 0);
				childView.setClickable(true);
				childView.setFocusable(true);

			} else {
				// to close
				traAnimation = new TranslateAnimation(0, xflag * cl, 0, yflag
						* ct);
				childView.setClickable(false);
				childView.setFocusable(false);
			}
			traAnimation.setFillAfter(true);
			traAnimation.setDuration(duration);

			traAnimation.setStartOffset((i * 100) / count);

			traAnimation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation arg0) {
					// TODO Auto-generated method stub
					if (mCurrStatus == Status.CLOSE) {
						childView.setVisibility(View.GONE);
					}
				}
			});

			/**
			 *  rotation animation
			 */
			RotateAnimation rotateAnimation = new RotateAnimation(0, 720,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rotateAnimation.setDuration(duration);
			rotateAnimation.setFillAfter(true);
			animationSet.addAnimation(rotateAnimation);
			animationSet.addAnimation(traAnimation);
			childView.startAnimation(animationSet);

			final int pos = i + 1;
			childView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (mMenuItemClickListener != null)
						mMenuItemClickListener.onclick(childView, pos);
					menuItemAnim(pos - 1);
					changeStatus();
					
					System.out.println("click~~~~"+v.getId());
				}
			});

		}
		// change the status of the menu
		changeStatus();

	}

	/**
	 *  add animation when click on the menu item
	 * @param pos
	 */
	private void menuItemAnim(int pos) {
		// TODO Auto-generated method stub
		for (int i = 0; i < getChildCount() - 1; i++) {
			View childView = getChildAt(i + 1);
			if (i == pos) {
				childView.startAnimation(scaleBigAnim(300));
			} else {

				childView.startAnimation(scaleSmallAnim(300));
			}

			childView.setClickable(false);
			childView.setFocusable(false);
		}
	}

	private Animation scaleSmallAnim(int duration) {
		// TODO Auto-generated method stub
		AnimationSet animationSet = new AnimationSet(true);

		ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0.0f);
		animationSet.addAnimation(scaleAnim);
		animationSet.addAnimation(alphaAnim);
		animationSet.setDuration(duration);
		animationSet.setFillAfter(true);
		return animationSet;
	}

	/**
	 * when click on the menu item, it will turn bigger and decrease the degree of transparent
	 * @param duration
	 * @return
	 */
	private Animation scaleBigAnim(int duration) {
		// TODO Auto-generated method stub
		AnimationSet animationSet = new AnimationSet(true);

		ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0.0f);

		animationSet.addAnimation(scaleAnim);
		animationSet.addAnimation(alphaAnim);

		animationSet.setDuration(duration);
		animationSet.setFillAfter(true);
		return animationSet;
	}

	private void changeStatus() {
		// TODO Auto-generated method stub
		mCurrStatus = (mCurrStatus == Status.CLOSE ? Status.OPEN : Status.CLOSE);
	}

	private void rotateCButton(View v, float start, float end, int duration) {
		// TODO Auto-generated method stub
		RotateAnimation animation = new RotateAnimation(start, end,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation.setDuration(duration);
		animation.setFillAfter(true);
		v.startAnimation(animation);

	}

}
