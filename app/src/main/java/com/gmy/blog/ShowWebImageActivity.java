package com.gmy.blog;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import com.gmy.blog.widget.webview.ZoomableImageView;

public class ShowWebImageActivity extends BaseActivity {
	private TextView imageTextView = null;
	private String imagePath = null;
	private ZoomableImageView imageView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public static Drawable loadImageFromUrl(String url) throws IOException {

		URL m = new URL(url);
		InputStream i = (InputStream) m.getContent();
		Drawable d = Drawable.createFromStream(i, "src");
		return d;
	}

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_showwebimage);

	}

	@Override
	public void initView() {
		this.imagePath = getIntent().getStringExtra("image");

		this.imageTextView = (TextView) findViewById(R.id.show_webimage_imagepath_textview);
		imageTextView.setText(this.imagePath);
		imageView = (ZoomableImageView) findViewById(R.id.show_webimage_imageview);

		try {
			imageView.setImageBitmap(((BitmapDrawable) ShowWebImageActivity
					.loadImageFromUrl(this.imagePath)).getBitmap());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
