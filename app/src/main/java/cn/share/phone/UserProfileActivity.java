package cn.share.phone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import cn.vipapps.JSON;
import cn.vipapps.MESSAGE;

import org.json.JSONArray;
import org.json.JSONObject;

import ios.ui.UINavigationBar;
import uc.CircleImageView;
import uc.TableRow;
import uc.XListView;

//用户信息界面
public class UserProfileActivity extends PGACTIVITY {

    XListView listView;
    BaseAdapter adapter;


    CircleImageView mp_avatar;
    TextView mp_name, mp_username, mp_userstatus;
    TableRow mp_password,mp_sex;
    RelativeLayout re;
    private String userId;

    //绑定控件
    void init() {
        mp_avatar = (CircleImageView) this.findViewById(R.id.mp_avatar);
        mp_name = (TextView) this.findViewById(R.id.mp_name);
        mp_username = (TextView) this.findViewById(R.id.mp_username);
        mp_userstatus = (TextView) this.findViewById(R.id.sex);
        mp_password = (TableRow) this.findViewById(R.id.mp_password);
        mp_sex = (TableRow) this.findViewById(R.id.mp_sex);
        re = (RelativeLayout)this.findViewById(R.id.re);
        mp_avatar.setImageDrawable(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_leaves);
        init();
        userId = getIntent().getStringExtra("userId");
        //绑定用户数据
        fillData();
        //刷新数据
        MESSAGE.receive(Common.MSG_SHARE, new CALLBACK<Bundle>() {
            @Override
            public void run(boolean isError, Bundle result) {
                reloadData();
            }
        });
        listView = (XListView) findViewById(R.id.msl_listview);
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("onItemClick: ", i + "");
                i = i - listView.getHeaderViewsCount();
                JSONObject object = (JSONObject) adapter.getItem(i);
                String messageId = object.optJSONObject("message").optString("messageId");
                Intent intent = new Intent(UserProfileActivity.this, HomeDetailActivity.class);
                intent.putExtra("messageId", messageId);
                startActivity(intent);


            }
        });

        //绑定数据源
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
                FaultPH msgPH;
                if (view == null) {
                    msgPH = new FaultPH();
                    //绑定每个item的控件
                    view = View.inflate(getBaseContext(), R.layout.item_listview_home, null);
                    msgPH.circlehead = (CircleImageView) view.findViewById(R.id.head_img);
                    msgPH.name = (TextView) view.findViewById(R.id.name_tv);
                    msgPH.conten = (TextView) view.findViewById(R.id.concent_tv);
                    msgPH.collectNum = (TextView) view.findViewById(R.id.collectNum);
                    msgPH.commentNum = (TextView) view.findViewById(R.id.commentNum);
                    msgPH.imgdig = (ImageView) view.findViewById(R.id.bigimg_img);
                    msgPH.imgbntcollect = (ImageView) view.findViewById(R.id.imgbnt_coll);
                    msgPH.imgbntpinglun = (ImageView) view.findViewById(R.id.imgbnt_pinglun);
                    view.setTag(msgPH);
                } else {
                    msgPH = (FaultPH) view.getTag();
                }
                final JSONObject share = getItem(i);
                JSONObject message = share.optJSONObject("message");
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
                            Intent intent = new Intent(UserProfileActivity.this, UserProfileActivity.class);
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
        this.navigationBar().title("用户详情");

    }



    JSONArray allLeave;

    private void reloadData() {
        allLeave = new JSONArray();
        listView.setPullLoadEnable(false);
        RestBLL.userMessageList(userId,new CALLBACK<JSONArray>() {
            @Override
            public void run(boolean isError, JSONArray result) {
                listView.stopRefresh();
                if (isError) {
                    return;
                }
                //更新数据
                allLeave = result;
                adapter.notifyDataSetChanged();

            }
        });

    }

    void fillData() {
        RestBLL.user(userId,new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {
                if (isError) {
                    return;
                }

                mp_username.setText(result.optString("sex"));
                mp_name.setText(result.optString("userName"));
                navigationBar().title(result.optString("userName"));
                //头像
                final String url = result.optString("pictureUrl");
                PGAJAX.getImage(url, true, new CALLBACK<Bitmap>() {
                    @Override
                    public void run(boolean isError, Bitmap result) {
                        if (isError) {
                            mp_avatar.setImageDrawable(getResources().getDrawable(R.mipmap.avatar));
                            return;
                        }
                        if (result == null) {
                            mp_avatar.setImageDrawable(getResources().getDrawable(R.mipmap.avatar));
                            return;
                        }
                        mp_avatar.setImageBitmap(result);
                        Drawable drawable =new BitmapDrawable(result);
                        re.setBackground(drawable);
                    }
                });
            }
        });
    }


    public class FaultPH {
        TextView name, conten, collectNum, commentNum;
        CircleImageView circlehead;
        ImageView imgdig, imgbntcollect, imgbntpinglun;
    }
}
