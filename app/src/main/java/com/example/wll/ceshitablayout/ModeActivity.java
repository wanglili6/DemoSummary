package com.example.wll.ceshitablayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.wll.ceshitablayout.adapter.CListAdapter;
import com.example.wll.ceshitablayout.bean.ModeAdapterBean;
import com.example.wll.ceshitablayout.bean.ModeBean;
import com.example.wll.ceshitablayout.bean.ModeGoupBean;
import com.example.wll.ceshitablayout.bean.ModeParentBean;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class ModeActivity extends AppCompatActivity {

    @BindView(R.id.list_view)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        ButterKnife.bind(this);
        OkHttpUtils
                .get()
                .url("http://a.hiyyh.com/api/testOneApi")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        ModeBean bean = gson.fromJson(response, ModeBean.class);
                        List<ModeBean.ListBean> list = bean.getList();
                        List<ModeGoupBean> groupList = new ArrayList<>();
                        for (int i2 = 0; i2 <list.size() ; i2++) {
                            ModeGoupBean modeGoupBean = new ModeGoupBean();
                            ModeBean.ListBean listBean = list.get(i2);
                            List<ModeBean.ListBean.TemplateBean> template = listBean.getTemplate();
                            modeGoupBean.setId(listBean.getModeId());
                            modeGoupBean.setName(listBean.getModeName());
                            List<ModeParentBean> parentList = new ArrayList<>();
                            for (int i = 0; i < template.size(); i++) {
                                List<ModeBean.ListBean.TemplateBean.ChildListBean> child_list = template.get(i).getChild_list();
                                ModeParentBean modeParentBean = new ModeParentBean();
                                modeParentBean.setId(template.get(i).getParent().get(0).getStdid());
                                List<ModeAdapterBean> childList = new ArrayList<>();
                                for (int i1 = 0; i1 < child_list.size(); i1++) {
                                    ModeAdapterBean modeBean = new ModeAdapterBean();
                                    modeBean.setAnswerid(0);
                                    modeBean.setId(child_list.get(i1).getStdid());
                                    modeBean.setName(child_list.get(i1).getStditemPoint());
                                    childList.add(modeBean);
                                }
                                modeParentBean.setAdapterBeanList(childList);
                                parentList.add(modeParentBean);
                            }
                            modeGoupBean.setAdapterBeanList(parentList);
                            groupList.add(modeGoupBean);
                        }
                        CListAdapter cListAdapter = new CListAdapter(ModeActivity.this, list,groupList);
                        listView.setAdapter(cListAdapter);
                        cListAdapter.notifyDataSetChanged();
                    }
                });

    }
}
