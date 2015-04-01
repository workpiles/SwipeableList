package com.example.swipeablelist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class SwipeableItemAdaper extends RecyclerView.Adapter<SwipeableItemViewHolder> {
    private ArrayList<ItemData> mDataset;

    public SwipeableItemAdaper(List<ItemData> dataset) {
        mDataset = new ArrayList<ItemData>();
        mDataset.addAll(dataset);
    }

    @Override
    public SwipeableItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, null, false);
        return new SwipeableItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SwipeableItemViewHolder viewHolder, int i){
        viewHolder.bindData(mDataset.get(i));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void remove(int pos) {
        mDataset.remove(pos);
        notifyItemRemoved(pos);
    }

    public void add(int pos, ItemData data) {
        mDataset.add(pos, data);
        notifyItemInserted(pos);

    }

}
