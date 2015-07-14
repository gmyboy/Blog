/*
 * Copyright (C) 2014 Lucas Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gmy.blog.widget;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gmy.blog.MeActivity;
import com.gmy.blog.R;
import com.gmy.blog.adapter.ImageOneAdapter;
import com.gmy.blog.adapter.TweetPresenter;
import com.gmy.blog.entity.Blog;
import com.gmy.blog.entity.BlogBean;
import com.gmy.blog.utils.Config;
import com.gmy.blog.utils.DateUtil;
import com.squareup.picasso.Picasso;

/**
 * 子item布局
 * 
 * @author Administrator
 *
 */
public class TweetCompositeView extends RelativeLayout implements
		TweetPresenter {
	TextView author;
	TextView date;
	EmoticonsTextView content;
	CircularImage avator;
	TextView forward;
	TextView comfrom;
	TextView comment;
	TextView zan;
	ScrollGridView pics;
	TextView intro;// 简介
	TextView location;
	private final EnumMap<Action, LinearLayout> mActionIcons;

	public TweetCompositeView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TweetCompositeView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		LayoutInflater.from(getContext()).inflate(R.layout.list_firstblog,
				this, true);
		author = (TextView) findViewById(R.id.tv_blogername);
		date = (TextView) findViewById(R.id.list_first_date);
		comfrom = (TextView) findViewById(R.id.list_blogerfrom);
		content = (EmoticonsTextView) findViewById(R.id.list_first_content);
		forward = (TextView) findViewById(R.id.txt1);
		comment = (TextView) findViewById(R.id.txt2);
		zan = (TextView) findViewById(R.id.txt3);
		avator = (CircularImage) findViewById(R.id.ci_userphoto);
		pics = (ScrollGridView) findViewById(R.id.gv_image);
		location = (TextView) findViewById(R.id.list_blogerlocation);

		mActionIcons = new EnumMap(Action.class);
		for (Action action : Action.values()) {
			final LinearLayout icon;
			switch (action) {
			case REPLY:
				icon = (LinearLayout) findViewById(R.id.list_first_lay_bottom1);
				break;

			case RETWEET:
				icon = (LinearLayout) findViewById(R.id.list_first_lay_bottom2);
				break;

			case FAVOURITE:
				icon = (LinearLayout) findViewById(R.id.list_first_lay_bottom3);
				break;

			default:
				throw new IllegalArgumentException("Unrecognized tweet action");
			}

			mActionIcons.put(action, icon);
		}
	}

	@Override
	public boolean shouldDelayChildPressedState() {
		return false;
	}

	@Override
	public void update(BlogBean tweetbean, EnumSet<UpdateFlags> flags) {
		Blog tweet = tweetbean.getBlog();
		final ArrayList<String> picurls = new ArrayList<String>();
		final int id;
		final String name;
		author.setText(tweet.getUser_name());
		SharedPreferences localspf = getContext().getSharedPreferences(
				"share_user", 1);
		String font = localspf.getString("Font", "midlle");
		if (font.equals("small")) {
			content.setTextSize(10);
		} else if (font.equals("big")) {
			content.setTextSize(20);
		} else {
			content.setTextSize(15);
		}
		content.setText("   " + tweet.getContent());
		date.setText(DateUtil.paselDate(tweet.getBlog_date()));
		comfrom.setText("来自 " + tweet.getComefrom());
		forward.setText(tweet.getForward_id() + "");
		// jia
		comment.setText("78");
		zan.setText(tweet.getSupport() + "");
		if ((!tweet.getLocation().equals("")) && (tweet.getLocation() != null)) {
			location.setText(tweet.getLocation());
			location.setVisibility(View.VISIBLE);
		}
		Picasso.with(getContext())
				.load(Config.IP + "avators/"
						+ URLEncoder.encode(tweet.getUser_name()) + "/0.jpg")
				.centerCrop().resize(100, 100).into(avator);
		if (tweet.getPicture() != null && !tweet.getPicture().equals("")) {
			String[] t = tweet.getPicture().split(";");
			// 拆分字符串
			for (String string : t) {
				picurls.add(Config.IP + "imgs/" + string);
			}
		}
		if (tweet.getMovie() != null && !tweet.getMovie().equals("")) {
			String[] t = tweet.getMovie().split(";");
			// 拆分字符串
			for (String string : t) {
				picurls.add(Config.IP + "videos/" + string);
			}
		}
		if (tweet.getMusic() != null && !tweet.getMusic().equals("")) {
			String[] t = tweet.getMusic().split(";");
			// 拆分字符串
			for (String string : t) {
				picurls.add(Config.IP + "audios/" + string);
			}
		}
		tweet.getMovie_length();
		if (!picurls.isEmpty()) {
			pics.setVisibility(View.VISIBLE);
			ImageOneAdapter adapter_image;
			if (1 == picurls.size()) {
				pics.setNumColumns(3);
				adapter_image = new ImageOneAdapter(getContext(), picurls,
						R.layout.view_one_iv_com);
			} else if (picurls.size() > 1 && picurls.size() <= 4) {
				pics.setNumColumns(3);
				adapter_image = new ImageOneAdapter(getContext(), picurls);
			} else if (picurls.size() > 4 && picurls.size() <= 6) {
				pics.setNumColumns(3);
				adapter_image = new ImageOneAdapter(getContext(), picurls,
						R.layout.view_one_iv_six);
			} else {
				pics.setNumColumns(4);
				adapter_image = new ImageOneAdapter(getContext(), picurls);
			}
			pics.setAdapter(adapter_image);
			pics.setSelector(android.R.color.transparent);
		} else
			pics.setVisibility(View.GONE);
		name = tweet.getUser_name();
		avator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getContext(), MeActivity.class);
				intent.putExtra("type", Config.ME_OTHER);
				intent.putExtra("username", name);
				getContext().startActivity(intent);
			}
		});
//		id = tweet.getId();
//		// 转发
//		mActionIcons.get(0).setOnClickListener(new OnClickListener() {
//			String back;
//
//			@Override
//			public void onClick(View v) {
				// new Thread() {
				// public void run() {
				// back = BlogNetUtil.forwardBlogs(username, id);
				// Looper.prepare();
				// if (back.equals("0")) {
				// CommentUtil.showToast(getContext(), "已转发");
				// } else {
				// CommentUtil.showToast(getContext(), "转发失败");
				// }
				// Looper.loop();
				// };
				// }.start();
//			}
//		});
		// // 评论
		// mActionIcons.get(1).setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // Intent intent = new Intent();
		// // intent.setClass(getContext(), CommentActivity.class);
		// // intent.putExtra("COMMENTS", (Serializable) comments);
		// // intent.putExtra("ID", id);
		// // getContext().startActivity(intent);
		// }
		// });
		// // 收藏
		// mActionIcons.get(2).setOnClickListener(new OnClickListener() {
		// String back;
		//
		// @Override
		// public void onClick(View v) {
		// new Thread() {
		// public void run() {
		//
		// back = BlogNetUtil.supportBlogs(id);
		// Looper.prepare();
		// if (back.equals("0")) {
		// CommentUtil.showToast(getContext(), "已赞");
		// } else {
		// CommentUtil.showToast(getContext(), "未赞");
		// }
		// Looper.loop();
		// };
		// }.start();
		// }
		// });
	}
}
