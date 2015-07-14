package com.gmy.blog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class AboutActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout about_guanfang;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_about);
	}

	@Override
	public void initView() {
		about_guanfang = (RelativeLayout) this
				.findViewById(R.id.about_guanfang);
		
		about_guanfang.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.about_guanfang:
			intent = new Intent().setClass(mContext, GuanfangActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
