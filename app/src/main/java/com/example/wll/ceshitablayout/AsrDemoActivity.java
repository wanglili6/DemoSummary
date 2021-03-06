package com.example.wll.ceshitablayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.example.wll.ceshitablayout.utils.RecordUtil;
import com.example.wll.ceshitablayout.utils.speech.setting.IatSettings;
import com.example.wll.ceshitablayout.utils.speech.setting.TtsSettings;
import com.example.wll.ceshitablayout.utils.speech.util.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 讯飞语音
 */
public class AsrDemoActivity extends AppCompatActivity {

    @BindView(R.id.imageView1)
    ImageView imageView1;
    @BindView(R.id.iv_volume)
    ImageView ivVolume;
    @BindView(R.id.tv_tack)
    TextView tvTack;
    @BindView(R.id.ll_record)
    LinearLayout llRecord;
    @BindView(R.id.iat_text)
    EditText iatText;
    @BindView(R.id.iv_voice_animal)
    ImageView ivVoiceAnimal;
    @BindView(R.id.iv_voice)
    ImageView ivVoice;
    @BindView(R.id.tv_start_recding)
    TextView tvStartRecding;
    @BindView(R.id.start_recding)
    RelativeLayout startRecding;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.rl_start_recding)
    RelativeLayout rlStartRecding;
    @BindView(R.id.btn_event_recording)
    LinearLayout btnEventRecording;
    @BindView(R.id.iat_recognize)
    Button iatRecognize;
    @BindView(R.id.iat_stop)
    Button iatStop;
    @BindView(R.id.iat_cancel)
    Button iatCancel;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.rl_select)
    RelativeLayout rlSelect;
    @BindView(R.id.rl_title_bg)
    RelativeLayout rlTitleBg;
    private RecordUtil mRecorduUtil;
    Handler mHandler = new Handler();
    private int time;
    private long currentTime;
    private String path;
    private int canceltTime;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private int ret;
    private static String TAG = AsrDemoActivity.class.getSimpleName();
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    private AudioDecode audioDecode;
    // 语音合成对象
    private SpeechSynthesizer mTts;

    // 默认发音人
    private String voicer = "xiaoyan";

    private String[] mCloudVoicersEntries;
    private String[] mCloudVoicersValue;

    // 缓冲进度
    private int mPercentForBuffering = 0;
    // 播放进度
    private int mPercentForPlaying = 0;
    private Toast mToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.isrdemo);
        ButterKnife.bind(this);
        tvBack.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("语音文字");
        mRecorduUtil = new RecordUtil();
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(AsrDemoActivity.this, mInitListener);
        mIatDialog = new RecognizerDialog(AsrDemoActivity.this, mInitListener);
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mEngineType = SpeechConstant.TYPE_CLOUD;
        mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME,
                Activity.MODE_PRIVATE);
