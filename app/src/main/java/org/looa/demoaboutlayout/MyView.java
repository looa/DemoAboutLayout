package org.looa.demoaboutlayout;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Px;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liaoinstan.springview.widget.SpringView;

/**
 * Created by ranxiangwei on 2017/2/21.
 */

public class MyView extends LinearLayout {
    private Context context;
    private TextView tv1, tv2;

    private View pageA;
    private View pageB;

    private SpringView springViewA;
    private SpringView springViewB;

    private View pageFree;//游离态页面（游离态页面在装填结束前都是游离态）
    private View pageFill;//已装填页面（已装填页面的marginTop一直处于0的状态）

    private int height;//myView 的高度

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
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

        pageFill = pageA;
        pageFree = pageB;

        springViewA = (SpringView) pageA.findViewById(R.id.sv_info);
        springViewB = (SpringView) pageB.findViewById(R.id.sv_info);

        tv1 = (TextView) pageA.findViewById(R.id.tv_content);
        tv1.setText("PAGE W");
        tv1.setTextColor(Color.GRAY);
        tv1.setBackgroundColor(Color.WHITE);
        tv1.setGravity(Gravity.CENTER);

        tv2 = (TextView) pageB.findViewById(R.id.tv_content);
        tv2.setText("PAGE B");
        tv2.setTextColor(Color.WHITE);
        tv2.setBackgroundColor(Color.parseColor("#444444"));
        tv2.setGravity(Gravity.CENTER);
    }

    public void hide(int position) {
        View v = position == 0 ? pageA : pageB;
        if (v.getParent() == null) return;
        detachViewFromParent(v);
        invalidate();
        requestLayout();
    }

    public void show(int position) {
        View v = position == 0 ? pageA : pageB;
        if (v.getParent() != null) return;
        LayoutParams params = (LayoutParams) v.getLayoutParams();
        params.topMargin = -height;
        attachViewToParent(v, 0, v.getLayoutParams());
        invalidate();
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if ((height = getHeight()) != 0) {
            pageA.getLayoutParams().height = height;
            pageB.getLayoutParams().height = height;
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
        if (pageFree.getParent() != null) return;
        LayoutParams params = (LayoutParams) pageFree.getLayoutParams();
        @Px int marginTop = position == Position.DOWN ? 0 : -height;
        params.topMargin = marginTop;
        attachViewToParent(pageFree, position == Position.DOWN ? -1 : 0, pageFree.getLayoutParams());
        invalidate();
        requestLayout();
    }

    /**
     * 移除上一次装填view，重置layoutParams等参数；
     * 在新装填的视图完全显示在parent里面之后调用；
     * 本例中，在onAnimationEnd中调用
     */
    private void clear() {
        detachViewFromParent(pageFill);//移除pre fill page
        LayoutParams params = (LayoutParams) pageFree.getLayoutParams();
        params.topMargin = 0;
        //交换free、fill的指针
        invalidate();
        requestLayout();
        View temp = pageFill;
        pageFill = pageFree;
        pageFree = temp;
    }

    /**
     * 包装LayoutParams，便于ObjectAnimator调用，进而完成移动动画
     */
    private class LayoutParamsWarpper {
        private LayoutParams params;

        LayoutParamsWarpper(LayoutParams params) {
            this.params = params;
        }

        public void setMarginTop(int marginTop) {
            params.topMargin = marginTop;
            pageFill.setLayoutParams(params);
        }
    }
}
