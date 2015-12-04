package com.example.test2;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;



/**
 * 
 *  Class : l'implementation du Sliding Menu
 *  
 */

public class SlidingLayout extends LinearLayout implements OnTouchListener {

	
	public static final int SNAP_VELOCITY = 200;
	private int screenWidth;

	/**
	 * left edge of sliding menu
	 */
	private int leftEdge;

	/**
	 * right edge of sliding menu = 0
	 */
	private int rightEdge = 0;

	/**
	 * the width of content view , when the sliding menu is shown completely 
	 */
	private int leftLayoutPadding = 100;

	private float xDown;
	private float xMove;
	private float xUp;
	private boolean isLeftLayoutVisible;

	/**
	 * Left layout view
	 */
	private View leftLayout;

	/**
	 * Right layout view
	 */
	private View rightLayout;

	/**
	 * view used to listen to the sliding event
	 */
	private View mBindView;

	private MarginLayoutParams leftLayoutParams;
	private MarginLayoutParams rightLayoutParams;
	private VelocityTracker mVelocityTracker;

	/**
	 * override the constructor of SlidingLayout, getting the width of screen
	 * 
	 * @param context
	 * @param attrs
	 */
	public SlidingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		screenWidth = wm.getDefaultDisplay().getWidth();
	}

	/**
	 *
	 * 
	 * @param bindView
	 *            
	 */
	public void setScrollEvent(View bindView) {
		mBindView = bindView;
		mBindView.setOnTouchListener(this);
	}

	/**
	 * setting the speed of sliding = 30, to show left view
	 */
	public void scrollToLeftLayout() {
		new ScrollTask().execute(30);
	}

	/**
	 * setting the speed of sliding = -30, to show right view
	 */
	public void scrollToRightLayout() {
		new ScrollTask().execute(-30);
	}

	public boolean isLeftLayoutVisible() {
		return isLeftLayoutVisible;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
	
			leftLayout = getChildAt(0);
			leftLayoutParams = (MarginLayoutParams) leftLayout.getLayoutParams();
			leftLayoutParams.width = screenWidth - leftLayoutPadding;
			// setting the width of left view
			leftEdge = -leftLayoutParams.width;
			leftLayoutParams.leftMargin = leftEdge;
			leftLayout.setLayoutParams(leftLayoutParams);
			// getting the object of right layout
			rightLayout = getChildAt(1);
			rightLayoutParams = (MarginLayoutParams) rightLayout.getLayoutParams();
			rightLayoutParams.width = screenWidth;
			rightLayout.setLayoutParams(rightLayoutParams);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		createVelocityTracker(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// finger down
			xDown = event.getRawX();
			break;
		case MotionEvent.ACTION_MOVE:
			// finger move
			xMove = event.getRawX();
			int distanceX = (int) (xMove - xDown);
			if (isLeftLayoutVisible) {
				leftLayoutParams.leftMargin = distanceX;
			} else {
				leftLayoutParams.leftMargin = leftEdge + distanceX;
			}
			if (leftLayoutParams.leftMargin < leftEdge) {
				leftLayoutParams.leftMargin = leftEdge;
			} else if (leftLayoutParams.leftMargin > rightEdge) {
				leftLayoutParams.leftMargin = rightEdge;
			}
			leftLayout.setLayoutParams(leftLayoutParams);
			break;
		case MotionEvent.ACTION_UP:
			// finger up
			xUp = event.getRawX();
			if (wantToShowLeftLayout()) {
				if (shouldScrollToLeftLayout()) {
					scrollToLeftLayout();
				} else {
					scrollToRightLayout();
				}
			} else if (wantToShowRightLayout()) {
				if (shouldScrollToContent()) {
					scrollToRightLayout();
				} else {
					scrollToLeftLayout();
				}
			}
			recycleVelocityTracker();
			break;
		}
		return isBindBasicLayout();
	}

	
	private boolean wantToShowRightLayout() {
		return xUp - xDown < 0 && isLeftLayoutVisible;
	}

	
	private boolean wantToShowLeftLayout() {
		return xUp - xDown > 0 && !isLeftLayoutVisible;
	}

	
	private boolean shouldScrollToLeftLayout() {
		return xUp - xDown > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
	}


	private boolean shouldScrollToContent() {
		return xDown - xUp + leftLayoutPadding > screenWidth / 2
				|| getScrollVelocity() > SNAP_VELOCITY;
	}


	private boolean isBindBasicLayout() {
		if (mBindView == null) {
			return false;
		}
		String viewName = mBindView.getClass().getName();
		return viewName.equals(LinearLayout.class.getName())
				|| viewName.equals(RelativeLayout.class.getName())
				|| viewName.equals(FrameLayout.class.getName())
				|| viewName.equals(TableLayout.class.getName());
	}


	private void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getXVelocity();
		return Math.abs(velocity);
	}

	private void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	class ScrollTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... speed) {
			int leftMargin = leftLayoutParams.leftMargin;
			
			while (true) {
				leftMargin = leftMargin + speed[0];
				if (leftMargin > rightEdge) {
					leftMargin = rightEdge;
					break;
				}
				if (leftMargin < leftEdge) {
					leftMargin = leftEdge;
					break;
				}
				publishProgress(leftMargin);
				sleep(20);
			}
			if (speed[0] > 0) {
				isLeftLayoutVisible = true;
			} else {
				isLeftLayoutVisible = false;
			}
			return leftMargin;
		}

		@Override
		protected void onProgressUpdate(Integer... leftMargin) {
			leftLayoutParams.leftMargin = leftMargin[0];
			leftLayout.setLayoutParams(leftLayoutParams);
		}

		@Override
		protected void onPostExecute(Integer leftMargin) {
			leftLayoutParams.leftMargin = leftMargin;
			leftLayout.setLayoutParams(leftLayoutParams);
		}
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}