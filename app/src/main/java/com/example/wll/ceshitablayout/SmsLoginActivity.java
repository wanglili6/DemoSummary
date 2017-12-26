package com.example.wll.ceshitablayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.example.wll.ceshitablayout.base.BaseActivity;
import com.example.wll.ceshitablayout.constant.Constants;
import com.example.wll.ceshitablayout.myInterface.SmsLoginSevser;
import com.example.wll.ceshitablayout.myInterface.SmsgetCodeSevser;
import com.example.wll.ceshitablayout.pojoBean.UserInfo;
import com.example.wll.ceshitablayout.utils.CodeUtils;
import com.example.wll.ceshitablayout.utils.PreferencesUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 短信登录
 */
public class SmsLoginActivity extends BaseActivity

{

    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.user_pwd)
    EditText userPwd;
    @BindView(R.id.btn_code)
    TextView btnCode;
    @BindView(R.id.tv_code_time)
    TextView tvCodeTime;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.iv_login)
    ImageView ivLogin;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.iv_user_login)
    ImageView ivUserLogin;
    private CodeUtils codeUtils;
    Timer timer = new Timer();
    private int recLen = 60;

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.iv_login:
                Bitmap bitmap = codeUtils.createBitmap();
                ivLogin.setImageBitmap(bitmap);
                break;
            case R.id.tv_login:
                String code = PreferencesUtils.getString(SmsLoginActivity.this, "code");
                String inputCode = edCode.getText().toString().trim();
                if (TextUtils.isEmpty(inputCode)) {
                    showToast("图形验证码不能为空!");
                } else {
                    if (inputCode.equals(code)) {
                        String phone = userName.getText().toString().trim();
                        String vercode = userPwd.getText().toString().trim();
                        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(vercode)) {
                            login(phone, vercode);
                        } else {
                            showToast("手机号或验证码不能为空");
                        }
                    } else {
                        showToast("图形验证码不正确!");
                    }
                }
                LogUtils.d(code);
                break;
            case R.id.iv_user_login:
                startActivity(LoginActivity.class);
                finish();
                break;
            case R.id.btn_code:
                String phone = userName.getText().toString().trim();
                getPhoneCode(phone);
                break;
        }
    }

    /**
     * 获取验证码
     *
     * @param phone
     */
    private void getPhoneCode(String phone) {
        btnCode.setVisibility(View.GONE);
        tvCodeTime.setVisibility(View.VISIBLE);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recLen--;
                        tvCodeTime.setText("重新获取(" + recLen + "秒)");
                        if (recLen < 0) {
                            timer.cancel();
                            tvCodeTime.setVisibility(View.GONE);
                            btnCode.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }, 1000, 1000);       // timeTask

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constants.BaseUrl)
                .build();
        SmsgetCodeSevser smsgetCodeSevser = retrofit.create(SmsgetCodeSevser.class);
        smsgetCodeSevser.sendsms(phone)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.i(e.toString());
                    }

                    @Override
                    public void onNext(UserInfo userInfo) {
                        LogUtils.i(userInfo.getMsg());
                    }
                });


    }

    /**
     * 手机号登陆
     *
     * @param phone
     * @param vercode
     */
    private void login(String phone, String vercode) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constants.BaseUrl).build();
        SmsLoginSevser smsLoginSercive = retrofit.create(SmsLoginSevser.class);
        smsLoginSercive.login(phone, vercode)//创建订阅者
                .subscribeOn(Schedulers.newThread())//创建一个新的线程
//                .observeOn(Schedulers.io())         //请求完成后在io线程中执行
//                .doOnNext(new Action1<UserInfo>() {
//                    @Override
//                    public void call(UserInfo userInfo) {
////                        saveUserInfo(userInfo);//保存用户信息到本地
//                    }
//                })
                .observeOn(AndroidSchedulers.mainThread())//获取订阅的消息交给主线程做
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
                        LogUtils.i(userInfo.getMsg());
                    }
                });

    }


    @Override
    public void initParms(Bundle parms) {
        setSteepStatusBar(false);
        codeUtils = new CodeUtils(SmsLoginActivity.this);
        Bitmap bitmap = codeUtils.createBitmap();
        ivLogin.setImageBitmap(bitmap);
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_sms_login;
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this);
    }

    @Override
    public void setListener() {
        ivLogin.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        btnCode.setOnClickListener(this);
        ivUserLogin.setOnClickListener(this);

    }

    @Override
    public void doBusiness(Context mContext) {

    }


}
