package cn.vipapps.android;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({ "deprecation", "unused" })
public class ActionBar extends android.app.ActionBar {
	Context _context;
	View _customView;
	final int HEIGHT = 50;
	List<android.app.ActionBar.Tab> _tabs = new ArrayList<android.app.ActionBar.Tab>();
	FrameLayout _actionBarView;
	LinearLayout compactView;
	RadioGroup navView;
	ImageView iconView;
	TextView titleView;
	int _selectedIndex;

	public ActionBar(Context context, int layoutID) {
		this._context = context;
		_actionBarView = (FrameLayout) View.inflate(_context, layoutID, null);
		//
		compactView = (LinearLayout) _actionBarView
				.findViewById(context.getResources().getIdentifier("compactView", "id", context.getPackageName()));
		iconView = (ImageView) _actionBarView
				.findViewById(context.getResources().getIdentifier("iconView", "id", context.getPackageName()));
		titleView = (TextView) _actionBarView
				.findViewById(context.getResources().getIdentifier("titleView", "id", context.getPackageName()));
		//
		navView = (RadioGroup) _actionBarView
				.findViewById(context.getResources().getIdentifier("navView", "id", context.getPackageName()));
	}

	@Override
	public void addOnMenuVisibilityListener(OnMenuVisibilityListener arg0) {

	}

	@Override
	public void addTab(android.app.ActionBar.Tab tab) {
		RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		layoutParams.weight = 1;
		CompoundButton tabView = ((Tab) tab)._tabView;
		navView.addView(tabView, layoutParams);
		if (_tabs.size() == 0) {
			tabView.setChecked(true);
		}
		_tabs.add(tab);

	}

	@Override
	public void addTab(android.app.ActionBar.Tab tab, boolean arg1) {
		addTab(tab);
	}

	@Override
	public void addTab(android.app.ActionBar.Tab tab, int index) {
		RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		layoutParams.weight = 1;
		CompoundButton tabView = ((Tab) tab)._tabView;
		navView.addView(tabView, index, layoutParams);
		if (_tabs.size() == 0) {
			tabView.setChecked(true);
		}
		_tabs.add(index, tab);

	}

	@Override
	public void addTab(android.app.ActionBar.Tab tab, int index, boolean arg2) {
		addTab(tab, index);

	}

	@Override
	public View getCustomView() {
		return _customView;
	}

	@Override
	public int getDisplayOptions() {
		return 0;
	}

	@Override
	public int getHeight() {
		return ACTIVITY.dp2px(HEIGHT);
	}

	@Override
	public int getNavigationItemCount() {
		return _tabs.size();
	}

	@Override
	public int getNavigationMode() {
		return 0;
	}

	@Override
	public int getSelectedNavigationIndex() {
		return 0;
	}

	@Override
	public android.app.ActionBar.Tab getSelectedTab() {
		return _tabs.get(_selectedIndex);
	}

	@Override
	public CharSequence getSubtitle() {
		return null;
	}

	@Override
	public android.app.ActionBar.Tab getTabAt(int index) {
		return _tabs.get(index);
	}

	@Override
	public int getTabCount() {
		return _tabs.size();
	}

	@Override
	public CharSequence getTitle() {
		return titleView.getText();
	}

	@Override
	public void hide() {

	}

	@Override
	public boolean isShowing() {
		return false;
	}

	@Override
	public android.app.ActionBar.Tab newTab() {
		return new Tab(_context);
	}

	@Override
	public void removeAllTabs() {
		_tabs.clear();
		navView.removeAllViews();
	}

	@Override
	public void removeOnMenuVisibilityListener(OnMenuVisibilityListener arg0) {

	}

	@Override
	public void removeTab(android.app.ActionBar.Tab tab) {
		_tabs.remove(tab);
		navView.removeView(((Tab) tab)._tabView);
	}

	@Override
	public void removeTabAt(int index) {
		_tabs.remove(index);
		navView.removeViewAt(index);

	}

	@Override
	public void selectTab(android.app.ActionBar.Tab tab) {
		int index = tab.getPosition();
		((CompoundButton) navView.getChildAt(index)).setChecked(true);
	}

	@Override
	public void setBackgroundDrawable(Drawable drawable) {
		_actionBarView.setBackgroundDrawable(drawable);

	}

	@Override
	public void setCustomView(View view) {

	}

	@Override
	public void setCustomView(int resID) {

	}

	@Override
	public void setCustomView(View view, LayoutParams layoutParams) {

	}

	@Override
	public void setDisplayHomeAsUpEnabled(boolean enabeld) {

	}

	@Override
	public void setDisplayOptions(int resID) {

	}

	@Override
	public void setDisplayOptions(int resID, int arg1) {

	}

