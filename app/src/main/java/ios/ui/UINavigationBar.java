package ios.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.share.R;
import cn.vipapps.CALLBACK;
import cn.vipapps.android.ACTIVITY;

public class UINavigationBar extends RelativeLayout {
	public UINavigationBar(Context context) {
		super(context);
		_init(context);
	}

	ViewGroup titleViewPH;
	TextView titleTextView;
	Button leftButton, rightButton;
	ImageButton backImageButton, leftImageButton, rightImageButton;

	public UINavigationBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		_init(context);
		_attrs(attrs);
	}

	@SuppressLint("Recycle")
	private void _attrs(AttributeSet attrs) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ios);
		int count = typedArray.getIndexCount();
		for (int i = 0; i < count; i++) {
			int resourceID = typedArray.getIndex(i);
			if (resourceID == R.styleable.ios_hideback) {
				_hideback = typedArray.getBoolean(resourceID, false);
			} else {

			}
		}
	}

	boolean _hideback;

	public void hideBack(boolean hide) {
		_hideback = hide;
		this.invalidate();
	}

	void _init(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.ios_uinavigationbar, this, true);
		titleTextView = (TextView) this.findViewById(R.id.titleTextView);
		titleViewPH = (ViewGroup) this.findViewById(R.id.titleViewPH);
		backImageButton = (ImageButton) this.findViewById(R.id.backImageButton);
		leftButton = (Button) this.findViewById(R.id.leftButton);
		leftImageButton = (ImageButton) this.findViewById(R.id.leftImageButton);
		rightButton = (Button) this.findViewById(R.id.rightButton);
		rightImageButton = (ImageButton) this.findViewById(R.id.rightImageButton);
		//
		titleTextView.setTextColor(getResources().getColor(R.color.W));
		rightButton.setTextColor(getResources().getColor(R.color.W));
		//
		backImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((Activity) getContext()).finish();
			}

		});
	}

	String _title;

	public void title(String title) {
		titleTextView.setText(title);
	}

	public String title() {
		return _title;
	}

	View _titleView;

	public void titleView(View titleView) {
		_titleView = titleView;
		titleTextView.setVisibility(titleView!=null? View.GONE: View.VISIBLE);
		titleViewPH.removeAllViews();
		if (titleView == null){
			return;
		}
		titleViewPH.addView(titleView);
	}

	public View titleView() {
		return _titleView;
	}

	public void leftNavButton(int icon, @SuppressWarnings("rawtypes") final CALLBACK callback) {
		leftImageButton.setImageResource(icon);
		leftImageButton.setVisibility(View.VISIBLE);
		leftImageButton.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View arg0) {
				if (callback != null) {
					callback.run(false, null);
				}
			}

		});
	}

	public void leftNavButton(String title, @SuppressWarnings("rawtypes") final CALLBACK callback) {
		leftButton.setText(title);
		leftButton.setVisibility(View.VISIBLE);
		leftButton.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View arg0) {
				if (callback != null) {
					callback.run(false, null);
				}
			}

		});
	}

	public void rightNavButton(int icon, @SuppressWarnings("rawtypes") final CALLBACK callback) {
		rightImageButton.setImageResource(icon);
		rightImageButton.setVisibility(View.VISIBLE);
		rightImageButton.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View arg0) {
				if (callback != null) {
					callback.run(false, null);
				}
			}

		});
	}

	public void rightNavButton(String title, @SuppressWarnings("rawtypes") final CALLBACK callback) {
		rightButton.setText(title);
		rightButton.setVisibility(View.VISIBLE);
		rightButton.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View arg0) {
				if (callback != null) {
					callback.run(false, null);
				}
			}

		});
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {// 重绘事件
		super.onDraw(canvas);
		this.setBackgroundResource(R.color.V);
		backImageButton.setVisibility(_hideback ? View.GONE : View.VISIBLE);
		Paint paint = new Paint();
		int width = this.getWidth();
		int height = this.getHeight();
		paint.setStrokeWidth(ACTIVITY.dp2px(1));
		paint.setColor(this.getContext().getResources().getColor(R.color.V));
		canvas.drawLine(0, height - ACTIVITY.dp2px(1), width, height - ACTIVITY.dp2px(1), paint);
	}
}
