package com.gmy.blog.adapter;

import java.io.File;
import java.util.List;

import com.gmy.blog.R;
import com.gmy.blog.utils.CommentUtil;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FoundPagerAdapter extends PagerAdapter {

	Context context;
	List<String> imgsUrl;

	public FoundPagerAdapter(Context context, List<String> imgsUrl) {
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
				R.layout.list_found_pager_item, null);
		ImageView img = (ImageView) view.findViewById(R.id.found_image);
		String url = imgsUrl.get(position);
		if (CommentUtil.isPic(url)) {
			Picasso.with(context).load(imgsUrl.get(position)).into(img);
			((ViewPager) container).addView(view);
		}

		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

}