// 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(AsrDemoActivity.this, mTtsInitListener);

        // 云端发音人名称列表
        mCloudVoicersEntries = getResources().getStringArray(R.array.voicer_cloud_entries);
        mCloudVoicersValue = getResources().getStringArray(R.array.voicer_cloud_values);

        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        initEvent();

    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                LogUtils.d("初始化失败，错误码：" + code);
            }
        }
    };

    /**
     * 录音的触摸事件
     */
    private void initEvent() {
        btnEventRecording.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getPression();
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        currentTime = System.currentTimeMillis();
                        tvTack.setText("请开始说话");
                        mRecorduUtil.startRecord();
                        if (mRecorduUtil.isRecording()) {
                            //将step2中的linearlayout设为可视
                            llRecord.setVisibility(View.VISIBLE);
                            /**
                             *启动线程<注意！这里不能用handler.post启动Runnable，否则无法removeCallBack>
                             */
                            Thread t = new Thread(mPollTask);
                            t.start();
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        myRecording(currentTime);
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        myRecording(currentTime);
                        break;
                }
                return true;
            }
        });

        rlStartRecding.setOnClickListener(new View.OnClickListener() {

            private AnimationDrawable animationDrawable;

            @Override
            public void onClick(View v) {
                //TODO:播放录音
                ivVoice.setVisibility(View.GONE);
                ivVoiceAnimal.setVisibility(View.VISIBLE);
                mRecorduUtil.startPlay(path, false);
                ivVoiceAnimal.setImageResource(R.drawable.animation_yunyin);
                animationDrawable = (AnimationDrawable) ivVoiceAnimal.getDrawable();
                animationDrawable.start();
                if (time != 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ivVoiceAnimal.setVisibility(View.GONE);
                                    ivVoice.setVisibility(View.VISIBLE);
                                }
                            });

                        }
                    }, time * 1000);
                } else if (canceltTime != 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ivVoiceAnimal.setVisibility(View.GONE);
                                    ivVoice.setVisibility(View.VISIBLE);
                                }
                            });

                        }
                    }, canceltTime * 1000);
                }

            }
        });


    }

    /**
     * 开启一个线程更改语音图片
     */
    private Runnable mPollTask = new Runnable() {
        public void run() {
            int mVolume = mRecorduUtil.getVolume();
            Log.d("volume", mVolume + "");
            updateVolume(mVolume);
            mHandler.postDelayed(mPollTask, 100);
        }
    };


    // 更新音量图
    private void updateVolume(int volume) {
        switch (volume) {
            case 1:
                ivVolume.setImageResource(R.drawable.volume_icon1);
                break;
            case 2:
                ivVolume.setImageResource(R.drawable.volume_icon2);
                break;
            case 3:
                ivVolume.setImageResource(R.drawable.volume_icon3);
                break;
            case 4:
                ivVolume.setImageResource(R.drawable.volume_icon4);
                break;
            case 5:
                ivVolume.setImageResource(R.drawable.volume_icon5);
                break;
            case 6:
                ivVolume.setImageResource(R.drawable.volume_icon6);
                break;
            case 7:
                ivVolume.setImageResource(R.drawable.volume_icon7);
                break;
            default:
                break;
        }
    }

    /**
     * 开始录音
     *
     * @param DownTime
     */
    private void myRecording(long DownTime) {
        long thisTime = System.currentTimeMillis();
        long recordingTime = thisTime - DownTime;
        time = (int) (recordingTime / 1000);
        llRecord.setVisibility(View.GONE);
        if (recordingTime < 1000) {
            tvTack.setText("说话时间太短");
//            ShowToast.show(EventReportActivity.this, "录音时间太短");
            mRecorduUtil.stopRecord();
            return;
        } else {
            if (time <= 1) {
                ViewGroup.LayoutParams layoutParams = startRecding.getLayoutParams();
                layoutParams.width = 160;
                startRecding.setLayoutParams(layoutParams);
            } else if (time > 1) {
                int s = time - 1;
                ViewGroup.LayoutParams pp = startRecding.getLayoutParams();
                if (160 + s * 9 < 500) {
                    pp.width = 160 + s * 9;
                    startRecding.setLayoutParams(pp);

                } else if (160 + s * 9 >= 500) {
                    pp.width = 500;
                    startRecding.setLayoutParams(pp);

                }
            }
            tvStartRecding.setText(time + "''");
            mRecorduUtil.stopRecord();
            btnEventRecording.setVisibility(View.GONE);
            rlStartRecding.setVisibility(View.VISIBLE);
        }
        mHandler.removeCallbacks(mPollTask);
        File folder = new File(RecordUtil.AUDIO_DIR);
        File[] files = folder.listFiles();
        if (files.length != 0) {
            path = files[files.length - 1].getAbsolutePath();
            File flacFile = new File(path);
            iatText.setText(null);// 清空显示内容
            mIatResults.clear();
            // 设置参数
            setParam();
            // 设置音频来源为外部文件
//                mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
            // 也可以像以下这样直接设置音频文件路径识别（要求设置文件在sdcard上的全路径）：
            mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
            mIat.setParameter(SpeechConstant.SAMPLE_RATE, "8000");//设置正确的采样率
            mIat.setParameter(SpeechConstant.ASR_SOURCE_PATH, path);
            ret = mIat.startListening(mRecognizerListener);
            if (ret != ErrorCode.SUCCESS) {
                LogUtils.d("识别失败,错误码：" + ret);
            } else {
                LogUtils.d("开始音频流识别");
                audioDecodeFun(path);
            }
        }
    }

    /**
     * 动态添加权限
     */
    public void getPression() {
        int checkSelfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (checkSelfPermission == PackageManager.PERMISSION_DENIED) {
            //没有权限，申请权限
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_SETTINGS,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.VIBRATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {
            //已经有了权限 ，不需要申请
//            initTraceLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "已经授权成功了", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @OnClick({R.id.tv_delete, R.id.iat_recognize, R.id.iat_stop, R.id.iat_cancel, R.id.tts_play, R.id.tts_pause, R.id.tts_resume, R.id.tts_btn_person_select})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_delete:
                //删除语音
                File folder = new File(RecordUtil.AUDIO_DIR);
                File[] files = folder.listFiles();
                LogUtils.d("wlllfiles", files.length);
                for (int i = 0; i < files.length; i++) {
                    myDeleteFile(files[i].getAbsolutePath());
                }
                path = "";
                LogUtils.d("wlllfiles---", files.length);
                rlStartRecding.setVisibility(View.GONE);
                btnEventRecording.setVisibility(View.VISIBLE);
                break;
            case R.id.iat_recognize:
                // 移动数据分析，收集开始听写事件
                FlowerCollector.onEvent(AsrDemoActivity.this, "iat_recognize");

                iatText.setText(null);// 清空显示内容
                mIatResults.clear();
                // 设置参数
                setParam();
                boolean isShowDialog = mSharedPreferences.getBoolean(
                        getString(R.string.pref_key_iat_show), true);
                if (isShowDialog) {
                    // 显示听写对话框
                    mIatDialog.setListener(mRecognizerDialogListener);
                    mIatDialog.show();
                    LogUtils.d(getString(R.string.text_begin));
                } else {
                    // 不显示听写对话框
                    ret = mIat.startListening(mRecognizerListener);
                    if (ret != ErrorCode.SUCCESS) {
                        LogUtils.d("听写失败,错误码：" + ret);
                    } else {
                        LogUtils.d(getString(R.string.text_begin));
                    }
                }

                break;
            case R.id.iat_cancel:
                mIat.cancel();
                break;
            case R.id.iat_stop:
                mIat.stopListening();
                break;
//            case R.id.iat_recognize_stream:
//                iatText.setText(null);// 清空显示内容
//                mIatResults.clear();
//                // 设置参数
//                setParam();
//                // 设置音频来源为外部文件
////                mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
//                // 也可以像以下这样直接设置音频文件路径识别（要求设置文件在sdcard上的全路径）：
//                mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
//                mIat.setParameter(SpeechConstant.SAMPLE_RATE, "8000");//设置正确的采样率
//                mIat.setParameter(SpeechConstant.ASR_SOURCE_PATH, "/storage/emulated/0/Horusch/audio/audio_1.wav");
//                ret = mIat.startListening(mRecognizerListener);
//                if (ret != ErrorCode.SUCCESS) {
//                    LogUtils.d("识别失败,错误码：" + ret);
//                } else {
//                    LogUtils.d("开始音频流识别");
//                    audioDecodeFun("/storage/emulated/0/Horusch/audio/audio_1.wav");
//                }
//                break;
            case R.id.tts_play:
                iatText.setText(R.string.text_tts_source);
                // 移动数据分析，收集开始合成事件
                FlowerCollector.onEvent(AsrDemoActivity.this, "tts_play");

                String text = iatText.getText().toString();
                // 设置参数
                setHechangParam();
                int code = mTts.startSpeaking(text, mTtsListener);
//			/** 
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
//			String path = Environment.getExternalStorageDirectory()+"/tts.ico";
//			int code = mTts.synthesizeToUri(text, path, mTtsListener);

                if (code != ErrorCode.SUCCESS) {
                    showTip("语音合成失败,错误码: " + code);
                }
                break;
            // 暂停播放
            case R.id.tts_pause:
                mTts.pauseSpeaking();
                break;
            // 继续播放
            case R.id.tts_resume:
                mTts.resumeSpeaking();
                break;
            // 选择发音人
            case R.id.tts_btn_person_select:
                showPresonSelectDialog();
                break;
        }

    }

    private boolean mTranslateEnable = false;
    private SharedPreferences mSharedPreferences;
    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            if (mTranslateEnable) {
                printTransResult(results);
            } else {
                printResult(results);
            }

        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            if (mTranslateEnable && error.getErrorCode() == 14002) {
                LogUtils.d(error.getPlainDescription(true) + "\n请确认是否已开通翻译功能");
            } else {
                LogUtils.d(error.getPlainDescription(true));
            }
        }

    };

    /**
     * 删除本地文件
     */

    public void myDeleteFile(String filePath) {
        File f = new File(filePath);
        if (f.exists()) {
            f.delete();
        }
    }

    /**
     * 听写参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        this.mTranslateEnable = mSharedPreferences.getBoolean("translate", false);
        if (mTranslateEnable) {
            Log.i(TAG, "translate enable");
            mIat.setParameter(SpeechConstant.ASR_SCH, "1");
            mIat.setParameter(SpeechConstant.ADD_CAP, "translate");
            mIat.setParameter(SpeechConstant.TRS_SRC, "its");
        }

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);

            if (mTranslateEnable) {
                mIat.setParameter(SpeechConstant.ORI_LANG, "en");
                mIat.setParameter(SpeechConstant.TRANS_LANG, "cn");
            }
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);

            if (mTranslateEnable) {
                mIat.setParameter(SpeechConstant.ORI_LANG, "cn");
                mIat.setParameter(SpeechConstant.TRANS_LANG, "en");
            }
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            LogUtils.d("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            if (mTranslateEnable && error.getErrorCode() == 14002) {
                LogUtils.d(error.getPlainDescription(true) + "\n请确认是否已开通翻译功能");
            } else {
                LogUtils.d(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            LogUtils.d("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            if (mTranslateEnable) {
                printTransResult(results);
            } else {
                printResult(results);
            }

            if (isLast) {
                // TODO 最后的结果
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            LogUtils.d("当前正在说话，音量大小：" + volume);
            LogUtils.d(data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private void printTransResult(RecognizerResult results) {
        String trans = JsonParser.parseTransResult(results.getResultString(), "dst");
        String oris = JsonParser.parseTransResult(results.getResultString(), "src");

        if (TextUtils.isEmpty(trans) || TextUtils.isEmpty(oris)) {
            LogUtils.d("解析结果失败，请确认是否已开通翻译功能。");
        } else {
            iatText.setText("原始语言:\n" + oris + "\n目标语言:\n" + trans);
        }

    }

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        iatText.setText(resultBuffer.toString());
        iatText.setSelection(iatText.length());
    }

    /**
     * 工具类
     *
     * @param audioPath
     */
    private void audioDecodeFun(String audioPath) {
        audioDecode = AudioDecode.newInstance();
        audioDecode.setFilePath(audioPath);
        audioDecode.prepare();
        audioDecode.setOnCompleteListener(new AudioDecode.OnCompleteListener() {
            @Override
            public void completed(final ArrayList<byte[]> pcmData) {
                if (pcmData != null) {
                    //写入音频文件数据，数据格式必须是采样率为8KHz或16KHz（本地识别只支持16K采样率，云端都支持），位长16bit，单声道的wav或者pcm
                    //必须要先保存到本地，才能被讯飞识别
                    //为防止数据较长，多次写入,把一次写入的音频，限制到 64K 以下，然后循环的调用wirteAudio，直到把音频写完为止
                    for (byte[] data : pcmData) {
                        mIat.writeAudio(data, 0, data.length);
                    }
                    Log.d("-----------stop", System.currentTimeMillis() + "");
                    mIat.stopListening();
                } else {
                    mIat.cancel();
                    Log.d(TAG, "--->读取音频流失败");
                }
                audioDecode.release();
            }
        });
        audioDecode.startAsync();
    }

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                LogUtils.d("初始化失败,错误码：" + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            mPercentForBuffering = percent;
            showTip(String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
            showTip(String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                showTip("播放完成");
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    private int selectedNum = 0;

    /**
     * 发音人选择。
     */
    private void showPresonSelectDialog() {

        new AlertDialog.Builder(this).setTitle("在线合成发音人选项")
                .setSingleChoiceItems(mCloudVoicersEntries, // 单选框有几项,各是什么名字
                        selectedNum, // 默认的选项
                        new DialogInterface.OnClickListener() { // 点击单选框后的处理
                            public void onClick(DialogInterface dialog,
                                                int which) { // 点击了哪一项
                                voicer = mCloudVoicersValue[which];
                                if ("catherine".equals(voicer) || "henry".equals(voicer) || "vimary".equals(voicer)) {
                                    iatText.setText(R.string.text_tts_source_en);
                                } else {
                                    iatText.setText(R.string.text_tts_source);
                                }
                                selectedNum = which;
                                dialog.dismiss();
                            }
                        }).show();


    }

    /**
     * 参数设置--合成
     *
     * @return
     */
    private void setHechangParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
             * 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }

    @Override
    protected void onResume() {
        //移动数据统计分析
        FlowerCollector.onResume(AsrDemoActivity.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(AsrDemoActivity.this);
        super.onPause();
    }
}
