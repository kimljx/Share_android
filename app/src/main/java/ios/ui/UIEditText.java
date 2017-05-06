package ios.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import cn.share.R;

public class UIEditText extends android.support.v7.widget.AppCompatEditText {

	public UIEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public UIEditText(Context context) {
		super(context);
		init(context);
	}
	@SuppressWarnings("deprecation")
	void init(Context context){
		this.setTextAppearance(context, R.style.H2);
	}
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setStrokeWidth(1);
		paint.setColor(getResources().getColor(R.color.LINE));
		canvas.drawLine(0, canvas.getHeight()-1, canvas.getWidth(), canvas.getHeight()-1, paint);
	}

}
