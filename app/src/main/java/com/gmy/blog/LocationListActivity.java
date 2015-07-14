package com.gmy.blog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.gmy.blog.widget.pulltorefresh.PullToRefreshScrollView;

public class LocationListActivity extends BaseActivity {
	private LocationClient mLocationClient;
	private LocationMode tempMode = LocationMode.Device_Sensors;
	private String tempcoor = "bd09ll";
	private PullToRefreshScrollView pullView;
	private TextView mLocationResult;
	private ScrollView scrollView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initLocation();
		mLocationClient.start();// 开始定位
		
	}

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_location);

	}

	@Override
	public void initView() {
		pullView = (PullToRefreshScrollView) this.findViewById(R.id.lo_pullview);
		mLocationResult=new TextView(mContext);
		scrollView =pullView.getRefreshableView();
		scrollView.addView(mLocationResult);
		mLocationClient = new LocationClient(this.getApplicationContext());
		mLocationClient.registerLocationListener(new MyLocationListener());
	}

	@Override
	protected void onStop() {
		mLocationClient.stop();
		super.onStop();
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// 设置定位模式
		option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(1000);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(final BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			mLocationResult.setText(sb.toString());
			mLocationResult.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=getIntent();
					intent.putExtra("location", location.getAddrStr());
					setResult(Activity.RESULT_OK, intent);
					finish();
				}
			});
			Log.i("BaiduLocationApiDem", sb.toString());
		}

	}

}
