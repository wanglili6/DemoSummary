package com.example.wll.ceshitablayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wll.ceshitablayout.utils.CreatDoc;
import com.example.wll.ceshitablayout.utils.FileUtils;
import com.example.wll.ceshitablayout.utils.SDpath;
import com.example.wll.ceshitablayout.utils.SignView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrintMainActivity extends Activity implements OnClickListener {

    private static final String SAVE_PATH = Environment.getExternalStorageDirectory() + "/wllview/";
    // 模板文集地址
    private static String demoPath = "";
    // 创建生成的文件地址
    private static String newPath = "";
    private static String newhtml2wordPath = "";
    //html文件存储位置
    private static String savePath = "";
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
    @BindView(R.id.office)
    WebView office;
    @BindView(R.id.btn_dachu)
    Button btnDachu;
    @BindView(R.id.btn_print)
    Button btnPrint;


    private String wordname="测试";//文件名


    private WebView webView;

    private int countpic = 0;
    private String signNames;
    private AlertDialog dialog;
    private SignView signView;
    String modename = "jcblMode.doc";
    String printname = "打印jcblMode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_print_html);
        ButterKnife.bind(this);
        init();

        try {

            InputStream inputStream = getAssets().open(modename);//必须用doc格式的
            FileUtils.writeFile(new File(demoPath), inputStream);
            doScan();
            doHtml();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void init() {
        tvTitle.setText("文书模板");
        ivBack.setVisibility(View.VISIBLE);
        btnDachu.setOnClickListener(this);
        rlBack.setOnClickListener(this);
        btnPrint.setOnClickListener(this);
        demoPath = SDpath.getSDPath() + "/" + "wll/" + modename + "";//必须用doc格式的
        newPath = SDpath.getSDPath() + "/" + "wll/" + printname + ".doc";//必须用doc格式的
        newhtml2wordPath = SDpath.getSDPath() + "/" + "wll/" + "wllHtml2WOrd" + ".doc";//必须用doc格式的
        savePath = SDpath.getSDPath() + "/" + "wll/" + printname + ".html";//存html的路径


    }


    private void doScan() {
        //获取模板文件
        File demoFile = new File(demoPath);
        //创建生成的文件
        File newFile = new File(newPath);
        if (newFile.exists()) {
            newFile.delete();
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("$printname$", "测试你");

        CreatDoc.writeDoc(demoFile, newFile, map);
    }

    private void doHtml() {
        //转换html
        signNames = CreatDoc.convert2Html(newPath, savePath, wordname);
        dowebviewhtml();

    }

    //WebView加载显示本地html文件
    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void dowebviewhtml() {
        //WebView加载显示本地html文件
        webView = (WebView) this.findViewById(R.id.office);
        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadsImagesAutomatically(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setBlockNetworkImage(false);
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl("file:///" + savePath + "");
        webView.addJavascriptInterface(new Javascript(this), "imagelistner");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:(function(){"
                        + "var objs1 = document.getElementsByTagName(\"img\"); "
                        + "for(var i=0;i<objs1.length;i++){"
                        + "  objs1[i].id=\"\"+i+\"\"        "
                        + "}"
                        + ""
                        + "var objs = document.getElementsByTagName(\"img\"); "
                        + "for(var i=0;i<objs.length;i++)  " + "{"
                        + "    objs[i].onclick=function()  " + "    {  "
                        + "        window.imagelistner.openImage(this.id);  "
                        + "    }  " + "}" + "})()");

                if (!webView.getSettings().getLoadsImagesAutomatically()) {
                    webView.getSettings().setLoadsImagesAutomatically(true);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

    }


    // js通信接口
    public class Javascript implements OnClickListener {

        private Context context;
        private int imageindex = 0;
        private AlertDialog.Builder customizeDialog;

        public Javascript(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void openImage(String imgindex) {
            imageindex = Integer.parseInt(imgindex);
            getDialog(imgindex);
        }

        private void getDialog(String imgindex) {
            customizeDialog = new AlertDialog.Builder(PrintMainActivity.this);
            final View dialogView = LayoutInflater.from(PrintMainActivity.this)
                    .inflate(R.layout.sign_activity, null);
            customizeDialog.setView(dialogView);
            dialog = customizeDialog.show();
            TextView tv_merchant_title = (TextView) dialog.findViewById(R.id.tv_merchant_title);
            signView = (SignView) dialog.findViewById(R.id.signView);
            signView.setLineColor(Color.BLACK);
            signView.setLineWidth(6);
            Button btn_cleanView = (Button) dialog.findViewById(R.id.btn_cleanView);
            Button btn_saveView = (Button) dialog.findViewById(R.id.btn_saveView);
            ImageView close_image = (ImageView) dialog.findViewById(R.id.close_image);
            btn_cleanView.setOnClickListener(this);
            btn_saveView.setOnClickListener(this);
            close_image.setOnClickListener(this);
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            int height = wm.getDefaultDisplay().getHeight();
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = width;
            params.height = (height) - 444;
            dialog.getWindow().setAttributes(params);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_cleanView:
                    signView.clearPath();
                    break;
                case R.id.btn_saveView:
                    String[] names = signNames.split(",");
                    if (signView.saveImageToFile(SAVE_PATH, names[imageindex])) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                dowebviewhtml();
                                dialog.dismiss();
                                countpic = countpic + 1;
                            }
                        });
                    } else {
                        Toast.makeText(PrintMainActivity.this, "签名失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.close_image:
                    dialog.dismiss();
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.btn_print:
                PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();
                PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
                printManager.print(wordname, printAdapter, null);
                break;
            case R.id.btn_dachu://导出
//                downloadword("");
//                if (wordname.equals("jcbl")) {
//                    if (countpic < 4) {
//                        Toast.makeText(PrintMainActivity.this, "缺少签名,请补充完整!", Toast.LENGTH_LONG).show();
//                    } else {
//                        saveImage(webView);
//                    }
//                } else if (wordname.equals("xwtzs")) {
//                    if (countpic < 3) {
//                        Toast.makeText(PrintMainActivity.this, "缺少签名,请补充完整!", Toast.LENGTH_LONG).show();
//                    } else {
//                        saveImage(webView);
//                    }
//                } else {
                    saveImage(webView);
//                }


                break;
        }
    }

    private void delFilePath() {
        File demo = new File(demoPath);
        if (demo.exists()) {
            demo.delete();
        }
        File sava = new File(savePath);
        if (sava.exists()) {
            sava.delete();
        }
        File newpathFile = new File(newPath);
        if (newpathFile.exists()) {
            newpathFile.delete();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        delFilePath();
    }


    /**
     * 把html转成word文档
     *
     * @param params
     * @return
     */
    private String downloadword(String params) {
        int contentLength = 0;
        //创建生成的文件
//        File newFile = new File(savePath);
//        if (newFile.exists()) {
//            newFile.delete();
//        }
        Log.i("打印文件", "downloadword: " + params);
        try {
            // 构造URL
            URL url = new URL("file:///" + savePath + "");
            // 打开连接
            URLConnection con = url.openConnection();
            //获得文件的长度
            contentLength = con.getContentLength();
//	            System.out.println("长度 :"+contentLength);
            // 输入流
            InputStream is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            OutputStream os = new FileOutputStream(newhtml2wordPath);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (contentLength > 0) {
            return "1";
        } else {
            return "-1";
        }
    }

    /**
     * 保存图片
     *
     * @param view
     */
    public void saveImage(View view) {

        Picture snapShot = webView.capturePicture();
        Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(), snapShot.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        snapShot.draw(canvas);
        String imgPath = SDpath.getSDPath() + "/" + "img/";

        File parent = new File(imgPath);
        if (!parent.exists()) {
            parent.mkdirs();
        }
        File file = new File(imgPath + wordname + ".jpg");
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file.getAbsolutePath());
            if (fos != null) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        this);
                builder.setTitle("导出成功, 路径:" + file.getAbsolutePath() + "");
                builder.setPositiveButton("确定", null);
                builder.setCancelable(false);
                builder.show();
                Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
        }
    }
}
