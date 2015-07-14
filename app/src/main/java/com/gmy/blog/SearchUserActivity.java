package com.gmy.blog;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gmy.blog.adapter.AddUserAdapter;
import com.gmy.blog.entity.UserBean;
import com.gmy.blog.utils.Config;
import com.gmy.blog.utils.UserNetUtil;

public class SearchUserActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	private String url = Config.URL_SEARCHUSERBEAN;
	private Button btn_back, btn_search;
	private ListView listView;
	private List<UserBean> list, fans, attentions;

	private AddUserAdapter adapter;
	private EditText edt_content;
	private TextView title;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				listView.setVisibility(View.VISIBLE);
				adapter = new AddUserAdapter(mContext, list, attentions, fans,
						getSharePreferences().getString(Config.USER_NAME, ""));
				listView.setAdapter(adapter);
			}

		};
	};

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_adduser);
	}

	@Override
	public void initView() {
		btn_back = (Button) this.findViewById(R.id.se_btn_back);
		btn_search = (Button) this.findViewById(R.id.se_search_person);
		listView = (ListView) this.findViewById(R.id.se_blog);
		title = (TextView) this.findViewById(R.id.se_text_toptitle);
		edt_content = (EditText) this.findViewById(R.id.se_list_header_edt);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		title.setText("搜索用户");
		btn_back.setOnClickListener(this);
		btn_search.setOnClickListener(this);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.se_btn_back:
			finish();
			break;
		case R.id.se_search_person:
			if (edt_content.getText().toString().equals("")) {
				showToast("查询内容不能为空");
			} else {
				new Thread() {
					public void run() {
						list = UserNetUtil.getsearchUserBean(url, edt_content
								.getText().toString());// 获取我输入的用户
						attentions = UserNetUtil
								.getAttentionUser(
										Config.URL_GETATTENTIONUSER,
										getSharePreferences().getString(
												"username", ""));// 获取我关注的人
						fans = UserNetUtil.getAttentionUser(
								Config.URL_GETFANUSER, getSharePreferences()
										.getString("username", ""));// 获取我的粉丝
						handler.sendEmptyMessage(1);
					};
				}.start();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

}
