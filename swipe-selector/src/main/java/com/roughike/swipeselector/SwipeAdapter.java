package com.roughike.swipeselector;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
class SwipeAdapter extends PagerAdapter implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private static final String TAG_CIRCLE = "TAG_CIRCLE";

    // For the left and right buttons when they're not visible
    private static final String TAG_HIDDEN = "TAG_HIDDEN";

    private final Context mContext;

    private final ViewPager mViewPager;
    private final ViewGroup mIndicatorContainer;

    private final LinearLayout.LayoutParams mCircleParams;
    private final ShapeDrawable mInActiveCircleDrawable;
    private final ShapeDrawable mActiveCircleDrawable;

    private Typeface mCustomTypeFace;
    private final int mTitleTextAppearance;
    private final int mDescriptionTextAppearance;
    private final int mDescriptionGravity;

    private final ImageView mLeftButton;
    private final ImageView mRightButton;

    private final int mSweetSixteen;
    private final int mContentLeftPadding;
    private final int mContentRightPadding;

    private OnSwipeItemSelectedListener mOnItemSelectedListener;
    private List<SwipeItem> mItems;
    private int mCurrentPosition;

    private SwipeAdapter(ViewPager viewPager, ViewGroup indicatorContainer, int indicatorSize, int indicatorMargin,
                         int inActiveIndicatorColor, int activeIndicatorColor, int leftButtonResource, int rightButtonResource,
                         ImageView leftButton, ImageView rightButton, String customFontPath, int titleTextAppearance, int descriptionTextAppearance,
                         int descriptionGravity) {
        mContext = viewPager.getContext();

        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);

        mIndicatorContainer = indicatorContainer;
        mCircleParams = new LinearLayout.LayoutParams(indicatorSize, indicatorSize);
        mCircleParams.leftMargin = indicatorMargin;

        mInActiveCircleDrawable = Indicator.newOne(
                indicatorSize, inActiveIndicatorColor);
        mActiveCircleDrawable = Indicator.newOne(
                indicatorSize, activeIndicatorColor);

        if (customFontPath != null && !customFontPath.isEmpty()) {
            mCustomTypeFace = Typeface.createFromAsset(mContext.getAssets(),
                    customFontPath);
        }

        mTitleTextAppearance = titleTextAppearance;
        mDescriptionTextAppearance = descriptionTextAppearance;
        mDescriptionGravity = getGravity(descriptionGravity);

        mLeftButton = leftButton;
        mLeftButton.setImageResource(leftButtonResource);

        mRightButton = rightButton;
        mRightButton.setImageResource(rightButtonResource);

        // Calculate paddings for the content so the left and right buttons
        // don't overlap.
        mSweetSixteen = (int) PixelUtils.dpToPixel(mContext, 16);
        mContentLeftPadding = ContextCompat.getDrawable(mContext, leftButtonResource)
                .getIntrinsicWidth() + mSweetSixteen;
        mContentRightPadding = ContextCompat.getDrawable(mContext, rightButtonResource)
                .getIntrinsicWidth() + mSweetSixteen;

        mLeftButton.setOnClickListener(this);
        mRightButton.setOnClickListener(this);

        mLeftButton.setTag(TAG_HIDDEN);
        mLeftButton.setClickable(false);
        mLeftButton.setAlpha(0.0f);
    }

    /**
     * Using the Java Builder Pattern here, because the SwipeSelector class was getting
     * messy and that's where most will look at. This class is protected, and contains no
     * methods that the users can use, so it's OK for this to look like absolute vomit.
     *
     * At least that's my opinion. But my opinions are always right.
     */
    protected static class Builder {
        private ViewPager viewPager;
        private ViewGroup indicatorContainer;

        private int indicatorSize;
        private int indicatorMargin;
        private int inActiveIndicatorColor;
        private int activeIndicatorColor;

        private int leftButtonResource;
        private int rightButtonResource;

        private ImageView leftButton;
        private ImageView rightButton;

        private String customFontPath;
        private int titleTextAppearance;
        private int descriptionTextAppearance;
        private int descriptionGravity;

        protected Builder(){}

        protected Builder viewPager(ViewPager viewPager) {
            this.viewPager = viewPager;
            return this;
        }

        protected Builder indicatorContainer(ViewGroup indicatorContainer) {
            this.indicatorContainer = indicatorContainer;
            return this;
        }

        protected Builder indicatorSize(int indicatorSize) {
            this.indicatorSize = indicatorSize;
            return this;
        }

        protected Builder indicatorMargin(int indicatorMargin) {
            this.indicatorMargin = indicatorMargin;
            return this;
        }

        protected Builder inActiveIndicatorColor(int inActiveIndicatorColor) {
            this.inActiveIndicatorColor = inActiveIndicatorColor;
            return this;
        }

        protected Builder activeIndicatorColor(int activeIndicatorColor) {
            this.activeIndicatorColor = activeIndicatorColor;
            return this;
        }

        protected Builder leftButtonResource(int leftButtonResource) {
            this.leftButtonResource = leftButtonResource;
            return this;
        }

        protected Builder rightButtonResource(int rightButtonResource) {
            this.rightButtonResource = rightButtonResource;
            return this;
        }

        protected Builder leftButton(ImageView leftButton) {
            this.leftButton = leftButton;
            return this;
        }

        protected Builder rightButton(ImageView rightButton) {
            this.rightButton = rightButton;
            return this;
        }

        protected Builder customFontPath(String customFontPath) {
            this.customFontPath = customFontPath;
            return this;
        }

        protected Builder titleTextAppearance(int titleTextAppearance) {
            this.titleTextAppearance = titleTextAppearance;
            return this;
        }

        protected Builder descriptionTextAppearance(int descriptionTextAppearance) {
            this.descriptionTextAppearance = descriptionTextAppearance;
            return this;
        }

        protected Builder descriptionGravity(int descriptionGravity) {
            this.descriptionGravity = descriptionGravity;
            return this;
        }

        protected SwipeAdapter build() {
            return new SwipeAdapter(viewPager,
                    indicatorContainer,
                    indicatorSize,
                    indicatorMargin,
                    inActiveIndicatorColor,
                    activeIndicatorColor,
                    leftButtonResource,
                    rightButtonResource,
                    leftButton,
                    rightButton,
                    customFontPath,
                    titleTextAppearance,
                    descriptionTextAppearance,
                    descriptionGravity);
        }
    }

    /**
     * Protected methods used by SwipeSelector
     */
    protected void setOnItemSelectedListener(OnSwipeItemSelectedListener listener) {
        mOnItemSelectedListener = listener;
    }

    protected void setItems(SwipeItem... items) {
        // If there are SwipeItems constructed using String resources
        // instead of Strings, loop through all of them and get the
        // Strings.
        if (SwipeItem.checkForStringResources) {
            ArrayList<SwipeItem> theRealOnes = new ArrayList<>();

            for (SwipeItem item : items) {
                if (item.titleRes != -1) {
                    item.title = mContext.getString(item.titleRes);
                }

                if (item.descriptionRes != -1) {
                    item.description = mContext.getString(item.descriptionRes);
                }

                theRealOnes.add(item);
            }

            mItems = theRealOnes;

            // reset
            SwipeItem.checkForStringResources = false;
        } else {
            mItems = new ArrayList<>(Arrays.asList(items));
        }

        mCurrentPosition = 0;
        setActiveIndicator(0);
        notifyDataSetChanged();
    }
    protected void setItems(List<SwipeItem> items) {
        mItems = items;
        mCurrentPosition = 0;
        setActiveIndicator(0);
        notifyDataSetChanged();
    }

    protected SwipeItem getSelectedItem() {
        return mItems.get(mCurrentPosition);
    }

    /**
     * Override methods / listeners
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LinearLayout layout = (LinearLayout) View.inflate(mContext, R.layout.swipeselector_content_item, null);
        TextView title = (TextView) layout.findViewById(R.id.swipeselector_content_title);
        TextView description = (TextView) layout.findViewById(R.id.swipeselector_content_description);

        SwipeItem slideItem = mItems.get(position);
        title.setText(slideItem.title);
        description.setText(slideItem.description);

        // We shouldn't get here if the typeface didn't exist.
        // But just in case, because we're paranoid.
        if (mCustomTypeFace != null) {
            title.setTypeface(mCustomTypeFace);
            description.setTypeface(mCustomTypeFace);
        }

        if (mTitleTextAppearance != -1) {
            setTextAppearanceCompat(title, mTitleTextAppearance);
        }

        if (mDescriptionTextAppearance != -1) {
            setTextAppearanceCompat(description, mDescriptionTextAppearance);
        }

        if (mDescriptionGravity != -1) {
            description.setGravity(mDescriptionGravity);
        }

        layout.setPadding(mContentLeftPadding,
                mSweetSixteen,
                mContentRightPadding,
                mSweetSixteen);

        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mItems != null? mItems.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void onPageSelected(int position) {
        if (getCount() == 0) return;
        setActiveIndicator(position);

        handleLeftButtonVisibility(position);
        handleRightButtonVisibility(position);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mLeftButton) && mCurrentPosition >= 1) {
            mViewPager.setCurrentItem(mCurrentPosition - 1, true);
        } else if (v.equals(mRightButton) && mCurrentPosition <= getCount() - 1) {
            mViewPager.setCurrentItem(mCurrentPosition + 1, true);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * Private convenience methods used by this class.
     */
    private void setActiveIndicator(int position) {
        if (mIndicatorContainer.findViewWithTag(TAG_CIRCLE) == null) {
            // No indicators yet, let's make some. Only run once per configuration.
            for (int i = 0; i < getCount(); i++) {
                ImageView indicator = (ImageView) View.inflate(mContext, R.layout.swipeselector_circle_item, null);

                if (i == position) {
                    indicator.setImageDrawable(mActiveCircleDrawable);
                } else {
                    indicator.setImageDrawable(mInActiveCircleDrawable);
                }

                indicator.setLayoutParams(mCircleParams);
                indicator.setTag(TAG_CIRCLE);
                mIndicatorContainer.addView(indicator);
            }
            return;
        }

        ImageView previousActiveIndicator = (ImageView) mIndicatorContainer.getChildAt(mCurrentPosition);
        ImageView nextActiveIndicator = (ImageView) mIndicatorContainer.getChildAt(position);

        previousActiveIndicator.setImageDrawable(mInActiveCircleDrawable);
        nextActiveIndicator.setImageDrawable(mActiveCircleDrawable);

        mCurrentPosition = position;

        if (mOnItemSelectedListener != null) {
            mOnItemSelectedListener.onItemSelected(getSelectedItem());
        }
    }

    @SuppressWarnings("deprecation")
    private void setTextAppearanceCompat(TextView textView, int appearanceRes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(appearanceRes);
        } else {
            textView.setTextAppearance(textView.getContext(), appearanceRes);
        }
    }

    private int getGravity(int gravity) {
        if (gravity == -1)
            return -1;

        int realGravityValue;

        switch (gravity) {
            case 0 :
                realGravityValue = Gravity.START;
                break;
            case 1 :
                realGravityValue = Gravity.CENTER_HORIZONTAL;
                break;
            case 2 :
                realGravityValue = Gravity.END;
                break;
            default :
                throw new IllegalArgumentException("Invalid value " +
                        "specified for swipe_descriptionGravity. " +
                        "Use \"left\", \"center\", \"right\" or leave " +
                        "blank for default.");
        }

        return realGravityValue;
    }

    private void handleLeftButtonVisibility(int position) {
        if (position < 1) {
            mLeftButton.setTag(TAG_HIDDEN);
            mLeftButton.setClickable(false);
            mLeftButton.animate()
                    .alpha(0.0f)
                    .setDuration(120)
                    .start();
        } else if (TAG_HIDDEN.equals(mLeftButton.getTag())) {
            mLeftButton.setTag(null);
            mLeftButton.setClickable(true);
            mLeftButton.animate()
                    .alpha(1.0f)
                    .setDuration(120)
                    .start();
        }
    }

    private void handleRightButtonVisibility(int position) {
        if (position == getCount() - 1) {
            mRightButton.setTag(TAG_HIDDEN);
            mRightButton.setClickable(false);
            mRightButton.animate()
                    .alpha(0.0f)
                    .setDuration(120)
                    .start();
        } else if (TAG_HIDDEN.equals(mRightButton.getTag())) {
            mRightButton.setTag(null);
            mRightButton.setClickable(true);
            mRightButton.animate()
                    .alpha(1.0f)
                    .setDuration(120)
                    .start();
        }
    }
}
