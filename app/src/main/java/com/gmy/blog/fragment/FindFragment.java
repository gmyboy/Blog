package com.gmy.blog.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmy.blog.R;
import com.gmy.blog.adapter.FoundPagerAdapter;
import com.gmy.blog.utils.Config;
import com.gmy.blog.widget.AutoScrollViewPager;

public class FindFragment extends Fragment {
	private List<String> urls;
	private AutoScrollViewPager viewPager;
	private FoundPagerAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag_found, null);
		initView(view);
		urls = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			urls.add(Config.IP + "imgs/topic/1.jpg");
		}
		adapter = new FoundPagerAdapter(getActivity(), urls);
		viewPager.setAdapter(adapter);
		viewPager.startAutoScroll();
		viewPager.setInterval(4000);
		return view;
	}

	private void initView(View view) {
		viewPager = (AutoScrollViewPager) view
				.findViewById(R.id.found_viewpager);
	}

}
