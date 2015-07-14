package com.gmy.blog;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gmy.blog.adapter.CommentAdapter;
import com.gmy.blog.entity.Comment;
import com.gmy.blog.utils.BlogNetUtil;
import com.gmy.blog.utils.Config;

/**
 * 微博评论
 * 
 * @author GMY
 *
 */
public class CommentActivity extends BaseActivity implements OnClickListener {
	private Intent intent;
	private List<Comment> comments;
	private CommentAdapter adapter;
	private Button btn_back, btn_send;
	private ListView listView;
	private TextView noComment;
	private TextView hintText;
	private EditText edt_message;
	private LinearLayout bottom_yes, bottom_no;

	private boolean isshow = false;
	private int id;

	private String msgsend;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				if (msgsend.equals("0")) {
					showToast("评论成功!!!");
				} else {
					showToast("评论失败!!!");
				}
			}
		};
	};

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_comment);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		intent = getIntent();
		comments = (List<Comment>) intent.getSerializableExtra("COMMENTS");
		id = intent.getIntExtra("ID", 0);
		btn_back = (Button) this.findViewById(R.id.comm_btn_back);// 返回按钮
		btn_send = (Button) this.findViewById(R.id.comm_btn_send);// 发送按钮
		listView = (ListView) this.findViewById(R.id.comm_list_view);// 显示评论列表的listview
		noComment = (TextView) this.findViewById(R.id.comm_txtPageNoData);// 没有评论时显示的textview
		hintText = (TextView) this.findViewById(R.id.comm_text_hint);// 编辑框提示内容
		edt_message = (EditText) this.findViewById(R.id.comm_edit_message);// 编辑框内容
		bottom_yes = (LinearLayout) this
				.findViewById(R.id.comm_layout_bottom_yes);// 编辑框
		bottom_no = (LinearLayout) this
				.findViewById(R.id.comm_layout_bottom_no);// 编辑框
		btn_back.setOnClickListener(this);
		bottom_no.setOnClickListener(this);
		hintText.setOnClickListener(this);
		btn_send.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (comments == null || comments.size() == 0) {
			noComment.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		} else {
			adapter = new CommentAdapter(mContext, comments);
			listView.setAdapter(adapter);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comm_btn_back:
			finish();
			break;
		case R.id.comm_text_hint:
			SetBottonFocuse(true);
			break;
		case R.id.comm_list_view:
			SetBottonFocuse(false);
			break;
		case R.id.comm_btn_send:
			if (edt_message.getText().toString().equals("")
					|| edt_message.getText() == null) {
				showToast("发送内容不能为空!!!");
			} else {
				new Thread() {
					@Override
					public void run() {
						msgsend = BlogNetUtil.commendBlogs(
								getSharePreferences().getString(
										Config.USER_NAME, ""), id, edt_message
										.getText().toString());
						handler.sendEmptyMessage(1);
					}
				}.start();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 设置编辑框的显示
	 * 
	 * @param paramBoolean
	 */
	private void SetBottonFocuse(boolean paramBoolean) {
		isshow = paramBoolean;
		if (paramBoolean) {
			edt_message.requestFocus();
			((InputMethodManager) edt_message.getContext().getSystemService(
					"input_method")).toggleSoftInput(0, 2);
			bottom_no.setVisibility(View.GONE);
			bottom_yes.setVisibility(View.VISIBLE);
		} else {
			edt_message.clearFocus();
			((InputMethodManager) getSystemService("input_method"))
					.hideSoftInputFromWindow(edt_message.getWindowToken(), 0);
			bottom_no.setVisibility(View.VISIBLE);
			bottom_yes.setVisibility(View.GONE);
		}
	}

}
