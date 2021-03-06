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

package com.gmy.blog.canvas;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup.LayoutParams;

public interface UIElementHost {
    public void requestLayout();

    public void invalidate();
    public void invalidate(int left, int top, int right, int bottom);

    public int[] getDrawableState();

    public Context getContext();
    public Resources getResources();

    public void invalidateDrawable(Drawable who);
    public void scheduleDrawable(Drawable who, Runnable what, long when);
    public void unscheduleDrawable(Drawable who);
    public void unscheduleDrawable(Drawable who, Runnable what);
}
