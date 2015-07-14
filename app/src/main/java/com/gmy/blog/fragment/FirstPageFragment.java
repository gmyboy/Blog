package com.gmy.blog.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.lucasr.smoothie.AsyncListView;
import org.lucasr.smoothie.ItemManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gmy.blog.AttentionUserActivity;
import com.gmy.blog.BlogDetailActvity;
import com.gmy.blog.R;
import com.gmy.blog.SearchUserActivity;
import com.gmy.blog.adapter.TweetsAdapter;
import com.gmy.blog.entity.BlogBean;
import com.gmy.blog.services.BlogListServices;
import com.gmy.blog.utils.BlogNetUtil;
import com.gmy.blog.utils.CommentUtil;
import com.gmy.blog.utils.Config;
import com.gmy.blog.utils.TweetsLayoutLoader;
import com.gmy.blog.widget.pulltorefresh.PullToRefreshBase;
import com.gmy.blog.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.gmy.blog.widget.pulltorefresh.PullToRefreshListView;

public class FirstPageFragment extends Fragment implements OnItemClickListener,
		OnClickListener, OnItemLongClickListener {
	private String url = Config.URL_GETFRIENDBLOG;
	private BlogListServices services = new BlogListServices(getActivity());
	private AsyncListView listView;
	private PullToRefreshListView pullView;
	private TweetsAdapter adapter;
	private Button btn_top_left, btn_top_right;// 顶部两个按钮，分别为，查看热门关注，和查询用户
	private TextView title;
	private SharedPreferences spf;
	private String username;
	private List<BlogBean> list;
	private SoundPool sp;// 声明一个SoundPool
	private int music;
	private boolean isMusic = false;
	private int page = 2;
	private int action_id;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == Config.NORMAL) {
				adapter = new TweetsAdapter(getActivity(), list, username);
				listView.setAdapter(adapter);
				setLastUpdateTime();
			} else if (msg.what == Config.PULL_DOWN) {
				adapter.notifyDataSetChanged();
				pullView.onPullDownRefreshComplete();
				setLastUpdateTime();
			} else if (msg.what == Config.PULL_UP) {
				page++;
				adapter.notifyDataSetChanged();
				pullView.onPullUpRefreshComplete();
				setLastUpdateTime();
			} else if (msg.what == Config.NO_MORE) {
				pullView.setHasMoreData(false);
				setLastUpdateTime();
			} else if (msg.what == Config.ACTION_BLOG_TWEET) {
				if (msg.obj.equals("0")) {
					CommentUtil.showToast(getActivity(), "已转发");
				} else {
					CommentUtil.showToast(getActivity(), "转发失败");
				}
			} else if (msg.what == Config.ACTION_BLOG_COLLECT) {
				if (msg.obj.equals("0")) {
					CommentUtil.showToast(getActivity(), "已收藏");
				} else {
					CommentUtil.showToast(getActivity(), "收藏失败");
				}
			}
