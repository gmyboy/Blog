package com.gmy.blog.adapter;

import java.net.URLDecoder;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmy.blog.R;
import com.gmy.blog.entity.Topic;
import com.gmy.blog.utils.Config;
import com.squareup.picasso.Picasso;

public class TopicAdapter extends BaseComAdapter<Topic> {

	public TopicAdapter(Context context, List<Topic> list) {
		super(list, context);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyHodler hodler = new MyHodler();
		Topic topic = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_topic, null);
			hodler.name = (TextView) convertView
					.findViewById(R.id.topic_author);
			hodler.pic = (ImageView) convertView.findViewById(R.id.topic_img);
			hodler.content = (TextView) convertView
					.findViewById(R.id.topic_intro);
			hodler.type = (TextView) convertView.findViewById(R.id.topic_btn);
			convertView.setTag(hodler);
		} else
			hodler = (MyHodler) convertView.getTag();
		topic = list.get(position);
		hodler.name.setText(URLDecoder.decode(topic.getName()));
		hodler.content.setText(URLDecoder.decode(topic.getContent()).substring(0, 15)+"...");
		hodler.type.setText("#"+URLDecoder.decode(topic.getType()));
		Picasso.with(context).load(Config.IP + "imgs/topic/" + topic.getPic())
				.centerCrop().resize(100, 100).into(hodler.pic);

		return convertView;
	}

	class MyHodler {
		ImageView pic;
		TextView name;
		TextView content;
		TextView type;
	}

}
