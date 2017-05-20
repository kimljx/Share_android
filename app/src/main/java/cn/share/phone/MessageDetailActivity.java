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
import cn.vipapps.MESSAGE;
import uc.CircleImageView;
import uc.XListView;
//通知详细页
public class MessageDetailActivity extends PGACTIVITY {

    TextView name, conten, collectNum, commentNum;
    CircleImageView circlehead;
    ImageView imgdig, imgbntcollect, imgbntpinglun;
    XListView listView;
    BaseAdapter adapter;
    String notificationId;
    String messageId;

    void init() {
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

    void loadMessage(){
        RestBLL.notification(notificationId, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, final JSONObject share) {

                if (isError){
                    return;
                }
                JSONObject message = share.optJSONObject("message");
                name.setText(share.optString("userName") + "");
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
                //点击收藏
                imgbntcollect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RestBLL.collect(messageId, new CALLBACK<JSONObject>() {
                            @Override
                            public void run(boolean isError, JSONObject result) {
                                if (isError){
                                    return;
                                }
                                loadMessage();
                            }
                        });
                    }
                });
                //点击评论
                imgbntpinglun.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MessageDetailActivity.this, AddCommentActivity.class);
                        intent.putExtra("messageId", messageId);
                        startActivity(intent);
                    }
                });
                //头像
                final String avatarurl = share.optString("pictureUrl");
                circlehead.setImageBitmap(null);
                PGAJAX.getImage(avatarurl, true, new CALLBACK<Bitmap>() {
                    @Override
                    public void run(boolean isError, Bitmap result) {
                        if (result == null) {
                            circlehead.setImageDrawable(getResources().getDrawable(R.mipmap.avatar));
                            return;
                        }
                        circlehead.setImageBitmap(result);
                    }
                });
                //跳转用户详细页
                circlehead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userId = share.optString("userId");
                        Intent intent = new Intent(MessageDetailActivity.this,UserProfileActivity.class);
                        intent.putExtra("userId",userId);
                        startActivity(intent);
                    }
                });
                //分享的图片
                final String imgurl = message.optString("pictureUrl");
                imgdig.setImageBitmap(null);
                PGAJAX.getImage(imgurl, true, new CALLBACK<Bitmap>() {
                    @Override
                    public void run(boolean isError, Bitmap result) {
                        if (result == null) {
                            imgdig.setVisibility(View.GONE);
                            return;
                        }
                        imgdig.setVisibility(View.VISIBLE);
                        imgdig.setImageBitmap(result);


                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        init();
        notificationId = getIntent().getStringExtra("notificationId");
        messageId = getIntent().getStringExtra("messageId");
        loadMessage();
        //刷新页面
        MESSAGE.receive(Common.MSG_COMMENT, new CALLBACK<Bundle>() {
            @Override
            public void run(boolean isError, Bundle result) {
                loadMessage();
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
                    msgPH.circlehead = (CircleImageView) view.findViewById(R.id.head_img);
                    msgPH.name = (TextView) view.findViewById(R.id.name_tv);
                    msgPH.conten = (TextView) view.findViewById(R.id.concent_tv);
                    view.setTag(msgPH);
                } else {
                    msgPH = (CommentPH) view.getTag();
                }
                final JSONObject share = getItem(i);
                JSONObject comment = share.optJSONObject("comment");
                msgPH.name.setText(share.optString("userName"));
                msgPH.conten.setText(comment.optString("commentInfo"));
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
                            Intent intent = new Intent(MessageDetailActivity.this,UserProfileActivity.class);
                            intent.putExtra("userId",userId);
                            startActivity(intent);
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
        this.navigationBar().title("分享正文");
        this.navigationBar().rightNavButton("评论", new CALLBACK() {
            @Override
            public void run(boolean isError, Object result) {
                Intent intent = new Intent(MessageDetailActivity.this, AddCommentActivity.class);
                intent.putExtra("messageId", messageId);
                startActivity(intent);
            }
        });
    }

    JSONArray allFault;

    private void reloadData() {
        allFault = new JSONArray();
        listView.setPullLoadEnable(false);
        RestBLL.commentList(messageId, new CALLBACK<JSONArray>() {
            @Override
            public void run(boolean isError, JSONArray result) {
                listView.stopRefresh();
                if (isError) {
                    return;
                }
                Log.e("commentList: ", result + "");
                //更新数据
                allFault = result;
                adapter.notifyDataSetChanged();

            }
        });

    }


    public class CommentPH {
        TextView name, conten;
        CircleImageView circlehead;
    }
    @Override
    protected void onStop() {
        super.onStop();
        MESSAGE.send(Common.MSG_MESSAGEREFESH,null);
    }
}