//			TweetsLayoutLoader loader = new TweetsLayoutLoader(getActivity());
//			ItemManager.Builder builder = new ItemManager.Builder(loader);
//			builder.setPreloadItemsEnabled(true).setPreloadItemsCount(5);
//			builder.setThreadPoolSize(4);
//			ItemManager itemManager = builder.build();
//			listView.setItemManager(itemManager);
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_firstpage, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		initView(view);
		if (CommentUtil.isNetworkAvailable(getActivity())) {
			new Thread() {
				public void run() {
					list = BlogNetUtil.getBlogs(url, username, "1");
					handler.sendEmptyMessage(Config.NORMAL);
				};
			}.start();
		} else {
			list = services.getList(0, 10);
			handler.sendEmptyMessage(Config.NORMAL);
		}

		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		btn_top_left.setOnClickListener(this);
		btn_top_right.setOnClickListener(this);
		pullView.setOnRefreshListener(new OnRefreshListener<AsyncListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<AsyncListView> refreshView) {

				new Thread() {
					public void run() {
						int t = 0;
						List<BlogBean> temp_list = BlogNetUtil.getBlogs(url,
								username, "1");
						if (list != null && list.size() != 0) {
							// 第一次运行会报空指针
							if (temp_list.get(0).getBlog().getId() <= list
									.get(0).getBlog().getId()) {
								Log.d("gmyboy", "没有最新数据");
							} else if (list.get(0).getBlog().getId() < temp_list
									.get(temp_list.size() - 1).getBlog()
									.getId()) {
								Log.d("gmyboy", "全是最新数据");
								for (int i = 0; i < temp_list.size(); i++) {
									list.add(
											0,
											temp_list.get(temp_list.size() - i
													- 1));
								}
							} else {
								for (int i = temp_list.size() - 1; i >= 0; i--) {
									if (temp_list.get(i).getBlog().getId() == list
											.get(i).getBlog().getId()) {
										t = i;
									}
								}
								for (int j = t - 1; j > -0; j--) {
									list.add(0, temp_list.get(j));
								}
								Log.d("gmyboy", "新数据不足十个");
							}
						}

						services.savaData(list);
						handler.sendEmptyMessage(Config.PULL_DOWN);
					};
				}.start();

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<AsyncListView> refreshView) {

				new Thread() {
					public void run() {
						List<BlogBean> temp_list = BlogNetUtil.getBlogs(url,
								username, String.valueOf(page));
						if (temp_list.size() != 0) {
							for (int i = 0; i < temp_list.size(); i++) {
								list.add(temp_list.get(i));
							}
							handler.sendEmptyMessage(Config.PULL_UP);
						} else {
							handler.sendEmptyMessage(Config.NO_MORE);
						}
					};
				}.start();

			}
		});
	}

	private void initView(View view) {
		btn_top_left = (Button) view.findViewById(R.id.add_content);
		btn_top_right = (Button) view.findViewById(R.id.main_search_person);
		title = (TextView) view.findViewById(R.id.text_toptitle);
		pullView = (PullToRefreshListView) view
				.findViewById(R.id.first_pullview);
		pullView.setPullLoadEnabled(false);
		pullView.setScrollLoadEnabled(true);
		spf = getActivity().getSharedPreferences("share_user", 1);
		username = spf.getString("username", "");
		isMusic = spf.getBoolean("Audio", false);
		title.setText(username);

		
		listView = pullView.getRefreshableView();
	
		listView.setDividerHeight(20);
		listView.setSelector(R.color.all_transparent);
		if (page == 2) {
			// 开始即刷新
			pullView.doPullRefreshing(true, 500);
		}
		setLastUpdateTime();

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
		// pullView.setLastUpdatedLabel(text);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), BlogDetailActvity.class);
		intent.putExtra("blog_id",
				String.valueOf(list.get(position).getBlog().getId()));
		startActivity(intent);

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {

		case R.id.add_content:
			intent.setClass(getActivity(), SearchUserActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(
					android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			break;
		case R.id.main_search_person:
			intent.setClass(getActivity(), AttentionUserActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		action_id = list.get(position).getBlog().getId();

		new MaterialDialog.Builder(getActivity()).items(R.array.action_blog)
				.itemsCallback(new MaterialDialog.ListCallback() {
					@Override
					public void onSelection(MaterialDialog dialog, View view,
							int which, CharSequence text) {

						switch (which) {
						case 0:
							new Thread() {

								public void run() {
									Message msg = new Message();
									msg.what = Config.ACTION_BLOG_TWEET;
									msg.obj = BlogNetUtil.forwardBlogs(
											username, action_id);
									handler.sendMessage(msg);
								};
							}.start();
							break;
						case 1:
							// Intent intent = new Intent();
							// intent.setClass(getActivity(),
							// CommentActivity.class);
							// intent.putExtra("COMMENTS", (Serializable)
							// comments);
							// intent.putExtra("ID", action_id);
							// context.startActivity(intent);
							break;
						case 2:
							new Thread() {
								public void run() {

									Message msg = new Message();
									msg.what = Config.ACTION_BLOG_COLLECT;
									msg.obj = BlogNetUtil
											.supportBlogs(action_id);
									handler.sendMessage(msg);
								};
							}.start();
							break;
						default:
							break;
						}
					}
				}).show();
		return false;
	}

}
