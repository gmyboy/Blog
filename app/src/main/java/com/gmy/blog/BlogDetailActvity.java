package com.gmy.blog;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gmy.blog.adapter.CommentAdapter;
import com.gmy.blog.adapter.ImageOneAdapter;
import com.gmy.blog.entity.Blog;
import com.gmy.blog.entity.BlogBean;
import com.gmy.blog.entity.Comment;
import com.gmy.blog.utils.BlogNetUtil;
import com.gmy.blog.utils.Config;
import com.gmy.blog.utils.DateUtil;
import com.gmy.blog.widget.CircularImage;
import com.gmy.blog.widget.CustomShareBoard;
import com.gmy.blog.widget.EmoticonsTextView;
import com.gmy.blog.widget.ScrollGridView;
import com.squareup.picasso.Picasso;

@SuppressLint("NewApi")
public class BlogDetailActvity extends BaseActivity implements OnClickListener {
	// private final UMSocialService mController = UMServiceFactory
	// .getUMSocialService(Config.DESCRIPTOR);
	// private SHARE_MEDIA mPlatform = SHARE_MEDIA.SINA;
	private String url = Config.URL_GETBLOGBYID;

	RelativeLayout viewPrice2;
	private RelativeLayout de_layout_topbar;
	int heightOffset, viewOffset;
	private String blog_id = "";
	private BlogBean blogBean;
	private Blog blog;
	private ArrayList<String> picurls = new ArrayList<String>();
	private List<Comment> comments = new ArrayList<Comment>();
	private Button de_btn_back, btn_detail_share;
	private HeaderParam headerParam = new HeaderParam();

	/**
	 * 头部元素
	 * 
	 * @author Administrator
	 *
	 */
	private class HeaderParam {
		Button de_btn_collect;
		CircularImage de_userphoto;
		TextView de_name, de_name_from;
		EmoticonsTextView de_first_content;
		ScrollGridView de_gv_image;
		RelativeLayout viewPrice1;
	}

