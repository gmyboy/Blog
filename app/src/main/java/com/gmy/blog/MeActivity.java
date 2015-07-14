package com.gmy.blog;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gmy.blog.adapter.TweetsAdapter;
import com.gmy.blog.entity.BlogBean;
import com.gmy.blog.utils.BlogNetUtil;
import com.gmy.blog.utils.CommentUtil;
import com.gmy.blog.utils.Config;
import com.gmy.blog.utils.ImageUtil;
import com.gmy.blog.widget.CircularImage;
import com.gmy.blog.widget.CustomShareBoard;
import com.gmy.blog.widget.ModelPopup;
import com.gmy.blog.widget.ModelPopup.OnDialogListener;
import com.gmy.blog.widget.ScrollListView;
import com.squareup.picasso.Picasso;

@SuppressLint({ "NewApi", "ResourceAsColor" })
public class MeActivity extends BaseActivity implements OnClickListener,
		OnDialogListener, OnItemClickListener {
	/***
	 * 使用照相机拍照获取图片
	 */
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	/***
	 * 使用相册中的图片
	 */
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	RelativeLayout viewPrice1;
	RelativeLayout viewPrice2;
	private LinearLayout tab1, tab2, tab3, tab4;
	private TextView txt1, txt2, txt3, txt4;
	private ImageView img_tab_now;
	private TextView[] txts;
	private LinearLayout tab1_l, tab2_l, tab3_l, tab4_l;
	private TextView txt1_l, txt2_l, txt3_l, txt4_l;
	private ImageView img_tab_now_l;
	private TextView[] txts_l;
	int zero = 0;
	int one = 142; // 设置水平动画平移大小
	int two = one * 2;
	int three = one * 3;
	private int current = 1;
	RelativeLayout rel;
	ScrollView scrollview;
	private Button btn1, btn2, btn3;
	private Button editprofile;
	int heightOffset, viewOffset;
	private TextView txt, me_name;
	private CircularImage avator;

	private String url = Config.URL_MEBLOG;
	private ListView listView;
	private TweetsAdapter adapter;

	private String username;
	private ModelPopup popup;
//	private FrameLayout mainLayout;
	private List<BlogBean> list;
	private int page = 2;
	private int type = 0;// 用户判断是看自己，还是见别人的
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				adapter = new TweetsAdapter(MeActivity.this, list,username);
				listView.setAdapter(adapter);
			}

		};
	};

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_me);
	}

	@Override
	public void initView() {
		type = getIntent().getIntExtra("type", 0);
		editprofile = (Button) findViewById(R.id.me_btn_edit);
		if (type == Config.ME_MAIN) {
			username = getSharePreferences().getString(Config.USER_NAME, "");
		} else {
			username = getIntent().getStringExtra("username");
			editprofile.setVisibility(View.GONE);
		}

		txt = (TextView) this.findViewById(R.id.txt_txt);
		me_name = (TextView) this.findViewById(R.id.me_name);
		viewPrice1 = (RelativeLayout) findViewById(R.id.view_price1);
		viewPrice2 = (RelativeLayout) findViewById(R.id.view_price2);
		tab1 = (LinearLayout) this.findViewById(R.id.me_tab_lay1);
		tab2 = (LinearLayout) this.findViewById(R.id.me_tab_lay2);
		tab3 = (LinearLayout) this.findViewById(R.id.me_tab_lay3);
		tab4 = (LinearLayout) this.findViewById(R.id.me_tab_lay4);
		txt1 = (TextView) this.findViewById(R.id.me_tab_txt1);
		txt2 = (TextView) this.findViewById(R.id.me_tab_txt2);
		txt3 = (TextView) this.findViewById(R.id.me_tab_txt3);
		txt4 = (TextView) this.findViewById(R.id.me_tab_txt4);
		img_tab_now = (ImageView) this.findViewById(R.id.img_tab_now);
		txts = new TextView[] { txt1, txt2, txt3, txt4 };
		tab1_l = (LinearLayout) this.findViewById(R.id.me_tab_lay1_l);
		tab2_l = (LinearLayout) this.findViewById(R.id.me_tab_lay2_l);
		tab3_l = (LinearLayout) this.findViewById(R.id.me_tab_lay3_l);
		tab4_l = (LinearLayout) this.findViewById(R.id.me_tab_lay4_l);
		txt1_l = (TextView) this.findViewById(R.id.me_tab_txt1_l);
		txt2_l = (TextView) this.findViewById(R.id.me_tab_txt2_l);
		txt3_l = (TextView) this.findViewById(R.id.me_tab_txt3_l);
		txt4_l = (TextView) this.findViewById(R.id.me_tab_txt4_l);
		img_tab_now_l = (ImageView) this.findViewById(R.id.img_tab_now_l);
		txts = new TextView[] { txt1, txt2, txt3, txt4 };
		txts_l = new TextView[] { txt1_l, txt2_l, txt3_l, txt4_l };
		rel = (RelativeLayout) this.findViewById(R.id.rel);
		scrollview = (ScrollView) findViewById(R.id.scrollview);
		btn1 = (Button) this.findViewById(R.id.set_back);// 返回
		btn2 = (Button) this.findViewById(R.id.set_search);// 查找
		btn3 = (Button) this.findViewById(R.id.set_more);// 更多
		avator = (CircularImage) this.findViewById(R.id.avator);// 头像
		listView = (ScrollListView) this.findViewById(R.id.blogme_article);
		listView.setSelector(R.color.all_transparent);
		popup = new ModelPopup(mContext, this);
//		mainLayout = (FrameLayout) this.findViewById(R.id.me_main_layout);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		avator.setOnClickListener(this);
		listView.setOnItemClickListener(this);
		tab1.setOnClickListener(this);
		tab2.setOnClickListener(this);
		tab3.setOnClickListener(this);
		tab4.setOnClickListener(this);
		tab1_l.setOnClickListener(this);
		tab2_l.setOnClickListener(this);
		tab3_l.setOnClickListener(this);
		tab4_l.setOnClickListener(this);
		editprofile.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		me_name.setText(username);
		txt.setText(username);
		Picasso.with(mContext)
				.load(Config.IP + "avators/" + URLEncoder.encode(username)
						+ "/0.jpg").resize(100, 100).centerCrop()
				.error(R.drawable.userinfo_shadow_round).into(avator);

		viewPrice1.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					public void onGlobalLayout() {
						// 获取顶部图片的高度
						heightOffset = viewPrice1.getTop();
						viewPrice1.getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
					}
				});
		rel.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						viewOffset = rel.getBottom();
						rel.getViewTreeObserver().removeOnGlobalLayoutListener(
								this);
					}
				});
		scrollview.getViewTreeObserver().addOnScrollChangedListener(
				new OnScrollChangedListener() {
					public void onScrollChanged() {
						int y = scrollview.getScrollY();
						System.out.println("x" + y);
						// 监听scrollview的滑动距离 如果小于heightOffset 不显示隐藏的相同布局
						// 否则显示相同的顶部布局
						// int px = (int)(73 *
						// MainActivity2.this.getResources().getDisplayMetrics().density+0.5f);
						if (y >= heightOffset - viewOffset) {
							viewPrice2.setVisibility(View.VISIBLE);
							txt.setVisibility(View.VISIBLE);
							rel.setBackgroundResource(R.color.white);
							btn1.setBackgroundResource(R.drawable.btn_back2);
							btn2.setBackgroundResource(R.drawable.btn_search2);
							btn3.setBackgroundResource(R.drawable.btn_more2);
						} else {
							viewPrice2.setVisibility(View.GONE);
							txt.setVisibility(View.INVISIBLE);
							rel.setBackgroundResource(R.color.transparent);
							btn1.setBackgroundResource(R.drawable.btn_back);
							btn2.setBackgroundResource(R.drawable.btn_search);
							btn3.setBackgroundResource(R.drawable.btn_more);
						}
					}
				});
		if (CommentUtil.isNetworkAvailable(mContext)) {
			new Thread() {
				public void run() {
					list = BlogNetUtil.getBlogs(url, username, "1");
					handler.sendEmptyMessage(1);
				};
			}.start();
		}
	}

	/**
	 * 调用postShare分享。跳转至分享编辑页，然后再分享。</br> [注意]<li>
	 * 对于新浪，豆瓣，人人，腾讯微博跳转到分享编辑页，其他平台直接跳转到对应的客户端
	 */
	private void postShare() {
		CustomShareBoard shareBoard = new CustomShareBoard(mContext,
				"我叫古明勇0，欢迎大家关注我，我可是要成为大V的人", new ArrayList<String>());
		shareBoard.showAtLocation(MeActivity.this.getWindow().getDecorView(),
				Gravity.BOTTOM, 0, 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.set_back:
			finish();
			break;
		case R.id.set_more:
			postShare();
			break;
		case R.id.avator:
			if (type == Config.ME_MAIN) {
				popup.showAtLocation(MeActivity.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
			}

			break;
		case R.id.me_tab_lay1:
			changeTab(1);
			break;
		case R.id.me_tab_lay2:
			changeTab(2);
			break;
		case R.id.me_tab_lay3:
			changeTab(3);
			break;
		case R.id.me_tab_lay4:
			changeTab(4);
			break;
		case R.id.me_tab_lay1_l:
			changeTab(1);
			break;
		case R.id.me_tab_lay2_l:
			changeTab(2);
			break;
		case R.id.me_tab_lay3_l:
			changeTab(3);
			break;
		case R.id.me_tab_lay4_l:
			changeTab(4);
			break;
		case R.id.me_btn_edit:
			Intent intent=new Intent();
			intent.setClass(mContext, ProfileEditActivity.class);
			startActivity(intent);
		break;
		default:
			break;
		}
	}

	private void changeTab(int i) {
		Animation animation = null;
		switch (i) {
		case 1:
			if (current == 2) {
				txt2.setTextColor(Color.BLACK);
				txt2_l.setTextColor(Color.BLACK);
				animation = new TranslateAnimation(one, 0, 0, 0);
			} else if (current == 3) {
				txt3.setTextColor(Color.BLACK);
				txt3_l.setTextColor(Color.BLACK);
				animation = new TranslateAnimation(two, 0, 0, 0);
			} else if (current == 4) {
				txt4.setTextColor(Color.BLACK);
				txt4_l.setTextColor(Color.BLACK);
				animation = new TranslateAnimation(three, 0, 0, 0);
			} else {
				animation = new TranslateAnimation(zero, zero, 0, 0);
			}

			break;
		case 2:
			if (current == 1) {
				animation = new TranslateAnimation(zero, one, 0, 0);
				txt1.setTextColor(Color.BLACK);
				txt1_l.setTextColor(Color.BLACK);
			} else if (current == 3) {
				txt3.setTextColor(Color.BLACK);
				txt3_l.setTextColor(Color.BLACK);
				animation = new TranslateAnimation(two, one, 0, 0);
			} else if (current == 4) {
				txt4.setTextColor(Color.BLACK);
				txt4_l.setTextColor(Color.BLACK);
				animation = new TranslateAnimation(three, one, 0, 0);
			} else {
				animation = new TranslateAnimation(one, one, 0, 0);
			}
			break;
		case 3:
			if (current == 1) {
				txt1.setTextColor(Color.BLACK);
				txt1_l.setTextColor(Color.BLACK);
				animation = new TranslateAnimation(zero, two, 0, 0);
			} else if (current == 2) {
				txt2.setTextColor(Color.BLACK);
				txt2_l.setTextColor(Color.BLACK);
				animation = new TranslateAnimation(one, two, 0, 0);
			} else if (current == 4) {
				txt4.setTextColor(Color.BLACK);
				txt4_l.setTextColor(Color.BLACK);
				animation = new TranslateAnimation(three, two, 0, 0);
			} else {
				animation = new TranslateAnimation(two, two, 0, 0);
			}
			break;
		case 4:
			if (current == 1) {
				txt1.setTextColor(Color.BLACK);
				txt1_l.setTextColor(Color.BLACK);
				animation = new TranslateAnimation(zero, three, 0, 0);
			} else if (current == 2) {
				txt2.setTextColor(Color.BLACK);
				txt2_l.setTextColor(Color.BLACK);
				animation = new TranslateAnimation(one, three, 0, 0);
			} else if (current == 3) {
				txt3.setTextColor(Color.BLACK);
				txt3_l.setTextColor(Color.BLACK);
				animation = new TranslateAnimation(two, three, 0, 0);
			} else {
				animation = new TranslateAnimation(three, three, 0, 0);
			}
			break;
		default:
			break;
		}
		txts[i - 1].setTextColor(getResources().getColor(
				R.color.frag_me_title_txt));
		txts_l[i - 1].setTextColor(getResources().getColor(
				R.color.frag_me_title_txt));
		current = i;
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(150);
		img_tab_now.startAnimation(animation);
		img_tab_now_l.startAnimation(animation);
	}

	@Override
	public void onChoosePhoto() {
		// 从相册中取图片
		Intent choosePictureIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(choosePictureIntent, SELECT_PIC_BY_PICK_PHOTO);

	}

	@Override
	public void onTakePhoto() {
		Intent intent = new Intent(mContext, CameraActivity.class);
		startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);

	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {

			String picPath = "";
			switch (requestCode) {
			case SELECT_PIC_BY_TACK_PHOTO:
				// 选择自拍结果
				picPath = intent.getStringExtra("path");

				break;
			case SELECT_PIC_BY_PICK_PHOTO:
				// 选择图库图片结果
				picPath = ImageUtil.getPicPathFromUri(intent.getData(),
						MeActivity.this);

				break;
			}
			Picasso.with(mContext).load(new File(picPath)).into(avator);

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		intent.setClass(mContext, BlogDetailActvity.class);
		intent.putExtra("blog_id",
				String.valueOf(list.get(position).getBlog().getId()));
		startActivity(intent);

	}

}
