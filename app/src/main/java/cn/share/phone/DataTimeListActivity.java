package cn.share.phone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import cn.share.R;
import cn.share.phone.uc.PGACTIVITY;

import java.util.ArrayList;

public class DataTimeListActivity extends PGACTIVITY {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_time_list);
        listView = (ListView)findViewById(R.id.dt_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String result = (String)listView.getAdapter().getItem(i);
                Intent intent = new Intent();
                intent.putExtra("result",result);
                DataTimeListActivity.this.setResult(1,intent);
                finish();
            }
        });
        reloadData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title("上报时段");
    }

    private void reloadData() {
        final ArrayList<String> messages = new ArrayList<>();
        messages.add("08:00 - 8:15");
        messages.add("08:15 - 8:30");
        messages.add("08:30 - 8:45");
        messages.add("08:45 - 9:00");
        messages.add("09:00 - 9:15");

        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return messages.size();
            }

            @Override
            public Object getItem(int i) {
                return messages.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                DTLPH dtlPH;
                if (view == null){
                    dtlPH = new DTLPH();
                    view = View.inflate(getBaseContext(),R.layout.item_listview_data,null);
                    dtlPH.data =(TextView) view.findViewById(R.id.data);
                    view.setTag(dtlPH);
                }else {
                    dtlPH = (DTLPH)view.getTag();
                }
                dtlPH.data.setText(messages.get(i));
                return view;
            }
        });

    }


    public class DTLPH {
        TextView data;
    }
}
