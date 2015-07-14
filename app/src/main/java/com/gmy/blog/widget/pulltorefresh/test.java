package com.gmy.blog.widget.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class test extends PullToRefreshBase<LinearLayout> {

	public test(Context context) {
		super(context);
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 */
	public test(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 * @param defStyle
	 *            defStyle
	 */
	public test(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected LinearLayout createRefreshableView(Context context,
			AttributeSet attrs) {
		LinearLayout layout = new LinearLayout(context);
		return layout;
	}

	@Override
	protected boolean isReadyForPullDown() {
		return mRefreshableView.getScrollY() == 0;
	}

	@Override
	protected boolean isReadyForPullUp() {
		// TODO Auto-generated method stub
		return false;
	}

}
