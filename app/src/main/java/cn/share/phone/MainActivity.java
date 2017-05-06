package cn.share.phone;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import cn.share.Common;
import cn.share.R;
import cn.vipapps.CALLBACK;
import cn.vipapps.MESSAGE;
import cn.vipapps.android.ACTIVITY;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ios.ui.UITab;

public class MainActivity extends ACTIVITY {

    public static TabHost tabHost;

    final String[] tabIDs = new String[]{"0", "1", "2", "3", "4"};
    final String[] tabTitles = new String[]{"首页", "消息", "", "发现", "我的"};
    final int[] tabIcons = new int[]{R.mipmap.menu_message_, R.mipmap.menu_offer, R.mipmap.menu_work,
            R.mipmap.menu_admain, R.mipmap.sub_visit};
    final Class[] tabClasses = new Class[]{MessageListActivity.class, JobActivity.class, WorkActivity.class,
            AdminActivity.class, MyActivity.class};
    int tintColor;

    TabHost.OnTabChangeListener onTabChangeListener = new TabHost.OnTabChangeListener() {
        @Override
        public void onTabChanged(String tabId) {
            TabWidget tabWidget = tabHost.getTabWidget();
            if (selectedTab != null) {
                for (int i = 0; i < tabIDs.length; i++) {
                    ((UITab) tabWidget.getChildAt(i)).select(false);
                }
            }
            selectedTab = Integer.parseInt(tabId);
            ((UITab) tabWidget.getChildAt(selectedTab)).select(true);

        }
    };

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        String token = CONFIG.getString(Common.CONFIG_TOKEN);
//        Log.e("token: ", token);
        tabHost = (TabHost) this.findViewById(R.id.tabhost);

        tintColor = this.getResources().getColor(R.color.V);

        //
        MESSAGE.receive(Common.MSG_LOGIN, new CALLBACK() {

            @Override
            public void run(boolean isError, Object result) {

                Intent intent = new Intent(MainActivity.this, GuestLoginActivity.class);
                intent.putExtra("modal", true);
                startActivity(intent);
                finish();
            }

        });
        //
        MESSAGE.receive(Common.MSG_CHANGEBAR, new CALLBACK<Bundle>() {
            @Override
            public void run(boolean isError, Bundle result) {
                boolean isChangeColor = result == null;
                if (!isChangeColor) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(Color.BLACK); // 黑色
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(getResources().getColor(R.color.V)); // 黄色
                    }
                }
            }
        });

        //
        LocalActivityManager activityManager = new LocalActivityManager(this, false);
        activityManager.dispatchCreate(savedInstanceState);
        tabHost.setup(activityManager);
        tabHost.setOnTabChangedListener(onTabChangeListener);
        for (int i = 0; i < tabIcons.length; i++) {
            TabHost.TabSpec tabPage = tabHost.newTabSpec(tabIDs[i]);

            UITab tab = new UITab(this);
            tab.title(tabTitles[i]);
            tab.icon(tabIcons[i]);
            tab.tintColor(tintColor);
            tabPage.setIndicator(tab);
            Intent intent = new Intent(this, tabClasses[i]);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intents.add(intent);
            tabPage.setContent(intent);
            tabHost.addTab(tabPage);
        }
    }

    List<Intent> intents = new ArrayList<Intent>();
    Integer selectedTab;
    int keyBackClickCount;

    //捕捉键盘
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyBackClickCount++) {
                case 0:
                    Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            keyBackClickCount = 0;
                        }
                    }, 3000);
                    break;
                case 1:
                    this.finish();
                    break;
                default:
                    break;
            }
            return true;
        }
        return super.dispatchKeyEvent(event);

    }

    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onStart() {
        super.onStart();

    }


}
