package com.example.wll.ceshitablayout.homePageFragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.example.wll.ceshitablayout.AsrDemoActivity;
import com.example.wll.ceshitablayout.Main3Activity;
import com.example.wll.ceshitablayout.PrintMainActivity;
import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.baiDuMap.MapShowActivity;
import com.example.wll.ceshitablayout.baiDuMap.MapTraceActivity;
import com.example.wll.ceshitablayout.tabLayout.CommonTabActivity;
import com.example.wll.ceshitablayout.tabLayout.SegmentTabActivity;
import com.example.wll.ceshitablayout.tabLayout.SlidingTabActivity;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by wll on 2017/12/9.
 */

public class HomeFragment extends Fragment {

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.search_saomiao)
    TextView searchSaomiao;
    @BindView(R.id.tv_search_saomiao)
    TextView tvSearchSaomiao;
    @BindView(R.id.tool_print)
    TextView toolPrint;
    @BindView(R.id.tv_tool_print)
    TextView tvToolPrint;
    @BindView(R.id.tool_updata)
    TextView toolUpdata;
    @BindView(R.id.tv_tool_updata)
    TextView tvToolUpdata;
    @BindView(R.id.tool_yuyin)
    TextView toolYuyin;
    @BindView(R.id.tv_tool_yuyin)
    TextView tvToolYuyin;
    @BindView(R.id.map_dingwei)
    TextView mapDingwei;
    @BindView(R.id.tv_map_dingwei)
    TextView tvMapDingwei;
    @BindView(R.id.map_guiji)
    TextView mapGuiji;
    @BindView(R.id.tv_map_guiji)
    TextView tvMapGuiji;
    @BindView(R.id.btn_jishiqi)
    TextView btnJishiqi;
    @BindView(R.id.tv_btn_jishiqi)
    TextView tvBtnJishiqi;
    @BindView(R.id.btn_commontabactivity)
    TextView btnCommontabactivity;
    @BindView(R.id.tv_btn_commontabactivity)
    TextView tvBtnCommontabactivity;
    @BindView(R.id.btn_segmenttabactivity)
    TextView btnSegmenttabactivity;
    @BindView(R.id.tv_btn_segmenttabactivity)
    TextView tvBtnSegmenttabactivity;
    @BindView(R.id.btn_slidingtabactivity)
    TextView btnSlidingtabactivity;
    @BindView(R.id.tv_btn_slidingtabactivity)
    TextView tvBtnSlidingtabactivity;
    private String TAG = "HomeFragment的log";
    List<Integer> imagesList = new ArrayList<>();
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.home_fragment, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        imagesList.clear();
        imagesList.add(R.mipmap.img_1);
        imagesList.add(R.mipmap.img_2);
        imagesList.add(R.mipmap.img_3);
        imagesList.add(R.mipmap.img_4);
        imagesList.add(R.mipmap.img_5);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(imagesList);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);


        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                LogUtils.i("点击轮播图" + position);
            }
        });
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        return inflate;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == 1) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    String substring = result.substring(0, 4);
                    LogUtils.d(substring);
                    if (substring.equals("http")) {
                        Toast.makeText(getContext(), "这个是地址", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getContext(), "这个是" + result, Toast.LENGTH_LONG).show();
                    }

                    LogUtils.d(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getContext(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.btn_jishiqi, R.id.search_saomiao, R.id.tool_print, R.id.tool_updata, R.id.tool_yuyin, R.id.btn_commontabactivity, R.id.btn_segmenttabactivity, R.id.btn_slidingtabactivity, R.id.map_dingwei, R.id.map_guiji})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_saomiao:
                /**
                 * 打开默认二维码扫描界面
                 */
                Intent intent = new Intent(getContext(), CaptureActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.tool_print:
                Intent intent1 = new Intent(getContext(), PrintMainActivity.class);
                startActivity(intent1);
                break;
            case R.id.tool_updata:
                //上传附件
                selectFile();
                break;
            case R.id.map_dingwei:
                //实时定位
                Intent intentMapShow = new Intent(getContext(), MapShowActivity.class);
                startActivity(intentMapShow);
                break;
            case R.id.map_guiji:
                //查询轨迹
                Intent intentMapTrace = new Intent(getContext(), MapTraceActivity.class);
                startActivity(intentMapTrace);
                break;
            case R.id.tool_yuyin:
                Intent intent6 = new Intent();
                intent6.setClass(getActivity(), AsrDemoActivity.class);
                startActivity(intent6);
                break;
            case R.id.btn_commontabactivity:
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), CommonTabActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_segmenttabactivity:
                Intent intent3 = new Intent();
                intent3.setClass(getActivity(), SegmentTabActivity.class);
                startActivity(intent3);
                break;
            case R.id.btn_slidingtabactivity:
                Intent intent4 = new Intent();
                intent4.setClass(getActivity(), SlidingTabActivity.class);
                startActivity(intent4);
                break;
            case R.id.btn_jishiqi:
                Intent intent7 = new Intent();
                intent7.setClass(getActivity(), Main3Activity.class);
                startActivity(intent7);
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
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "请安装一个文件管理器.", Toast.LENGTH_SHORT).show();

        }
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */

            //Glide 加载图片简单用法
            Glide.with(context).load(path).into(imageView);


        }


    }
}
