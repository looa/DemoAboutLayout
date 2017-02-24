package org.looa.demoaboutlayout;

/**
 * Created by ranxiangwei on 2017/2/22.
 */

public abstract class StickyPageBaseAdapter<T> {

    private StickyPageView parent;

    /**
     * 用来创建View，以及其他必要信息（存储在ViewHolder中）
     *
     * @param parent
     * @return
     */
    public StickyPageView.ViewHolder onCreateView(StickyPageView parent) {
        this.parent = parent;
        return null;
    }

    /**
     * 当翻页的时候，会调用改方法
     *
     * @param viewHolder
     * @param position
     * @param isNext
     * @param isMove     是否是移动控件导致的变化
     */
    public void onChangePosition(StickyPageView.ViewHolder viewHolder, int position, boolean isNext, boolean isMove) {
    }

    public void notifyDataSetChanged() {
        parent.refreshAdapter();
    }

    protected int getCount() {
        return 0;
    }

    protected T getItem(int position) {
        return null;
    }
}
