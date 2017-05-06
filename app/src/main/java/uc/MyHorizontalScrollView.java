package uc;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class MyHorizontalScrollView extends HorizontalScrollView {

	public MyHorizontalScrollView(Context context) {
		super(context);

	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (onScrollChanged != null) {
			onScrollChanged.onScrollChanged(this, x);
		}
	}

	public OnScrollChanged onScrollChanged;

	public interface OnScrollChanged {

		void onScrollChanged(MyHorizontalScrollView scrollView, int x);
	}

}
