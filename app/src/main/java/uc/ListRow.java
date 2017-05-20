package uc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;

import cn.share.R;
import cn.vipapps.android.ACTIVITY;

//表格行
public class ListRow extends RelativeLayout {
	private float margleft;

	private boolean showbitmap = true;

	public ListRow(Context context) {
		super(context);
		init();
	}

	public ListRow(Context context, AttributeSet attrs) {
		super(context, attrs);
		_attrs(attrs);
		init();
	}

	void init() {// 初始化
		this.setBackgroundResource(R.drawable.tablerow);
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setPadding(ACTIVITY.dp2px(20), ACTIVITY.dp2px(10), ACTIVITY.dp2px(20), ACTIVITY.dp2px(10));
	}

	@SuppressLint("DrawAllocation")
	@SuppressWarnings("deprecation")
	@Override
	protected void onDraw(Canvas canvas) {// 重绘事件

		super.onDraw(canvas);
		int left = ACTIVITY.dp2px(margleft);
		Paint paint = new Paint();
		int width = this.getWidth();
		int height = this.getHeight();

		paint.setStrokeWidth(1);

		paint.setColor(this.getContext().getResources().getColor(R.color.LINE));

		canvas.drawLine(left, height - 1, width, height - 1, paint);

		if (showbitmap) {
			BitmapDrawable drawable = (BitmapDrawable) this.getResources().getDrawable(R.mipmap.arrow_right);
			Bitmap bitmap = drawable.getBitmap();
			int bitmapHeight = bitmap.getHeight();
			int bitmapWidth = bitmap.getWidth();
			int bitmaptop = (height - bitmapHeight) / 2;
			int bitmapleft = width - bitmapWidth - ACTIVITY.dp2px(15);
			canvas.drawBitmap(bitmap, bitmapleft, bitmaptop, paint);
		}

	}

	@SuppressLint("Recycle")
	private void _attrs(AttributeSet attrs) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ios);
		int count = typedArray.getIndexCount();
		for (int i = 0; i < count; i++) {
			int resourceID = typedArray.getIndex(i);
			if (resourceID == R.styleable.ios_marginleftlength) {
				margleft = Float.parseFloat(typedArray.getString(R.styleable.ios_marginleftlength));
			} else {

			}
			if (resourceID == R.styleable.ios_showarrow) {
				showbitmap = Boolean.parseBoolean(typedArray.getString(R.styleable.ios_showarrow));
			}

		}
	}

	public void setMarginleft(float marginleft) {
		margleft = marginleft;
		invalidate();

	}

}
