package com.example.wll.ceshitablayout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.wll.ceshitablayout.NoScrollExpandablelistview;
import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.bean.ModeAdapterBean;
import com.example.wll.ceshitablayout.bean.ModeBean;
import com.example.wll.ceshitablayout.bean.ModeGoupBean;
import com.example.wll.ceshitablayout.bean.ModeParentBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Apathy、恒
 *         <p>
 *         子类子类列表的适配器
 */
public class CListAdapter extends BaseAdapter {

    private Context mContext;
    private List<ModeBean.ListBean> mChilds;
    private List<ModeGoupBean> groupList;

    public CListAdapter(Context context, List<ModeBean.ListBean> childs, List<ModeGoupBean> groupList) {
        this.mContext = context;
        this.mChilds = childs;
        this.groupList = groupList;
    }

    @Override
    public int getCount() {
        return mChilds != null ? mChilds.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        if ((getCount() > 0) && (position > 0 && position < mChilds.size())) {
            return mChilds.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.calendar_prant, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ModeBean.ListBean listBean = mChilds.get(position);
        holder.tvChildTitle.setText(listBean.getModeName());
        List<ModeBean.ListBean.TemplateBean> template = listBean.getTemplate();
        ModeGoupBean modeGoupBean = groupList.get(position);

        ExpandListAdapter expandListAdapter = new ExpandListAdapter(mContext, template, modeGoupBean.getAdapterBeanList());
        holder.listView.setAdapter(expandListAdapter);
        expandListAdapter.notifyDataSetChanged();

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.tv_child_title)
        TextView tvChildTitle;
        @BindView(R.id.list_view)
        NoScrollExpandablelistview listView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
