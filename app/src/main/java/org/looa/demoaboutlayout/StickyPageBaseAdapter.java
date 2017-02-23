package org.looa.demoaboutlayout;

import android.view.ViewGroup;

/**
 * Created by ranxiangwei on 2017/2/22.
 */

public abstract class StickyPageBaseAdapter<T> {
    /**
     * 用来创建View，以及其他必要信息（存储在ViewHolder中）
     *
     * @param parent
     * @return
     */
    public abstract StickyPageView.ViewHolder onCreateView(ViewGroup parent);

    /**
     * 当翻页的时候，会调用改方法
     *
     * @param viewHolder
     * @param position
     * @param isNext
     */
    public abstract void onChangePosition(StickyPageView.ViewHolder viewHolder, int position, boolean isNext);

    protected int getCount() {
        return 0;
    }

    protected T getItem(int position) {
        return null;
    }
}
