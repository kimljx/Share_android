package uc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.share.R;

public class TableHeader extends LinearLayout {

	private TextView textView;
//	public String headerText;
	private String title;

	public TableHeader(Context context) {
		super(context);
		init(context);

	}

	public TableHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		_attrs(attrs);

	}

	void init(Context context) {// 初始化
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.uc_tableheader, this, true);
		this.setBackgroundColor(Color.TRANSPARENT);
		textView = (TextView) this.findViewById(R.id.headertextView);
	}
	@SuppressLint("DrawAllocation")
	@SuppressWarnings("deprecation")
	@Override
	protected void onDraw(Canvas canvas) {// 重绘事件

		super.onDraw(canvas);
		Paint paint = new Paint();
		int width = this.getWidth();
		int height = this.getHeight();
		
		paint.setStrokeWidth(1);
		paint.setColor(this.getContext().getResources().getColor(R.color.LINE));
		canvas.drawLine(0, height - 1, width, height - 1, paint);
		
	}

	@SuppressLint("Recycle")
	private void _attrs(AttributeSet attrs) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ios);
		int count = typedArray.getIndexCount();
		for (int i = 0; i < count; i++) {
			int resourceID = typedArray.getIndex(i);
			if (resourceID == R.styleable.ios_title) {
				title = typedArray.getString(R.styleable.ios_title);
				textView.setText(title);
			}

		}
	}

	public void setHeaderText(String title) {
		textView.setText(title);
		invalidate();
	}

}
