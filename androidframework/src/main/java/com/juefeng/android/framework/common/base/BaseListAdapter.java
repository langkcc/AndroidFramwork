package com.juefeng.android.framework.common.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/11/24
 * Time: 13:20
 * Description:
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {


    protected List<T> list;

    private Context context;

    private LayoutInflater inflater;


    public BaseListAdapter(List<T> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public BaseListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void cleanData(){
        if (this.list!=null){
            this.list.clear();
        }
    }

    public void addData(List<T> list){
        if (list==null){
            return;
        }
        if (this.list == null){
            this.list = list;
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void updateData(List<T> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public abstract int getContentLayoutId();

    public abstract ViewHolder getViewHolder();

    @Override
    public int getCount() {
        if (list==null){
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        if (list==null){
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T t = list.get(position);
        ViewHolder viewHolder;
        if (convertView==null){
            convertView = inflater.inflate(getContentLayoutId(), null);
            viewHolder = getViewHolder();
            viewHolder.initView(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.initData(t);
        viewHolder.initEvent(t);
        return convertView;
    }


    protected interface ViewHolder<T> {

        void initView(View view);

        void initData(T t);

        void initEvent(T t);
    }
}
