package com.example.wll.ceshitablayout;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.example.wll.ceshitablayout.base.BaseActivity;
import com.example.wll.ceshitablayout.constant.Constants;
import com.example.wll.ceshitablayout.constant.UserMsg;
import com.example.wll.ceshitablayout.pojoBean.UserInfo;
import com.example.wll.ceshitablayout.myInterface.LoginSevser;
import com.example.wll.ceshitablayout.utils.PreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class LoginActivity extends BaseActivity {
    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.user_pwd)
    EditText userPwd;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.iv_user_login)
    ImageView tvUns;

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                String username = userName.getText().toString().trim();
                String userpwd = userPwd.getText().toString().trim();
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(userpwd)) {
                    login(username, userpwd);
                } else {
                    showToast("用户名或者密码不能为空!");
                }

                break;

            case R.id.tv_register:

                startActivity(RegistrationActivity.class);
                finish();
                break;

            case R.id.iv_user_login:
                startActivity(SmsLoginActivity.class);
                finish();
                break;
        }
    }

    /**
     * 登录界面
     */
    private void login(String name, final String pwd) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//新的配置
                .baseUrl(Constants.BaseUrl)
                .build();
        LoginSevser service = retrofit.create(LoginSevser.class);

        service.login(name, pwd)               //获取Observable对象
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(Schedulers.io())         //请求完成后在io线程中执行
                .doOnNext(new Action1<UserInfo>() {
                    @Override
                    public void call(UserInfo userInfo) {
//                        saveUserInfo(userInfo);//保存用户信息到本地
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(new Subscriber<UserInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //请求失败
                        LogUtils.i(e.toString());
                    }

                    @Override
                    public void onNext(UserInfo userInfo) {
                        //请求成功
                        if (userInfo != null) {
                            showToast(userInfo.getMsg());
                            if (userInfo.getMsg().equals("登录成功")) {
                                PreferencesUtils.putString(LoginActivity.this, UserMsg.UserName, userInfo.getData().getName());
                                PreferencesUtils.putString(LoginActivity.this, UserMsg.UserPassword, pwd);
                                PreferencesUtils.putString(LoginActivity.this, UserMsg.UserId, userInfo.getData().getId());
                                PreferencesUtils.putString(LoginActivity.this, UserMsg.UserEmail, userInfo.getData().getName());
                                startActivity(MainActivity.class);
                                finish();
                            }
                        }
                    }
                });


    }

    /**
     * 初始化
     *
     * @param parms
     */
    @Override
    public void initParms(Bundle parms) {
        setSteepStatusBar(false);

        if (parms != null) {
            String name = parms.getString("name");
            String pwd = parms.getString("pwd");
            if (!TextUtils.isEmpty(pwd)) {
                userPwd.setText(pwd + "");

            }
            if (!TextUtils.isEmpty(name)) {
                userName.setText(name + "");
                PreferencesUtils.putString(LoginActivity.this, UserMsg.UserName, name);
                PreferencesUtils.putString(LoginActivity.this, UserMsg.UserId, "");
            }

            String preName = PreferencesUtils.getString(LoginActivity.this, UserMsg.UserName);
            String prePwd = PreferencesUtils.getString(LoginActivity.this, UserMsg.UserPassword);
            if (TextUtils.isEmpty(preName) && TextUtils.isEmpty(prePwd)) {

            } else {
                userName.setText(preName);
                userPwd.setText(prePwd);
            }
        }
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this);
    }

    /**
     * 点击事件
     */
    @Override
    public void setListener() {
        tvLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvUns.setOnClickListener(this);
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
