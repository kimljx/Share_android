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

public class SegmentView extends LinearLayout {
	private TextView textView1;
	private TextView textView2;
	private onSegmentViewClickListener listener;

	public SegmentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SegmentView(Context context) {
		super(context);
		init();
	}

	public void setTitles(String[] titles) {
		textView1.setText(titles[0]);
		textView2.setText(titles[1]);
	}

	public void setSeclect(int index){
		switch(index){
			case 0:
				if (textView1.isSelected()) {
					return;
				}
				textView1.setSelected(true);
				textView2.setSelected(false);
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
				if (listener != null) {
					listener.onSegmentViewClick(textView2, 1);
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
		textView1.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
		textView2.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
		XmlPullParser xrp = getResources().getXml(R.xml.seg_text_color_selector);

//		XmlPullParser xrp = getResources().getXml(R.drawable.seg_text_color_selector);
		try {
			@SuppressWarnings("deprecation")
			ColorStateList csl = ColorStateList.createFromXml(getResources(), xrp);
			textView1.setTextColor(csl);
			textView2.setTextColor(csl);
		} catch (Exception e) {
		}
		textView1.setGravity(Gravity.CENTER);
		textView2.setGravity(Gravity.CENTER);
		textView1.setPadding(dp2px(5), dp2px(5), dp2px(5), dp2px(5));
		textView2.setPadding(dp2px(5), dp2px(5), dp2px(5), dp2px(5));
		setSegmentTextSize(getContext().getResources().getDimension(R.dimen.size3));
		textView1.setBackgroundResource(R.drawable.seg_left);
		textView2.setBackgroundResource(R.drawable.seg_right);
		textView1.setSelected(true);
		this.removeAllViews();
		this.addView(textView1);
		this.addView(textView2);
		this.invalidate();

		textView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (textView1.isSelected()) {
					return;
				}
				textView1.setSelected(true);
				textView2.setSelected(false);
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
				if (listener != null) {
					listener.onSegmentViewClick(textView2, 1);
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
