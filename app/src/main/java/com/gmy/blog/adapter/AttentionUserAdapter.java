package com.gmy.blog.adapter;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import android.content.Context;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmy.blog.R;
import com.gmy.blog.entity.User;
import com.gmy.blog.entity.UserBean;
import com.gmy.blog.utils.CommentUtil;
import com.gmy.blog.utils.Config;
import com.gmy.blog.utils.UserNetUtil;
import com.squareup.picasso.Picasso;

public class AttentionUserAdapter extends BaseComAdapter<UserBean> {

	private String username;

	public AttentionUserAdapter(Context context, List<UserBean> list,
			String username) {
		super(list, context);
		this.username = username;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyHodler hodler = new MyHodler();
		User user_item = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_attention, null);
			hodler.author = (TextView) convertView
					.findViewById(R.id.att_author);
			hodler.avater = (ImageView) convertView.findViewById(R.id.att_img);
			hodler.intro = (TextView) convertView.findViewById(R.id.att_intro);
			hodler.btn = (ImageView) convertView.findViewById(R.id.att_btn);
			convertView.setTag(hodler);
		} else
			hodler = (MyHodler) convertView.getTag();
		user_item = list.get(position).getUser();
		hodler.author.setText(URLDecoder.decode(user_item.getUsername()));
		hodler.intro.setText(URLDecoder.decode(user_item.getIntro()));
		hodler.btn.setImageResource(R.drawable.attention_card_top_bg);
		Picasso.with(context)
				.load(Config.IP + "avators/"
						+ URLEncoder.encode(user_item.getUsername()) + "/0.jpg")
				.centerCrop().resize(100, 100).into(hodler.avater);
		final String temp = user_item.getUsername();
		hodler.btn.setOnClickListener(new OnClickListener() {
			String back;

			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						back = UserNetUtil.delattentionUser(username, temp);
						Looper.prepare();
						if (back.equals("0")) {
							CommentUtil.showToast(context, "已取消关注");
						} else if (back.equals("1")) {
							CommentUtil.showToast(context, "未关注");
						} else {
							CommentUtil.showToast(context, "取消关注失败");
						}
						Looper.loop();
					};
				}.start();
			}
		});
		return convertView;
	}

	class MyHodler {
		ImageView avater;
		TextView author;
		TextView intro;
		ImageView btn;
	}

}
