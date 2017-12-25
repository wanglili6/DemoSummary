package com.example.wll.ceshitablayout.servser;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.constant.Constants;
import com.example.wll.ceshitablayout.utils.SdcardUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;

/**
 * Created by wll on 2017/12/9.
 * 使用OKhttpUtils更新下载
 */

public class UpdateService extends Service {
    private String apkURL;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private int mProceess = -1;
    private SdcardUtils sdcardUtils;

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }

    @Override
    public void onCreate() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        sdcardUtils = new SdcardUtils();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            notifyUser("下载失败", 0);
            stopSelf();//取消
        } else {
            apkURL = intent.getStringExtra("apkUrl");
            initNotify();
            initFileDir();
            notifyUser("下载开始", 0);
            startDownload();//启动下载
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 创建文件夹
     */
    private void initFileDir() {
        if (SdcardUtils.existSdcard()) {
            if (!sdcardUtils.isFileExist(Constants.FILE_DIR_NAME)) {//不存在就重新创建文件
                sdcardUtils.creatSDDir(Constants.FILE_DIR_NAME);
            }
        } else {
            Toast.makeText(getApplicationContext(), "sd卡不c在", Toast.LENGTH_LONG).show();
        }
    }

    private void initNotify() {
        builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(Constants.APP_NAME);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT));
    }

    private void startDownload() {
        OkHttpUtils//
                .get()//
                .tag(this)
                .url(apkURL)//
                .build()//
                .execute(new FileCallBack(sdcardUtils.getSDPATH() + "/" + Constants.FILE_DIR_NAME, Constants.APP_NAME)//
                {
                    int fProgress;

                    @Override
                    public void inProgress(float progress, long total, int id) {//下载进度
                        super.inProgress(progress, total, id);
                        fProgress = (int) (100 * progress);
                        if (fProgress != mProceess) {//避免刷新太快，会卡死
                            mProceess = fProgress;
                            notifyUser("正在下载", fProgress);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {//下载失败
                        notifyUser("下载失败", 0);
                        stopSelf();
                    }

                    @Override
                    public void onResponse(File response, int id) {//下载完成
                        notifyUser("下载完成", 100);
                        stopSelf();

                        //下载完成则安装
                        if (Build.VERSION.SDK_INT < 24) {
                            File apkFile = new File(sdcardUtils.getSDPATH() + "/" + Constants.FILE_DIR_NAME + "/" + Constants.APP_NAME);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setDataAndType(Uri.parse("file://" + apkFile.getAbsolutePath()),
                                    "application/vnd.android.package-archive");
                            startActivity(intent);
                        } else {
                            File apkFile = new File(sdcardUtils.getSDPATH() + "/" + Constants.FILE_DIR_NAME + "/" + Constants.APP_NAME);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Uri uriForFile = FileProvider.getUriForFile(UpdateService.this,  "com.example.wll.ceshitablayout.fileProvider", apkFile);
                            intent.setDataAndType(uriForFile,
                                    "application/vnd.android.package-archive");
                            startActivity(intent);
                        }

                        if (notificationManager != null) {
                            notificationManager.cancelAll();
                        }

                    }
                });
    }

    private void notifyUser(String result, int process) {
        if (builder == null) {
            return;
        }
        if (process > 0 && process < 100) {
            builder.setProgress(100, process, false);
        } else {
            builder.setProgress(0, 0, false);
        }
        if (process >= 100) {
            builder.setContentIntent(getContentIntent());
        }
        builder.setContentInfo(process + "%");
        builder.setContentText(result);
        notificationManager.notify(0, builder.build());
    }

    private PendingIntent getContentIntent() {

//        File file= new File(sdcardUtils.getSDPATH() + "/" + Constants.FILE_DIR_NAME + "/" + Constants.APP_NAME);
////        Uri apkUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.wll.ceshitablayout.fileprovider", file);//在AndroidManifest中的android:authorities值
//        Uri apkUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileProvider", file);//在AndroidManifest中的android:authorities值
//        Intent install = new Intent(Intent.ACTION_VIEW);
//        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
//        install.setDataAndType(apkUri, "application/vnd.android.package-archive");


        //下载完成则安装


        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT < 24) {
            File apkFile = new File(sdcardUtils.getSDPATH() + "/" + Constants.FILE_DIR_NAME + "/" + Constants.APP_NAME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + apkFile.getAbsolutePath()),
                    "application/vnd.android.package-archive");
        } else {
            File apkFile = new File(sdcardUtils.getSDPATH() + "/" + Constants.FILE_DIR_NAME + "/" + Constants.APP_NAME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uriForFile = FileProvider.getUriForFile(UpdateService.this,"com.example.wll.ceshitablayout.fileProvider", apkFile);
            intent.setDataAndType(uriForFile,
                    "application/vnd.android.package-archive");
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
