package org.looa.demoaboutlayout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by ranxiangwei on 2017/2/21.
 */

public class MyView extends LinearLayout {
    private Context context;
    private TextView tv1, tv2;
    private LayoutParams params1, params2;

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

        tv1 = new TextView(context);
        tv1.setText("PAGE W");
        tv1.setTextColor(Color.GRAY);
        tv1.setBackgroundColor(Color.WHITE);
        tv1.setGravity(Gravity.CENTER);

        tv2 = new TextView(context);
        tv2.setText("PAGE B");
        tv2.setTextColor(Color.WHITE);
        tv2.setBackgroundColor(Color.parseColor("#444444"));
        tv2.setGravity(Gravity.CENTER);

        params1 = new LayoutParams(LayoutParams.MATCH_PARENT, 200);
        params2 = new LayoutParams(LayoutParams.MATCH_PARENT, 200);

        addView(tv1, params1);
        addView(tv2, params2);
    }
}
