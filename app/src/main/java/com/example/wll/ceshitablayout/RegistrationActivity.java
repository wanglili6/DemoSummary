package com.example.wll.ceshitablayout;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.example.wll.ceshitablayout.base.BaseActivity;
import com.example.wll.ceshitablayout.constant.Constants;
import com.example.wll.ceshitablayout.pojoBean.RegisterInfo;
import com.example.wll.ceshitablayout.pojoBean.UserInfo;
import com.example.wll.ceshitablayout.servser.LoginSevser;
import com.example.wll.ceshitablayout.servser.RegisterSevser;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class RegistrationActivity extends BaseActivity {


    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.user_pwd)
    EditText userPwd;
    @BindView(R.id.tv_register)
    TextView tvLogin;

    @Override
    public void widgetClick(View v) {
switch (v.getId()){
    case R.id.tv_register:
        String username = userName.getText().toString().trim();
        String userpwd = userPwd.getText().toString().trim();
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(userpwd)) {
            register(username, userpwd);
        } else {
            showToast("用户名或者密码不能为空!");
        }

        break;
}
    }

    /**
     * 注册
     * @param username
     * @param userpwd
     */
    private void register(final String username, final String userpwd) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//新的配置
                .baseUrl(Constants.BaseUrl)
                .build();
        RegisterSevser service = retrofit.create(RegisterSevser.class);

        service.register(username, userpwd)               //获取Observable对象
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(Schedulers.io())         //请求完成后在io线程中执行
                .doOnNext(new Action1<RegisterInfo>() {
                    @Override
                    public void call(RegisterInfo userInfo) {
//                        saveUserInfo(userInfo);//保存用户信息到本地
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(new Subscriber<RegisterInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //请求失败
                        LogUtils.i(e.toString());
                    }

                    @Override
                    public void onNext(RegisterInfo registerInfo) {
                        //请求成功
                        if (registerInfo!=null){
                                showToast(registerInfo.getMsg());
                            if (registerInfo.getMsg().equals("注册成功")){
                                Bundle bundle = new Bundle();
                                bundle.putString("name",username);
                                bundle.putString("pwd",userpwd);
                                startActivity(LoginActivity.class,bundle);
                                finish();
                            }
                        }
                    }
                });
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_registration;
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this);
    }

    @Override
    public void setListener() {
        tvLogin.setOnClickListener(this);

    }

    @Override
    public void doBusiness(Context mContext) {

    }


}
