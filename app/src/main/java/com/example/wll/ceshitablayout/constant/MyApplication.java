package com.example.wll.ceshitablayout.constant;

import android.app.Application;

import com.apkfuns.log2file.LogFileEngineFactory;
import com.apkfuns.logutils.LogLevel;
import com.apkfuns.logutils.LogUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by wll on 2017/12/9.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化OKhttpUtils
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

        //初始化zxinger二维码
        ZXingLibrary.initDisplayOpinion(this);

        LogUtils.getLogConfig()
                .configAllowLog(true)
                .configTagPrefix("LogUtilsDemo")
                .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}")
                .configShowBorders(true)
//                .configMethodOffset(1)
                .configLevel(LogLevel.TYPE_VERBOSE);
        LogUtils.getLog2FileConfig().configLog2FileEnable(true)
                .configLog2FilePath("/sdcard/LogUtils/logs/")
                .configLog2FileNameFormat("Hi-%d{yyyyMMdd}-1.txt")
                .configLog2FileLevel(LogLevel.TYPE_VERBOSE)
                .configLogFileEngine(new LogFileEngineFactory());




    }
}