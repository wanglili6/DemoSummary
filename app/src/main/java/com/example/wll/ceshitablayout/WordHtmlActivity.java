package com.example.wll.ceshitablayout;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;


import com.example.wll.ceshitablayout.utils.FileUtils;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.hwpf.usermodel.Range;
import org.w3c.dom.Document;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by fuweiwei on 2015/11/28.
 */
public class WordHtmlActivity extends FragmentActivity {
    SdcardUtils sdcardUtils = new SdcardUtils();
    //文件存储位置
    private String docPath = "file:///android_asset/test.doc";
    //文件名称
    private String docName = "test.doc";
    //html文件存储位置
    private String savePath = sdcardUtils.getSDPATH();
    private String TAG="WordHtmlActivity+hhh";
    private File file;
    private String demoPath;
    private String newPath=sdcardUtils.getSDPATH()+"newtest.doc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.html);
        String name = docName.substring(0, docName.indexOf("."));
        InputStream inputStream = null;
        demoPath = sdcardUtils.getSDPATH()+"test.doc";
        try {
            inputStream = this.getAssets().open("test.doc");
            file = new File(demoPath);
            FileUtils.writeFile(file, inputStream);
            Log.i(TAG, "onCreate: "+file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "onCreate: "+e.toString());
        }
        doScan();


//        try {
//            convert2Html(demoPath, savePath + name + ".html");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //WebView加载显示本地html文件
//        WebView webView = (WebView) this.findViewById(R.id.office);
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setLoadsImagesAutomatically(false);
//        webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setJavaScriptEnabled(true);
//
//        String htmlurl = savePath + name + ".html";
//        webView.loadUrl("file://" + htmlurl);
    }


    private void doScan(){
        //获取模板文件
        File demoFile=new File(demoPath);
        //创建生成的文件
        File newFile=new File(newPath);
        Map<String, String> map = new HashMap<String, String>();
        map.put("$QYMC$", "xxx科技股份有限公司");
        map.put("$QYDZ$", "上海市杨浦区xx路xx号");
        map.put("$QYFZR$", "张三");
        map.put("$FRDB$", "李四");
        map.put("$CJSJ$", "2000-11-10");
        map.put("$SCPZMSJWT$", "5");
        map.put("$XCJCJBQ$", "6");
        map.put("$JLJJJFF$", "7");
        map.put("$QYFZRQM$", "张三");
        map.put("$CPRWQM$", "赵六");
        map.put("$ZFZH$", "100001");
        map.put("$BZ$", "无");
        writeDoc(demoFile,newFile,map);
        try {
            convert2Html(newFile.getAbsolutePath(), savePath + "newtest.html");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView加载显示本地html文件
        WebView webView = (WebView) this.findViewById(R.id.office);
        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadsImagesAutomatically(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);


        String htmlurl = savePath + "newtest.html";
        webView.loadUrl("file://" + htmlurl);

    }
    /**
     * demoFile 模板文件
     * newFile 生成文件
     * map 要填充的数据
     * */
    public void writeDoc(File demoFile ,File newFile ,Map<String, String> map)
    {
        try
        {
            FileInputStream in = new FileInputStream(demoFile);
            HWPFDocument hdt = new HWPFDocument(in);
            // Fields fields = hdt.getFields();
            // 读取word文本内容
            Range range = hdt.getRange();
            // System.out.println(range.text());

            // 替换文本内容
            for(Map.Entry<String, String> entry : map.entrySet())
            {
                range.replaceText(entry.getKey(), entry.getValue());
            }
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            FileOutputStream out = new FileOutputStream(newFile, true);
            hdt.write(ostream);
            // 输出字节流
            out.write(ostream.toByteArray());
            out.close();
            ostream.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * word文档转成html格式
     */
    public void convert2Html(String fileName, String outPutFile) {
        HWPFDocument wordDocument = null;
        try {
            wordDocument = new HWPFDocument(new FileInputStream(fileName));
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                    DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
            //设置图片路径
            wordToHtmlConverter.setPicturesManager(new PicturesManager() {
                public String savePicture(byte[] content,
                                          PictureType pictureType, String suggestedName,
                                          float widthInches, float heightInches) {
                    String name = docName.substring(0, docName.indexOf("."));
                    return name + "/" + suggestedName;
                }
            });
            //保存图片
            List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();
            if (pics != null) {
                for (int i = 0; i < pics.size(); i++) {
                    Picture pic = (Picture) pics.get(i);
                    Log.i(TAG, "convert2Html: "+pic.suggestFullFileName());
                    try {
                        String name = docName.substring(0, docName.indexOf("."));
                        String file = savePath + name + "/"
                                + pic.suggestFullFileName();
                        FileUtils.makeDirs(file);
                        pic.writeImageContent(new FileOutputStream(file));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            wordToHtmlConverter.processDocument(wordDocument);
            Document htmlDocument = wordToHtmlConverter.getDocument();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(out);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
            out.close();
            //保存html文件
            writeFile(new String(out.toByteArray()), outPutFile);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("word===", "convert2Html: " + e.toString());
        }
    }

    /**
     * 将html文件保存到sd卡
     */
    public void writeFile(String content, String path) {
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"));
            bw.write(content);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fos != null)
                    fos.close();
            } catch (IOException ie) {
            }
        }
    }
}
