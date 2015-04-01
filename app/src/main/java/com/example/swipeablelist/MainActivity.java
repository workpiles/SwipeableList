package com.example.swipeablelist;

import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private RecyclerView mRecyclerView;
    private SwipeableItemAdaper mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SoundPool mSoundPool;
    private int mSoundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<ItemData> data = new ArrayList<ItemData>();
        for (int i=0;i<50;i++) {
            data.add(new ItemData("Card No."+i));
        }

        mAdapter = new SwipeableItemAdaper(data);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnTouchListener(new SwipeableItemTouchListener(mRecyclerView,
                new SwipeableItemTouchListener.Callbacks() {
                @Override
                public void onLeftSw(int viewPos) {
                    Log.d("test", "onLeft:" + viewPos);
                    mAdapter.remove(viewPos);
                }

                @Override
                public void onRightSw(int viewPos) {
                    Log.d("test", "onRight:" + viewPos);
                    mAdapter.add(viewPos + 1, new ItemData("追加"));
                }

                @Override
                public void onPreLeftSwOn(int viewPos) {
                    mSoundPool.play(mSoundId, 1.0f, 1.0f, 0, 0, 1.0f);

                }

                @Override
                public void onPreRightSwOn(int viewPos) {
                    mSoundPool.play(mSoundId, 1.0f, 1.0f, 0, 0, 1.0f);

                }
            }));

    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<String> mData;
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mView;
            public ViewHolder(View itemView) {
                super(itemView);
                mView = (TextView)itemView;
//                mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(v.getContext(), "pos:" + getAdapterPosition(), Toast.LENGTH_LONG).show();
//                        int pos = getAdapterPosition();
//                        mData.remove(pos);
//                        notifyItemRemoved(pos);
//                    }
//                });
            }

        }

        public MyAdapter(String[] data) {
            mData = new ArrayList<String>();
            for (int i=0;i<data.length;i++) mData.add(data[i]);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            TextView v = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.text, null,false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.mView.setText(mData.get(i));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSoundId = mSoundPool.load(this, R.raw.pico, 1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSoundPool.release();
    }
}
