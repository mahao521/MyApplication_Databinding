package com.baisoo.myapplication_databinding;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Sense on 2016/3/9.
 */
public class ListViewAdapter extends BaseAdapter {
    private Context contextl;
    private List data;
    private int item;
    public ListViewAdapter(Context contextl,int item,List data){
        this.contextl=contextl;
        this.data=data;
        this.item=item;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewDataBinding viewDataBinding;
        if (convertView == null) {
            viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(contextl),
                    item, parent, false);
            convertView = viewDataBinding.getRoot();
            convertView.setTag(viewDataBinding);
        } else {
            viewDataBinding = (ViewDataBinding) convertView.getTag();
        }
        viewDataBinding.setVariable(com.baisoo.myapplication_databinding.BR.item,
                data.get(position));
        viewDataBinding.setVariable(com.baisoo.myapplication_databinding.BR.index,
                position);

        return viewDataBinding.getRoot();
    }
}
