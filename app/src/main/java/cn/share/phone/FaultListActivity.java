package cn.share.phone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.share.Common;
import cn.share.R;
import cn.share.RestBLL;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.MESSAGE;

import org.json.JSONArray;
import org.json.JSONObject;

import uc.SwipeListView;
import uc.XListView;

import static cn.share.Common.PAGESIZE;

public class FaultListActivity extends PGACTIVITY {
    private int start = 0;
    BaseAdapter adapter;
    SwipeListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault_list);
        //
        MESSAGE.receive(Common.MSG_FAULTREFESH, new CALLBACK<Bundle>() {
            @Override
            public void run(boolean isError, Bundle result) {
                reloadData();
            }
        });

        listView = (SwipeListView)findViewById(R.id.fault_listview);

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
        adapter =new BaseAdapter() {
            @Override
            public int getCount() {
                return allFault.length();
            }

            @Override
            public JSONObject getItem(int i) {
                return allFault.optJSONObject(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                FaultPH msgPH;
                if (view == null){
                    msgPH = new FaultPH();
                    view = View.inflate(getBaseContext(),R.layout.item_listview_fault_repair,null);
                    msgPH.item_left = (RelativeLayout) view.findViewById(R.id.item_left);
                    msgPH.item_right = (RelativeLayout) view.findViewById(R.id.item_right);


                    msgPH.name =(TextView) view.findViewById(R.id.name);
                    msgPH.content =(TextView) view.findViewById(R.id.content);
                    msgPH.status =(TextView) view.findViewById(R.id.status);
                    msgPH.time =(TextView) view.findViewById(R.id.time);
                    view.setTag(msgPH);
                }else {
                    msgPH = (FaultPH)view.getTag();
                }
                JSONObject fault = getItem(i);
                msgPH.name.setText(fault.optString("name"));
                msgPH.content.setText(fault.optString("content"));
                msgPH.status.setText(fault.optString("status").equals("0")?"待处理":"已处理");
                msgPH.time.setText(fault.optString("create_time"));
                msgPH.item_right.setTag(i);
                msgPH.item_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) v.getTag();
                        JSONObject fault = getItem(position);
                        String id = fault.optString("id");
                        RestBLL.delete_breakdown(id, new CALLBACK<JSONObject>() {
                            @Override
                            public void run(boolean isError, JSONObject result) {
                                if (isError){
                                    return;
                                }
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
        this.navigationBar().title("我的报修");
        this.navigationBar().rightNavButton(R.mipmap.icon_add, new CALLBACK() {
            @Override
            public void run(boolean isError, Object result) {
                Intent intent = new Intent(FaultListActivity.this,FaultFormActivity.class);
                startActivity(intent);
            }
        });
    }



    JSONArray allFault;

    private void reloadData()
    {
        start = 0;
        allFault = new JSONArray();
        listView.setPullLoadEnable(false);
        loadData();

    }

    private void loadData() {

        RestBLL.get_self_breakdown_list(start, new CALLBACK<JSONArray>() {
            @Override
            public void run(boolean isError, JSONArray messages) {
                listView.stopLoadMore();
                listView.stopRefresh();
                if (isError) {
                    return;
                }
                /*
                 */
                start += PAGESIZE;
                if (messages.length() < PAGESIZE) {
                    listView.setPullLoadEnable(false);
                }else {
                    listView.setPullLoadEnable(true);
                }
                for (int i = 0; i < messages.length(); i++) {
                    allFault.put(messages.opt(i));
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

    public class FaultPH{
        TextView name,content,status,time;
        public RelativeLayout item_left,item_right;
    }
}
