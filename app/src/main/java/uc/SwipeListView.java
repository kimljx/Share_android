package uc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import cn.share.R;

public class SwipeListView extends ListView implements AbsListView.OnScrollListener {
	private Boolean mIsHorizontal;
	private View mPreItemView;
	private View mCurrentItemView;
	private float mFirstX;
	private float mFirstY;
	private int mRightViewWidth;
	private final int mDuration = 100;
	private final int mDurationStep = 10;
	private boolean mIsShown;

	public SwipeListView(Context context) {

		this(context, null);
		initWithContext(context);
	}

	public SwipeListView(Context context, AttributeSet attrs) {

		this(context, attrs, 0);
		initWithContext(context);
	}

	public SwipeListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mCurrentItemView = new View(context);
		mCurrentItemView.setEnabled(false);
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.swipelistviewstyle);
		// 获取自定义属性和默认值
		mRightViewWidth = (int) mTypedArray.getDimension(R.styleable.swipelistviewstyle_right_width, 200);
		mTypedArray.recycle();
	}

	/**
	 * return true, deliver to listView. return false, deliver to child. if
	 * move, return true
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		float lastX = ev.getX();
		float lastY = ev.getY();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mIsHorizontal = null;
			mFirstX = lastX;
			mFirstY = lastY;
			int motionPosition = pointToPosition((int) mFirstX, (int) mFirstY);

			if (motionPosition >= 0) {
				View currentItemView = getChildAt(motionPosition - getFirstVisiblePosition());
				mPreItemView = mCurrentItemView;
				mCurrentItemView = currentItemView;

			}
			break;

		case MotionEvent.ACTION_MOVE:
			float dx = lastX - mFirstX;
			float dy = lastY - mFirstY;

			if (Math.abs(dx) >= 5 && Math.abs(dy) >= 5) {
				return true;
			}

			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (mIsShown && (mPreItemView != mCurrentItemView || isHitCurItemLeft(lastX))) {
				/**
				 * 情况一： 一个Item的右边布局已经显示， 这时候点击任意一个item, 那么那个右边布局显示的item隐藏其右边布局
				 */
				hiddenRight(mPreItemView);
			}
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	private boolean isHitCurItemLeft(float x) {
		return x < getWidth() - mRightViewWidth;
	}

	/**
	 * @param dx
	 * @param dy
	 * @return judge if can judge scroll direction
	 */
	private boolean judgeScrollDirection(float dx, float dy) {
		boolean canJudge = true;
		if (Math.abs(dx) > 30 && Math.abs(dx) > 2 * Math.abs(dy)) {
			mIsHorizontal = true;
		} else if (Math.abs(dy) > 30 && Math.abs(dy) > 2 * Math.abs(dx)) {
			mIsHorizontal = false;
		} else {
			canJudge = false;
		}
		return canJudge;
	}

	/**
	 * return false, can't move any direction. return true, cant't move
	 * vertical, can move horizontal. return super.onTouchEvent(ev), can move
	 * both.
	 */
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		float lastX = ev.getX();
		float lastY = ev.getY();
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//
			mLastY = ev.getRawY();
			break;

		case MotionEvent.ACTION_MOVE:
			//
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			if (getFirstVisiblePosition() == 0
					&& (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)
					&& mEnablePullRefresh) {
				// the first item is showing, header has shown or pull down.
				updateHeaderHeight(deltaY / OFFSET_RADIO);
				invokeOnScrolling();
			} else if (getLastVisiblePosition() == mTotalItemCount - 1
					&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)
					&& mEnablePullLoad) {
				// last item, already pulled up or want to pull up.
				updateFooterHeight(-deltaY / OFFSET_RADIO);
			}
			//
			if (!mCurrentItemView.isEnabled()) {
				return true;
			}
			float dx = lastX - mFirstX;
			float dy = lastY - mFirstY;
			if (mIsHorizontal == null) {
				if (!judgeScrollDirection(dx, dy)) {
					break;
				}
			}

			if (mIsHorizontal) {
				if (mIsShown && mPreItemView != mCurrentItemView) {
					/**
					 * 情况二： 一个Item的右边布局已经显示，
					 * 这时候左右滑动另外一个item,那个右边布局显示的item隐藏其右边布局
					 * 向左滑动只触发该情况，向右滑动还会触发情况五
					 */
					hiddenRight(mPreItemView);
				}

				if (mIsShown && mPreItemView == mCurrentItemView) {
					dx = dx - mRightViewWidth;
				}
				if (dx < 0 && dx > -mRightViewWidth) {
					mCurrentItemView.scrollTo((int) (-dx), 0);
				}
				return true;
			} else {
				if (mIsShown) {
					/**
					 * 情况三： 一个Item的右边布局已经显示，
					 * 这时候上下滚动ListView,那么那个右边布局显示的item隐藏其右边布局
					 */
					hiddenRight(mPreItemView);
				}
			}

			break;

		case MotionEvent.ACTION_UP:
			mLastY = -1; // reset
			if (getFirstVisiblePosition() == 0) {
				// invoke refresh
				if (mEnablePullRefresh
						&& mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
					mPullRefreshing = true;
					mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
					if (mListViewListener != null) {
						mListViewListener.onRefresh();
					}
				}
				resetHeaderHeight();
			} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
				// invoke load more.
				if (mEnablePullLoad
						&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA
						&& !mPullLoading) {
					startLoadMore();
				}
				resetFooterHeight();
			}
		case MotionEvent.ACTION_CANCEL:
			//
			mLastY = -1; // reset
			if (getFirstVisiblePosition() == 0) {
				// invoke refresh
				if (mEnablePullRefresh
						&& mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
					mPullRefreshing = true;
					mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
					//if (mListViewListener != null) {
					//	mListViewListener.onRefresh();
					//}
				}
				resetHeaderHeight();
			} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
				// invoke load more.
				if (mEnablePullLoad
						&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA
						&& !mPullLoading) {
					startLoadMore();
				}
				resetFooterHeight();
			}
			//
			clearPressedState();
			if (mIsShown) {
				/**
				 * 情况四： 一个Item的右边布局已经显示， 这时候左右滑动当前一个item,那个右边布局显示的item隐藏其右边布局
				 */
				hiddenRight(mPreItemView);
			}

			if (mIsHorizontal != null && mIsHorizontal) {
				if (mFirstX - lastX > mRightViewWidth / 2) {
					showRight(mCurrentItemView);
				} else {
					/**
					 * 情况五：
					 * <p>
					 * 向右滑动一个item,且滑动的距离超过了右边View的宽度的一半，隐藏之。
					 */
					hiddenRight(mCurrentItemView);
				}
				return true;
			}
			break;
			default:
				mLastY = -1; // reset
				if (getFirstVisiblePosition() == 0) {
					// invoke refresh
					if (mEnablePullRefresh
							&& mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
						mPullRefreshing = true;
						mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
						if (mListViewListener != null) {
							mListViewListener.onRefresh();
						}
					}
					resetHeaderHeight();
				} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
					// invoke load more.
					if (mEnablePullLoad
							&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA
							&& !mPullLoading) {
						startLoadMore();
					}
					resetFooterHeight();
				}
				break;
		}
		return super.onTouchEvent(ev);
	}


