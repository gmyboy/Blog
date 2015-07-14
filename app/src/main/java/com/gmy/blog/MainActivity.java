package com.gmy.blog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.gmy.blog.fragment.FindFragment;
import com.gmy.blog.fragment.FirstPageFragment;
import com.gmy.blog.fragment.MeFragment;
import com.gmy.blog.fragment.MessageFragment;
import com.gmy.blog.utils.StackOfActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author GMY
 *
 */
public class MainActivity extends FragmentActivity implements OnClickListener {
	private static final String TAG = "MainActivity";
	private String[] menuText = { "首页", "消息", "", "发现", "我", };
	@SuppressWarnings("rawtypes")
	private Class[] fragmentarray = { FirstPageFragment.class,
			MessageFragment.class, null, FindFragment.class, MeFragment.class };
	private int[] imgres = { R.drawable.main_tab_item01,
			R.drawable.main_tab_item02,
			R.drawable.tabbar_compose_background_icon_add,
			R.drawable.main_tab_item03, R.drawable.main_tab_item04 };
	private FragmentTabHost mTabHost;

	private View popview;
	private PopupWindow popup;

	private Button btn_wenzi, btn_xiangce, btn_paishe, btn_qiandao, btn_zan,
			btn_more;// popupwindows里面的按钮
	private Animation anim_rotate, anim_rotate_back;// 中间tab点击时动画
	private ImageView imageView;
	private long exitTime = 0;
	private SharedPreferences spf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(this);
	}

	private void initView() {

		spf = MainActivity.this.getSharedPreferences("share_user", 1);
		// 获取tabhost
		mTabHost = (FragmentTabHost) this.findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		for (int i = 0; i < 5; i++) {
			TabHost.TabSpec localTabSpec = mTabHost.newTabSpec(menuText[i])
					.setIndicator(getTabHost(i));
			mTabHost.addTab(localTabSpec, fragmentarray[i], null);
		}

		initPopupWindow();
		mTabHost.getTabWidget().getChildAt(2)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						imageView = (ImageView) v
								.findViewById(R.id.tab_main_third_img);
						if (!popup.isShowing()) {
							imageView.startAnimation(anim_rotate);
							showPopupWindow();
						} else if (popup.isShowing()) {
							imageView.startAnimation(anim_rotate_back);
							popup.dismiss();
						}
					}
				});
		btn_wenzi.setOnClickListener(this);
		btn_xiangce.setOnClickListener(this);
		btn_paishe.setOnClickListener(this);
		btn_qiandao.setOnClickListener(this);
		btn_zan.setOnClickListener(this);
		btn_more.setOnClickListener(this);

	}

	/**
	 * 获得tabhost内容
	 * 
	 * @param i
	 *            tab的位置
	 * @return
	 */
	private View getTabHost(int i) {
		if (i == 2) {
			View localView = LayoutInflater.from(MainActivity.this).inflate(
					R.layout.list_tab_third, null);
			ImageView imageView = (ImageView) localView
					.findViewById(R.id.tab_main_third_img);
			imageView.setBackgroundResource(imgres[i]);
			return localView;
		} else {
			View localView = LayoutInflater.from(MainActivity.this).inflate(
					R.layout.list_tab_main, null);
			TextView textView = (TextView) localView
					.findViewById(R.id.tab_main_text);
			ImageView imageView = (ImageView) localView
					.findViewById(R.id.tab_main_img);
			textView.setText(menuText[i]);
			imageView.setBackgroundResource(imgres[i]);
			return localView;
		}

	}

	/**
	 * 初始化popview
	 */
	private void initPopupWindow() {
		anim_rotate = AnimationUtils.loadAnimation(MainActivity.this,
				R.anim.tab_rotate);// 旋转的动画方法
		anim_rotate_back = AnimationUtils.loadAnimation(MainActivity.this,
				R.anim.tab_rotate_back);
		anim_rotate_back.setFillAfter(true);
		anim_rotate.setFillAfter(true);// 动画之后保持之后的样子
		// 获取屏幕宽度
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		popview = LayoutInflater.from(MainActivity.this).inflate(
				R.layout.view_popup, null);
		btn_wenzi = (Button) popview.findViewById(R.id.btn_center_pop1);
		btn_xiangce = (Button) popview.findViewById(R.id.btn_center_pop2);
		btn_paishe = (Button) popview.findViewById(R.id.btn_center_pop3);
		btn_qiandao = (Button) popview.findViewById(R.id.btn_center_pop4);
		btn_zan = (Button) popview.findViewById(R.id.btn_center_pop5);
		btn_more = (Button) popview.findViewById(R.id.btn_center_pop6);
		popup = new PopupWindow(popview, LayoutParams.MATCH_PARENT,
				metrics.heightPixels - mTabHost.getTabWidget().getHeight());
		// 使用系统动画
		popup.setAnimationStyle(R.style.anim_pop);
		// 以下三行作用是点击空白处popup会消失
		popup.setFocusable(true);
		popup.setOutsideTouchable(true);
		popup.update();
		popup.getContentView().setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				popup.setFocusable(false);
				imageView.startAnimation(anim_rotate_back);
				popup.dismiss();
				return true;
			}
		});
	}

	/**
	 * 显示popview
	 */
	private void showPopupWindow() {
		if (!popup.isShowing()) {
			popup.showAtLocation(popview, Gravity.CENTER_HORIZONTAL
					| Gravity.BOTTOM, 0, mTabHost.getTabWidget().getHeight());
		}
	}

	// /**
	// * menuitem点击事件
	// */
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// Intent intent;
	// switch (item.getItemId()) {
	// case R.id.menu_exit:
	// StackOfActivity.getInstance().exit();
	// break;
	// case R.id.menu_settings:
	// intent = new Intent();
	// intent.setClass(MainActivity.this, SettingActivity.class);
	// startActivity(intent);
	// break;
	// case R.id.menu_about:
	// intent = new Intent();
	// intent.setClass(MainActivity.this, AboutActivity.class);
	// startActivity(intent);
	// break;
	// default:
	// break;
	// }
	// return super.onOptionsItemSelected(item);
	// }

	/**
	 * 实现联系按两次推出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			StackOfActivity.getInstance().exit();
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.btn_center_pop1:
			intent.setClass(MainActivity.this, PublishActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_center_pop2:
			intent.setClass(MainActivity.this, PublishActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_center_pop3:
			intent.setClass(MainActivity.this, PublishActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_center_pop4:
			intent.setClass(MainActivity.this, PublishActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_center_pop5:
			intent.setClass(MainActivity.this, PublishActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_center_pop6:
			intent.setClass(MainActivity.this, PublishActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		if (popup.isShowing()) {
			imageView.startAnimation(anim_rotate_back);
			popup.dismiss();
		}
	}
}
