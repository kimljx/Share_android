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
import cn.vipapps.JSON;
import cn.vipapps.MESSAGE;

import org.json.JSONArray;
import org.json.JSONObject;

import uc.SegmentView;
import uc.XListView;

import static cn.share.Common.PAGESIZE;

public class LeaveListActivity extends PGACTIVITY {


    private int start = 0;
    private int type = 0;
    XListView listView;
    BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_list);
        MESSAGE.receive(Common.MSG_LEAVELIST, new CALLBACK<Bundle>() {
            @Override
            public void run(boolean isError, Bundle result) {
                if (select == 0) {
                    type = 0;
                    isApproval = false;
                } else {
                    type = 3;
                    isApproval = true;
                }
                Log.e("run:== ", "11111111");
                onStart();
                reloadData();
            }
        });
        listView = (XListView) findViewById(R.id.leave_listview);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }

            @Override
            public void onLoadMore() {
                loadData();
            }
        });
        reloadData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("onItemClick: ", i + "");
                i = i - listView.getHeaderViewsCount();
                JSONObject object = (JSONObject) adapter.getItem(i);
                String leave = JSON.stringify(object);
                String status = object.optString("status");
                if (status.equals("0")) {
                    Intent intent = new Intent(LeaveListActivity.this, LeaveDetailActivity.class);
                    intent.putExtra("leave", leave);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LeaveListActivity.this, StaffLeaveActivity.class);
                    intent.putExtra("leave", leave);
                    startActivity(intent);
                }

            }
        });

        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                if (allLeaves == null) {
                    return 0;
                }
                return allLeaves.length();
            }

            @Override
            public JSONObject getItem(int i) {
                return allLeaves.optJSONObject(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                LeavePH leavePH;
                if (view == null) {
                    leavePH = new LeavePH();
                    view = View.inflate(getBaseContext(), R.layout.item_listview_leave, null);
                    leavePH.name = (TextView) view.findViewById(R.id.name);
                    leavePH.status = (TextView) view.findViewById(R.id.status);
                    leavePH.content = (TextView) view.findViewById(R.id.content);
                    leavePH.time = (TextView) view.findViewById(R.id.time);
                    view.setTag(leavePH);
                } else {
                    leavePH = (LeavePH) view.getTag();
                }
                JSONObject leave = getItem(i);
                leavePH.name.setText(leave.optString("account"));
                leavePH.content.setText(leave.optString("content"));
                leavePH.time.setText(leave.optString("start") + "——" + leave.optString("end"));
                leavePH.status.setText(Common.approvalStatus(leave.optString("status")));
                leavePH.status.setVisibility(isApproval ? View.VISIBLE : View.GONE);
                return view;
            }
        };
        listView.setAdapter(adapter);
    }

    boolean isApproval;
    int select = 0;

    @Override
    protected void onStart() {
        super.onStart();
        SegmentView segmentView = new SegmentView(this);
        String[] title = new String[]{"待审假条", "已审假条"};
        segmentView.setTitles(title);
        segmentView.setSeclect(select);
        segmentView.setOnSegmentViewClickListener(new SegmentView.onSegmentViewClickListener() {
            @Override
            public void onSegmentViewClick(View v, int position) {
                if (position == 0) {
                    type = 0;
                    isApproval = false;
                } else {
                    type = 3;
                    isApproval = true;
                }
                select = position;
                reloadData();
            }
        });
        this.navigationBar().titleView(segmentView);
    }


    JSONArray allLeaves;

    private void reloadData() {
        start = 0;
        allLeaves = new JSONArray();
        listView.setPullLoadEnable(false);
        loadData();

    }

    private void loadData() {

        RestBLL.get_check_leaves_list(start, type, new CALLBACK<JSONArray>() {
            @Override
            public void run(boolean isError, JSONArray messages) {
                listView.stopLoadMore();
                listView.stopRefresh();
                if (isError) {
                    return;
                }
                start += PAGESIZE;
                if (messages.length() < PAGESIZE) {
                    listView.setPullLoadEnable(false);
                }else {
                    listView.setPullLoadEnable(true);
                }
                /*
                 */
                for (int i = 0; i < messages.length(); i++) {
                    allLeaves.put(messages.opt(i));
//                    }

                }
                adapter.notifyDataSetChanged();
            }
        });

    }


    public class LeavePH {
        TextView name, time, content, status;
    }
}
