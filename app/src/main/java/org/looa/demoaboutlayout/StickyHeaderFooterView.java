package org.looa.demoaboutlayout;

import android.support.annotation.Px;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * 上拉或者下拉头
 * <p>
 * Created by ranxiangwei on 2017/2/16.
 */

public class StickyHeaderFooterView extends BaseHeader {

    private OnReachLimitListener listener;

    private int limitHeight;

    public final static int HEADER = 1;
    public final static int FOOTER = 2;

    private int type;

    /**
     * 设置Header、footer类型
     *
     * @param resID R.layout.xxx
     */
    public StickyHeaderFooterView(int resID) {
        this.type = resID;
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup viewGroup) {
        View view;
        if (type == HEADER) {
            view = inflater.inflate(R.layout.sticky_paging_header, viewGroup, true);
        } else if (type == FOOTER) {
            view = inflater.inflate(R.layout.sticky_paging_footer, viewGroup, true);
        } else {
            view = inflater.inflate(type, viewGroup, true);
        }
        limitHeight = (int) view.getResources().getDimension(R.dimen.header_height);
        return view;
    }

    public void setOnReachLimitListener(OnReachLimitListener listener) {
        this.listener = listener;
    }

    public void setLimitHeight(@Px int limitHeight) {
        this.limitHeight = limitHeight;
    }

    @Override
    public void onPreDrag(View rootView) {

    }

    @Override
    public void onDropAnim(View rootView, int dy) {

    }

    @Override
    public void onLimitDes(View rootView, boolean upORdown) {
    }

    @Override
    public void onStartAnim() {
        if (listener != null) {
            listener.onReached(this);
        }
    }

    @Override
    public void onFinishAnim() {

    }

    @Override
    public int getDragLimitHeight(View rootView) {
        return limitHeight;
    }

    @Override
    public int getDragSpringHeight(View rootView) {
        return 1;
    }
}
