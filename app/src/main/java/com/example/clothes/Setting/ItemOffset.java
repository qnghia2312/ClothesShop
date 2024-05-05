package com.example.clothes.Setting;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ItemOffset extends RecyclerView.ItemDecoration {
    private int mItemOffset;
    public ItemOffset(int itemOffset) {
        mItemOffset = itemOffset;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
    }
}
