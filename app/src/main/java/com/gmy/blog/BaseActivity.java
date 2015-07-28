package com.gmy.blog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

import com.gmy.blog.utils.Config;
import com.gmy.blog.utils.StackOfActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * 所有activity的基类
 *
 * @author Administrator
 */
public abstract class BaseActivity extends Activity {

    private long exitTime = 0;
    private boolean isTwiceExit = false;
    public String TAG = this.getClass().getSimpleName();
    public Context mContext = this;
    public Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StackOfActivity.getInstance().addActivity(this);
        // bug分析
        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.updateOnlineConfig(this);
        setLayout();
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 加载布局
     */
    public abstract void setLayout();

    /**
     * find the widget
     */
    public abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    public SharedPreferences getSharePreferences() {
        return getSharedPreferences(Config.USER_SHAREDPREFERENCES, 1);
    }

    public SharedPreferences.Editor getSharePreferencesEditor() {
        return getSharePreferences().edit();
    }

    /**
     * 设置是否可以按两次退出
     *
     * @param isTwiceExit
     */
    public void setTwiceExit(boolean isTwiceExit) {
        this.isTwiceExit = isTwiceExit;
    }

    /**
     * 实现联系按两次推出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isTwiceExit) {
            twiceexit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void twiceexit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            exit();
        }
    }

    /**
     * 退出所有activity
     */
    public void exit() {
        StackOfActivity.getInstance().exit();
    }

    /**
     * 显示toast
     *
     * @param context
     * @param massage
     */
    public void showToast(String massage) {
        Toast.makeText(this, massage, Toast.LENGTH_SHORT).show();
    }
}
