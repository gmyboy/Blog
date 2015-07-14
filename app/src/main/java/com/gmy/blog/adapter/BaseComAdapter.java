package com.gmy.blog.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author Administrator
 * @version
 * @param <E>
 */
public abstract class BaseComAdapter<T> extends BaseAdapter {
	public List<T> list;
	public Context context;

	public BaseComAdapter(Context context) {
		super();
		this.context = context;
	}

	public BaseComAdapter(List<T> list, Context context) {
		this(context);
		this.list = list;
	}

	/**
	 * 得到adapter的数量
	 */
	@Override
	public int getCount() {
		return list.size();
	}

	/**
	 * 得到对应position的对象
	 */
	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	/**
	 * 得到adapter视图的id
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * @param <E>
	 * @Title: add
	 * @Description: 添加对象进入adapter
	 * @param obj
	 *            Aug 23, 2014 4:47:18 PM
	 */
	public void add(T t) {
		if (t != null) {
			list.add(t);
		}
	}

	/**
	 * @Title: clear
	 * @Description: adapter清空 Aug 23, 2014 4:47:38 PM
	 */
	public void clear() {
		if (!list.isEmpty())
			list.clear();
	}

	/**
	 * @Title: refresh
	 * @Description: adapter重置
	 * @param list
	 *            Aug 23, 2014 4:47:48 PM
	 */
	public void refresh(List<?> list) {
		list = (List<T>) list;
	}

	/**
	 * 得到视图
	 */
	@Override
	public abstract View getView(int position, View convertView,
			ViewGroup parent);
}
