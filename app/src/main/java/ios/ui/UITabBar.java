package ios.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TabWidget;

import cn.share.R;
import cn.vipapps.android.ACTIVITY;

public class UITabBar extends TabWidget {
	public UITabBar(Context context) {
		super(context);
		_init();
	}

	public UITabBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		_init();
	}

	void _init() {
	}
	@SuppressWarnings("deprecation")
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {//重绘事件
		super.onDraw(canvas);
		Paint paint = new Paint();
		int width = this.getWidth();
		paint.setStrokeWidth(ACTIVITY.dp2px(1));
		paint.setColor(this.getContext().getResources().getColor(R.color.LINE));
		canvas.drawLine(0, 0, width, 0, paint);
	}
}
