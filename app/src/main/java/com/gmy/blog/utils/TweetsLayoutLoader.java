/*
 * Copyright (C) 2014 Lucas Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gmy.blog.utils;

import org.lucasr.smoothie.SimpleItemLoader;

import android.content.Context;
import android.view.View;
import android.widget.Adapter;

import com.gmy.blog.application.BlogApplication;
import com.gmy.blog.cache.UIElementCache;
import com.gmy.blog.entity.BlogBean;
import com.gmy.blog.widget.TweetCompositeView;

public class TweetsLayoutLoader extends
		SimpleItemLoader<BlogBean, TweetCompositeView> {
	private final Context mContext;
	private final UIElementCache mElementCache;

	public TweetsLayoutLoader(Context context) {
		mContext = context;
		mElementCache = BlogApplication.getInstance(context).getElementCache();
	}

	@Override
	public BlogBean getItemParams(Adapter adapter, int position) {
		return (BlogBean) adapter.getItem(position);
	}

	@Override
	public TweetCompositeView loadItem(BlogBean tweet) {
		return null;
	}

	@Override
	public TweetCompositeView loadItemFromMemory(BlogBean tweet) {
		return mElementCache.get((long) tweet.getBlog().getId());
	}

	@Override
	public void displayItem(View itemView, TweetCompositeView result,
			boolean fromMemory) {
		// Do nothing as we're only using this loader to pre-measure/layout
		// TweetElements that are off screen.
	}
}
