package com.example.wll.ceshitablayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.example.wll.ceshitablayout.base.BaseActivity;
import com.example.wll.ceshitablayout.utils.CodeUtils;
import com.example.wll.ceshitablayout.utils.PreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 短信登录
 */
public class SmsLoginActivity extends BaseActivity

{

    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.user_pwd)
    EditText userPwd;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.iv_login)
    ImageView ivLogin;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_uns)
    TextView tvUns;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    private CodeUtils codeUtils;

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.iv_login:
                Bitmap bitmap = codeUtils.createBitmap();
                ivLogin.setImageBitmap(bitmap);
                break;
            case R.id.tv_login:
                String code = PreferencesUtils.getString(SmsLoginActivity.this, "code");
                LogUtils.d(code);
                break;
            case R.id.tv_uns:
                startActivity(LoginActivity.class);
                finish();
                break;
        }
    }


    @Override
    public void initParms(Bundle parms) {
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
        tvUns.setOnClickListener(this);

    }

    @Override
    public void doBusiness(Context mContext) {

    }


}
