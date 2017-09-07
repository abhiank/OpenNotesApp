package com.abhiank.opennotes.customview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

//From http://stackoverflow.com/a/27037230/3090120
public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;
    private boolean isBottomToTop;

    public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    public VerticalSpaceItemDecoration(int verticalSpaceHeight, boolean isLaidBottomToTop) {
        this.verticalSpaceHeight = verticalSpaceHeight;
        this.isBottomToTop = isLaidBottomToTop;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            if (isBottomToTop)
                outRect.top = verticalSpaceHeight;
            else
                outRect.bottom = verticalSpaceHeight;
        }
    }
}