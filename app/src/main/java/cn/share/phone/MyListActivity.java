package cn.share.phone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.share.Common;
import cn.share.PGAJAX;
import cn.share.R;
import cn.share.RestBLL;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.DIALOG;
import cn.vipapps.JSON;
import cn.vipapps.MESSAGE;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.vipapps.STRING;
import ios.ui.UINavigationBar;
import uc.CircleImageView;
import uc.XListView;

//我的收藏，评论，分享共用的界面
public class MyListActivity extends PGACTIVITY {

    private int start = 0;
    BaseAdapter adapter;
    XListView listView;
    String type;
    JSONArray delShare = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_list);
        type = getIntent().getStringExtra("type");
        //刷新数据
        MESSAGE.receive(Common.MSG_HOMELIST, new CALLBACK<Bundle>() {
            @Override
            public void run(boolean isError, Bundle result) {
                reloadData();
            }
        });

        listView = (XListView) findViewById(R.id.repair_listview);

        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }

            @Override
            public void onLoadMore() {

            }
        });
        reloadData();
        //绑定数据源
        adapter = new BaseAdapter() {
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
                FaultPH msgPH;
                if (view == null) {
                    msgPH = new FaultPH();
                    view = View.inflate(getBaseContext(), R.layout.item_listview_home, null);
                    msgPH.circlehead = (CircleImageView) view.findViewById(R.id.head_img);
                    msgPH.name = (TextView) view.findViewById(R.id.name_tv);
                    msgPH.conten = (TextView) view.findViewById(R.id.concent_tv);
                    msgPH.collectNum = (TextView) view.findViewById(R.id.collectNum);
                    msgPH.commentNum = (TextView) view.findViewById(R.id.commentNum);
                    msgPH.imgdig = (ImageView) view.findViewById(R.id.bigimg_img);
                    msgPH.imgbntcollect = (ImageView) view.findViewById(R.id.imgbnt_coll);
                    msgPH.imgbntpinglun = (ImageView) view.findViewById(R.id.imgbnt_pinglun);
                    msgPH.relativeLayout = (RelativeLayout) view.findViewById(R.id.rec);
                    view.setTag(msgPH);
                } else {
                    msgPH = (FaultPH) view.getTag();
                }
                final JSONObject share = getItem(i);
                JSONObject message = share.optJSONObject("message");
                msgPH.relativeLayout.setVisibility(JSON.contains(delShare, message) ? View.VISIBLE : View.GONE);
                msgPH.name.setText(share.optString("userName"));
                msgPH.conten.setText(message.optString("messageInfo"));
                int n1 = message.optInt("messageCollectnum");
                int n2 = message.optInt("messageCommentnum");
                msgPH.collectNum.setText(n1 == 0 ? "" : n1 + "");
                msgPH.commentNum.setText(n2 == 0 ? "" : n2 + "");
                msgPH.imgbntcollect.setImageDrawable(
                        share.optBoolean("isCollect")
                                ? getResources().getDrawable(R.mipmap.collect_t)
                                : getResources().getDrawable(R.mipmap.collect_f));
                msgPH.imgbntpinglun.setImageDrawable(
                        share.optBoolean("isComment")
                                ? getResources().getDrawable(R.mipmap.comment_t)
                                : getResources().getDrawable(R.mipmap.comment_f));
                //头像
                final String avatarurl = share.optString("pictureUrl");
                final ImageView avatar = msgPH.circlehead;
                avatar.setImageBitmap(null);
                avatar.setTag(avatarurl);
                PGAJAX.getImage(avatarurl, true, new CALLBACK<Bitmap>() {
                    @Override
                    public void run(boolean isError, Bitmap result) {
                        if (avatar.getTag() == avatarurl) {
                            if (result == null) {
                                avatar.setImageDrawable(getResources().getDrawable(R.mipmap.avatar));
                                return;
                            }
                            avatar.setImageBitmap(result);
                        } else {

                        }

                    }
                });
                //跳转用户详细页
                avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (avatar.getTag() == avatarurl) {
                            String userId = share.optString("userId");
                            Intent intent = new Intent(MyListActivity.this, UserProfileActivity.class);
                            intent.putExtra("userId", userId);
                            startActivity(intent);
                        } else {

                        }

                    }
                });
                //分享的图片
                final String imgurl = message.optString("pictureUrl");
                final ImageView img = msgPH.imgdig;
                img.setImageBitmap(null);
                img.setTag(imgurl);
                PGAJAX.getImage(imgurl, true, new CALLBACK<Bitmap>() {
                    @Override
                    public void run(boolean isError, Bitmap result) {
                        if (img.getTag() == imgurl) {
                            if (result == null) {
                                img.setVisibility(View.GONE);
//                                avatar.setImageDrawable(getResources().getDrawable(R.mipmap.avatar));
                                return;
                            }
                            img.setVisibility(View.VISIBLE);
                            img.setImageBitmap(result);
                        } else {

                        }

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
        final UINavigationBar navigationBar = this.navigationBar();
        this.navigationBar().title(type);
        if (type.equals("我的分享")) {
            //长按选中事件
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {

                    i = i - listView.getHeaderViewsCount();
                    JSONObject message = ((JSONObject) adapter.getItem(i)).optJSONObject("message");
                    if (JSON.contains(delShare, message)) {
                        delShare = JSON.remove(delShare, message);
                    } else {
                        delShare.put(message);
                    }
                    adapter.notifyDataSetChanged();

                    return true;
                }
            });
            navigationBar.rightNavButton("删除", new CALLBACK() {
                @Override
                public void run(boolean isError, Object result) {
                    DIALOG.confirm("确认删除？", new CALLBACK<Object>() {
                        @Override
                        public void run(boolean isError, Object result) {
                            if (delShare.length()<1){
                                DIALOG.alert("长按选择要删除的分享！");
                                return;
                            }
                            RestBLL.delMessageList(JSON.stringify(delShare), new CALLBACK<JSONObject>() {
                                @Override
                                public void run(boolean isError, JSONObject result) {
                                    if (isError){
                                        return;
                                    }
                                    DIALOG.alert("删除成功！", new CALLBACK<Object>() {
                                        @Override
                                        public void run(boolean isError, Object result) {
                                            delShare = new JSONArray();
                                            reloadData();
                                        }
                                    });

                                }
                            });
                        }
                    }, new CALLBACK<Object>() {
                        @Override
                        public void run(boolean isError, Object result) {

                        }
                    });
                }
            });

            listView.setOnItemClickListener(itemClickListener);

        } else {
            listView.setOnItemClickListener(itemClickListener);
        }
    }

    //点击事件，转跳详细页
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Log.e("onItemClick: ", i + "");
            i = i - listView.getHeaderViewsCount();
            JSONObject object = (JSONObject) adapter.getItem(i);
            String messageId = object.optJSONObject("message").optString("messageId");
            Intent intent = new Intent(MyListActivity.this, HomeDetailActivity.class);
            intent.putExtra("messageId", messageId);
            startActivity(intent);

        }
    };

    JSONArray allRepair;

    //通过type的值获取不同的接口数据
    private void reloadData() {
        listView.setPullLoadEnable(false);
        start = 0;
        allRepair = new JSONArray();
        switch (type) {
            case "我的收藏":
                RestBLL.myCollectList(new CALLBACK<JSONArray>() {
                    @Override
                    public void run(boolean isError, JSONArray result) {
                        listView.stopRefresh();
                        if (isError) {
                            return;
                        }
                        //更新数据
                        allRepair = result;
                        adapter.notifyDataSetChanged();

                    }
                });
                break;
            case "我的评论":
                RestBLL.myCommentList(new CALLBACK<JSONArray>() {
                    @Override
                    public void run(boolean isError, JSONArray result) {
                        listView.stopRefresh();
                        if (isError) {
                            return;
                        }
                        //更新数据
                        allRepair = result;
                        adapter.notifyDataSetChanged();
                    }
                });
                break;
            case "我的分享":
                RestBLL.myMessageList(new CALLBACK<JSONArray>() {
                    @Override
                    public void run(boolean isError, JSONArray result) {
                        listView.stopRefresh();
                        if (isError) {
                            return;
                        }
                        //更新数据
                        allRepair = result;
                        adapter.notifyDataSetChanged();
                    }
                });
                break;
        }

    }

    public class FaultPH {
        TextView name, conten, collectNum, commentNum;
        CircleImageView circlehead;
        ImageView imgdig, imgbntcollect, imgbntpinglun;
        RelativeLayout relativeLayout;
    }

}
