package com.roughike.swipeselector;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.iroughapps.swipeselector.R;

/*
 * SwipeSelector library for Android
 * Copyright (c) 2016 Iiro Krankka (http://github.com/roughike).
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
public class SwipeSelector extends FrameLayout {
    private static final int DEFAULT_INDICATOR_SIZE = 6;
    private static final int DEFAULT_INDICATOR_MARGIN = 8;

    private ViewPager mPager;
    private SwipeAdapter mAdapter;

    private int mIndicatorSize;
    private int mIndicatorMargin;
    private int mIndicatorInActiveColor;
    private int mIndicatorActiveColor;

    public SwipeSelector(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public SwipeSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public SwipeSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwipeSelector(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SwipeSelector, defStyleAttr, defStyleRes);

        try {
            mIndicatorSize = (int) ta.getDimension(R.styleable.SwipeSelector_swipe_indicatorSize,
                    PixelUtils.dpToPixel(context, DEFAULT_INDICATOR_SIZE));
            mIndicatorMargin = (int) ta.getDimension(R.styleable.SwipeSelector_swipe_indicatorMargin,
                    PixelUtils.dpToPixel(context, DEFAULT_INDICATOR_MARGIN));
            mIndicatorInActiveColor = ta.getColor(R.styleable.SwipeSelector_swipe_indicatorInActiveColor,
                    ContextCompat.getColor(context, R.color.swipeselector_color_indicator_inactive));
            mIndicatorActiveColor = ta.getColor(R.styleable.SwipeSelector_swipe_indicatorActiveColor,
                    ContextCompat.getColor(context, R.color.swipeselector_color_indicator_active));
        } finally {
            ta.recycle();
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.swipeselector_layout, this);

        mPager = (ViewPager) findViewById(R.id.swipeselector_layout_swipePager);
        mAdapter = new SwipeAdapter(mPager,
                (ViewGroup) findViewById(R.id.swipeselector_layout_circleContainer),
                mIndicatorSize,
                mIndicatorMargin,
                mIndicatorInActiveColor,
                mIndicatorActiveColor,
                findViewById(R.id.swipeselector_layout_leftButton),
                findViewById(R.id.swipeselector_layout_rightButton));
        mPager.setAdapter(mAdapter);
    }

    /**
     * Set a listener to be fired every time a different item is chosen.
     * @param listener the listener that gets fired on item selection
     */
    public void setOnItemSelectedListener(OnSwipeItemSelectedListener listener) {
        mAdapter.setOnItemSelectedListener(listener);
    }

    /**
     * A method for giving this SwipeSelector something to show.
     *
     * @param swipeItems an array of {@link SwipeItem} to show
     * inside this view.
     */
    public void setItems(SwipeItem... swipeItems) {
        mAdapter.setItems(swipeItems);
    }

    /**
     * @return the selected slides' SwipeItem.
     */
    public SwipeItem getSelectedItem() {
        if (mAdapter.getCount() == 0) {
            throw new UnsupportedOperationException("The SwipeSelector " +
                    "doesn't have any items! Use the setItems() method " +
                    "for setting the items before calling getSelectedItem().");
        }

        return mAdapter.getSelectedItem();
    }
}
