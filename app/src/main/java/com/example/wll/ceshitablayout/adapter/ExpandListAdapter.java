package com.example.wll.ceshitablayout.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.bean.ModeAdapterBean;
import com.example.wll.ceshitablayout.bean.ModeBean;
import com.example.wll.ceshitablayout.bean.ModeParentBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/8/4.
 */

public class ExpandListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<ModeBean.ListBean.TemplateBean> titleList;
    private List<ModeParentBean> childList;
    private Map<String, String> jcnr_map;
    boolean isShowList = false;

    public ExpandListAdapter(Context context, List<ModeBean.ListBean.TemplateBean> titleList, List<ModeParentBean> childList) {
        this.context = context;
        this.titleList = titleList;
        this.childList = childList;
        jcnr_map = new HashMap<>();

    }

    //获取的组的个数
    @Override
    public int getGroupCount() {
        return titleList != null ? titleList.size() : 0;
    }

    //获取孩子的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).getAdapterBeanList() != null ? childList.get(groupPosition).getAdapterBeanList().size() : 0;
    }

    //获取组
    @Override
    public Object getGroup(int groupPosition) {
        return titleList.get(groupPosition).getParent().get(0).getStditemPoint();
    }

    //获取孩子
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (childList.get(groupPosition).getAdapterBeanList() != null
                && childList.get(groupPosition).getAdapterBeanList().size() > 0)
            return childList.get(groupPosition).getAdapterBeanList()
                    .get(childPosition).getName();
        return null;
    }

    //虎丘区组的id
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //孩子ID
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //    按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
    @Override
    public boolean hasStableIds() {
        return false;
    }

    //  获得父项显示的view
    @Override
    public View getGroupView(int parentPos, boolean isExpanded, View view, ViewGroup viewGroup) {
        ViewHolderGroup viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.calendar_group, viewGroup, false);
            viewHolder = new ViewHolderGroup(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderGroup) view.getTag();
        }
        viewHolder.tvCalendarGroupPName.setText(titleList.get(parentPos).getParent().get(0).getStditemPoint());

        //箭头右移后判断是否展开状态，添加不同方向箭头
        if (isExpanded) {
            viewHolder.ivJIantou.setBackgroundResource(R.mipmap.list_press_icon1);
        } else {
            viewHolder.ivJIantou.setBackgroundResource(R.mipmap.list_normal_icon1);

        }

        return view;
    }

    //  获得子项显示的view
    @Override
    public View getChildView(int parentPos, final int childPos, boolean b, View view, ViewGroup viewGroup) {
        ViewHolderChild viewHolderChild = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.child_child_item, viewGroup, false);
            viewHolderChild = new ViewHolderChild(view);
            view.setTag(viewHolderChild);
        } else {
            viewHolderChild = (ViewHolderChild) view.getTag();
        }
        ModeParentBean modeParentBean = childList.get(parentPos);
        final List<ModeAdapterBean> adapterBeanList = modeParentBean.getAdapterBeanList();
        ModeAdapterBean modeAdapterBean = adapterBeanList.get(childPos);
        viewHolderChild.childChildTV.setText(modeAdapterBean.getName());
        String stdid = modeAdapterBean.getId();
        viewHolderChild.radioGroup.setId(Integer.parseInt(stdid));
        viewHolderChild.radioGroup.setOnCheckedChangeListener(null);

        for (int i = 0; i < adapterBeanList.size(); i++) {
            if (adapterBeanList.get(childPos).getId().equals(stdid)) {
                switch (adapterBeanList.get(childPos).getAnswerid()) {
                    case 1:
                        viewHolderChild.radioGroup.check(R.id.radio_yes);
                        break;
                    case 2:
                        viewHolderChild.radioGroup.check(R.id.radio_no);
                        break;
                    default:
                        viewHolderChild.radioGroup.clearCheck();
                        break;
                }
            }
        }

        viewHolderChild.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < adapterBeanList.size(); i++) {
                    String id = adapterBeanList.get(i).getId();
                    if (Integer.parseInt(id) == group.getId()) {
                        ModeAdapterBean modeAdapterBean1 = adapterBeanList.get(childPos);
                        switch (checkedId) {
                            case R.id.radio_yes:
                                jcnr_map.put(adapterBeanList.get(i).getId() + "", "1");
                                modeAdapterBean1.setAnswerid(1);
                                break;
                            case R.id.radio_no:
                                jcnr_map.put(adapterBeanList.get(i).getId() + "", "2");
                                modeAdapterBean1.setAnswerid(2);
                                break;
                        }
                    }
                }
            }
        });
        return view;
    }

    //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    static class ViewHolderGroup {
        //        @BindView(R.id.tv_calendar_group_name)
//        TextView tvCalendarGroupName;
        @BindView(R.id.tv_calendar_group_pname)
        TextView tvCalendarGroupPName;
        @BindView(R.id.iv_jintou)
        ImageView ivJIantou;

        ViewHolderGroup(View view) {
            ButterKnife.bind(this, view);
        }
    }


    static class ViewHolderChild {
        @BindView(R.id.childChildTV)
        TextView childChildTV;
        @BindView(R.id.group_radio)
        RadioGroup radioGroup;
        @BindView(R.id.radio_yes)
        RadioButton radioYes;
        @BindView(R.id.radio_no)
        RadioButton radioNo;

        ViewHolderChild(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
