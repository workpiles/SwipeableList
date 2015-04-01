package com.example.swipeablelist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class SwipeableItemTouchListener implements View.OnTouchListener {
    public static final int LEFT_OFF = 0;
    public static final int LEFT_ON = 1;
    public static final int RIGHT_OFF = 2;
    public static final int RIGHT_ON = 3;
    public static final int NEUTRAL = 4;

    private int mSlop;
    private int mSwipingSlop;
    private boolean mSwiping = false;
    private int mAnimationTime;
    private RecyclerView mRecyclerView;
    private Callbacks mCallbacks;
    private SwipeableItemViewHolder mTouchedView;
    private Point mDownPos = new Point();
    private int mDownViewPos;
    private int mSwitchState = NEUTRAL;
    private int mSwitchThreshold;

    public interface Callbacks {
        void onPreLeftSwOn(int viewPos);
        void onPreRightSwOn(int viewPos);
        void onLeftSw(int viewPos);
        void onRightSw(int viewPos);
    }

    public SwipeableItemTouchListener(RecyclerView view, Callbacks callbacks) {
        mRecyclerView = view;
        mCallbacks = callbacks;
        ViewConfiguration vc = ViewConfiguration.get(view.getContext());
        mSlop = vc.getScaledTouchSlop();
        mAnimationTime = view.getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);
        mSwitchThreshold = (int)view.getContext().getResources().getDimension(R.dimen.swipe_width);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:

                Rect rect = new Rect();
                int[] listViewCoords = new int[2];
                mRecyclerView.getLocationOnScreen(listViewCoords);
                int x = (int)event.getRawX() - listViewCoords[0];
                int y = (int)event.getRawY() - listViewCoords[1];
                View child;
                for (int i=0;i<mRecyclerView.getChildCount();i++) {
                    child = mRecyclerView.getChildAt(i);
                    child.getHitRect(rect);
                    if (rect.contains(x, y)) {
                        mDownViewPos = mRecyclerView.getChildAdapterPosition(child);
                        mTouchedView = (SwipeableItemViewHolder)mRecyclerView.getChildViewHolder(child);
                        mDownPos.set((int)event.getRawX(), (int)event.getRawY());
                        break;
                    }
                }

                break;
            case MotionEvent.ACTION_CANCEL:
                if (mTouchedView != null && mSwiping) {
                    mTouchedView.getContentView().animate()
                            .translationX(0)
                            .setDuration(mAnimationTime)
                            .setListener(null);
                }
                mDownPos.set(0,0);
                mSwiping = false;
                mSwitchState = NEUTRAL;
                mTouchedView = null;
                break;
            case MotionEvent.ACTION_UP:
                if (!mSwiping) break;
                final int downViewPos = mDownViewPos;
                final int switchState = mSwitchState;
                mTouchedView.getContentView().animate()
                        .translationX(0)
                        .setDuration(mAnimationTime)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                if (switchState == LEFT_ON) {
                                    mCallbacks.onLeftSw(downViewPos);
                                } else if (switchState == RIGHT_ON) {
                                    mCallbacks.onRightSw(downViewPos);
                                }
                            }
                        });

                mDownPos.set(0,0);
                mSwiping = false;
                mSwitchState = NEUTRAL;
                mTouchedView = null;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = event.getRawX() - mDownPos.x;
                float deltaY = event.getRawY() - mDownPos.y;
                if (mSwiping) {
                    if (Math.abs(deltaX - mSwipingSlop) > mSwitchThreshold) {
                        int oldState = mSwitchState;
                        mSwitchState = (deltaX > 0 ? LEFT_ON : RIGHT_ON);
                        if (oldState != mSwitchState) {
                            if (mSwitchState == LEFT_ON) {
                                mCallbacks.onPreLeftSwOn(mDownViewPos);
                            } else {
                                mCallbacks.onPreRightSwOn(mDownViewPos);
                            }
                        }
                    } else {
                        mSwitchState = (deltaX > 0 ? LEFT_OFF : RIGHT_OFF);
                        mTouchedView.getContentView().setTranslationX(deltaX - mSwipingSlop);
                    }
                    mTouchedView.setBackgroundView(mSwitchState);
                    return true;

                } else {
                    if (Math.abs(deltaX) > mSlop && Math.abs(deltaY) < Math.abs(deltaX) / 2) {
                        mSwiping = true;
                        mSwipingSlop = (deltaX > 0 ? mSlop : -mSlop);
                        mSwitchState = (deltaX > 0 ? LEFT_OFF : RIGHT_OFF);
                        mRecyclerView.requestDisallowInterceptTouchEvent(true);

                        MotionEvent cancelEvent = MotionEvent.obtain(event);
                        cancelEvent.setAction(MotionEvent.ACTION_CANCEL |
                                (event.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                        mRecyclerView.onTouchEvent(cancelEvent);
                        cancelEvent.recycle();
                    }
                }

                break;
        }
        return false;
    }
}
