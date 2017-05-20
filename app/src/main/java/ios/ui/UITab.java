package ios.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.share.R;
import cn.vipapps.COLOR;
import cn.vipapps.IMAGE;
import cn.vipapps.android.Res;

public class UITab extends LinearLayout {
	public UITab(Context context) {
		super(context);
		_init(context);
	}

	int COLOR_UNSELECT = COLOR.parse("#808080");

	ImageView iconImageView;
	TextView titleTextView;

	public UITab(Context context, AttributeSet attrs) {
		super(context, attrs);
		_init(context);
		_attrs(attrs);
	}

	void _init(Context context) {
		this.setOrientation(LinearLayout.VERTICAL);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.ios_uitab, this,true);
		//
		iconImageView = (ImageView) this.findViewById(R.id.iconImageView);
		titleTextView = (TextView) this.findViewById(R.id.titleTextView);
	}

	@SuppressLint("Recycle")
	private void _attrs(AttributeSet attrs) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ios);
		int count = typedArray.getIndexCount();
		for (int i = 0; i < count; i++) {
			int resourceID = typedArray.getIndex(i);
			if (resourceID == R.styleable.ios_icon) {
				icon(typedArray.getDrawable(resourceID));
			} else if (resourceID == R.styleable.ios_title) {
				title(typedArray.getString(resourceID));
			} else if (resourceID == R.styleable.ios_tintColor) {
				tintColor(typedArray.getColor(resourceID, 0));
			} else {

			}
		}
	}

	int _tintColor = COLOR.parse("#0000FF");

	public void tintColor(int tintColor) {
		_tintColor = tintColor;
		_update();
	}

	public int tintColor() {
		return _tintColor;
	}

	public void title(String title) {
		titleTextView.setText(title);
	}


	public String title() {
		return titleTextView.getText().toString();
	}



	Bitmap _icon_org, _icon_unselect, _icon_select;

	public void icon(int id) {
		Drawable drawable = Res.drawable(id);
		_icon_org = IMAGE.parse(drawable);
		_update();
	}

	public void icon(Bitmap bitmap) {
		_icon_org = bitmap;
		_update();
	}

	public void icon(Drawable drawable) {
		_icon_org = IMAGE.parse(drawable);
		_update();
	}

	boolean _select;

	public void select(boolean select) {
		_select = select;
		_update();
	}

	public boolean select() {
		return _select;
	}

	@SuppressWarnings("deprecation")
	public Drawable icon() {
		return new BitmapDrawable(_icon_org);
	}

	void _update() {
		if (!this.isAttachedToWindow()) {
			return;
		}
		_icon_unselect = IMAGE.changeColor(_icon_org, COLOR_UNSELECT);
		_icon_select = IMAGE.changeColor(_icon_org, _tintColor);
		titleTextView.setTextColor(_select ? _tintColor : COLOR_UNSELECT);
		iconImageView.setImageBitmap(_select ? _icon_select : _icon_unselect);
	}

	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		LayoutParams layoutParams = (LayoutParams) this.getLayoutParams();
		layoutParams.weight = 1;
		this.setLayoutParams(layoutParams);
		_update();
	}
}
