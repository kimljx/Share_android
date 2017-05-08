package cn.share.phone;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.share.Common;
import cn.share.R;
import cn.share.RestBLL;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.MESSAGE;

import org.json.JSONArray;
import org.json.JSONObject;

import uc.SegmentView;
import uc.XListView;

import static cn.share.Common.PAGESIZE;

public class MessageListActivity extends PGACTIVITY {

    private int start = 0;
    private int type = 0;
    XListView listView;
    BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        //
        MESSAGE.receive(Common.MSG_MESSAGEREFESH, new CALLBACK<Bundle>() {
            @Override
            public void run(boolean isError, Bundle result) {

                onStart();
                reloadData();

            }
        });
        listView = (XListView) findViewById(R.id.msg_listview);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }

            @Override
            public void onLoadMore() {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("onItemClick: ", i + "  "+(i - listView.getHeaderViewsCount()));
                int j = i - listView.getHeaderViewsCount();
                String messageId = ((JSONObject) adapter.getItem(j)).optString("id");
                Intent intent = new Intent(MessageListActivity.this, MessageDetailActivity.class);
                intent.putExtra("messageId", messageId);
                Log.e("messageId: ", "  "+messageId);
                startActivity(intent);
            }
        });
        reloadData();
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return allMessage.length();
            }

            @Override
            public JSONObject getItem(int i) {
                return allMessage.optJSONObject(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                MSGPH msgPH;
                if (view == null) {
                    msgPH = new MSGPH();
                    view = View.inflate(getBaseContext(), R.layout.item_listview_msg, null);
                    msgPH.message = (TextView) view.findViewById(R.id.message);
                    msgPH.time = (TextView) view.findViewById(R.id.time);
                    view.setTag(msgPH);
                } else {
                    msgPH = (MSGPH) view.getTag();
                }
                JSONObject message = getItem(i);
                msgPH.message.setText(message.optString("notificationInfo"));
//                msgPH.time.setText(message.optString("ctime"));
                return view;
            }
        };
        listView.setAdapter(adapter);
        MESSAGE.receive(Common.MSG_NOTIFICATION, new CALLBACK<Bundle>() {
            @Override
            public void run(boolean isError, Bundle result) {
//                DIALOG.toast("333333");
                onStart();
                reloadData();
            }
        });
    }

    int select = 0;
    @Override
    protected void onStart() {
        super.onStart();
        MESSAGE.send(Common.MSG_CHANGEBAR,null);
        SegmentView segmentView = new SegmentView(this);
        String[] title = new String[]{"未读", "全部"};
        segmentView.setTitles(title);
        segmentView.setSeclect(select);
        segmentView.setOnSegmentViewClickListener(new SegmentView.onSegmentViewClickListener() {
            @Override
            public void onSegmentViewClick(View v, int position) {
                if (position == 0) {
                    type = 0;
                } else {
                    type = 1;
                }
                select = position;
                reloadData();
            }
        });
        this.navigationBar().titleView(segmentView);
    }

    JSONArray allMessage;

    private void reloadData() {
        start = 0;
        allMessage = new JSONArray();
        listView.setPullLoadEnable(false);
        RestBLL.notificationList(String.valueOf(type),new CALLBACK<JSONArray>() {
            @Override
            public void run(boolean isError, JSONArray result) {
                if (isError) {
                    listView.stopRefresh();
                    return;
                }
                //更新数据
                allMessage = result;
                adapter.notifyDataSetChanged();
                listView.stopRefresh();
            }
        });

    }



    public class MSGPH {
        TextView message, time;
    }
}
