package cn.share.phone;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import cn.share.R;
import cn.share.RestBLL;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepairSpareActivity extends PGACTIVITY {

    ListView listView;
    BaseAdapter adapter;
    //    String queryName = null;
    @BindView(R.id.SearchBar)
    SearchView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_spare);
        ButterKnife.bind(this);
//        searchBar.setIconified(true);
        searchBar.onActionViewExpanded();
        if (searchBar != null) {
            try {        //--拿到字节码
                Class<?> argClass = searchBar.getClass();
                //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                //--暴力反射,只有暴力反射才能拿到私有属性
                ownField.setAccessible(true);
                View mView = (View) ownField.get(searchBar);
                //--设置背景
                mView.setBackgroundColor(Color.TRANSPARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (searchBar == null) {
            return;
        } else {
//获取到TextView的ID
            int id = searchBar.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
//获取到TextView的控件
            TextView textView = (TextView) searchBar.findViewById(id);
//设置字体大小为14sp
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);//14sp
//设置字体颜色
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setGravity(Gravity.BOTTOM);
//设置提示文字颜色
        }
        searchBar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                reloadData();
                return false;
            }
        });

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String arg0) {
                Log.e("onQueryTextChange: ", arg0);
                Query(arg0);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                Query(arg0);
                return false;
            }


        });

        listView = (ListView) findViewById(R.id.rs_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String result = JSON.stringify((JSONObject) adapter.getItem(i));
                Intent intent = new Intent();
                intent.putExtra("result", result);
                RepairSpareActivity.this.setResult(4, intent);
                finish();
            }
        });
        reloadData();
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return assets_list.length();
            }

            @Override
            public JSONObject getItem(int i) {
                return assets_list.optJSONObject(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                RepPH repPH;
                if (view == null) {
                    repPH = new RepPH();
                    view = View.inflate(getBaseContext(), R.layout.item_listview_data, null);
                    repPH.name = (TextView) view.findViewById(R.id.data);
                    view.setTag(repPH);
                } else {
                    repPH = (RepPH) view.getTag();
                }
                JSONObject asset = getItem(i);
                String name = asset.optString("name");
                repPH.name.setText(name);
                return view;
            }
        };
        listView.setAdapter(adapter);

    }


    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title("使用备件");
    }

    JSONArray assets_list;
    JSONArray assets_copy;

    private void Query(String queryName) {
        assets_list = assets_copy;
        JSONArray query = new JSONArray();
        if (queryName != null) {
            for (int i = 0; i < assets_list.length(); i++) {
                JSONObject asset = assets_list.optJSONObject(i);
                if (asset == null) {
                    continue;
                }
                String name = asset.optString("name");
                if (name.indexOf(queryName) != -1) {
                    query.put(asset);
                }
            }
            assets_list = query;
            adapter.notifyDataSetChanged();
        } else {
            reloadData();
        }
    }

    private void reloadData() {
        assets_copy = new JSONArray();
        assets_list = new JSONArray();
//        RestBLL.get_assets_list(new CALLBACK<JSONArray>() {
//            @Override
//            public void run(boolean isError, JSONArray result) {
//                assets_list = result;
//                assets_copy = assets_list;
//                adapter.notifyDataSetChanged();
//            }
//        });

    }


    public static class RepPH {
        TextView name;
    }

}