	private View headerView = null;
	private ListView listView;
	private TextView de_comm_txtPageNoData;
	private CommentAdapter commentAdapter;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				if (blogBean != null) {
					blog = blogBean.getBlog();
					Picasso.with(mContext)
							.load(Config.IP + "avators/"
									+ URLEncoder.encode(blog.getUser_name())
									+ "/0.jpg").into(headerParam.de_userphoto);

					headerParam.de_name.setText(blog.getUser_name());
					headerParam.de_name_from.setText(DateUtil.paselDate(blog
							.getBlog_date()) + "   " + blog.getComefrom());
					headerParam.de_first_content.setText("   "
							+ blog.getContent());
					if (blog.getPicture() != null
							&& !blog.getPicture().equals("")) {
						String[] t = blog.getPicture().split(";");
						// 拆分字符串
						for (String string : t) {
							picurls.add(Config.IP + "imgs/" + string);
						}
					}
					if (blog.getMovie() != null && !blog.getMovie().equals("")) {
						String[] t = blog.getMovie().split(";");
						// 拆分字符串
						for (String string : t) {
							picurls.add(Config.IP + "videos/" + string);
						}
					}
					if (blog.getMusic() != null && !blog.getMusic().equals("")) {
						String[] t = blog.getMusic().split(";");
						// 拆分字符串
						for (String string : t) {
							picurls.add(Config.IP + "audios/" + string);
						}
					}
					if (!picurls.isEmpty()) {
						headerParam.de_gv_image.setVisibility(View.VISIBLE);
						ImageOneAdapter adapter_image;
						if (1 == picurls.size()) {
							headerParam.de_gv_image.setNumColumns(1);
							adapter_image = new ImageOneAdapter(mContext,
									picurls, R.layout.big_view_one_iv_com);
						} else if (picurls.size() > 1 && picurls.size() <= 4) {
							headerParam.de_gv_image.setNumColumns(2);
							adapter_image = new ImageOneAdapter(mContext,
									picurls, R.layout.big_view_one_iv_com);
						} else if (picurls.size() > 4 && picurls.size() <= 6) {
							headerParam.de_gv_image.setNumColumns(2);
							adapter_image = new ImageOneAdapter(mContext,
									picurls, R.layout.big_view_one_iv_six);
						} else {
							headerParam.de_gv_image.setNumColumns(3);
							adapter_image = new ImageOneAdapter(mContext,
									picurls, R.layout.big_view_one_iv);
						}
						headerParam.de_gv_image.setAdapter(adapter_image);
						headerParam.de_gv_image
								.setSelector(android.R.color.transparent);
						headerParam.de_gv_image
								.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										Intent intent = new Intent();
										intent.setClass(mContext,
												ImagesShowActivity.class);
										intent.putStringArrayListExtra("mlist",
												picurls);
										intent.putExtra("pos", position);
										startActivity(intent);

									}
								});
					} else
						headerParam.de_gv_image.setVisibility(View.GONE);
					if (blogBean.getList() != null
							&& blogBean.getList().size() != 0) {
						comments = blogBean.getList();
						commentAdapter = new CommentAdapter(mContext, comments);
						listView.setAdapter(commentAdapter);
					} else {
						de_comm_txtPageNoData.setVisibility(View.VISIBLE);
						listView.setVisibility(View.GONE);
					}
				}
			}
		}
	};

	@Override
	public void initView() {
		headerView = getLayoutInflater().inflate(R.layout.view_detail_header,
				null);

		viewPrice2 = (RelativeLayout) findViewById(R.id.view_detail_l);
		de_layout_topbar = (RelativeLayout) findViewById(R.id.de_layout_topbar);
		de_btn_back = (Button) this.findViewById(R.id.de_btn_back);
		btn_detail_share = (Button) this.findViewById(R.id.btn_detail_share);
		headerParam.de_btn_collect = (Button) headerView
				.findViewById(R.id.de_btn_collect);
		headerParam.de_userphoto = (CircularImage) headerView
				.findViewById(R.id.de_userphoto);
		headerParam.de_name = (TextView) headerView.findViewById(R.id.de_name);
		headerParam.de_name_from = (TextView) headerView
				.findViewById(R.id.de_name_from);
		headerParam.de_first_content = (EmoticonsTextView) headerView
				.findViewById(R.id.de_first_content);
		headerParam.de_gv_image = (ScrollGridView) headerView
				.findViewById(R.id.de_gv_image);
		headerParam.viewPrice1 = (RelativeLayout) headerView
				.findViewById(R.id.view_detail);
		listView = (ListView) this.findViewById(R.id.de_comm_list_view);
		de_comm_txtPageNoData = (TextView) this
				.findViewById(R.id.de_comm_txtPageNoData);
		blog_id = getIntent().getStringExtra("blog_id");
		headerParam.viewPrice1.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					public void onGlobalLayout() {
						// 获取顶部图片的高度
						heightOffset = headerParam.viewPrice1.getTop();
						headerParam.viewPrice1.getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
					}
				});
		de_layout_topbar.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						viewOffset = de_layout_topbar.getBottom();
						de_layout_topbar.getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
					}
				});
		headerView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {

					}
				});
		listView.getViewTreeObserver().addOnScrollChangedListener(
				new OnScrollChangedListener() {
					public void onScrollChanged() {
						int y = listView.getScrollY();
						Log.i("gmyboy", "-------------" + y);
						// 监听scrollview的滑动距离 如果小于heightOffset 不显示隐藏的相同布局
						// 否则显示相同的顶部布局
						// int px = (int)(73 *
						//
						// MainActivity2.this.getResources().getDisplayMetrics().density+0.5f);
						if (y >= heightOffset - viewOffset) {
							viewPrice2.setVisibility(View.VISIBLE);
						} else {
							viewPrice2.setVisibility(View.GONE);
						}
					}
				});
		listView.addHeaderView(headerView);
		btn_detail_share.setOnClickListener(this);

	}

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_blogdetail);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (blog_id != null && !blog_id.equals("")) {

			new Thread() {
				@Override
				public void run() {
					super.run();
					blogBean = BlogNetUtil.getBlogByID(url, blog_id);
					handler.sendEmptyMessage(1);
				}
			}.start();
		}

	}

	/**
	 * 调用postShare分享。跳转至分享编辑页，然后再分享。</br> [注意]<li>
	 * 对于新浪，豆瓣，人人，腾讯微博跳转到分享编辑页，其他平台直接跳转到对应的客户端
	 */
	private void postShare() {
		CustomShareBoard shareBoard = new CustomShareBoard(
				BlogDetailActvity.this, headerParam.de_first_content.getText()
						.toString(), picurls);
		shareBoard.showAtLocation(BlogDetailActvity.this.getWindow()
				.getDecorView(), Gravity.BOTTOM, 0, 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_detail_share:
			postShare();
			break;

		default:
			break;
		}
	}

	/**
	 * 分享功能
	 * 
	 * @param context
	 *            上下文
	 * @param activityTitle
	 *            Activity的名字
	 * @param msgTitle
	 *            消息标题
	 * @param msgText
	 *            消息内容
	 * @param imgPath
	 *            图片路径，不分享图片则传null
	 */
	public void shareMsg(String activityTitle, String msgTitle, String msgText,
			String imgPath) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		if (imgPath == null || imgPath.equals("")) {
			intent.setType("text/plain"); // 纯文本
		} else {
			File f = new File(imgPath);
			if (f != null && f.exists() && f.isFile()) {
				intent.setType("image/jpg");
				Uri u = Uri.fromFile(f);
				intent.putExtra(Intent.EXTRA_STREAM, u);
			}
		}
		intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
		intent.putExtra(Intent.EXTRA_TEXT, msgText);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(Intent.createChooser(intent, activityTitle));
	}

}
