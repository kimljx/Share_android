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

public class DataPeopleListActivity extends PGACTIVITY {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_people_list);
        listView = (ListView)findViewById(R.id.dp_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               String result = (String)listView.getAdapter().getItem(i);
                Intent intent = new Intent();
                intent.putExtra("result",result);
                DataPeopleListActivity.this.setResult(1,intent);
                finish();
            }
        });
        reloadData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title("上报人数");
    }

    private void reloadData() {
        final ArrayList<String> messages = new ArrayList<>();
        messages.add("0");
        messages.add("1 - 5");
        messages.add("6 - 10");
        messages.add("11 - 15");
        messages.add("16 - 20");
        messages.add("21 - 25");
        messages.add("26 - 30");
        messages.add("31 - 35");
        messages.add("36 - 40");
        messages.add(">40");
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
                DPLPH dplPH;
                if (view == null){
                    dplPH = new DPLPH();
                    view = View.inflate(getBaseContext(),R.layout.item_listview_data,null);
                    dplPH.dtata =(TextView) view.findViewById(R.id.data);
                    view.setTag(dplPH);
                }else {
                    dplPH = (DPLPH)view.getTag();
                }
                dplPH.dtata.setText(messages.get(i));
                return view;
            }
        });

    }


    public class DPLPH {
        TextView dtata;
    }
}

