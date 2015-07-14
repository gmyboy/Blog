package com.gmy.blog.adapter;

import java.util.EnumSet;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmy.blog.R;
import com.gmy.blog.entity.BlogBean;

public class TweetsAdapter extends BaseComAdapter<BlogBean> {
	private final Context mContext;
	private List<BlogBean> list;

	public TweetsAdapter(Context context, List<BlogBean> list, String username) {
		super(list, context);
		this.mContext = context;
		this.list = list;
	}

	public TweetsAdapter(Context context) {
		super(context);
		this.mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final TweetPresenter presenter;
		if (convertView == null) {
			presenter = (TweetPresenter) LayoutInflater.from(mContext).inflate(
					R.layout.list_firstblog_container, parent, false);
		} else {
			presenter = (TweetPresenter) convertView;
		}

		BlogBean tweet = (BlogBean) getItem(position);
		presenter.update(tweet,
				EnumSet.noneOf(TweetPresenter.UpdateFlags.class));

		return (View) presenter;
	}

}
