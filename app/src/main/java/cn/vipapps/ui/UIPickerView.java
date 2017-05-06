package cn.vipapps.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import cn.vipapps.COLOR;
import cn.vipapps.ui.picker.ItemsRange;
import cn.vipapps.ui.picker.OnWheelChangedListener;
import cn.vipapps.ui.picker.OnWheelClickedListener;
import cn.vipapps.ui.picker.OnWheelScrollListener;
import cn.vipapps.ui.picker.WheelRecycle;
import cn.vipapps.ui.picker.WheelScroller;
import cn.vipapps.ui.picker.adapter.WheelViewAdapter;

import java.util.LinkedList;
import java.util.List;

@SuppressLint("ClickableViewAccessibility")
public class UIPickerView extends ViewGroup {

	/**
	 * Top and bottom shadows colors 顶部和底部的阴影颜色
	 * */
	/*
	 * / Modified by wulianghuan 2014-11-25 private int[] SHADOWS_COLORS = new
	 * int[] { 0xFF111111, 0x00AAAAAA, 0x00AAAAAA }; //
	 */
	private int[] SHADOWS_COLORS = new int[] { 0xefFFFFFF, 0xcfFFFFFF,
			0x3fFFFFFF };

	/**
	 * Top and bottom items offset (to hide that) 顶部和底部的项目等距（隐藏）
	 * */
	private static final int ITEM_OFFSET_PERCENT = 0;

	/**
	 * Left and right padding value 左，右填充值
	 * */
	private static final int PADDING = 10;

	/**
	 * Default count of visible items 可视项默认数量
	 * */
	private static final int DEF_VISIBLE_ITEMS = 5;

	/**
	 * Wheel Values 滚轮值
	 * */
	private int currentItem = 0;

	/**
	 * Count of visible items 有形物品计数
	 * */
	private int visibleItems = DEF_VISIBLE_ITEMS;

	/**
	 * Item height 项目高度
	 * */
	private int itemHeight = 0;

	/**
	 * Center Line 中心线
	 * */
	private Drawable centerDrawable;

	/**
	 * Wheel drawables 滚轮可绘区域
	 * */
	private Drawable wheelBackground = stroke_1();
	private Drawable wheelForeground = stroke();

	/**
	 * Shadows drawables 阴影可绘区域
	 * */
	private GradientDrawable topShadow;
	private GradientDrawable bottomShadow;

	/**
	 * Draw Shadows 绘制阴影
	 */
	private boolean drawShadows = true;

	/**
	 * Scrolling 滚动
	 */
	private WheelScroller scroller;
	private boolean isScrollingPerformed;
	private int scrollingOffset;

	/**
	 * Cyclic 循环
	 * */
	boolean isCyclic = false;

	/**
	 * Items layout 项目布局
	 * */
	private LinearLayout itemsLayout;

	/**
	 * The number of first item in layout
	 * 
	 * 在布局的第一个项目的数目
	 * */
	private int firstItem;

	/**
	 * View adapter
	 * 
	 * 查看适配器
	 * */
	private WheelViewAdapter viewAdapter;

	/**
	 * recycle 回收
	 * */
	private WheelRecycle recycle = new WheelRecycle(this);

	// Listeners
	private List<OnWheelChangedListener> changingListeners = new LinkedList<OnWheelChangedListener>();
	private List<OnWheelScrollListener> scrollingListeners = new LinkedList<OnWheelScrollListener>();
	private List<OnWheelClickedListener> clickingListeners = new LinkedList<OnWheelClickedListener>();

	String label = "";

