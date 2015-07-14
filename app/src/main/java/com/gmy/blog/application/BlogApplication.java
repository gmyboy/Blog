package com.gmy.blog.application;

import android.app.Application;
import android.content.Context;

import com.gmy.blog.cache.UIElementCache;

public class BlogApplication extends Application {
	private UIElementCache mElementCache;

	@Override
	public void onCreate() {
		super.onCreate();
		mElementCache = new UIElementCache();
	}

	public UIElementCache getElementCache() {
		return mElementCache;
	}

	public static BlogApplication getInstance(Context context) {
		return (BlogApplication) context.getApplicationContext();
	}
}
