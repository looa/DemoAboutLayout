package org.looa.demoaboutlayout;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

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
    public StickyPageView.ViewHolder onCreateView(StickyPageView parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.sticky_content_recycler, null);
        return new Holder(view);
    }

    @Override
    public void onChangePosition(StickyPageView.ViewHolder viewHolder, int position, boolean isNext, boolean isMove) {
        if (viewHolder instanceof Holder) {
            ((Holder) viewHolder).setData(position);
            ((RecyclerView) viewHolder.itemView).scrollToPosition(isNext ? 0 : ((Holder) viewHolder).data.size() - 1);
        }
    }

    @Override
    protected int getCount() {
        return 5;
    }

    private class Holder extends StickyPageView.ViewHolder {

        private RecyclerView.Adapter adapter;
        private List<String> data;

        public Holder(View itemView) {
            super(itemView);
            if (itemView instanceof RecyclerView) {
                data = new ArrayList<>();
                for (int i = 'A'; i < 'z'; i++) {
                    data.add(" Char " + (char) i);
                }
                ((RecyclerView) itemView).setLayoutManager(new LinearLayoutManager(context));
                ((RecyclerView) itemView).setAdapter(adapter = new MyRecyclerAdapter(context, data));
            }
        }

        public void setData(int position) {
            data.clear();
            for (int i = 'A'; i < 'z'; i++) {
                data.add("Position " + position + " Char " + (char) i);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
