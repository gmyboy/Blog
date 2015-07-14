package com.gmy.blog.adapter;

import java.net.URLEncoder;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmy.blog.R;
import com.gmy.blog.entity.Comment;
import com.gmy.blog.utils.Config;
import com.gmy.blog.widget.CircularImage;
import com.squareup.picasso.Picasso;

public class CommentAdapter extends BaseComAdapter<Comment> {

	public CommentAdapter(Context context, List<Comment> list) {
		super(list, context);
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyHolder holder = new MyHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_comment, null);
			holder.imageView = (CircularImage) convertView
					.findViewById(R.id.img_avatar);
			holder.text_username = (TextView) convertView
					.findViewById(R.id.text_username);
			holder.text_comment = (TextView) convertView
					.findViewById(R.id.text_comment);
			holder.text_date = (TextView) convertView
					.findViewById(R.id.text_date);
			convertView.setTag(holder);
		} else {
			holder = (MyHolder) convertView.getTag();
		}
		Comment bean = list.get(position);
		holder.text_username.setText(bean.getUser_name());
		if (bean.getContent().length() < 25) {
			holder.text_comment.setText(bean.getContent());
		} else {
			holder.text_comment.setText(bean.getContent().substring(0, 23)
					+ "...");
		}
		holder.text_date.setText(bean.getComment_date().substring(0, 16));
		Picasso.with(context)
				.load(Config.IP + "avators/"
						+ URLEncoder.encode(bean.getUser_name()) + "/0.jpg")
				.resize(50, 50).centerCrop().into(holder.imageView);
		return convertView;
	}

	class MyHolder {
		CircularImage imageView;
		TextView text_username;
		TextView text_comment;
		TextView text_date;
	}
}
