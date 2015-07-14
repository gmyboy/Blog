package com.gmy.blog;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmy.blog.adapter.EmoViewPagerAdapter;
import com.gmy.blog.adapter.EmoteAdapter;
import com.gmy.blog.adapter.PublishGridAdapter;
import com.gmy.blog.entity.Attachment;
import com.gmy.blog.entity.FaceText;
import com.gmy.blog.utils.BlogNetUtil;
import com.gmy.blog.utils.Config;
import com.gmy.blog.utils.FaceTextUtils;
import com.gmy.blog.utils.ImageUtil;
import com.gmy.blog.widget.EmoticonsEditText;
import com.gmy.blog.widget.ScrollGridView;

/**
 * 用来提供其他应用分享的界面
 * 
 * @author GMY
 *
 */
public class SharePanelActivity extends BaseActivity implements
		OnClickListener, TextWatcher, OnItemClickListener {

	/***
	 * 使用照相机拍照获取图片
	 */
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	/***
	 * 使用相册中的图片
	 */
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	/** 获取地理位置 */
	public static final int SELECT_LOCATION = 3;
	private TextView txt_send;
	private EmoticonsEditText edt_content;// 自定义，可以解析表情字体为表情图片
	private TextView tv_count, tv_location;// 计算文字数量
	private Button btn_back;

	private Intent intent;
	private String username;// 用户名
	private String location = "";// 地理位置
	private String back;
	private ViewPager pager_emo;// 表情页面
	private ImageView picture, emotion, text, camera;// 底部洗个按钮
	private ScrollGridView pic_grid;
	private PublishGridAdapter adapter;
	private List<Attachment> files;// 存放所有要上传文件
	private LinearLayout layout_more, layout_emo;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				if (back.equals("0")) {
					showToast("发表成功!!");
					files.clear();
					adapter.notifyDataSetChanged();
					edt_content.setText("");
				} else {
					showToast("发表失败!!");
				}
			}
		};
	};

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_publish);

	}

	@Override
	public void initView() {

		intent = new Intent();

		username = getSharePreferences().getString(Config.USER_NAME, "");
		files = new ArrayList<Attachment>();
		txt_send = (TextView) this.findViewById(R.id.send_text_send);// 发送按钮
		edt_content = (EmoticonsEditText) this
				.findViewById(R.id.send_article_content);// 编辑内容
		tv_count = (TextView) this.findViewById(R.id.tv_count);
		tv_location = (TextView) this.findViewById(R.id.tv_location);
		btn_back = (Button) this.findViewById(R.id.send_btn_back);
		picture = (ImageView) this.findViewById(R.id.send_img01);// 图片
		emotion = (ImageView) this.findViewById(R.id.send_img02);// 表情
		text = (ImageView) this.findViewById(R.id.send_img03);// 文字
		camera = (ImageView) this.findViewById(R.id.send_img04);// 拍照
		pic_grid = (ScrollGridView) this.findViewById(R.id.picture_grid);
		adapter = new PublishGridAdapter(files, mContext);
		pic_grid.setAdapter(adapter);
		pic_grid.setOnItemClickListener(this);
		initEmoView();
		tv_location.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (username == null) {
			intent.setClass(mContext, LoginActivity.class);
			startActivity(intent);
		}
		btn_back.setOnClickListener(this);
		txt_send.setOnClickListener(this);
		txt_send.setClickable(false);
		picture.setOnClickListener(this);
		emotion.setOnClickListener(this);
		text.setOnClickListener(this);
		camera.setOnClickListener(this);
		edt_content.addTextChangedListener(this);
	}

	List<FaceText> emos;

	/**
	 * 初始化表情布局
	 */
	private void initEmoView() {
		layout_more = (LinearLayout) findViewById(R.id.layout_more);
		layout_emo = (LinearLayout) findViewById(R.id.layout_emo);
		pager_emo = (ViewPager) findViewById(R.id.pager_emo);
		emos = FaceTextUtils.faceTexts;

		List<View> views = new ArrayList<View>();
		for (int i = 0; i < 4; ++i) {// 2页
			views.add(getGridView(i));
		}
		pager_emo.setAdapter(new EmoViewPagerAdapter(views));
	}

	/**
	 * 获得表情viewpager每一页的gridview
	 * 
	 * @param i
	 *            页码
	 * @return
	 */
	private View getGridView(final int i) {
		View view = View.inflate(this, R.layout.include_emo_gridview, null);
		GridView gridview = (GridView) view.findViewById(R.id.gridview);
		List<FaceText> list = new ArrayList<FaceText>();
		if (i == 0) {
			list.addAll(emos.subList(0, 21));
		} else if (i == 1) {
			list.addAll(emos.subList(21, 42));
		} else if (i == 2) {
			list.addAll(emos.subList(42, 63));
		} else if (i == 3) {
			list.addAll(emos.subList(63, emos.size()));
		}
		final EmoteAdapter gridAdapter = new EmoteAdapter(mContext, list);
		gridview.setAdapter(gridAdapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				FaceText name = (FaceText) gridAdapter.getItem(position);
				// 此地应该做判断是否是表情字符在最后 Y: 删除表情 N: 正常的删除
				String key = name.text.toString();
				try {
					if (edt_content != null && !TextUtils.isEmpty(key)) {
						if (position == 20 || position == 7) {
							int start = edt_content.getSelectionStart();// 获取edittext的最后一个字符的位置
							if (edt_content.getText().charAt(start - 6) == '\\') {// \ue549
																					// 解决
																					// 表情+文字混排时删除问题
								CharSequence content = edt_content.getText()
										.delete(start - key.length(), start);
								edt_content.setText(content);
								// 定位光标位置
								CharSequence info = edt_content.getText();
								if (info instanceof Spannable) {
									Spannable spanText = (Spannable) info;
									Selection.setSelection(spanText, start
											- key.length());// 设置光标位置为表情之后
								}
							} else {
								CharSequence content = edt_content.getText()
										.delete(start - 1, start);
								edt_content.setText(content);
								// 定位光标位置
								CharSequence info = edt_content.getText();
								if (info instanceof Spannable) {
									Spannable spanText = (Spannable) info;
									Selection.setSelection(spanText, start - 1);// 设置光标位置为表情之后
								}

							}
						} else {

							int start = edt_content.getSelectionStart();// 获取edittext的最后一个字符的位置
							CharSequence content = edt_content.getText()
									.insert(start, key);
							edt_content.setText(content);
							// 定位光标位置
							CharSequence info = edt_content.getText();
							if (info instanceof Spannable) {
								Spannable spanText = (Spannable) info;
								Selection.setSelection(spanText,
										start + key.length());// 设置光标位置为表情之后
							}
						}
					}
				} catch (Exception e) {
				}
			}
		});
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send_btn_back:
			finish();
			break;
		case R.id.send_text_send:
			if (edt_content.getText().toString().equals("")) {
				showToast("发送内容不能为空");
			} else {
				new Thread() {
					public void run() {
						back = BlogNetUtil.sendBlogs(username, edt_content
								.getText().toString().trim(),
								android.os.Build.MODEL, location, files);
						handler.sendEmptyMessage(1);
					};
				}.start();
			}
			break;
		case R.id.send_img01:
			Intent choosePictureIntent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(choosePictureIntent,
					SELECT_PIC_BY_PICK_PHOTO);
			break;
		case R.id.send_img02:
			if (layout_more.getVisibility() == View.GONE) {
				showEditState(true);
			} else {
				layout_more.setVisibility(View.GONE);
			}
			break;
		case R.id.send_img03:
			// 如果软键盘关闭状态
			if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
				layout_more.setVisibility(View.GONE);
				hideSoftInputView();
			} else {
				showSoftInputView();
			}
			break;
		case R.id.send_img04:
			intent = new Intent(mContext, CameraActivity.class);
			startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
			break;
		case R.id.tv_location:
			intent = new Intent(mContext, LocationListActivity.class);
			startActivityForResult(intent, SELECT_LOCATION);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			pic_grid.setVisibility(View.VISIBLE);
			String picPath;
			Attachment attachment;
			switch (requestCode) {
			case SELECT_PIC_BY_TACK_PHOTO:
				// 选择自拍结果
				picPath = data.getStringExtra("path");
				attachment = new Attachment();
				attachment.setFile_path(picPath);
				attachment.setFlag(Config.FLAG_FILE);
				attachment.setFiletype(Config.FILE_IMG_TYPE);
				files.add(attachment);
				adapter.notifyDataSetChanged();
				break;
			case SELECT_PIC_BY_PICK_PHOTO:
				// 选择图库图片结果
				picPath = ImageUtil.getPicPathFromUri(data.getData(),
						SharePanelActivity.this);
				attachment = new Attachment();
				attachment.setFile_path(picPath);
				attachment.setFlag(Config.FLAG_FILE);
				attachment.setFiletype(Config.FILE_IMG_TYPE);
				files.add(attachment);
				adapter.notifyDataSetChanged();
				break;
			case SELECT_LOCATION:
				location = data.getStringExtra("location");
				if (!location.equals("")) {
					tv_location.setText(location);
				}
				break;
			}

		}
	}

	/**
	 * 改变软键盘的状态
	 * 
	 * @param isEmo
	 */
	private void showEditState(boolean isEmo) {

		edt_content.requestFocus();
		if (isEmo) {
			layout_more.setVisibility(View.VISIBLE);
			layout_more.setVisibility(View.VISIBLE);
			layout_emo.setVisibility(View.VISIBLE);
			hideSoftInputView();
		} else {
			layout_more.setVisibility(View.GONE);
			showSoftInputView();
		}
	}

	/**
	 * 隐藏软键盘
	 */
	public void hideSoftInputView() {
		InputMethodManager manager = ((InputMethodManager) this
				.getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 显示软键盘
	 */
	public void showSoftInputView() {
		if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.showSoftInput(edt_content, 0);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		if (s.length() >= 140) {
			showToast("最多为140个字");
			edt_content.setEnabled(false);
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.length() <= 0 || s.length() > 140) {
			txt_send.setTextColor(getResources().getColor(R.color.grey));
			txt_send.setClickable(false);
		} else {
			txt_send.setTextColor(Color.BLACK);
			txt_send.setClickable(true);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

		tv_count.setText(s.length() + "/140");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ArrayList<String> picurls = new ArrayList<String>();
		for (int i = 0; i < files.size(); i++) {
			picurls.add(files.get(i).getFile_path());
		}
		Intent intent = new Intent();
		intent.setClass(mContext, ImagesShowActivity.class);
		intent.putStringArrayListExtra("mlist", picurls);
		intent.putExtra("pos", position);
		startActivity(intent);
	}

}
