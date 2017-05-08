package cn.share.phone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

import uc.XListView;

import static cn.share.Common.PAGESIZE;

public class RepairListActivity extends PGACTIVITY {

    private int start = 0;
    BaseAdapter adapter;
    XListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_list);
        //
        MESSAGE.receive(Common.MSG_REPAIRREFESH, new CALLBACK<Bundle>() {
            @Override
            public void run(boolean isError, Bundle result) {
                reloadData();
            }
        });

        listView = (XListView)findViewById(R.id.repair_listview);

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
                return allRepair.length();
            }

            @Override
            public JSONObject getItem(int i) {
                return allRepair.optJSONObject(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                RepairPH repPH;
                if (view == null){
                    repPH = new RepairPH();
                    view = View.inflate(getBaseContext(),R.layout.item_listview_fault_repair,null);
                    repPH.name =(TextView) view.findViewById(R.id.name);
                    repPH.content =(TextView) view.findViewById(R.id.content);
                    repPH.status =(TextView) view.findViewById(R.id.status);
                    repPH.time =(TextView) view.findViewById(R.id.time);
                    view.setTag(repPH);
                }else {
                    repPH = (RepairPH)view.getTag();
                }
                JSONObject fault = getItem(i);
                repPH.name.setText(fault.optString("name"));
                repPH.content.setText(fault.optString("content"));
                repPH.status.setText(fault.optString("status").equals("0")?"待处理":"已处理");
                repPH.time.setText(fault.optString("create_time"));
                return view;
            }
        };
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title("我的故障处理");
        this.navigationBar().rightNavButton(R.mipmap.icon_add, new CALLBACK() {
            @Override
            public void run(boolean isError, Object result) {
                Intent intent = new Intent(RepairListActivity.this,RepairFormActivity.class);
                startActivity(intent);
            }
        });
    }


    JSONArray allRepair;

    private void reloadData() {
        listView.setPullLoadEnable(false);
        start = 0;
        allRepair = new JSONArray();
        loadData();

    }

    private void loadData() {

//        RestBLL.get_device_mod_list(start, new CALLBACK<JSONArray>() {
//            @Override
//            public void run(boolean isError, JSONArray messages) {
//                listView.stopLoadMore();
//                listView.stopRefresh();
//                if (isError) {
//                    return;
//                }
//                /*
//
//                 */
//                start += PAGESIZE;
//                if (messages.length() < PAGESIZE) {
//                    listView.setPullLoadEnable(false);
//                }else {
//                    listView.setPullLoadEnable(true);
//                }
//                for (int i = 0; i < messages.length(); i++) {
//                    allRepair.put(messages.opt(i));
//                }
//                adapter.notifyDataSetChanged();
//            }
//        });

    }

    public class RepairPH {
        TextView name,content,status,time;
    }

}
