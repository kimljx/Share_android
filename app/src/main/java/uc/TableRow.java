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

import cn.share.R;
import cn.vipapps.COLOR;
import cn.vipapps.android.ACTIVITY;

//表格行
public class TableRow extends android.widget.TableRow {
	
	private boolean showbitmap =false;
	private float margleft;
	public TableRow(Context context) {
		super(context);
		init();
	}

	public TableRow(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		_attrs(attrs);
	}

	void init() {//初始化
		this.setBackgroundResource(R.drawable.tablerow);
		this.setGravity(Gravity.CENTER_VERTICAL);
		//表格间距左上右下
		this.setPadding(ACTIVITY.dp2px(15), ACTIVITY.dp2px(11), ACTIVITY.dp2px(15), ACTIVITY.dp2px(11));
		
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {//重绘事件
		super.onDraw(canvas);
		int left = ACTIVITY.dp2px(margleft);
		Paint paint = new Paint();
		int width = this.getWidth();
		int height = this.getHeight();
		paint.setStrokeWidth(1);
		paint.setColor(COLOR.parse("#d8d8d8"));
		canvas.drawLine(left, height - 1, width, height - 1, paint);
		if (showbitmap) {
			@SuppressWarnings("deprecation")
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
				margleft= Float.parseFloat(typedArray.getString(R.styleable.ios_marginleftlength));
			}else {

			}
			if (resourceID == R.styleable.ios_showarrow) {
				showbitmap = Boolean.parseBoolean(typedArray.getString(R.styleable.ios_showarrow));
			}
		}
	}
	
	public  void setMarginleft(float marginleft) {
		
		margleft = marginleft;
		invalidate();
		
	}
}
