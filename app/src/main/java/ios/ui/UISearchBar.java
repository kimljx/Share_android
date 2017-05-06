package ios.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import cn.share.R;
import cn.vipapps.android.ACTIVITY;

import java.lang.reflect.Field;

public class UISearchBar extends FrameLayout {
	LinearLayout mainView;
	SearchView searchView;
	TextView textView;
	View maskView;

	public UISearchBar(Context context) {
		super(context);
		init(context);
	}

	public UISearchBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void setHint(String hint) {
		textView.setText(hint);
		searchView.setQueryHint(hint);
	}

	public String getQuery() {
		return searchView.getQuery().toString();
	}

	@SuppressWarnings("deprecation")
	void init(Context context) {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		layoutParams.leftMargin = ACTIVITY.dp2px(20);
		layoutParams.rightMargin = ACTIVITY.dp2px(20);
		layoutParams.topMargin = ACTIVITY.dp2px(5);
		layoutParams.bottomMargin = ACTIVITY.dp2px(5);
		View backgroundView = new View(context);
		backgroundView.setLayoutParams(layoutParams);
		backgroundView.setBackgroundResource(R.drawable.ios_search_bar);
		this.addView(backgroundView);
		//
		mainView = new LinearLayout(context);
		mainView.setGravity(Gravity.CENTER);
		this.addView(mainView);
		//
		searchView = new SearchView(context);
//		searchView.setOnCloseListener(new SearchView.OnCloseListener(){
//
//			@Override
//			public boolean onClose() {
//
//				return false;
//			}
//		});
		searchView.setOnQueryTextFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(arg1) {
					changeMode(arg1);
				}
			}

		});
		mainView.addView(searchView);
		Class<?> searchViewClass = searchView.getClass();

		try {
			Field fSearchPlate = searchViewClass.getDeclaredField("mSearchPlate");
			fSearchPlate.setAccessible(true);
			View mSearchPlate = (View) fSearchPlate.get(searchView);
			mSearchPlate.setBackgroundColor(Color.TRANSPARENT);
			//
			Field fSearchButton = searchViewClass.getDeclaredField("mSearchButton");
			fSearchButton.setAccessible(true);
			ImageView mSearchButton = (ImageView) fSearchButton.get(searchView);
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mSearchButton.getLayoutParams();
			lp.width = ACTIVITY.dp2px(45);
			lp.height = ACTIVITY.dp2px(45);
			mSearchButton.setLayoutParams(lp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//
		textView = new TextView(context);
		textView.setGravity(Gravity.CENTER);
		textView.setTextAppearance(context, R.style.L2);
		mainView.addView(textView);
		//
		maskView = new View(context);
		this.addView(maskView);
		maskView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				searchView.setIconified(false);
			}

		});
	}

	public  void changeMode(boolean arg1){
		textView.setVisibility(arg1 ? View.GONE : View.VISIBLE);
		maskView.setVisibility(arg1 ? View.GONE : View.VISIBLE);
	}


	public void setOnQueryTextListener(OnQueryTextListener listener) {
		searchView.setOnQueryTextListener(listener);
	}

	public void setOnQueryCloseListener(SearchView.OnCloseListener listener) {
		changeMode(false);
		searchView.setOnCloseListener(listener);
	}
}
