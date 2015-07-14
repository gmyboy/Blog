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

package com.gmy.blog.adapter;

import java.util.EnumSet;

import com.gmy.blog.entity.Blog;
import com.gmy.blog.entity.BlogBean;
/**
 * adapter 工具类    只提供一个更新的方法
 * @author Administrator
 *
 */
public interface TweetPresenter {
	public enum UpdateFlags {
		NO_IMAGE_LOADING
	}

	public enum Action {
		REPLY, RETWEET, FAVOURITE
	}

	public void update(BlogBean tweet, EnumSet<UpdateFlags> flags);
}
