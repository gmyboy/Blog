package com.gmy.blog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gmy.blog.thread.DataRunnable;
import com.gmy.blog.thread.TaskExecutor;
import com.gmy.blog.utils.Config;
import com.gmy.blog.utils.UserNetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 用户登录
 *
 * @author GMY
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.etLoginName)
    EditText etLoginName;
    @Bind(R.id.et_login_pwd)
    EditText etLoginPwd;
    @Bind(R.id.b_login)
    Button bLogin;
    @Bind(R.id.tv_login_findpwd)
    TextView tvLoginFindpwd;
    @Bind(R.id.tv_login_regist)
    TextView tvLoginRegist;

    private String loginback;

    @OnClick(R.id.b_login)
    void submit() {
        if (etLoginName.getText().toString().equals("")) {
            showToast("用户名不能为空!!");
        } else if (etLoginPwd.getText().toString().equals("")) {
            showToast("密码不能为空!!");
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("username", etLoginName.getText().toString().trim());
            map.put("password", etLoginPwd.getText().toString().trim());
            TaskExecutor.Execute(new DataRunnable(this, "/user/login", mHandler, Config.WHAT_ONE, map));
        }
    }

    @OnClick(R.id.tv_login_findpwd)
    void findPwd() {
        Intent intent1 = new Intent();
        intent1.setClass(mContext, FindPwdActivity.class);
        startActivityForResult(intent1, 1000);
    }

    @OnClick(R.id.tv_login_regist)
    void regist() {
        Intent intent = new Intent();
        intent.setClass(mContext, RegistActivity.class);
        startActivityForResult(intent, 1000);
    }

//    @SuppressLint("HandlerLeak")
//    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            if (msg.what == 1) {
//                if (loginback.equals("1")) {
//                    showToast("用户名不存在!!");
//                } else if (loginback.equals("2")) {
//                    showToast("密码错误!!");
//                } else if (loginback.equals("0")) {
//                    showToast("登录成功");
//
//                    getSharePreferencesEditor()
//                            .remove(Config.USER_NAME)
//                            .putString(Config.USER_NAME,
//                                    etLoginName.getText().toString().trim())
//                            .putString("list_username",
//                                    etLoginName.getText().toString().trim())
//                            .putString("list_pwd",
//                                    etLoginPwd.getText().toString().trim())
//                            .commit();
//                    Intent intent = new Intent();
//                    intent.setClass(mContext, MainActivity.class);
//                    startActivityForResult(intent, 1000);
//                    finish();
//                } else {
//                    showToast("登录失败");
//                }
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(
                    LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }


    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initView() {
        mHandler = new LoginHandler(this);
    }

    private class LoginHandler extends Handler {
        private WeakReference<Activity> mWeak;

        public LoginHandler(Activity activity) {
            mWeak = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Activity activity = mWeak.get();
            doAction(activity, msg.what, (String) msg.obj);
        }

        /**
         * @Title doAction
         * @Description 动作
         */
        private void doAction(Activity activity, int what, String json) {
            switch (what) {
                case Config.WHAT_ONE: // 验证用户名
                    Log.d("gmyboy", json);
                    if (!json.isEmpty()) {
                        try {
                            JSONObject jb = new JSONObject(json);
                            String status = jb.getString("status");
                            String msg = jb.getString("msg");
                            if (status.equals("0")) {
                                showToast("登录成功");
                                getSharePreferencesEditor()
                                        .remove(Config.USER_NAME)
                                        .putString(Config.USER_NAME,
                                                etLoginName.getText().toString().trim())
                                        .putString("list_username",
                                                etLoginName.getText().toString().trim())
                                        .putString("list_pwd",
                                                etLoginPwd.getText().toString().trim())
                                        .commit();
                                Intent intent = new Intent();
                                intent.setClass(mContext, MainActivity.class);
                                startActivityForResult(intent, 1000);
                                finish();
                            }else
                                showToast(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, e.getMessage());
                        }
                    }
                    break;
            }
        }
    }
}
