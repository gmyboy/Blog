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

package com.gmy.blog.cache;

import android.annotation.SuppressLint;
import android.util.LruCache;

import com.gmy.blog.widget.TweetCompositeView;

@SuppressLint("NewApi")
public class UIElementCache extends LruCache<Long, TweetCompositeView> {
	private static final int MAX_CACHED_ELEMENTS = 30;

	public UIElementCache() {
		super(MAX_CACHED_ELEMENTS);
	}
}
