package com.juefeng.android.framework.common.base;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/11/24
 * Time: 13:21
 * Description:
 */
import android.view.ViewGroup;

public interface ViewCreator<T, H extends BaseListAdapter.ViewHolder> {

    H createHolder(int position, ViewGroup parent);

    /**
     * 设置列表里的视图内容
     *
     * @param position 在列表中的位置
     * @param holder   该位置对应的视图
     */
    void bindData(int position, H holder, T data);
}