	@Override
	public void setDisplayShowCustomEnabled(boolean enabeld) {

	}

	@Override
	public void setDisplayShowHomeEnabled(boolean enabeld) {

	}

	@Override
	public void setDisplayShowTitleEnabled(boolean enabeld) {

	}

	@Override
	public void setDisplayUseLogoEnabled(boolean enabeld) {

	}

	@Override
	public void setIcon(int resID) {
		iconView.setImageResource(resID);

	}

	@Override
	public void setIcon(Drawable drawable) {
		iconView.setImageDrawable(drawable);

	}

	@Override
	public void setListNavigationCallbacks(SpinnerAdapter adapter, OnNavigationListener listener) {

	}

	@Override
	public void setLogo(int resID) {

	}

	@Override
	public void setLogo(Drawable drawable) {

	}

	@Override
	public void setNavigationMode(int mode) {
		compactView.setVisibility(View.GONE);
		navView.setVisibility(View.VISIBLE);
	}

	@Override
	public void setSelectedNavigationItem(int index) {
		((CompoundButton) navView.getChildAt(index)).setChecked(true);
	}

	@Override
	public void setSubtitle(CharSequence text) {

	}

	@Override
	public void setSubtitle(int resID) {

	}

	@Override
	public void setTitle(CharSequence text) {
		titleView.setText(text);

	}

	@Override
	public void setTitle(int resID) {
		titleView.setText(resID);

	}

	@Override
	public void show() {

	}

	public class Tab extends android.app.ActionBar.Tab {
		Context _context;
		CompoundButton _tabView;
		TabListener _tabListener;

		int _getIndex(View v) {
			for (int i = 0; i < _tabs.size(); i++) {
				Tab temp = (Tab) _tabs.get(i);
				if (temp._tabView.hashCode() == v.hashCode()) {
					return i;

				}
			}
			return -1;
		}

		public Tab(Context context) {
			this._context = context;
			_tabView = new RadioButton(context);
			_tabView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			layoutParams.weight = 1;
			_tabView.setLayoutParams(layoutParams);
			_tabView.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
			_tabView.setBackgroundResource(context.getResources().getIdentifier("actionbar_tab_indicator2", "drawable",context.getPackageName()));

			_tabView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean checked) {
					int index = _getIndex(arg0);
					if (index < 0) {
						return;
					}
					Tab tab = (Tab) _tabs.get(index);
					FragmentManager fragmentManager = ((Activity) _context).getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					if (checked) {
						if (_tabListener != null) {
							if (_selectedIndex == index) {
								_tabListener.onTabReselected(tab, fragmentTransaction);
							} else {
								_selectedIndex = index;
								_tabListener.onTabSelected(tab, fragmentTransaction);
							}
						}
					} else {
						if (_tabListener != null) {
							_tabListener.onTabUnselected(tab, fragmentTransaction);
						}
					}
					fragmentTransaction.commit();
				}
			});
		}

		@Override
		public CharSequence getContentDescription() {
			return _tabView.getContentDescription();
		}

		@Override
		public View getCustomView() {
			return null;
		}

		@Override
		public Drawable getIcon() {
			return null;
		}

		@Override
		public int getPosition() {
			return _getIndex(_tabView);
		}

		@Override
		public Object getTag() {
			return _tabView.getTag();
		}

		@Override
		public CharSequence getText() {
			return _tabView.getText();
		}

		@Override
		public void select() {
			_tabView.setChecked(true);
		}

		@Override
		public android.app.ActionBar.Tab setContentDescription(int resID) {
			_tabView.setContentDescription(_context.getString(resID));
			return this;
		}

		@Override
		public android.app.ActionBar.Tab setContentDescription(CharSequence text) {
			_tabView.setContentDescription(text);
			return this;
		}

		@Override
		public android.app.ActionBar.Tab setCustomView(View view) {
			return this;
		}

		@Override
		public android.app.ActionBar.Tab setCustomView(int resID) {
			return this;
		}

		@Override
		public android.app.ActionBar.Tab setIcon(Drawable drawable) {
			return this;
		}

		@Override
		public android.app.ActionBar.Tab setIcon(int resID) {
			return this;
		}

		@Override
		public android.app.ActionBar.Tab setTabListener(TabListener tabListener) {
			_tabListener = tabListener;
			return this;
		}

		@Override
		public android.app.ActionBar.Tab setTag(Object object) {
			this.setTag(object);
			return this;
		}

		@Override
		public android.app.ActionBar.Tab setText(CharSequence text) {
			_tabView.setText(text);
			return this;
		}

		@Override
		public android.app.ActionBar.Tab setText(int resID) {
			_tabView.setText(resID);
			return this;
		}

	}
}
