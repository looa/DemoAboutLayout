package org.looa.demoaboutlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * ahh
 * Created by ranxiangwei on 2017/2/22.
 */

public class TestAdapter extends StickyPageBaseAdapter {

    private Context context;

    public TestAdapter(Context context) {
        this.context = context;
    }

    @Override
    public StickyPageView.ViewHolder onCreateView(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_content, null);
        return new Holder(view);
    }

    @Override
    public void onChangePosition(StickyPageView.ViewHolder viewHolder, ViewGroup parent, int position) {
        if (viewHolder instanceof Holder) {
            ((Holder) viewHolder).getTextView().setText("Position -> " + position);
        }
    }

    @Override
    protected int getCount() {
        return 5;
    }

    private class Holder extends StickyPageView.ViewHolder {

        private TextView textView;

        public Holder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_sample);
        }

        public TextView getTextView() {
            return textView;
        }
    }
}
