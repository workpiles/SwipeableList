package com.example.swipeablelist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;

public class SwipeableItemViewHolder extends RecyclerView.ViewHolder {
    public static final int LEFT_OFF = 0;
    public static final int LEFT_ON = 1;
    public static final int RIGHT_OFF = 2;
    public static final int RIGHT_ON = 3;

    private View mContentView;
    private TextView mContentText;
    private ArrayList<View> mBackgrounds = new ArrayList<>();

    public SwipeableItemViewHolder(View itemView) {
        super(itemView);
        mContentView = itemView.findViewById(R.id.itemContent);
        mContentText = (TextView)itemView.findViewById(R.id.itemContentText);
        mBackgrounds.add(LEFT_OFF, itemView.findViewById(R.id.itemLeftOff));
        mBackgrounds.add(LEFT_ON, itemView.findViewById(R.id.itemLeftOn));
        mBackgrounds.add(RIGHT_OFF, itemView.findViewById(R.id.itemRightOff));
        mBackgrounds.add(RIGHT_ON, itemView.findViewById(R.id.itemRightOn));
        for (View v : mBackgrounds) v.setVisibility(View.INVISIBLE);

    }

    public View getContentView() {
        return mContentView;
    }

    public void setBackgroundView(int pos) {
        for (int i=0;i<mBackgrounds.size();i++) {
            if (pos == i) {
                mBackgrounds.get(i).setVisibility(View.VISIBLE);
            } else {
                mBackgrounds.get(i).setVisibility(View.INVISIBLE);
            }
        }
    }

    public void bindData(ItemData data) {
        mContentText.setText(data.getText());
    }
}
