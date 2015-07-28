package com.gmy.blog.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.gmy.blog.utils.Config;
import com.gmy.blog.utils.NetworkUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/28.
 */
public class DataRunnable implements Runnable {
    /**
     * 响应值
     */
    private int mWhat = Config.WHAT_DEFAULT;
    /**
     * 网络连接路径
     */
    private String mUrl;
    /**
     * 数据
     */
    private Map<String, String> mMap;
    /**
     * 图片路径
     */
    private List<String> mList;
    /**
     * 主线程
     */
    private Handler mHandler;
    /**
     * 睡眠时间
     */
    private long mSleep = 0;
    /**
     * 传递数据
     */
    private Message mMessage;
    /**
     * Activity
     */
    private Context mContext;
    /**
     * 请求类型
     */
    private String mMethod = "post";

    public DataRunnable(Context context, String url, Handler handler) {
        this.mUrl = url;
        this.mHandler = handler;
        this.mContext = context;
        this.mMessage = mHandler.obtainMessage();
    }

    public DataRunnable(Context context, String url, Handler handler, int what) {
        this(context, url, handler);
        this.mWhat = what;
    }

    public DataRunnable(Context context, String url, Handler handler, Map<String, String> map, List<String> list) {
        this(context, url, handler, map);
        this.mList = list;
    }

    public DataRunnable(Context context, String url, Handler handler, Map<String, String> map) {
        this(context, url, handler);
        this.mMap = map;
    }

    public DataRunnable(Context context, String url, Handler handler, int what, Map<String, String> map) {
        this(context, url, handler, what);
        this.mMap = map;
    }

    public DataRunnable(Context context, String url, Handler handler, int what, Map<String, String> map, List<String> list) {
        this(context, url, handler, what, map);
        this.mList = list;
    }

    public DataRunnable(Context context, String url, Handler handler, int what, Map<String, String> map, long sleep) {
        this(context, url, handler, what, map);
        this.mSleep = sleep;
    }

    public DataRunnable(Context context, String url, Handler handler, long sleep) {
        this(context, url, handler);
        this.mSleep = sleep;
    }

    public DataRunnable(Context context, String s, Handler mHandler, int whatTwo, Map<String, String> map, String mMethod) {
        this(context, s, mHandler, whatTwo, map);
        this.mMethod = mMethod;
    }

    @Override
    public void run() {
        try {
            if (mSleep > 0)
                Thread.sleep(mSleep);
        } catch (Exception e) {
            Log.d("BUG", e.toString());
        }

        if (mMap.isEmpty())
            mMap = new HashMap<>();
        mMessage.what = mWhat;
        try {
            mMessage.obj = NetworkUtil.post(mUrl, mMap);
        } catch (IOException e) {
            mMessage.obj = e.getMessage();
        }
        mHandler.sendMessage(mMessage); // 推送给主UI线程
    }
}
