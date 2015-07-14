package com.gmy.blog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gmy.blog.utils.Config;
import com.gmy.blog.utils.UserNetUtil;

/**
 * 用户登录
 * 
 * @author GMY
 *
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private EditText edt_username;
	private EditText edt_password;
	private Button btn_submit;
	private ImageView iv_login_arrow;

	private String loginback;
	private TextView txt_regist, txt_findpwd;
	private View popview;
	private PopupWindow popup;
	private ListView listView;
	private RelativeLayout rl_login_a;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				if (loginback.equals("1")) {
					showToast("用户名不存在!!");
				} else if (loginback.equals("2")) {
					showToast("密码错误!!");
				} else if (loginback.equals("0")) {
					showToast("登录成功");

					getSharePreferencesEditor()
							.remove(Config.USER_NAME)
							.putString(Config.USER_NAME,
									edt_username.getText().toString().trim())
							.putString("list_username",
									edt_username.getText().toString().trim())
							.putString("list_pwd",
									edt_password.getText().toString().trim())
							.commit();
					Intent intent = new Intent();
					intent.setClass(mContext, MainActivity.class);
					startActivityForResult(intent, 1000);
					finish();
				} else {
					showToast("登录失败");
				}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			getWindow().addFlags(
					LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}

		initPopupWindow();

	}

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_login);
	}

	@Override
	public void initView() {

		rl_login_a = (RelativeLayout) this.findViewById(R.id.rl_login_a);
		iv_login_arrow = (ImageView) this.findViewById(R.id.iv_login_arrow);
		edt_username = (EditText) this.findViewById(R.id.et_login_name);
		edt_password = (EditText) this.findViewById(R.id.et_login_pwd);
		btn_submit = (Button) this.findViewById(R.id.b_login);
		txt_regist = (TextView) this.findViewById(R.id.tv_login_regist);
		txt_findpwd = (TextView) this.findViewById(R.id.tv_login_findpwd);
		iv_login_arrow.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		txt_regist.setOnClickListener(this);
		txt_findpwd.setOnClickListener(this);
	}

	/**
	 * 初始化popview
	 */
	private void initPopupWindow() {

		popview = LayoutInflater.from(mContext).inflate(
				R.layout.view_login_sign, null);
		listView = (ListView) popview.findViewById(R.id.lv_login_sign);

		popup = new PopupWindow(popview, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		// 使用系统动画
		popup.setAnimationStyle(R.style.anim_pop_f);
		// 以下三行作用是点击空白处popup会消失
		popup.setFocusable(true);
		popup.setOutsideTouchable(true);

	}

	/**
	 * 显示popview
	 */
	private void showPopupWindow() {
		if (!popup.isShowing()) {
			popup.showAsDropDown(rl_login_a);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.b_login:
			if (edt_username.getText().toString().equals("")) {
				showToast("用户名不能为空!!");
			} else if (edt_password.getText().toString().equals("")) {
				showToast("密码不能为空!!");
			} else {
				new Thread() {
					public void run() {
						loginback = UserNetUtil.Login(edt_username.getText()
								.toString(), edt_password.getText().toString());
						handler.sendEmptyMessage(1);
					};
				}.start();
			}
			break;
		case R.id.tv_login_regist:
			Intent intent = new Intent();
			intent.setClass(mContext, RegistActivity.class);
			startActivityForResult(intent, 1000);
			break;
		case R.id.tv_login_findpwd:
			Intent intent1 = new Intent();
			intent1.setClass(mContext, FindPwdActivity.class);
			startActivityForResult(intent1, 1000);
			break;
		case R.id.iv_login_arrow:
			showPopupWindow();
			break;
		default:
			break;
		}
	}

}
