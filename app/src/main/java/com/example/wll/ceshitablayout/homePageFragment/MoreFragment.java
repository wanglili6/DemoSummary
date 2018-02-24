package com.example.wll.ceshitablayout.homePageFragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.example.wll.ceshitablayout.AsrDemoActivity;
import com.example.wll.ceshitablayout.LoginActivity;
import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.constant.MyApplication;
import com.example.wll.ceshitablayout.constant.UserMsg;
import com.example.wll.ceshitablayout.tabLayout.SegmentTabActivity;
import com.example.wll.ceshitablayout.utils.FileUtils;
import com.example.wll.ceshitablayout.utils.PreferencesUtils;
import com.iflytek.cloud.GrammarListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import org.apache.poi.ss.formula.functions.IfFunc;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by wll on 2017/12/9.
 * 更多
 */

public class MoreFragment extends Fragment {
    @BindView(R.id.btn_upfile)
    Button btnCeshi;
    @BindView(R.id.btn_yuyin)
    Button btnYuyin;
    @BindView(R.id.btn_dayin)
    Button btnDayin;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.more_fragment, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        return inflate;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_upfile, R.id.btn_dayin, R.id.btn_yuyin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_yuyin:
                Intent intent1 = new Intent();
                intent1.setClass(getActivity(), AsrDemoActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_upfile:
                //上传附件
                selectFile();
                break;
            case R.id.btn_dayin:
                //退出
                PreferencesUtils.putString(getContext(), UserMsg.UserId, "");
                MyApplication.finish();
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
        }
    }

    /**
     * 通过手机选择文件
     */
    public void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "选择文件上传"), 345);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "请安装一个文件管理器.", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 345) {//345选择文件的请求码
            if (data != null) {
                if (resultCode == -1) {
                    if (data.getData() == null) {
                        return;
                    }
                    if (!FileUtils.isSdcardExist()) {
                        Toast.makeText(getContext(), "SD卡不可用,请检查", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Uri uri = data.getData();
                    LogUtils.d(uri + "===");
                    String path = FileUtils.getPhotoPathFromContentUri(getContext(), uri);
                    LogUtils.d(path + "===");
                }
            }
        }
    }


}
