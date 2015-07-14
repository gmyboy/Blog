package com.gmy.blog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.gmy.blog.utils.UserNetUtil;

public class RegistActivity extends BaseActivity implements OnClickListener {
	private Button btn_back, btn_submit, btn_cancel;
	private EditText edt_username, edt_password, edt_email;
	private String back;

	private CheckBox ch_xiexi;// 是否注册协议
	private TextView txt_xieyi;// 注册协议内容
	private Intent intent;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				if (back.equals("1")) {
					showToast("用户名已存在");
				} else if (back.equals("0")) {
					showToast("注册成功,快去体验吧");
					intent.putExtra("resultname", edt_username.getText()
							.toString());
					intent.putExtra("resultpwd", edt_password.getText()
							.toString());
					setResult(1001, intent);
					finish();
				} else {
					showToast("注册失败");
				}
			}
		};
	};

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_regist);
	}

	@Override
	public void initView() {
		intent = new Intent();
		btn_back = (Button) this.findViewById(R.id.reg_btn_back);
		btn_submit = (Button) this.findViewById(R.id.reg_btn_finish);
		btn_cancel = (Button) this.findViewById(R.id.reg_btn_cancel);
		edt_username = (EditText) this.findViewById(R.id.reg_edt_username);
		edt_password = (EditText) this.findViewById(R.id.reg_edit_password);
		edt_email = (EditText) this.findViewById(R.id.reg_edit_mail);
		ch_xiexi = (CheckBox) this.findViewById(R.id.reg_cbox_agree);// 注册协议
		txt_xieyi = (TextView) this.findViewById(R.id.reg_text_reg_content);// 注册协议内容
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		btn_back.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		txt_xieyi.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reg_btn_back:
			finish();
			break;
		case R.id.reg_btn_cancel:
			finish();
			break;
		case R.id.reg_btn_finish:
			if (!ch_xiexi.isChecked()) {
				showToast("请先仔细阅读注册协议");
			} else if (edt_username.getText().toString().equals("")) {
				showToast("用户名不能为空");
			} else if (edt_password.getText().toString().length() < 5
					|| edt_password.getText().toString().length() > 16) {
				showToast("密码必须是5-16位");
			} else if (edt_email.getText().toString().equals("")) {
				showToast("留下邮箱,方便联系");
			} else {
				new Thread() {
					public void run() {
						back = UserNetUtil.register(edt_username.getText()
								.toString(), edt_password.getText().toString(),
								edt_email.getText().toString());
						handler.sendEmptyMessage(1);
					};
				}.start();
			}
			break;
		case R.id.reg_text_reg_content:
			// 弹出注册协议具体内容
			AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
			localBuilder.setTitle("注册协议");
			localBuilder.setIcon(android.R.drawable.ic_dialog_alert);
			localBuilder.setMessage(R.string.service_message_detail);
			localBuilder.setNegativeButton("确定", null);
			AlertDialog localAlertDialog = localBuilder.create();
			localAlertDialog.getWindow().setFlags(1024, 1024);
			localAlertDialog.show();
			break;

		default:
			break;
		}
	}

}
