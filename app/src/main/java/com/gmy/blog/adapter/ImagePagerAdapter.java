package com.gmy.blog.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gmy.blog.R;
import com.gmy.blog.utils.CommentUtil;
import com.squareup.picasso.Picasso;

/**
 * 图片浏览的PagerAdapter
 */
public class ImagePagerAdapter extends PagerAdapter {
	Context context;
	List<String> imgsUrl;

	// view内控件
	ImageView full_image;
	TextView progress_text;
	ProgressBar progress;
	TextView retry;

	public ImagePagerAdapter(Context context, List<String> imgsUrl) {
		this.context = context;
		this.imgsUrl = imgsUrl;

	}

	@Override
	public int getCount() {
		return imgsUrl == null ? 0 : imgsUrl.size();
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (View) arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.list_imageshow_item, null);
		full_image = (ImageView) view.findViewById(R.id.full_image);
		progress_text = (TextView) view.findViewById(R.id.progress_text);
		progress = (ProgressBar) view.findViewById(R.id.progress);
		retry = (TextView) view.findViewById(R.id.retry);// 加载失败
		progress_text.setText(String.valueOf(position));

		progress.setVisibility(View.GONE);
		progress_text.setVisibility(View.GONE);
		full_image.setVisibility(View.VISIBLE);
		retry.setVisibility(View.GONE);
		String  url=imgsUrl.get(position);
		if (CommentUtil.isPic(url)) {
			Picasso.with(context).load(imgsUrl.get(position)).into(full_image);
			((ViewPager) container).addView(view);
		}else if(CommentUtil.isMovie(url)){
			
		}else if(CommentUtil.isAudio(url)){
			
		}else {
			Picasso.with(context).load(new File(url)).into(full_image);
			((ViewPager) container).addView(view);
		}
		
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}
}
