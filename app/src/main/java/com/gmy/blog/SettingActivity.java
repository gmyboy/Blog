package com.gmy.blog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;

public class SettingActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout set_about, set_account, set_scale, set_music,
			set_feedback, set_light, set_cache;
	private Button set_btn_exit, set_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_setting);
	}

	@Override
	public void initView() {
		set_btn_exit = (Button) this.findViewById(R.id.set_btn_exit);
		set_back = (Button) this.findViewById(R.id.set_back);
		set_about = (RelativeLayout) this.findViewById(R.id.set_about);
		set_account = (RelativeLayout) this.findViewById(R.id.set_account);
		set_scale = (RelativeLayout) this.findViewById(R.id.set_scale);
		set_music = (RelativeLayout) this.findViewById(R.id.set_music);
		set_feedback = (RelativeLayout) this.findViewById(R.id.set_feedback);
		set_light = (RelativeLayout) this.findViewById(R.id.set_light);
		set_cache = (RelativeLayout) this.findViewById(R.id.set_cache);
		set_about.setOnClickListener(this);
		set_account.setOnClickListener(this);
		set_scale.setOnClickListener(this);
		set_music.setOnClickListener(this);
		set_feedback.setOnClickListener(this);
		set_light.setOnClickListener(this);
		set_cache.setOnClickListener(this);
		set_btn_exit.setOnClickListener(this);
		set_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.set_btn_exit:
			
			new MaterialDialog.Builder(this).content("您确定要退出微博吗？")
					.positiveText("确定").negativeText("取消")
					.callback(new MaterialDialog.ButtonCallback() {
						@Override
						public void onPositive(MaterialDialog dialog) {
							// TODO Auto-generated method stub
							super.onPositive(dialog);
							exit();
						}
					}).show();
			break;
		case R.id.set_back:
			finish();
			break;
		case R.id.set_about:
			intent = new Intent().setClass(mContext, AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.set_account:
			intent = new Intent().setClass(mContext, AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.set_scale:

			break;
		case R.id.set_music:

			break;
		case R.id.set_feedback:

			break;
		case R.id.set_light:

			break;
		case R.id.set_cache:
			new MaterialDialog.Builder(this).content("您确定要清除所有缓存吗？")
			.positiveText("确定").negativeText("取消")
			.callback(new MaterialDialog.ButtonCallback() {
				@Override
				public void onPositive(MaterialDialog dialog) {
					// TODO Auto-generated method stub
					super.onPositive(dialog);
					exit();
				}
			}).show();
			break;

		default:
			break;
		}
	}
}