	public UIPickerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		initData(context);
	}

	public UIPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context);
	}

	public UIPickerView(Context context) {
		super(context);
		initData(context);
	}

	private Drawable stroke() {
		GradientDrawable gd = new GradientDrawable();// 创建drawable
		gd.setStroke(1, COLOR.parse("#FFFFFF"));
		Drawable drawable1 = gd;
		return drawable1;
	}
	
	private Drawable stroke_1() {
		GradientDrawable gd = new GradientDrawable();// 创建drawable
		Drawable drawable1 = gd;
		return drawable1;
	}

	/**
	 * Initializes class data
	 * 
	 * 初始化类数据
	 * 
	 * @param context
	 *            the context
	 */
	private void initData(Context context) {
		scroller = new WheelScroller(getContext(), scrollingListener);
	}

	/**
	 * Scrolling listener
	 * 
	 * 滚动监听器
	 * 
	 * */
	WheelScroller.ScrollingListener scrollingListener = new WheelScroller.ScrollingListener() {
		@Override
		public void onStarted() {
			isScrollingPerformed = true;
			notifyScrollingListenersAboutStart();
		}

		@Override
		public void onScroll(int distance) {
			doScroll(distance);

			int height = getHeight();
			if (scrollingOffset > height) {
				scrollingOffset = height;
				scroller.stopScrolling();
			} else if (scrollingOffset < -height) {
				scrollingOffset = -height;
				scroller.stopScrolling();
			}
		}

		@Override
		public void onFinished() {
			if (isScrollingPerformed) {
				notifyScrollingListenersAboutEnd();
				isScrollingPerformed = false;
			}

			scrollingOffset = 0;
			invalidate();
		}

		@Override
		public void onJustify() {
			if (Math.abs(scrollingOffset) > WheelScroller.MIN_DELTA_FOR_SCROLLING) {
				scroller.scroll(scrollingOffset, 0);
			}
		}
	};

	/**
	 * Set the the specified scrolling interpolator
	 * 
	 * 设置指定的滚动插值
	 * 
	 * @param interpolator
	 *            the interpolator
	 */
	public void setInterpolator(Interpolator interpolator) {
		scroller.setInterpolator(interpolator);
	}

	/**
	 * Gets count of visible items
	 * 
	 * 获取可见的项目数
	 * 
	 * @return the count of visible items 可见项目总数
	 */
	public int numberOfComponents() {
		return visibleItems;
	}

	/**
	 * Sets the desired count of visible items. Actual amount of visible items
	 * depends on wheel layout parameters. To apply changes and rebuild view
	 * call measure().
	 * 
	 * 设置可见项目所需要的数量、可见项目的实际金额取决于滚轮布局参数、应用更改并重新查看呼叫措施()。
	 * 
	 * @param count
	 *            the desired count for visible items
	 */
	public void setVisibleItems(int count) {
		visibleItems = count;
	}

	/**
	 * Gets view adapter
	 * 
	 * 获取视图适配器
	 * 
	 * @return the view adapter
	 */
	public WheelViewAdapter getViewAdapter() {
		return viewAdapter;
	}

	// Adapter listener
	private DataSetObserver dataObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			invalidateWheel(false);
		}

		@Override
		public void onInvalidated() {
			invalidateWheel(true);
		}
	};

	/**
	 * Sets view adapter. Usually new adapters contain different views, so it
	 * needs to rebuild view by calling measure().
	 * 
	 * 集视图适配器。通常新的适配器包含不同的看法，所以它需要通过调用的措施来重建视图()。
	 * 
	 * @param viewAdapter
	 *            the view adapter
	 */
	public void setViewAdapter(WheelViewAdapter viewAdapter) {
		if (this.viewAdapter != null) {
			this.viewAdapter.unregisterDataSetObserver(dataObserver);
		}
		this.viewAdapter = viewAdapter;
		if (this.viewAdapter != null) {
			this.viewAdapter.registerDataSetObserver(dataObserver);
		}

		invalidateWheel(true);
	}

	/**
	 * Adds wheel changing listener
	 * 
	 * 添加滚轮变化监听器
	 * 
	 * @param listener
	 *            the listener
	 */
	public void addChangingListener(OnWheelChangedListener listener) {
		changingListeners.add(listener);
	}

	/**
	 * Removes wheel changing listener
	 * 
	 * 移除滚轮变化监听器
	 * 
	 * @param listener
	 *            the listener
	 */
	public void removeChangingListener(OnWheelChangedListener listener) {
		changingListeners.remove(listener);
	}

	/**
	 * Notifies changing listeners
	 * 
	 * 通知改变监听器
	 * 
	 * @param oldValue
	 *            the old wheel value
	 * @param newValue
	 *            the new wheel value
	 */
	protected void notifyChangingListeners(int oldValue, int newValue) {
		for (OnWheelChangedListener listener : changingListeners) {
			listener.onChanged(this, oldValue, newValue);
		}
	}

	/**
	 * Adds wheel scrolling listener
	 * 
	 * 增加滚轮滚动监听器
	 * 
	 * @param listener
	 *            the listener
	 */
	public void addScrollingListener(OnWheelScrollListener listener) {
		scrollingListeners.add(listener);
	}

	/**
	 * Removes wheel scrolling listener
	 * 
	 * 删除滚轮滚动监听器
	 * 
	 * @param listener
	 *            the listener
	 */
	public void removeScrollingListener(OnWheelScrollListener listener) {
		scrollingListeners.remove(listener);
	}

	/**
	 * Notifies listeners about starting scrolling 通知有关启动滚动监听器
	 */
	protected void notifyScrollingListenersAboutStart() {
		for (OnWheelScrollListener listener : scrollingListeners) {
			listener.onScrollingStarted(this);
		}
	}

	/**
	 * Notifies listeners about ending scrolling 通知对结束滚动监听器
	 */
	protected void notifyScrollingListenersAboutEnd() {
		for (OnWheelScrollListener listener : scrollingListeners) {
			listener.onScrollingFinished(this);
		}
	}

	/**
	 * Adds wheel clicking listener
	 * 
	 * 增加滚轮点击监听器
	 * 
	 * @param listener
	 *            the listener
	 */
	public void addClickingListener(OnWheelClickedListener listener) {
		clickingListeners.add(listener);
	}

	/**
	 * Removes wheel clicking listener
	 * 
	 * 移除滚轮点击监听器
	 * 
	 * @param listener
	 *            the listener
	 */
	public void removeClickingListener(OnWheelClickedListener listener) {
		clickingListeners.remove(listener);
	}

	/**
	 * Notifies listeners about clicking 通知关于点击监听器
	 */
	protected void notifyClickListenersAboutClick(int item) {
		for (OnWheelClickedListener listener : clickingListeners) {
			listener.onItemClicked(this, item);
		}
	}

	/**
	 * Gets current value
	 * 
	 * 获取当前值
	 * 
	 * @return the current value
	 */
	public int getCurrentItem() {
		return currentItem;
	}

	/**
	 * Sets the current item. Does nothing when index is wrong.
	 * 
	 * 设置当前项目。当索引是错误时，不执行任何操作。
	 * 
	 * @param index
	 *            the item index
	 * @param animated
	 *            the animation flag
	 */
	public void setCurrentItem(int index, boolean animated) {
		if (viewAdapter == null || viewAdapter.getItemsCount() == 0) {
			return; // throw?
		}

		int itemCount = viewAdapter.getItemsCount();
		if (index < 0 || index >= itemCount) {
			if (isCyclic) {
				while (index < 0) {
					index += itemCount;
				}
				index %= itemCount;
			} else {
				return; // throw?
			}
		}
		if (index != currentItem) {
			if (animated) {
				int itemsToScroll = index - currentItem;
				if (isCyclic) {
					int scroll = itemCount + Math.min(index, currentItem)
							- Math.max(index, currentItem);
					if (scroll < Math.abs(itemsToScroll)) {
						itemsToScroll = itemsToScroll < 0 ? scroll : -scroll;
					}
				}
				scroll(itemsToScroll, 0);
			} else {
				scrollingOffset = 0;

				int old = currentItem;
				currentItem = index;

				notifyChangingListeners(old, currentItem);

				invalidate();
			}
		}
	}

	/**
	 * Sets the current item w/o animation. Does nothing when index is wrong.
	 * 
	 * 设置当前项目的w/o动画，当索引错误时，不执行操作。
	 * 
	 * @param index
	 *            the item index
	 */
	public void setCurrentItem(int index) {
		setCurrentItem(index, false);
	}

	/**
	 * Tests if wheel is cyclic. That means before the 1st item there is shown
	 * the last one
	 * 
	 * 测试滚轮是否循环，这意味着所示的最后一个在第一项之前。
	 * 
	 * @return true if wheel is cyclic
	 */
	public boolean isCyclic() {
		return isCyclic;
	}

	/**
	 * Set wheel cyclic flag
	 * 
	 * 设置滚轮循环标志
	 * 
	 * @param isCyclic
	 *            the flag to set
	 */
	public void setCyclic(boolean isCyclic) {
		this.isCyclic = isCyclic;
		invalidateWheel(false);
	}

	/**
	 * Determine whether shadows are drawn
	 * 
	 * 确定阴影是否绘制
	 * 
	 * @return true is shadows are drawn
	 */
	public boolean drawShadows() {
		return drawShadows;
	}

	/**
	 * Set whether shadows should be drawn
	 * 
	 * 设置阴影是否应绘制
	 * 
	 * @param drawShadows
	 *            flag as true or false
	 */
	public void setDrawShadows(boolean drawShadows) {
		this.drawShadows = drawShadows;
	}

	/**
	 * Set the shadow gradient color
	 * 
	 * 设置阴影的渐变颜色
	 * 
	 * @param start
	 * @param middle
	 * @param end
	 */
	public void setShadowColor(int start, int middle, int end) {
		SHADOWS_COLORS = new int[] { start, middle, end };
	}

	/**
	 * Sets the drawable for the wheel background
	 * 
	 * 设置绘制的滚轮的背景色
	 * 
	 * @param resource
	 */
	@SuppressWarnings("deprecation")
	public void setWheelBackground(int resource) {
		wheelBackground = getResources().getDrawable(resource);
		setBackground(wheelBackground);
	}

	/**
	 * Sets the drawable for the wheel foreground
	 * 
	 * 设置绘制的滚轮前景色
	 * 
	 * @param resource
	 */
	@SuppressWarnings("deprecation")
	public void setWheelForeground(int resource) {
		wheelForeground = getResources().getDrawable(resource);
		centerDrawable = wheelForeground;
	}

	/**
	 * Invalidates wheel
	 * 
	 * 无效滚轮
	 * 
	 * @param clearCaches
	 *            如果真的话，那么缓存视图将明确。
	 */
	public void invalidateWheel(boolean clearCaches) {
		if (clearCaches) {
			recycle.clearAll();
			if (itemsLayout != null) {
				itemsLayout.removeAllViews();
			}
			scrollingOffset = 0;
		} else if (itemsLayout != null) {
			// cache all items
			recycle.recycleItems(itemsLayout, firstItem, new ItemsRange());
		}

		invalidate();
	}

	/**
	 * Initializes resources 初始化资源
	 */
	private void initResourcesIfNecessary() {
		if (centerDrawable == null) {
			centerDrawable = wheelForeground;
		}

		if (topShadow == null) {
			topShadow = new GradientDrawable(Orientation.TOP_BOTTOM,
					SHADOWS_COLORS);
		}

		if (bottomShadow == null) {
			bottomShadow = new GradientDrawable(Orientation.BOTTOM_TOP,
					SHADOWS_COLORS);
		}

		setBackground(wheelBackground);
	}

	/**
	 * Calculates desired height for layout
	 * 
	 * 计算所需的高度布局
	 * 
	 * @param layout
	 *            源布局
	 * @return 所需的布局高度
	 */
	private int getDesiredHeight(LinearLayout layout) {
		if (layout != null && layout.getChildAt(0) != null) {
			itemHeight = layout.getChildAt(0).getMeasuredHeight();
		}

		int desired = itemHeight * visibleItems - itemHeight
				* ITEM_OFFSET_PERCENT / 50;

		return Math.max(desired, getSuggestedMinimumHeight());
	}

	/**
	 * Returns 滚轮的高度
	 * 
	 * @return 项目高度
	 */
	private int getItemHeight() {
		if (itemHeight != 0) {
			return itemHeight;
		}

		if (itemsLayout != null && itemsLayout.getChildAt(0) != null) {
			itemHeight = itemsLayout.getChildAt(0).getHeight();
			return itemHeight;
		}

		return getHeight() / visibleItems;
	}

	/**
	 * Calculates control width and creates text layouts
	 * 
	 * 计算控制宽度，并创建文本布局
	 * 
	 * @param widthSize
	 *            输入布局宽度 the input layout width
	 * @param mode
	 *            布局模式 the layout mode
	 * @return 计算控制宽度 the calculated control width
	 */
	private int calculateLayoutWidth(int widthSize, int mode) {
		initResourcesIfNecessary();

		// TODO: make it static 使它静止
		itemsLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		itemsLayout
				.measure(MeasureSpec.makeMeasureSpec(widthSize,
						MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(
						0, MeasureSpec.UNSPECIFIED));
		int width = itemsLayout.getMeasuredWidth();

		if (mode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {
			width += 2 * PADDING;

			// Check against our minimum width 检查我们的最小宽度
			width = Math.max(width, getSuggestedMinimumWidth());

			if (mode == MeasureSpec.AT_MOST && widthSize < width) {
				width = widthSize;
			}
		}

		itemsLayout.measure(MeasureSpec.makeMeasureSpec(width - 2 * PADDING,
				MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0,
				MeasureSpec.UNSPECIFIED));

		return width;
	}

	@Override
	/*测量事件*/protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		buildViewForMeasuring();

		int width = calculateLayoutWidth(widthSize, widthMode);

		int height;
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = getDesiredHeight(itemsLayout);

			if (heightMode == MeasureSpec.AT_MOST) {
				height = Math.min(height, heightSize);
			}
		}

		setMeasuredDimension(width, height);
	}

	@Override
	/*布局事件*/protected void onLayout(boolean changed, int l, int t, int r, int b) {
		layout(r - l, b - t);
	}

	/**
	 * Sets layouts width and height
	 * 
	 * 设置布局宽度和高度
	 * 
	 * @param width
	 *            the layout width
	 * @param height
	 *            the layout height
	 */
	private void layout(int width, int height) {
		int itemsWidth = width - 2 * PADDING;

		itemsLayout.layout(0, 0, itemsWidth, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {//重绘事件
		super.onDraw(canvas);

		if (viewAdapter != null && viewAdapter.getItemsCount() > 0) {
			updateView();

			drawItems(canvas);
			drawCenterRect(canvas);
		}

		if (drawShadows)
			drawShadows(canvas);
	}

	/**
	 * Draws shadows on top and bottom of control 在控制的顶部和底部绘制阴影
	 * 
	 * @param canvas
	 *            绘画的画布 the canvas for drawing
	 */
	private void drawShadows(Canvas canvas) {
		/*
		 * / Modified by wulianghuan 2014-11-25 int height = (int)(1.5 *
		 * getItemHeight()); //
		 */
		int height = 3 * getItemHeight();
		// */
		topShadow.setBounds(0, 0, getWidth(), height);
		topShadow.draw(canvas);

		bottomShadow
				.setBounds(0, getHeight() - height, getWidth(), getHeight());
		bottomShadow.draw(canvas);
	}

	/**
	 * Draws items 绘制项目
	 * 
	 * @param canvas
	 *            the canvas for drawing
	 */
	private void drawItems(Canvas canvas) {
		canvas.save();

		int top = (currentItem - firstItem) * getItemHeight()
				+ (getItemHeight() - getHeight()) / 2;
		canvas.translate(PADDING, -top + scrollingOffset);

		itemsLayout.draw(canvas);

		canvas.restore();
	}

	/**
	 * Draws rect for current value 绘制矩形的当前值
	 * 
	 * @param canvas
	 *            the canvas for drawing
	 */
	private void drawCenterRect(Canvas canvas) {
		int center = getHeight() / 2;
		int offset = (int) (getItemHeight() / 2 * 1.2);
		/*
		 * / Remarked by wulianghuan 2014-11-27 使用自己的画线，而不是描边 Rect rect = new
		 * Rect(left, top, right, bottom) centerDrawable.setBounds(bounds)
		 * centerDrawable.setBounds(0, center - offset, getWidth(), center +
		 * offset); centerDrawable.draw(canvas); //
		 */
		Paint paint = new Paint();
		paint.setColor(Color.parseColor("#FFFFFF"));
		// 设置线宽
		paint.setStrokeWidth((float) 3);
		// 绘制上边直线
		canvas.drawLine(0, center - offset, getWidth(), center - offset, paint);
		// 绘制下边直线
		canvas.drawLine(0, center + offset, getWidth(), center + offset, paint);
		// */
	}

	@Override
	/*触摸事件*/public boolean onTouchEvent(MotionEvent event) {
		if (!isEnabled() || getViewAdapter() == null) {
			return true;
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			if (getParent() != null) {
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;

		case MotionEvent.ACTION_UP:
			if (!isScrollingPerformed) {
				int distance = (int) event.getY() - getHeight() / 2;
				if (distance > 0) {
					distance += getItemHeight() / 2;
				} else {
					distance -= getItemHeight() / 2;
				}
				int items = distance / getItemHeight();
				if (items != 0 && isValidItemIndex(currentItem + items)) {
					notifyClickListenersAboutClick(currentItem + items);
				}
			}
			break;
		}

		return scroller.onTouchEvent(event);
	}

	/**
	 * Scrolls the wheel 滚动滚轮
	 * 
	 * @param delta
	 *            滚动值 the scrolling value
	 */
	private void doScroll(int delta) {
		scrollingOffset += delta;

		int itemHeight = getItemHeight();
		int count = scrollingOffset / itemHeight;

		int pos = currentItem - count;
		int itemCount = viewAdapter.getItemsCount();

		int fixPos = scrollingOffset % itemHeight;
		if (Math.abs(fixPos) <= itemHeight / 2) {
			fixPos = 0;
		}
		if (isCyclic && itemCount > 0) {
			if (fixPos > 0) {
				pos--;
				count++;
			} else if (fixPos < 0) {
				pos++;
				count--;
			}
			// fix position by rotating
			while (pos < 0) {
				pos += itemCount;
			}
			pos %= itemCount;
		} else {
			//
			if (pos < 0) {
				count = currentItem;
				pos = 0;
			} else if (pos >= itemCount) {
				count = currentItem - itemCount + 1;
				pos = itemCount - 1;
			} else if (pos > 0 && fixPos > 0) {
				pos--;
				count++;
			} else if (pos < itemCount - 1 && fixPos < 0) {
				pos++;
				count--;
			}
		}

		int offset = scrollingOffset;
		if (pos != currentItem) {
			setCurrentItem(pos, false);
		} else {
			invalidate();
		}

		// update offset
		scrollingOffset = offset - count * itemHeight;
		if (scrollingOffset > getHeight()) {
			scrollingOffset = scrollingOffset % getHeight() + getHeight();
		}
	}

	/**
	 * Scroll the wheel 滚动的滚轮
	 * 
//	 * @param itemsToSkip
	 *            项目滚动 items to scroll
	 * @param time
	 *            滚动持续时间 scrolling duration
	 */
	public void scroll(int itemsToScroll, int time) {
		int distance = itemsToScroll * getItemHeight() - scrollingOffset;
		scroller.scroll(distance, time);
	}

	/**
	 * Calculates range for wheel items 计算范围滚轮项目
	 * 
	 * @return the items range 项目范围
	 */
	private ItemsRange getItemsRange() {
		if (getItemHeight() == 0) {
			return null;
		}

		int first = currentItem;
		int count = 1;

		while (count * getItemHeight() < getHeight()) {
			first--;
			count += 2; // top + bottom items
		}

		if (scrollingOffset != 0) {
			if (scrollingOffset > 0) {
				first--;
			}
			count++;

			// process empty items above the first or below the second
			// 上述第一或低于第二过程空项
			int emptyItems = scrollingOffset / getItemHeight();
			first -= emptyItems;
			count += Math.asin(emptyItems);
		}
		return new ItemsRange(first, count);
	}

	/**
	 * Rebuilds wheel items if necessary. Caches all unused items.
	 * 如果有必要重建滚轮项目，缓存所有未使用的项目。
	 * 
	 * @return true if items are rebuilt
	 */
	private boolean reloadAllComponents() {
		boolean updated = false;
		ItemsRange range = getItemsRange();
		if (itemsLayout != null) {
			int first = recycle.recycleItems(itemsLayout, firstItem, range);
			updated = firstItem != first;
			firstItem = first;
		} else {
			createItemsLayout();
			updated = true;
		}

		if (!updated) {
			updated = firstItem != range.getFirst()
					|| itemsLayout.getChildCount() != range.getCount();
		}

		if (firstItem > range.getFirst() && firstItem <= range.getLast()) {
			for (int i = firstItem - 1; i >= range.getFirst(); i--) {
				if (!addViewItem(i, true)) {
					break;
				}
				firstItem = i;
			}
		} else {
			firstItem = range.getFirst();
		}

		int first = firstItem;
		for (int i = itemsLayout.getChildCount(); i < range.getCount(); i++) {
			if (!addViewItem(firstItem + i, false)
					&& itemsLayout.getChildCount() == 0) {
				first++;
			}
		}
		firstItem = first;

		return updated;
	}

	/**
	 * Updates view. Rebuilds items and label if necessary, recalculate items
	 * sizes. 更新视图，如果有必要重建的项目和标签，重新计算的物品的尺寸。
	 */
	private void updateView() {
		if (reloadAllComponents()) {
			calculateLayoutWidth(getWidth(), MeasureSpec.EXACTLY);
			layout(getWidth(), getHeight());
		}
	}

	/**
	 * Creates item layouts if necessary 创建项目布局，如果有必要
	 */
	private void createItemsLayout() {
		if (itemsLayout == null) {
			itemsLayout = new LinearLayout(getContext());
			itemsLayout.setOrientation(LinearLayout.VERTICAL);
		}
	}

	/**
	 * Builds view for measuring 构建视图，用于测量
	 */
	private void buildViewForMeasuring() {
		// clear all items
		if (itemsLayout != null) {
			recycle.recycleItems(itemsLayout, firstItem, new ItemsRange());
		} else {
			createItemsLayout();
		}

		// add views
		int addItems = visibleItems / 2;
		for (int i = currentItem + addItems; i >= currentItem - addItems; i--) {
			if (addViewItem(i, true)) {
				firstItem = i;
			}
		}
	}

	/**
	 * Adds view for item to items layout 增加视图项项目布局
	 * 
	 * @param index
	 *            the item index
	 * @param first
	 *            该标志指示是否认为应该是第一 the flag indicates if view should be first
	 * @return 如果存在相应的项目，并且添加 true if corresponding item exists and is added
	 */
	private boolean addViewItem(int index, boolean first) {
		View view = getItemView(index);
		if (view != null) {
			if (first) {
				itemsLayout.addView(view, 0);
			} else {
				itemsLayout.addView(view);
			}

			return true;
		}

		return false;
	}

	/**
	 * Checks whether intem index is valid 检查项指标是有效的
	 * 
	 * @param index
	 *            the item index
	 * @return 如果项目真的索引没有出界或滚轮循环 true if item index is not out of bounds or the
	 *         wheel is cyclic
	 */
	private boolean isValidItemIndex(int index) {
		return viewAdapter != null
				&& viewAdapter.getItemsCount() > 0
				&& (isCyclic || index >= 0
						&& index < viewAdapter.getItemsCount());
	}

	/**
	 * Returns view for specified item 返回查看特定项目
	 * 
	 * @param index
	 *            the item index
	 * @return 如果索引超出范围项目视图或为空视图 item view or empty view if index is out of
	 *         bounds
	 */
	private View getItemView(int index) {
		if (viewAdapter == null || viewAdapter.getItemsCount() == 0) {
			return null;
		}
		int count = viewAdapter.getItemsCount();
		if (!isValidItemIndex(index)) {
			return viewAdapter
					.getEmptyItem(recycle.getEmptyItem(), itemsLayout);
		} else {
			while (index < 0) {
				index = count + index;
			}
		}

		index %= count;
		return viewAdapter.getItem(index, recycle.getItem(), itemsLayout);
	}

	/**
	 * Stops scrolling 停止滚动
	 */
	public void stopScrolling() {
		scroller.stopScrolling();
	}
}
