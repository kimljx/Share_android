package cn.share.phone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.share.Common;
import cn.share.PGAJAX;
import cn.share.R;
import cn.share.RestBLL;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.JSON;
import cn.vipapps.MESSAGE;
import uc.CircleImageView;
import uc.XListView;

public class HomeDetailActivity extends PGACTIVITY {
    TextView name, conten,collectNum,commentNum;
    CircleImageView circlehead;
    ImageView imgdig, imgbntcollect,imgbntpinglun;
    XListView listView;
    BaseAdapter adapter;
    String messageId;

    void init(){
        circlehead = (CircleImageView) findViewById(R.id.head_img);
        name = (TextView) findViewById(R.id.name_tv);
        conten = (TextView) findViewById(R.id.concent_tv);
        collectNum = (TextView) findViewById(R.id.collectNum);
        commentNum = (TextView) findViewById(R.id.commentNum);
        imgdig = (ImageView) findViewById(R.id.bigimg_img);
        imgbntcollect = (ImageView) findViewById(R.id.imgbnt_coll);
        imgbntpinglun = (ImageView) findViewById(R.id.imgbnt_pinglun);
        listView = (XListView) findViewById(R.id.xlistview);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        init();
        String string = getIntent().getStringExtra("share");
        JSONObject share = JSON.parse(string);
        Log.e("onCreate: ", share+"");
        if(share == null){
            return;
        }
        JSONObject message = share.optJSONObject("message");
        messageId = message.optString("messageId");
        name.setText(share.optString("userName")+"");
        conten.setText(message.optString("messageInfo"));
        int n1 = message.optInt("messageCollectnum");
        int n2 = message.optInt("messageCommentnum");
        collectNum.setText(n1 == 0 ? "" : n1 + "");
        commentNum.setText(n2 == 0 ? "" : n1 + "");
        imgbntcollect.setImageDrawable(
                share.optBoolean("isCollect")
                        ? getResources().getDrawable(R.mipmap.collect_t)
                        : getResources().getDrawable(R.mipmap.collect_f));
        imgbntpinglun.setImageDrawable(
                share.optBoolean("isComment")
                        ? getResources().getDrawable(R.mipmap.comment_t)
                        : getResources().getDrawable(R.mipmap.comment_f));
        //头像
        final String avatarurl = share.optString("pictureUrl");
        final ImageView avatar = circlehead;
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

        //分享的图片
        final String imgurl = message.optString("pictureUrl");
        final ImageView img = imgdig;
        img.setImageBitmap(null);
        img.setTag(imgurl);
        PGAJAX.getImage(imgurl, true, new CALLBACK<Bitmap>() {
            @Override
            public void run(boolean isError, Bitmap result) {
                if (img.getTag() == imgurl) {
                    if (result == null) {
                        avatar.setImageDrawable(getResources().getDrawable(R.mipmap.avatar));
                        return;
                    }
                    img.setImageBitmap(result);
                } else {

                }

            }
        });
        //
        MESSAGE.receive(Common.MSG_HOMELIST, new CALLBACK<Bundle>() {
            @Override
            public void run(boolean isError, Bundle result) {
                reloadData();
            }
        });


        listView.setPullLoadEnable(false);
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
                CommentPH msgPH;
                if (view == null) {
                    msgPH = new CommentPH();
                    view = View.inflate(getBaseContext(), R.layout.item_listview_comment, null);
                    circlehead = (CircleImageView) view.findViewById(R.id.head_img);
                    name = (TextView) view.findViewById(R.id.name_tv);
                    conten = (TextView) view.findViewById(R.id.concent_tv);
                    view.setTag(msgPH);
                } else {
                    msgPH = (CommentPH) view.getTag();
                }
                JSONObject share = getItem(i);
                JSONObject comment = share.optJSONObject("comment");
                name.setText(share.optString("userName"));
                conten.setText(comment.optString("commentInfo"));
                //头像
                final String avatarurl = share.optString("pictureUrl");
                final ImageView avatar = circlehead;
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

                return view;
            }
        };
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MESSAGE.send(Common.MSG_CHANGEBAR,null);
        this.navigationBar().title("分享正文");
        this.navigationBar().rightNavButton("评论", new CALLBACK() {
            @Override
            public void run(boolean isError, Object result) {
                Intent intent = new Intent(HomeDetailActivity.this, HomeDetailActivity.class);
                intent.putExtra("messageId", messageId);
                startActivity(intent);
            }
        });
    }
    JSONArray allFault;

    private void reloadData() {
        allFault = new JSONArray();
        listView.setPullLoadEnable(false);
        RestBLL.commentList(messageId,new CALLBACK<JSONArray>() {
            @Override
            public void run(boolean isError, JSONArray result) {
                if (isError) {
                    return;
                }
                Log.e("commentList: ", result+"");
                //更新数据
                allFault = result;
                adapter.notifyDataSetChanged();
                listView.stopRefresh();
            }
        });

    }


    public class CommentPH {
        TextView name, conten;
        CircleImageView circlehead;
    }
}
