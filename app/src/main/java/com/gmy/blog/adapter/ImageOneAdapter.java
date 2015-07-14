package com.gmy.blog.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

import com.gmy.blog.ImagesShowActivity;
import com.gmy.blog.PlayMovieActivity;
import com.gmy.blog.R;
import com.gmy.blog.utils.CommentUtil;
import com.gmy.blog.utils.Config;
import com.gmy.blog.utils.ImageUtil;
import com.gmy.blog.widget.RippleView;
import com.squareup.picasso.Picasso;

/**
 * @author Administrator
 * @version
 */
public class ImageOneAdapter extends BaseComAdapter<String> {

	/** xml视图配置文件 */
	private int mRes = Config.DEFAULT_ERROR;
	/** 显示数量 */
	private int mMax = Config.DEFAULT_ERROR;

	private class ControlView {
		RippleView fl_item;
		ImageView iv_one;
	}

	public ImageOneAdapter(Context context, ArrayList<String> list) {
		super(list, context);
	}

	public ImageOneAdapter(Context context, List<String> list, int res) {
		super(list, context);
		this.mRes = res;
	}

	public ImageOneAdapter(Activity activity, List<String> list, int res,
			int max) {
		super(list, activity);
		this.mRes = res;
		this.mMax = max;
	}

	@Override
	public int getCount() {
		if (Config.DEFAULT_ERROR == mMax)
			return list.size();
		else if (list.size() < mMax)
			return list.size();
		else
			return mMax;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		if (list.isEmpty())
			return view;
		if (Config.DEFAULT_ERROR != mMax && list.size() >= mMax)
			return view;
		ControlView cv;
		if (null == view) {
			cv = new ControlView();
			if (Config.DEFAULT_ERROR == mRes)
				view = LayoutInflater.from(context).inflate(
						R.layout.view_one_iv, null);
			else
				view = LayoutInflater.from(context).inflate(mRes, null);
			cv.iv_one = (ImageView) view.findViewById(R.id.iv_one);
			cv.fl_item = (RippleView) view.findViewById(R.id.fl_item);
			view.setTag(cv);
		} else
			cv = (ControlView) view.getTag();
		final String str = list.get(position);
		if (CommentUtil.isPic(str)) {
			Picasso.with(context).load(str).resize(200, 200).centerCrop()
					.into(cv.iv_one);
		} else if (CommentUtil.isMovie(str)) {
			new MyTask(cv.iv_one).execute(str);
		} else if (CommentUtil.isAudio(str)) {
			Picasso.with(context).load(R.drawable.have_music).resize(100, 100)
					.centerCrop().into(cv.iv_one);
		} else {

		}
		cv.fl_item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if (CommentUtil.isPic(str)) {
					intent.setClass(context, ImagesShowActivity.class);
					intent.putStringArrayListExtra("mlist",
							(ArrayList<String>) list);
					intent.putExtra("pos", position);
				} else if (CommentUtil.isMovie(str)) {
					intent.setClass(context, PlayMovieActivity.class);
					intent.putExtra("url", str);
				}

				context.startActivity(intent);

			}
		});
		return view;
	}

	private class MyTask extends AsyncTask<String, Integer, Bitmap> {

		private ImageView img;

		public MyTask(ImageView img) {
			super();
			this.img = img;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			return ImageUtil.createVideoThumbnail(params[0], 80, 90);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			img.setImageBitmap(result);
		}
	}

}
