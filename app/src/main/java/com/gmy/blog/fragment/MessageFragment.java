package com.gmy.blog.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gmy.blog.R;
import com.gmy.blog.utils.Config;
import com.gmy.blog.widget.pulltorefresh.PullToRefreshBase;
import com.gmy.blog.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.gmy.blog.widget.pulltorefresh.PullToRefreshScrollView;

public class MessageFragment extends Fragment {
	private PullToRefreshScrollView pullView;
	private TextView msg_start_chat;
	private ScrollView scollview;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == Config.PULL_DOWN) {
				pullView.onPullDownRefreshComplete();
				setLastUpdateTime();
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag_msg, null);
		View view2 = inflater.inflate(R.layout.view_frag_msg, null);
		initView(view, view2);
		pullView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ScrollView> refreshView) {
				new Thread() {
					public void run() {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
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

	private void initView(View view, View view2) {
		msg_start_chat = (TextView) view.findViewById(R.id.msg_start_chat);
		pullView = (PullToRefreshScrollView) view
				.findViewById(R.id.msg_pullview);
		scollview = pullView.getRefreshableView();
		scollview.addView(view2);
		pullView.setPullLoadEnabled(true);
		pullView.setScrollLoadEnabled(false);
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
		pullView.setLastUpdatedLabel(text);
	}
}
