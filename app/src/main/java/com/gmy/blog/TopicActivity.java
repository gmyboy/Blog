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
import android.widget.TextView;

import com.gmy.blog.adapter.TopicAdapter;
import com.gmy.blog.entity.Topic;
import com.gmy.blog.utils.BlogNetUtil;
import com.gmy.blog.utils.Config;
import com.gmy.blog.widget.pulltorefresh.PullToRefreshBase;
import com.gmy.blog.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.gmy.blog.widget.pulltorefresh.PullToRefreshListView;

public class TopicActivity extends BaseActivity implements OnClickListener {
	private TextView txt_back;
	private AsyncListView listView;
	private List<Topic> list;
	private TopicAdapter adapter;
	private PullToRefreshListView pullView;
	private Intent intent;
	private String from = "";
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == Config.PULL_DOWN) {
				pullView.onPullDownRefreshComplete();
				adapter = new TopicAdapter(mContext, list);
				listView.setAdapter(adapter);

			}

		};
	};
	@Override
	public void setLayout() {
		setContentView(R.layout.activity_topic);
	}

	@Override
	public void initView() {
		intent=getIntent();
		from = intent.getStringExtra("FROM");
		txt_back = (TextView) this.findViewById(R.id.topic_cancel);
		pullView = (PullToRefreshListView) findViewById(R.id.topic_pullview);
		pullView.setPullLoadEnabled(false);
		pullView.setScrollLoadEnabled(true);
		listView = pullView.getRefreshableView();
		listView.setDividerHeight(0);
		listView.setSelector(R.color.all_transparent);
		pullView.doPullRefreshing(true, 500);
		setLastUpdateTime();
		
		if (from.equals("PublishActivity")) {
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					intent.putExtra("name", list.get(position).getName());
					setResult(RESULT_OK,intent);
					finish();
				}
			});
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		txt_back.setOnClickListener(this);
		pullView.setOnRefreshListener(new OnRefreshListener<AsyncListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<AsyncListView> refreshView) {
				new Thread() {
					public void run() {
						list =BlogNetUtil.getTopics(Config.URL_GETTOPIC);
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
		case R.id.topic_cancel:
			finish();
			break;

		default:
			break;
		}
	}
}
