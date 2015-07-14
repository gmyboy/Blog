package com.gmy.blog.widget;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.util.HttpURLConnection;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.gmy.blog.R;
import com.gmy.blog.utils.Config;

/**
 * 自定义分享界面
 */
public class CustomShareBoard extends PopupWindow implements OnClickListener {

	private Context context;
	private Intent share;
	private String pack_weixin = "", pack_sina = "", pack_qq = "",
			pack_qzone = "";
	private String content = "";
	private ArrayList<String> picurls;
	InputStream inStream;

	public CustomShareBoard(Context context, String string,
			ArrayList<String> picurls) {
		super(context);
		this.context = context;
		this.content = string;
		this.picurls = picurls;
		initView(context);
	}

	@SuppressWarnings("deprecation")
	private void initView(Context context) {
		View rootView = LayoutInflater.from(context).inflate(
				R.layout.custom_board, null);
		ImageButton btn1 = (ImageButton) rootView.findViewById(R.id.wechat);
		ImageButton btn2 = (ImageButton) rootView.findViewById(R.id.sina);
		ImageButton btn3 = (ImageButton) rootView.findViewById(R.id.qq);
		ImageButton btn4 = (ImageButton) rootView.findViewById(R.id.qzone);
		share = new Intent(Intent.ACTION_SEND);
		share.setType("image/*");
		List<ResolveInfo> resInfo = context.getPackageManager()
				.queryIntentActivities(share, 0);
		if (!resInfo.isEmpty()) {
			for (ResolveInfo info : resInfo) {
				Log.i("gmyboy", info.activityInfo.name + "---"
						+ info.activityInfo.packageName + "\n");
				if (info.activityInfo.packageName.toLowerCase().contains(
						Config.PACKAGE_WEIXIN)
						|| info.activityInfo.name.toLowerCase().contains(
								Config.PACKAGE_WEIXIN)) {
					pack_weixin = info.activityInfo.packageName;
					btn1.setBackgroundResource(R.drawable.umeng_socialize_wechat);
					btn1.setClickable(true);
				}
				if (info.activityInfo.packageName.toLowerCase().contains(
						Config.PACKAGE_SINA)
						|| info.activityInfo.name.toLowerCase().contains(
								Config.PACKAGE_SINA)) {
					pack_sina = info.activityInfo.packageName;
					btn2.setBackgroundResource(R.drawable.umeng_socialize_sina_on);
					btn2.setClickable(true);
				}
				if (info.activityInfo.packageName.toLowerCase().contains(
						Config.PACKAGE_QQ)
						|| info.activityInfo.name.toLowerCase().contains(
								Config.PACKAGE_QQ)) {
					pack_qq = info.activityInfo.packageName;
					btn3.setBackgroundResource(R.drawable.umeng_socialize_qq_on);
					btn3.setClickable(true);
				}
				if (info.activityInfo.packageName.toLowerCase().contains(
						Config.PACKAGE_QZONE)
						|| info.activityInfo.name.toLowerCase().contains(
								Config.PACKAGE_QZONE)) {
					pack_qzone = info.activityInfo.packageName;
					btn4.setBackgroundResource(R.drawable.umeng_socialize_qzone_on);
					btn4.setClickable(true);
				}
			}
		}

		setContentView(rootView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setTouchable(true);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
	}

	/*
	 * case ID_QQWEIBO: initShareIntent("com.tencent.wblog"); break; case
	 * ID_WEIXIN: initShareIntent("com.tencent.mm"); break; case ID_EVERNOTE:
	 * initShareIntent("evernote"); break; case
	 * ID_SINAWEIBO:initShareIntent("com.sina.weibo"); break; case
	 * ID_RENREN:initShareIntent("renren"); break; case ID_QQ:
	 * initShareIntent("tencent.mobileqq");
	 */

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.wechat:
			performShare(Config.SHARE_WEIXIN);
			break;
		case R.id.sina:
			performShare(Config.SHARE_SINA);
			break;
		case R.id.qq:
			performShare(Config.SHARE_QQ);
			break;
		case R.id.qzone:
			performShare(Config.SHARE_QZONE);
			break;
		default:
			break;
		}
	}

	private void performShare(int platform) {
		String pack = "";
		switch (platform) {
		case Config.SHARE_WEIXIN:
			pack = pack_weixin;
			break;
		case Config.SHARE_SINA:
			pack = pack_sina;
			break;
		case Config.SHARE_QQ:
			pack = pack_qq;
			break;
		case Config.SHARE_QZONE:
			pack = pack_qzone;
			break;
		default:
			break;
		}
		share.putExtra(Intent.EXTRA_SUBJECT, "subject");
		share.putExtra(Intent.EXTRA_TEXT, content);
		share.setPackage(pack);
		File f = new File(Config.CAMREA_IMG_PATH, "IMG_20150418151548.jpg");
		if (f != null && f.exists() && f.isFile()) {
			share.setType("image/*");
			share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
		}
		context.startActivity(Intent.createChooser(share, "Select"));

	}

}
