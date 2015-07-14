package com.gmy.blog;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.gmy.blog.adapter.ImagePagerAdapter;

public class ImagesShowActivity extends BaseActivity {
	private ViewPager viewPager;
	// private int[] res = { R.drawable.login_bg, R.drawable.login_bg,
	// R.drawable.login_bg, R.drawable.login_bg };
	private ImagePagerAdapter adapter;

	private List<String> res = new ArrayList<String>();
	private int position;
	private TextView txt;
	private Button back;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_imagesshow);

	}

	@Override
	public void initView() {
		viewPager = (ViewPager) this.findViewById(R.id.viewpage);
		txt = (TextView) findViewById(R.id.show_txt);
		back = (Button) findViewById(R.id.show_back);
		res = getIntent().getStringArrayListExtra("mlist");
		position = getIntent().getIntExtra("pos", 0);

		if (res != null && res.size() != 0) {
			adapter = new ImagePagerAdapter(mContext, res);
		}
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(position);
		txt.setText(position + 1 + "/" + res.size());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				txt.setText((arg0 + 1) + "/" + res.size());
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// if (arg1 > 0.5 && arg1 < 1 && arg0 == 0) {
				// finish();
				// }
				// if (arg0==res.size()-1&&arg1<0.5&&arg1>0) {
				// finish();
				// }

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

}
