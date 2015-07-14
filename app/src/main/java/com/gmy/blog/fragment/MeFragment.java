package com.gmy.blog.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gmy.blog.AttentionUserActivity;
import com.gmy.blog.LoginActivity;
import com.gmy.blog.MeActivity;
import com.gmy.blog.R;
import com.gmy.blog.SettingActivity;
import com.gmy.blog.entity.User;
import com.gmy.blog.entity.UserBean;
import com.gmy.blog.utils.CommentUtil;
import com.gmy.blog.utils.Config;
import com.gmy.blog.utils.StackOfActivity;
import com.gmy.blog.utils.UserNetUtil;
import com.gmy.blog.widget.pulltorefresh.PullToRefreshBase;
import com.gmy.blog.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.gmy.blog.widget.pulltorefresh.PullToRefreshScrollView;
import com.squareup.picasso.Picasso;

public class MeFragment extends Fragment implements OnClickListener {
	private static final String TAG = "MeFragment";
	private RelativeLayout lay_avater, lay_perfect, lay_rank, lay_collect,// 完善资料、我的等级、我的收藏
			lay_alumb;// 我的相册
	private LinearLayout lyMBlogs, lyFavs, lyFans;
	private PullToRefreshScrollView pullView;
	private Button me_btn_offlog;
	private Intent intent;
	private TextView name, intro, blogsnum, attentionnum, fansnum;
	private TextView addfriend, me_setting;
	private UserBean userBean;
	private SharedPreferences spf;
	private Context context;
	private User user;
	private String url = Config.URL_GETUSERINFO;
	private ImageView img_avator;
	private String collectNum;
	private Uri uri;
	private String back;
	private ScrollView scollview;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == Config.PULL_DOWN) {
				Picasso.with(getActivity())
						.load(Config.IP + "avators/"
								+ spf.getString(Config.USER_NAME, "")
								+ "/0.jpg").resize(100, 100).centerCrop()
						.into(img_avator);
				user = userBean.getUser();
				name.setText(user.getUsername());
				if (user.getIntro().equals("") || user.getIntro() == null) {
					intro.setText(" 添加简介让别人更了解你");
				} else {
					intro.setText(" 简介: " + user.getIntro());
				}
				blogsnum.setText(collectNum);
				attentionnum.setText(userBean.getAttention() + "");
				fansnum.setText(userBean.getFans() + "");
				pullView.onPullDownRefreshComplete();
				SharedPreferences.Editor editor = getActivity()
						.getSharedPreferences(Config.USER_SHAREDPREFERENCES, 1)
						.edit();
				editor.putString(Config.USER_COLLECT, collectNum);
				editor.putString(Config.USER_ATTENTION, userBean.getAttention()
						+ "");
				editor.putString(Config.USER_FANS, userBean.getFans() + "");
				editor.putString(Config.USER_INTRO, user.getIntro());
				editor.putString(Config.USER_ME_TOKEN, "1");
				editor.commit();
				setLastUpdateTime();
			}
			if (msg.what == 2) {
				if (back.equals("0")) {
					CommentUtil.showToast(context, "头像上传成功!!!");
				} else {
					CommentUtil.showToast(context, "头像上传失败!!!");
				}

			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_my, null);
		View view2 = inflater.inflate(R.layout.view_frag_me, null);
		init(view, view2);
		lyFans.setOnClickListener(this);
		lyFavs.setOnClickListener(this);
		lyMBlogs.setOnClickListener(this);
		me_btn_offlog.setOnClickListener(this);
		lay_perfect.setOnClickListener(this);
		lay_rank.setOnClickListener(this);
		lay_collect.setOnClickListener(this);
		lay_alumb.setOnClickListener(this);
		lay_avater.setOnClickListener(this);
		addfriend.setOnClickListener(this);
		me_setting.setOnClickListener(this);
		pullView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ScrollView> refreshView) {
				new Thread() {
					public void run() {
						userBean = UserNetUtil.getUserInfo(url,
								spf.getString("username", ""));
						collectNum = UserNetUtil.getCollectNum(
								Config.URL_GETCOLLECTNUM,
								spf.getString("username", ""));
						handler.sendEmptyMessage(Config.PULL_DOWN);
					};
				}.start();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ScrollView> refreshView) {

			}
		});
		return view;
	}

	private void init(View view, View view2) {
		context = getActivity();
		spf = context.getSharedPreferences("share_user", 1);
		lay_avater = (RelativeLayout) view2.findViewById(R.id.me_lay_me);
		me_btn_offlog = (Button) view2.findViewById(R.id.me_btn_offlog);
		lyMBlogs = (LinearLayout) view2.findViewById(R.id.lyMBlogs);
		lyFans = (LinearLayout) view2.findViewById(R.id.lyFans);
		lyFavs = (LinearLayout) view2.findViewById(R.id.lyFavs);
		lay_perfect = (RelativeLayout) view2.findViewById(R.id.me_lay_perfect);
		lay_rank = (RelativeLayout) view2.findViewById(R.id.me_lay_rank);
		lay_collect = (RelativeLayout) view2.findViewById(R.id.me_lay_collect);
		lay_alumb = (RelativeLayout) view2.findViewById(R.id.me_lay_alumb);
		name = (TextView) view2.findViewById(R.id.me_text_username);
		blogsnum = (TextView) view2.findViewById(R.id.tvMBlogNum);
		attentionnum = (TextView) view2.findViewById(R.id.tvFavNum);
		intro = (TextView) view2
				.findViewById(R.id.me_text_account_instroduction);
		fansnum = (TextView) view2.findViewById(R.id.tvFanNum);
		img_avator = (ImageView) view2.findViewById(R.id.ivItemPortraitV);

		addfriend = (TextView) view.findViewById(R.id.add_friend);
		me_setting = (TextView) view.findViewById(R.id.me_setting);
		pullView = (PullToRefreshScrollView) view
				.findViewById(R.id.me_pullview);
		scollview = pullView.getRefreshableView();
		scollview.addView(view2);
		pullView.setPullLoadEnabled(true);
		pullView.setScrollLoadEnabled(false);
		Picasso.with(getActivity())
				.load(Config.IP + "avators/"
						+ spf.getString(Config.USER_NAME, "") + "/0.jpg")
				.resize(100, 100).centerCrop().into(img_avator);
		if (spf.getString(Config.USER_ME_TOKEN, "0").equals("1")) {
			name.setText(spf.getString(Config.USER_NAME, ""));
			intro.setText(" 简介: " + spf.getString(Config.USER_INTRO, ""));
			blogsnum.setText(spf.getString(Config.USER_COLLECT, ""));
			attentionnum.setText(spf.getString(Config.USER_ATTENTION, ""));
			fansnum.setText(spf.getString(Config.USER_FANS, ""));
		} else {
			pullView.doPullRefreshing(true, 500);
			setLastUpdateTime();
		}
	}

	private void setLastUpdateTime() {
		SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
		String text;
		Long time = System.currentTimeMillis();
		if (System.currentTimeMillis() == 0) {
			text = "";
		} else {
			text = mDateFormat.format(new Date(time));
		}
		pullView.setLastUpdatedLabel(text);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.me_lay_me:
			intent = new Intent();
			intent.setClass(getActivity(), MeActivity.class);
			intent.putExtra("type", Config.ME_MAIN);
			startActivity(intent);
			break;
		case R.id.me_btn_offlog:
			new MaterialDialog.Builder(getActivity()).content("您确定要注销当前账号吗？")
					.positiveText("确定").negativeText("取消")
					.callback(new MaterialDialog.ButtonCallback() {
						@Override
						public void onPositive(MaterialDialog dialog) {
							// TODO Auto-generated method stub
							super.onPositive(dialog);
							SharedPreferences.Editor localEditor = getActivity()
									.getSharedPreferences("share_user", 0)
									.edit();
							localEditor.remove("username");
							localEditor.commit();
							intent = new Intent();
							intent.setClass(getActivity(), LoginActivity.class);
							startActivity(intent);
							StackOfActivity.getInstance().exit();// 退出登录后要清空后台堆栈
						}
					}).show();
			break;
		case R.id.me_setting:
			intent = new Intent();
			intent.setClass(getActivity(), SettingActivity.class);
			startActivity(intent);
			break;
		case R.id.lyFavs:
			intent = new Intent();
			intent.putExtra("FROM", TAG);
			intent.setClass(getActivity(), AttentionUserActivity.class);
			startActivity(intent);
			break;
		// case R.id.me_lay_fans:
		// intent = new Intent();
		// intent.setClass(getActivity(), FansActivity.class);
		// startActivity(intent);
		// break;
		// case R.id.me_lay_collect:
		// intent = new Intent();
		// intent.setClass(getActivity(), CollectionBlogActivity.class);
		// startActivity(intent);
		// break;
		default:
			break;
		}
	}

}