//	@SuppressLint("ClickableViewAccessibility")
//	@Override
//	public boolean onTouchEvent(MotionEvent ev) {
//		if (mLastY == -1) {
//			mLastY = ev.getRawY();
//		}
//
//		switch (ev.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//				mLastY = ev.getRawY();
//				break;
//			case MotionEvent.ACTION_MOVE:
//				final float deltaY = ev.getRawY() - mLastY;
//				mLastY = ev.getRawY();
//				if (getFirstVisiblePosition() == 0
//						&& (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)
//						&& mEnablePullRefresh) {
//					// the first item is showing, header has shown or pull down.
//					updateHeaderHeight(deltaY / OFFSET_RADIO);
//					invokeOnScrolling();
//				} else if (getLastVisiblePosition() == mTotalItemCount - 1
//						&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)
//						&& mEnablePullLoad) {
//					// last item, already pulled up or want to pull up.
//					updateFooterHeight(-deltaY / OFFSET_RADIO);
//				}
//				break;
//			default:
//				mLastY = -1; // reset
//				if (getFirstVisiblePosition() == 0) {
//					// invoke refresh
//					if (mEnablePullRefresh
//							&& mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
//						mPullRefreshing = true;
//						mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
//						if (mListViewListener != null) {
//							mListViewListener.onRefresh();
//						}
//					}
//					resetHeaderHeight();
//				} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
//					// invoke load more.
//					if (mEnablePullLoad
//							&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA
//							&& !mPullLoading) {
//						startLoadMore();
//					}
//					resetFooterHeight();
//				}
//				break;
//		}
//		return super.onTouchEvent(ev);
//	}


	private void clearPressedState() {
		mCurrentItemView.setPressed(false);
		setPressed(false);
		refreshDrawableState();
	}

	private void showRight(View view) {
		Message msg = new MoveHandler().obtainMessage();
		msg.obj = view;
		msg.arg1 = view.getScrollX();
		msg.arg2 = mRightViewWidth;
		msg.sendToTarget();

		mIsShown = true;
	}

	private void hiddenRight(View view) {
		if (mCurrentItemView == null) {
			return;
		}
		if (view == null) {
			return;
		}
		Message msg = new MoveHandler().obtainMessage();//
		msg.obj = view;
		msg.arg1 = view.getScrollX();
		msg.arg2 = 0;
		msg.sendToTarget();
		mIsShown = false;
	}

	/**
	 * show or hide right layout animation
	 */
	@SuppressLint("HandlerLeak")
	class MoveHandler extends Handler {
		int stepX = 0;
		int fromX;
		int toX;
		View view;
		private boolean mIsInAnimation = false;

		private void animatioOver() {
			mIsInAnimation = false;
			stepX = 0;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (stepX == 0) {
				if (mIsInAnimation) {
					return;
				}
				mIsInAnimation = true;
				view = (View) msg.obj;

				fromX = msg.arg1;
				toX = msg.arg2;
				stepX = (int) ((toX - fromX) * mDurationStep * 1.0 / mDuration);
				if (stepX < 0 && stepX > -1) {
					stepX = -1;
				} else if (stepX > 0 && stepX < 1) {
					stepX = 1;
				}
				if (Math.abs(toX - fromX) < 10) {
					view.scrollTo(toX, 0);
					animatioOver();
					return;
				}
			}

			fromX += stepX;
			boolean isLastStep = (stepX > 0 && fromX > toX) || (stepX < 0 && fromX < toX);
			if (isLastStep) {
				fromX = toX;
			}
			view.scrollTo(fromX, 0);
			invalidate();
			if (!isLastStep) {
				this.sendEmptyMessageDelayed(0, mDurationStep);
			} else {
				animatioOver();
			}
		}
	}

	public int getRightViewWidth() {
		return mRightViewWidth;
	}

	public void setRightViewWidth(int mRightViewWidth) {
		this.mRightViewWidth = mRightViewWidth;
	}

	public void deleteItem(View v) {
		hiddenRight(v);
	}

