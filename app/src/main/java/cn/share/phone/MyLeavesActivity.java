package cn.share.phone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
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

import uc.SwipeListView;
import uc.XListView;

import static cn.share.Common.PAGESIZE;

public class MyLeavesActivity extends PGACTIVITY {
    private static int start = 0;
    private static int type = 100;
    SwipeListView listView;
    BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_leaves);
        //
        MESSAGE.receive(Common.MSG_LEAVEADDREFESH, new CALLBACK<Bundle>() {
            @Override
            public void run(boolean isError, Bundle result) {
                reloadData();
            }
        });
        listView = (SwipeListView) findViewById(R.id.msl_listview);
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
                Intent intent = new Intent(MyLeavesActivity.this, StaffLeaveActivity.class);
                intent.putExtra("leave", leave);
                startActivity(intent);
            }
        });

        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return allLeave.length();
            }

            @Override
            public JSONObject getItem(int i) {
                return allLeave.optJSONObject(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                SLLPH sllPH;
                if (view == null) {
                    sllPH = new SLLPH();
                    view = View.inflate(getBaseContext(), R.layout.item_listview_staff_leave, null);
                    sllPH.item_left = (RelativeLayout) view.findViewById(R.id.item_left);
                    sllPH.item_right = (RelativeLayout) view.findViewById(R.id.item_right);

                    sllPH.name = (TextView) view.findViewById(R.id.name);
                    sllPH.status = (TextView) view.findViewById(R.id.status);
                    sllPH.content = (TextView) view.findViewById(R.id.content);
                    sllPH.time = (TextView) view.findViewById(R.id.time);
                    sllPH.ctime = (TextView) view.findViewById(R.id.ctime);
                    view.setTag(sllPH);
                } else {
                    sllPH = (SLLPH) view.getTag();
                }
                JSONObject leave = getItem(i);
                sllPH.name.setText(leave.optString("check_user"));
                sllPH.content.setText(leave.optString("content"));
                sllPH.time.setText(leave.optString("start")+"  -  "+leave.optString("end"));
                sllPH.status.setText(Common.approvalStatus(leave.optString("status")));
                sllPH.ctime.setText(leave.optString("check_time"));

                sllPH.item_right.setTag(i);
                sllPH.item_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) v.getTag();
                        JSONObject fault = getItem(position);
                        String id = fault.optString("id");
                        Log.e( "onClick: ", id);
                        RestBLL.delete_leaves(id, new CALLBACK<JSONObject>() {
                            @Override
                            public void run(boolean isError, JSONObject result) {
                                if (isError){
                                    return;
                                }
                                onStart();
                                reloadData();
                            }
                        });

                    }
                });
                return view;
            }
        };
        listView.setAdapter(adapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title("我的假条");
        this.navigationBar().rightNavButton(R.mipmap.icon_add, new CALLBACK() {
            @Override
            public void run(boolean isError, Object result) {
                Intent intent = new Intent(MyLeavesActivity.this,LeaveAddActivity.class);
                startActivity(intent);
            }
        });
    }


    JSONArray allLeave;

    private void reloadData() {
        start = 0;
        allLeave = new JSONArray();
        listView.setPullLoadEnable(false);
        loadData();

    }

    private void loadData() {
        RestBLL.get_leaves_list(start, type, new CALLBACK<JSONArray>() {
            @Override
            public void run(boolean isError, JSONArray messages) {
                listView.stopLoadMore();
                listView.stopRefresh();
                if (isError) {
                    return;
                }
                /*

                 */
                start+=PAGESIZE;
                if (messages.length() < PAGESIZE) {
                    listView.setPullLoadEnable(false);
                }else {
                    listView.setPullLoadEnable(true);
                }
                for (int i = 0; i < messages.length(); i++) {
                    allLeave.put(messages.opt(i));
                }
                adapter.notifyDataSetChanged();
            }
        });

    }


    public class SLLPH {
        TextView name, time,content,status,ctime;
        public RelativeLayout item_left,item_right;
    }
}
