package org.looa.demoaboutlayout;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Px;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;


/**
 * 粘性翻页view
 * <p>
 * Created by ranxiangwei on 2017/2/21.
 */

public class StickyPageView extends LinearLayout implements SpringView.OnRefreshListener, OnReachLimitListener, Animator.AnimatorListener {
    private Context context;

    private View pageA;
    private View pageB;

    private SpringView springViewA;
    private SpringView springViewB;

    private StickyHeaderFooterView headerA;
    private StickyHeaderFooterView footerA;
    private StickyHeaderFooterView headerB;
    private StickyHeaderFooterView footerB;

    private View pageFree;//游离态页面（游离态页面在装填结束前都是游离态）
    private View pageFill;//已装填页面（已装填页面的marginTop一直处于0的状态）

    private ViewHolder holderA;
    private ViewHolder holderB;

    private int height;//myView 的高度
    private int width;
    private boolean isFinishAnim = true;

    private StickyPageBaseAdapter adapter;
    private int size = 0;//item的数量，根据adapter动态设置
    private int position = 0;//当前位置，可以自动计算

    private Interpolator interpolator = new DecelerateInterpolator();
    private long time = 300;

    public StickyPageView(Context context) {
        this(context, null);
    }

    public StickyPageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickyPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);

        LayoutInflater inflater = LayoutInflater.from(context);
        pageA = inflater.inflate(R.layout.sticky_paging_info, null);
        pageB = inflater.inflate(R.layout.sticky_paging_info, null);

        addView(pageA);
        addView(pageB);

        setPageFill(pageA);
        setPageFree(pageB);

        springViewA = (SpringView) pageA.findViewById(R.id.sv_info);
        springViewB = (SpringView) pageB.findViewById(R.id.sv_info);

        headerA = new StickyHeaderFooterView(R.layout.sticky_paging_empty);
        footerA = new StickyHeaderFooterView(R.layout.sticky_paging_empty);
        headerB = new StickyHeaderFooterView(R.layout.sticky_paging_empty);
        footerB = new StickyHeaderFooterView(R.layout.sticky_paging_empty);

        headerA.setOnReachLimitListener(this);
        footerA.setOnReachLimitListener(this);
        headerB.setOnReachLimitListener(this);
        footerB.setOnReachLimitListener(this);

        springViewA.setOnRefreshListener(this);
        springViewB.setOnRefreshListener(this);
    }

    /**
     * 设置当前位置，只能在控件非动画期间调用
     *
     * @param position
     */
    public void setCurPosition(int position) {
        if (!isFinishAnim) return;
        this.position = position;
        if (adapter != null) {
            adapter.onChangePosition((ViewHolder) getPageFill().getTag(), position, true);
        }
    }

    /**
     * 设置adapter，必要的
     *
     * @param adapter StickyPageBaseAdapter的实现类
     */
    public void setAdapter(StickyPageBaseAdapter adapter) {
        if (adapter == null) return;
        this.adapter = adapter;
        holderA = adapter.onCreateView(this);
        holderB = adapter.onCreateView(this);
        if (holderA.itemView instanceof RecyclerView ||
                holderA.itemView instanceof WebView ||
                holderA.itemView instanceof ScrollView ||
                holderA.itemView instanceof ListView) {
            springViewA.removeAllViews();
            springViewB.removeAllViews();
            springViewA.addView(holderA.itemView);
            springViewB.addView(holderB.itemView);
        } else {
            ((ScrollView) springViewA.getChildAt(0)).removeAllViews();
            ((ScrollView) springViewB.getChildAt(0)).removeAllViews();
            ((ScrollView) springViewA.getChildAt(0)).setFillViewport(true);
            ((ScrollView) springViewB.getChildAt(0)).setFillViewport(true);
            ((ScrollView) springViewA.getChildAt(0)).addView(holderA.itemView);
            ((ScrollView) springViewB.getChildAt(0)).addView(holderB.itemView);
            Log.i(holderA.itemView.getClass().getName(), "((ScrollView) springViewA.getChildAt(0)).removeAllViews();");
        }
        pageA.setTag(holderA);
        pageB.setTag(holderB);
        this.size = adapter.getCount();
        this.position = 0;
        if (size > 0) {
            adapter.onChangePosition((ViewHolder) getPageFill().getTag(), 0, true);
        }

        springViewA.setHeader(headerA);
        springViewA.setFooter(footerA);
        springViewB.setHeader(headerB);
        springViewB.setFooter(footerB);
    }

    /**
     * 设置翻页的动画时长
     *
     * @param time 300ms default
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * 设置触发翻页事件的最小距离
     *
     * @param offSet 偏移量
     */
    public void setLimitHeight(@Px int offSet) {
        headerA.setLimitHeight(offSet);
        footerA.setLimitHeight(offSet);
        headerB.setLimitHeight(offSet);
        footerB.setLimitHeight(offSet);
    }

    /**
     * 设置翻页滑动的插值器
     *
     * @param interpolator DecelerateInterpolator
     */
    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    private View getPageFree() {
        return pageFree;
    }

    private void setPageFree(View pageFree) {
        this.pageFree = pageFree;
    }

    private View getPageFill() {
        return pageFill;
    }

    private void setPageFill(View pageFill) {
        this.pageFill = pageFill;
    }


    @Override
    public void onRefresh(View view) {
    }

    @Override
    public void onLoadMore(View view) {
    }


    @Override
    public void onReached(Object o) {
        boolean isNext = true;
        if (o == headerA) {
            moveUp();
            isNext = false;
        } else if (o == footerA) {
            moveDown();
            isNext = true;
        } else if (o == headerB) {
            moveUp();
            isNext = false;
        } else if (o == footerB) {
            moveDown();
            isNext = true;
        }
        if (adapter != null) {
            adapter.onChangePosition((ViewHolder) getPageFree().getTag(), position, isNext);
        }
        if (o instanceof SpringView) {
            ((SpringView) o).onFinishFreshAndLoad();
        }
    }

    public void moveDown() {
        if (position + 1 < size) {
            position++;
            fill(Position.DOWN);
            move(true);
        }
    }

    public void moveUp() {
        if (position - 1 >= 0) {
            position--;
            fill(Position.UP);
            move(false);
        }
    }

    private void move(boolean isNext) {
        if (!isFinishAnim) return;
        View movePage;
        if (isNext) movePage = getPageFill();
        else movePage = getPageFree();

        LayoutParams params = (LayoutParams) movePage.getLayoutParams();
        LayoutParamsWarpper warpper = new LayoutParamsWarpper(movePage);
        int topMargin = params.topMargin;
        if (topMargin == 0 && isNext) {
            ObjectAnimator animator = ObjectAnimator.ofInt(warpper, "marginTop", 0, -height);
            animator.setDuration(time);
            animator.setInterpolator(interpolator);
            animator.addListener(this);
            animator.start();
        } else if (topMargin != 0 && !isNext) {
            ObjectAnimator animator = ObjectAnimator.ofInt(warpper, "marginTop", -height, 0);
            animator.setDuration(time);
            animator.setInterpolator(interpolator);
            animator.addListener(this);
            animator.start();
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
        isFinishAnim = false;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        isFinishAnim = true;
        clear();
    }

    @Override
    public void onAnimationCancel(Animator animation) {
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if ((height = getHeight()) != 0) {
            width = getWidth();
            pageA.getLayoutParams().height = height;
            pageB.getLayoutParams().height = height;
            if (holderA != null) {
                holderA.itemView.getLayoutParams().width = width;
                holderB.itemView.getLayoutParams().width = width;
            }
        }
    }

    private enum Position {
        DOWN,
        UP
    }

    /**
     * 自动装填到相应的位置
     *
     * @param position
     */
    private void fill(Position position) {
        View pageFree = getPageFree();
        if (pageFree.getParent() != null) return;
        LayoutParams params = (LayoutParams) pageFree.getLayoutParams();
        @Px int marginTop = position == Position.DOWN ? 0 : -height;
        params.topMargin = marginTop;
        attachViewToParent(pageFree, position == Position.DOWN ? -1 : 0, params);
        invalidate();
        requestLayout();
    }

    /**
     * 移除上一次装填view，重置layoutParams等参数；
     * 在新装填的视图完全显示在parent里面之后调用；
     * 本例中，在onAnimationEnd中调用
     */
    private void clear() {
        View pageFill = getPageFill();
        View pageFree = getPageFree();
        detachViewFromParent(pageFill);//移除pre fill page
        LayoutParams params = (LayoutParams) pageFree.getLayoutParams();
        params.topMargin = 0;
        pageFree.setLayoutParams(params);
        //交换free、fill的指针
        invalidate();
        requestLayout();
        setPageFill(pageFree);
        setPageFree(pageFill);
    }

    /**
     * 包装LayoutParams，便于ObjectAnimator调用，进而完成移动动画
     */
    private class LayoutParamsWarpper {
        private View view;
        private LayoutParams params;

        LayoutParamsWarpper(View view) {
            this.view = view;
            params = (LayoutParams) view.getLayoutParams();
        }

        public void setMarginTop(int marginTop) {
            params.topMargin = marginTop;
            view.setLayoutParams(params);
        }
    }


    public abstract static class ViewHolder {
        public final View itemView;

        public ViewHolder(View itemView) {
            if (itemView == null) {
                throw new IllegalArgumentException("itemView may not be null");
            } else {
                this.itemView = itemView;
            }
        }
    }
}
