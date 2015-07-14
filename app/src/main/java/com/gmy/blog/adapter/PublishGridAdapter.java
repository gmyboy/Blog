package com.gmy.blog.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gmy.blog.R;
import com.gmy.blog.entity.Attachment;
import com.squareup.picasso.Picasso;

public class PublishGridAdapter extends BaseComAdapter<Attachment> {

	public PublishGridAdapter(List<Attachment> files, Context context) {
		super(files, context);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView img;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_publish_grid_item, null);
			img = (ImageView) convertView.findViewById(R.id.img_publish_item);
			convertView.setTag(img);
		} else {
			img = (ImageView) convertView.getTag();
		}

		Picasso.with(context).load(new File(list.get(position).getFile_path()))
				.resize(100, 88).centerCrop().into(img);

		return convertView;
	}

}
