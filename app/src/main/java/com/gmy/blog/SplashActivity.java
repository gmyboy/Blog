package com.gmy.blog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.gmy.blog.utils.Config;

/**
 * 应该添加从网络上获取splash图片
 * 
 * @author GMY
 *
 */
public class SplashActivity extends BaseActivity {
	private String name;
	@SuppressWarnings("rawtypes")
	private Class mClass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent();
				intent.setClass(SplashActivity.this, mClass);
				startActivity(intent);
				SplashActivity.this.finish();
			}
		}, 2000);
	}

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_splash);

	}

	@Override
	public void initView() {
		name = getSharePreferences().getString(Config.USER_NAME, "");
		if (name.equals("") || name == null) {
			mClass = LoginActivity.class;
		} else {
			mClass = MainActivity.class;
		}

	}

}
