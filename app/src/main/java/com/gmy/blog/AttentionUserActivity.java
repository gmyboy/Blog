package com.gmy.blog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.lucasr.smoothie.AsyncListView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.gmy.blog.adapter.AttentionUserAdapter;
import com.gmy.blog.entity.UserBean;
import com.gmy.blog.utils.Config;
import com.gmy.blog.utils.UserNetUtil;
import com.gmy.blog.widget.pulltorefresh.PullToRefreshBase;
import com.gmy.blog.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.gmy.blog.widget.pulltorefresh.PullToRefreshListView;

public class AttentionUserActivity extends BaseActivity implements
		OnClickListener {
	private String url = Config.URL_GETATTENTIONUSER;
	private Button btn_back;
	private AsyncListView listView;
	private List<UserBean> list;
	private AttentionUserAdapter adapter;
	private PullToRefreshListView pullView;
	private String from = "";
	private Intent intent;
	// private View header;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == Config.PULL_DOWN) {
				pullView.onPullDownRefreshComplete();
				adapter = new AttentionUserAdapter(mContext, list,
						getSharePreferences().getString(Config.USER_NAME, ""));
				listView.setAdapter(adapter);

			}

		};
	};

	@Override
	public void initView() {
		intent=getIntent();
		from = intent.getStringExtra("FROM");
		btn_back = (Button) this.findViewById(R.id.att_btn_back);
		pullView = (PullToRefreshListView) findViewById(R.id.atten_pullview);
		pullView.setPullLoadEnabled(false);
		pullView.setScrollLoadEnabled(true);
		listView = pullView.getRefreshableView();
		listView.setDividerHeight(0);
		listView.setSelector(R.color.all_transparent);
		pullView.doPullRefreshing(true, 500);
		setLastUpdateTime();
		// header = LayoutInflater.from(mContext).inflate(
		// R.layout.view_attention_list_header, null);
		btn_back.setOnClickListener(this);
		if (from.equals("PublishActivity")) {
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					intent.putExtra("name", list.get(position).getUser().getUsername());
					setResult(RESULT_OK,intent);
					finish();
				}
			});
		}
	}

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_attention);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// listView.addHeaderView(header);// 给listview添加头部
		pullView.setOnRefreshListener(new OnRefreshListener<AsyncListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<AsyncListView> refreshView) {
				new Thread() {
					public void run() {
						list = UserNetUtil.getAttentionUser(
								url,
								getSharePreferences().getString(
										Config.USER_NAME, ""));
						handler.sendEmptyMessage(Config.PULL_DOWN);
					};
				}.start();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<AsyncListView> refreshView) {

			}
		});

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
		case R.id.att_btn_back:
			finish();
			break;

		default:
			break;
		}
	}

}
