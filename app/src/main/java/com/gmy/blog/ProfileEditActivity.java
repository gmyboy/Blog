package com.gmy.blog;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gmy.blog.entity.User;
import com.gmy.blog.entity.UserBean;
import com.gmy.blog.utils.Config;
import com.gmy.blog.utils.UserNetUtil;

public class ProfileEditActivity extends BaseActivity implements
		OnClickListener {
	private String url = Config.URL_GETUSERINFO;
	private RelativeLayout lay_nick, lay_sex, lay_intro, lay_locat, lay_hobby,
			lay_birth, lay_pwd, lay_regisdate;
	private TextView tx_nick, tx_sex, tx_intro, tx_locat, tx_hobby, tx_birth,
			tx_send, tx_regisdate;
	private UserBean userBean;
	private User user;
	private Button btn_back;
	private String back;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				user = userBean.getUser();
				tx_nick.setText(user.getUsername());
				tx_sex.setText(user.getSex());
				tx_birth.setText(user.getBirthday());
				tx_hobby.setText(user.getHobby());
				tx_intro.setText(user.getIntro());
				tx_locat.setText(user.getLocation());
				tx_regisdate.setText(user.getRegist_date());
			}
			if (msg.what == 2) {
				if (back.equals("0")) {
					showToast("提交成功!!!");
				} else {
					showToast("提交失败!!!");
				}
			}
		};
	};

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_profile);
	}

	@Override
	public void initView() {
		lay_birth = (RelativeLayout) this.findViewById(R.id.rlbirthday);
		lay_hobby = (RelativeLayout) this.findViewById(R.id.rlhobby);
		lay_intro = (RelativeLayout) this.findViewById(R.id.rlIntro);
		lay_locat = (RelativeLayout) this.findViewById(R.id.rllocation);
		lay_nick = (RelativeLayout) this.findViewById(R.id.rlNick);
		lay_sex = (RelativeLayout) this.findViewById(R.id.rlSex);
		lay_pwd = (RelativeLayout) this.findViewById(R.id.rlpwd);
		lay_regisdate = (RelativeLayout) this.findViewById(R.id.rlregisdate);
		tx_birth = (TextView) this
				.findViewById(R.id.tvAccount_birthday_content);
		tx_hobby = (TextView) this.findViewById(R.id.tvAccount_hobby_content);
		tx_intro = (TextView) this
				.findViewById(R.id.tvAccount_introduce_content);
		tx_locat = (TextView) this.findViewById(R.id.tvlocation_content);
		tx_nick = (TextView) this.findViewById(R.id.tvNick_content);
		tx_sex = (TextView) this.findViewById(R.id.tvAccount_sex_content);
		btn_back = (Button) this.findViewById(R.id.pro_back);
		tx_send = (TextView) this.findViewById(R.id.pro_send);
		tx_regisdate = (TextView) this
				.findViewById(R.id.tvAccount_regisdate_content);
		btn_back.setOnClickListener(this);
		lay_birth.setOnClickListener(this);
		lay_hobby.setOnClickListener(this);
		lay_intro.setOnClickListener(this);
		lay_locat.setOnClickListener(this);
		lay_nick.setOnClickListener(this);
		lay_sex.setOnClickListener(this);
		lay_pwd.setOnClickListener(this);
		lay_regisdate.setOnClickListener(this);
		tx_send.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new Thread() {
			public void run() {
				userBean = UserNetUtil.getUserInfo(url, getSharePreferences()
						.getString(Config.USER_NAME, ""));
				handler.sendEmptyMessage(1);
			};
		}.start();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pro_back:
			finish();
			break;
		case R.id.rlbirthday:
			String birth = tx_birth.getText().toString().trim();
			if (birth.equals("")) {
				birth = "2014-11-11";
			}
			String[] str = birth.split("-");
			int day = Integer.parseInt(str[2]);
			int month = Integer.parseInt(str[1]);
			int year = Integer.parseInt(str[0]);
			DatePickerDialog dialog = new DatePickerDialog(mContext,
					new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							tx_birth.setText("   " + year + "-"
									+ (monthOfYear + 1) + "-" + dayOfMonth);
						}
					}, year, month - 1, day);
			dialog.show();
			break;
		case R.id.rlhobby:
			new MaterialDialog.Builder(this)
					.title(R.string.hobby)
					.inputType(
							InputType.TYPE_CLASS_TEXT
									| InputType.TYPE_TEXT_VARIATION_PERSON_NAME
									| InputType.TYPE_TEXT_FLAG_CAP_WORDS)
					.input(R.string.hobby_hint, 0, false,
							new MaterialDialog.InputCallback() {
								@Override
								public void onInput(MaterialDialog dialog,
										CharSequence input) {
									tx_hobby.setText(input);
								}
							}).show();
			break;
		case R.id.rlIntro:
			new MaterialDialog.Builder(this)
					.title(R.string.intro)
					.inputType(
							InputType.TYPE_CLASS_TEXT
									| InputType.TYPE_TEXT_VARIATION_PERSON_NAME
									| InputType.TYPE_TEXT_FLAG_CAP_WORDS)
					.backgroundColor(android.R.color.transparent)
					.input(R.string.intro_hint, 0, false,
							new MaterialDialog.InputCallback() {
								@Override
								public void onInput(MaterialDialog dialog,
										CharSequence input) {
									tx_intro.setText(input);
								}
							}).show();
			break;
		case R.id.rllocation:
			// SelectCityDialog cityDialog = null;
			// try {
			// cityDialog = new SelectCityDialog(context, tx_locat);
			// } catch (XmlPullParserException e) {
			// e.printStackTrace();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// cityDialog.show();
			break;
		case R.id.rlSex:
			new MaterialDialog.Builder(this).items(R.array.sex)
					.itemsCallback(new MaterialDialog.ListCallback() {
						@Override
						public void onSelection(MaterialDialog dialog,
								View view, int which, CharSequence text) {
							tx_sex.setText(text);
						}
					}).show();
			break;
		case R.id.rlpwd:
			// Intent intent = new Intent();
			// intent.setClass(context, ChangePwdActivity.class);
			// startActivity(intent);
			break;
		case R.id.rlNick:
			break;
		case R.id.pro_send:
			new Thread() {
				public void run() {
					back = UserNetUtil.perfectUserInfo(getSharePreferences()
							.getString(Config.USER_NAME, ""), tx_sex.getText()
							.toString().trim(), tx_locat.getText().toString()
							.trim(), tx_intro.getText().toString().trim(),
							tx_hobby.getText().toString().trim(), tx_birth
									.getText().toString().trim());
					handler.sendEmptyMessage(2);
				};
			}.start();
			break;
		default:
			break;
		}
	}
}
