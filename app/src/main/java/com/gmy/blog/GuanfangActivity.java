package com.gmy.blog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.gmy.blog.utils.Config;

public class GuanfangActivity extends BaseActivity {
	private WebView contentWebView = null;
	private ProgressBar progressBar;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_guanfang);
	}

	@Override
	public void initView() {
		contentWebView = (WebView) findViewById(R.id.webview);
		progressBar = (ProgressBar) findViewById(R.id.gf_progress);
		progressBar.setVisibility(View.VISIBLE);
		// 启用javascript
		contentWebView.getSettings().setJavaScriptEnabled(true);
		// 随便找了个带图片的网站
		// contentWebView.loadUrl("http://www.sina.com.cn/");
		contentWebView.loadUrl(Config.IP + "index2.html");
		// 添加js交互接口类，并起别名 imagelistner
		contentWebView.addJavascriptInterface(new scriptInterface(this),
				"imagelistner");
		contentWebView.setWebViewClient(new MyWebViewClient());
		contentWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				progressBar.setProgress(newProgress);
			}
			
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	// 注入js函数监听
	@android.webkit.JavascriptInterface
	private void addImageClickListner() {
		// 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，在还是执行的时候调用本地接口传递url过去

		contentWebView.loadUrl("javascript:(function(){"
				+ "var objs = document.getElementsByTagName(\"img\"); "
				+ "for(var i=0;i<objs.length;i++)  " + "{"
				+ "    objs[i].onclick=function()  " + "    {  "
				+ "        window.imagelistner.openImage(this.src);  "
				+ "    }  " + "}" + "})()");
	}

	// js通信接口
	class scriptInterface {

		private Context context;

		public scriptInterface(Context context) {
			this.context = context;
		}

		@android.webkit.JavascriptInterface
		public void openImage(String img) {
			System.out.println(img);
			//
			Intent intent = new Intent();
			intent.putExtra("image", img);
			intent.setClass(context, ShowWebImageActivity.class);
			context.startActivity(intent);
			System.out.println(img);
		}
	}

	// 监听
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {

			view.getSettings().setJavaScriptEnabled(true);

			super.onPageFinished(view, url);
			// html加载完成之后，添加监听图片的点击js函数
			addImageClickListner();
			progressBar.setVisibility(View.GONE);

		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			view.getSettings().setJavaScriptEnabled(true);

			super.onPageStarted(view, url, favicon);
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {

			super.onReceivedError(view, errorCode, description, failingUrl);

		}
	}
}
