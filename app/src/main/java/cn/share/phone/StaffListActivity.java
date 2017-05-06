package cn.share.phone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cn.share.PGAJAX;
import cn.share.R;
import cn.share.RestBLL;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;

import org.json.JSONArray;
import org.json.JSONObject;

public class StaffListActivity extends PGACTIVITY {

    ListView listView;
    BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_list);
        listView = (ListView) findViewById(R.id.staff_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String staffId = ((JSONObject)adapter.getItem(i)).optString("id");
                Intent intent = new Intent(StaffListActivity.this, RegisterActivity.class);
                intent.putExtra("staffId",staffId);
                startActivity(intent);
            }
        });
        reloadData();
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return allStaff.length();
            }

            @Override
            public JSONObject getItem(int i) {
                return allStaff.optJSONObject(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                STAPH staPH;
                if (view == null) {
                    staPH = new STAPH();
                    view = View.inflate(getBaseContext(), R.layout.item_listview_staff, null);
                    staPH.avatar = (ImageView) view.findViewById(R.id.avatar);
                    staPH.name = (TextView) view.findViewById(R.id.name);
                    staPH.leave = (TextView) view.findViewById(R.id.leave);
                    staPH.trace = (TextView) view.findViewById(R.id.trace);
                    staPH.statu = (TextView) view.findViewById(R.id.statu);
                    view.setTag(staPH);
                } else {
                    staPH = (STAPH) view.getTag();
                }
                final JSONObject staff = getItem(i);
                staPH.name.setText(staff.optString("name"));
                staPH.statu.setText(staff.optString("status").equals("1")?"在岗":"离岗");
                final ImageView avatar = staPH.avatar;

                String id = staff.optString("id");
                final String url = String.format("user_%s.png",id);
                avatar.setImageBitmap(null);
                avatar.setTag(url);
                PGAJAX.getImage(url,true, new CALLBACK<Bitmap>() {
                    @Override
                    public void run(boolean isError, Bitmap result) {
//                        if (isError){
//                            avatar.setImageDrawable(getResources().getDrawable(R.mipmap.avatar));
//                            return;
//                        }
                        if (avatar.getTag() == url){
                            if (result == null){
                                avatar.setImageDrawable(getResources().getDrawable(R.mipmap.avatar));
                                return;
                            }
                            avatar.setImageBitmap(result);
                        }else {

                        }

                    }
                });
                staPH.trace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String staffId = staff.optString("id");
                        String staffName = staff.optString("name");
                        Intent intent = new Intent(StaffListActivity.this, StaffTraceActivity.class);
                        intent.putExtra("staffId",staffId);
                        intent.putExtra("staffName",staffName);
                        startActivity(intent);
                    }
                });
                staPH.leave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String staffId = staff.optString("id");
                        Intent intent = new Intent(StaffListActivity.this, StaffLeavesActivity.class);
                        intent.putExtra("staffId",staffId);
                        startActivity(intent);
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
        this.navigationBar().title("员工管理");

    }

    JSONArray allStaff;
    private void reloadData() {

        allStaff = new JSONArray();

        RestBLL.get_staff_list(new CALLBACK<JSONArray>() {
            @Override
            public void run(boolean isError, JSONArray result) {
                allStaff = result;
                adapter.notifyDataSetChanged();
            }
        });
    }



    public class STAPH {
        ImageView avatar;
        TextView name, leave, trace, statu;
    }
}