//===========================================================================================
	private float mLastY = -1; // save event y
	private Scroller mScroller; // used for scroll back
	private OnScrollListener mScrollListener; // user's scroll listener

	// the interface to trigger refresh and load more.
	private XListView.IXListViewListener mListViewListener;

	// -- header view
	private XListViewHeader mHeaderView;
	// header view content, use it to calculate the Header's height. And hide it
	// when disable pull refresh.
	private RelativeLayout mHeaderViewContent;
	private TextView mHeaderTimeView;
	private int mHeaderViewHeight; // header view's height
	private boolean mEnablePullRefresh = true;
	private boolean mPullRefreshing = false; // is refreashing.

	// -- footer view
	private XListViewFooter mFooterView;
	private boolean mEnablePullLoad;
	private boolean mPullLoading;
	private boolean mIsFooterReady = false;

	// total list items, used to detect is at the bottom of listview.
	private int mTotalItemCount;

	// for mScroller, scroll back from header or footer.
	private int mScrollBack;
	private final static int SCROLLBACK_HEADER = 0;
	private final static int SCROLLBACK_FOOTER = 1;

	private final static int SCROLL_DURATION = 400; // scroll back duration
	private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
	// at bottom, trigger
	// load more.
	private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
	// feature.


	private void initWithContext(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);

		// init header view
		mHeaderView = new XListViewHeader(context);
		mHeaderViewContent = (RelativeLayout) mHeaderView
				.findViewById(R.id.xlistview_header_content);
		mHeaderTimeView = (TextView) mHeaderView
				.findViewById(R.id.xlistview_header_time);
		addHeaderView(mHeaderView);

		// init footer view
		mFooterView = new XListViewFooter(context);

		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@SuppressWarnings("deprecation")
					@Override
					public void onGlobalLayout() {
						mHeaderViewHeight = mHeaderViewContent.getHeight();
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// make sure XListViewFooter is the last footer view, and only add once.
		if (mIsFooterReady == false) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
		super.setAdapter(adapter);
	}

	/**
	 * enable or disable pull down refresh feature.
	 *
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) { // disable, hide the content
			mHeaderViewContent.setVisibility(View.INVISIBLE);
		} else {
			mHeaderViewContent.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * enable or disable pull up load more feature.
	 *
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
		} else {
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
			// both "pull up" and "click" will invoke load more.
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	/**
	 * stop refresh, reset header view.
	 */
	public void stopRefresh() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}

	/**
	 * stop load more, reset footer view.
	 */
	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
		}
	}

	/**
	 * set last refresh time
	 *
	 * @param time
	 */
	public void setRefreshTime(String time) {
		mHeaderTimeView.setText(time);
	}

	private void invokeOnScrolling() {
		if (mScrollListener instanceof XListView.OnXScrollListener) {
			XListView.OnXScrollListener l = (XListView.OnXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}

	private void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta
				+ mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(XListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(XListViewHeader.STATE_NORMAL);
			}
		}
		setSelection(0); // scroll to top each time
	}

	/**
	 * reset header view's height.
	 */
	private void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) // not visible.
			return;
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height,
				SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
	}

	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
				// more.
				mFooterView.setState(XListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(XListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);

		// setSelection(mTotalItemCount - 1); // scroll to bottom
	}

	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(XListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			mListViewListener.onLoadMore();
		}
	}



	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {
		// send to user's listener
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

	public void setXListViewListener(XListView.IXListViewListener l) {
		mListViewListener = l;
	}

	/**
	 * you can listen ListView.OnScrollListener or this one. it will invoke
	 * onXScrolling when header/footer scroll back.
	 */
	public interface OnXScrollListener extends OnScrollListener {
		void onXScrolling(View view);
	}

	/**
	 * implements this interface to get refresh/load more event.
	 */
	public interface IXListViewListener {
		void onRefresh();

		void onLoadMore();
	}
}
