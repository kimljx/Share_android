package uc;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.share.R;

import org.xmlpull.v1.XmlPullParser;

public class ReportSegmentView extends LinearLayout {
	private TextView textView1;
	private TextView textView2;
	private TextView textView3;
	private TextView textView4;
	private onSegmentViewClickListener listener;

	public ReportSegmentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ReportSegmentView(Context context) {
		super(context);
		init();
	}

	public void setTitles(String[] titles) {
		textView1.setText(titles[0]);
		textView2.setText(titles[1]);
		textView3.setText(titles[2]);
		textView4.setText(titles[3]);
	}

	public void setSeclect(int index){
		switch(index){
			case 0:
				if (textView1.isSelected()) {
					return;
				}
				textView1.setSelected(true);
				textView2.setSelected(false);
				textView3.setSelected(false);
				textView4.setSelected(false);
				if (listener != null) {
					listener.onSegmentViewClick(textView1, 0);
				}
				break;
			case 1:
				if (textView2.isSelected()) {
					return;
				}
				textView2.setSelected(true);
				textView1.setSelected(false);
				textView3.setSelected(false);
				textView4.setSelected(false);
				if (listener != null) {
					listener.onSegmentViewClick(textView2, 1);
				}
				break;
			case 2:
				if (textView3.isSelected()) {
					return;
				}
				textView3.setSelected(true);
				textView1.setSelected(false);
				textView2.setSelected(false);
				textView4.setSelected(false);
				if (listener != null) {
					listener.onSegmentViewClick(textView3, 2);
				}
				break;
			case 3:
				if (textView4.isSelected()) {
					return;
				}
				textView4.setSelected(true);
				textView1.setSelected(false);
				textView3.setSelected(false);
				textView2.setSelected(false);
				if (listener != null) {
					listener.onSegmentViewClick(textView4, 3);
				}
				break;
			default:
				if (textView1.isSelected()) {
					return;
				}
				textView1.setSelected(true);
				textView2.setSelected(false);
				if (listener != null) {
					listener.onSegmentViewClick(textView1, 0);
				}
				break;
		}
	}

	private void init() {
		// this.setLayoutParams(new
		// LinearLayout.LayoutParams(dp2Px(getContext(), 60),
		// LinearLayout.LayoutParams.WRAP_CONTENT));
		textView1 = new TextView(getContext());
		textView2 = new TextView(getContext());
		textView3 = new TextView(getContext());
		textView4 = new TextView(getContext());
		textView1.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
		textView2.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
		textView3.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
		textView4.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
		XmlPullParser xrp = getResources().getXml(R.xml.seg_text_color_selector);
		try {
			@SuppressWarnings("deprecation")
			ColorStateList csl = ColorStateList.createFromXml(getResources(), xrp);
			textView1.setTextColor(csl);

			textView2.setTextColor(csl);
			textView3.setTextColor(csl);
			textView4.setTextColor(csl);
		} catch (Exception e) {
		}
		textView1.setGravity(Gravity.CENTER);
		textView2.setGravity(Gravity.CENTER);
		textView3.setGravity(Gravity.CENTER);
		textView4.setGravity(Gravity.CENTER);
		textView1.setPadding(dp2px(5), dp2px(5), dp2px(5), dp2px(5));
		textView2.setPadding(dp2px(5), dp2px(5), dp2px(5), dp2px(5));
		textView3.setPadding(dp2px(5), dp2px(5), dp2px(5), dp2px(5));
		textView4.setPadding(dp2px(5), dp2px(5), dp2px(5), dp2px(5));
		setSegmentTextSize(getContext().getResources().getDimension(R.dimen.size3));
		textView1.setBackgroundResource(R.drawable.seg_left);
		textView2.setBackgroundResource(R.drawable.seg_center);
		textView3.setBackgroundResource(R.drawable.seg_center);
		textView4.setBackgroundResource(R.drawable.seg_right);
		textView1.setSelected(true);
		this.removeAllViews();
		this.addView(textView1);
		this.addView(textView2);
		this.addView(textView3);
		this.addView(textView4);
		this.invalidate();

		textView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (textView1.isSelected()) {
					return;
				}
				textView1.setSelected(true);
				textView2.setSelected(false);
				textView3.setSelected(false);
				textView4.setSelected(false);
				if (listener != null) {
					listener.onSegmentViewClick(textView1, 0);
				}
			}
		});
		textView2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (textView2.isSelected()) {
					return;
				}
				textView2.setSelected(true);
				textView1.setSelected(false);
				textView3.setSelected(false);
				textView4.setSelected(false);
				if (listener != null) {
					listener.onSegmentViewClick(textView2, 1);
				}
			}
		});

		textView3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (textView3.isSelected()) {
					return;
				}
				textView3.setSelected(true);
				textView2.setSelected(false);
				textView1.setSelected(false);
				textView4.setSelected(false);
				if (listener != null) {
					listener.onSegmentViewClick(textView3, 2);
				}
			}
		});
		textView4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (textView4.isSelected()) {
					return;
				}
				textView4.setSelected(true);
				textView1.setSelected(false);
				textView3.setSelected(false);
				textView2.setSelected(false);
				if (listener != null) {
					listener.onSegmentViewClick(textView4, 3);
				}
			}
		});
	}

	/**
	 * 设置字体大小 单位dip
	 * <p>
	 * 2014年7月18日
	 * </p>
	 * 
	 * @param dp
	 * @author RANDY.ZHANG
	 */
	public void setSegmentTextSize(float dp) {
		textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, dp);
		textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, dp);
		textView3.setTextSize(TypedValue.COMPLEX_UNIT_PX, dp);
		textView4.setTextSize(TypedValue.COMPLEX_UNIT_PX, dp);
	}

	private int dp2px(float dp) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public void setOnSegmentViewClickListener(onSegmentViewClickListener listener) {
		this.listener = listener;
	}

	/**
	 * 设置文字
	 * <p>
	 * 2014年7月18日
	 * </p>
	 * 
	 * @param text
	 * @param position
	 * @author RANDY.ZHANG
	 */
	public void setSegmentText(CharSequence text, int position) {
		if (position == 0) {
			textView1.setText(text);
		}
		if (position == 1) {
			textView2.setText(text);
		}
		if (position == 2) {
			textView3.setText(text);
		}
		if (position == 3) {
			textView4.setText(text);
		}
	}

	public interface onSegmentViewClickListener {
		/**
		 * 
		 * <p>
		 * 2014年7月18日
		 * </p>
		 * 
		 * @param v
		 * @param position
		 *            0-左边 1-右边
		 * @author RANDY.ZHANG
		 */
		void onSegmentViewClick(View v, int position);
	}
}
