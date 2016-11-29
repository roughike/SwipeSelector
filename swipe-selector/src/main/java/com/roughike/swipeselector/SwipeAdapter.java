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

package com.roughike.swipeselector;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

class SwipeAdapter extends PagerAdapter implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private static final String STATE_CURRENT_POSITION = "STATE_CURRENT_POSITION";
    private static final String TAG_CIRCLE = "TAG_CIRCLE";

    // For the left and right buttons when they're not visible
    private static final String TAG_HIDDEN = "TAG_HIDDEN";

    private final Context context;

    private final ViewPager viewPager;
    private final ViewGroup indicatorContainer;

    private final LinearLayout.LayoutParams circleParams;
    private final ShapeDrawable inActiveCircleDrawable;
    private final ShapeDrawable activeCircleDrawable;

    private Typeface customTypeFace;
    private final int titleTextAppearance;
    private final int descriptionTextAppearance;
    private final int descriptionGravity;

    private final ImageView leftButton;
    private final ImageView rightButton;

    private final int sixteenDp;
    private final int contentLeftPadding;
    private final int contentRightPadding;

    private OnSwipeItemSelectedListener onItemSelectedListener;
    private List<SwipeItem> items;
    private int currentPosition;

    private SwipeAdapter(Builder builder) {
        context = builder.viewPager.getContext();

        viewPager = builder.viewPager;
        viewPager.addOnPageChangeListener(this);

        indicatorContainer = builder.indicatorContainer;
        circleParams = new LinearLayout.LayoutParams(builder.indicatorSize, builder.indicatorSize);
        circleParams.leftMargin = builder.indicatorMargin;

        inActiveCircleDrawable = Indicator.newOne(
                builder.indicatorSize, builder.inActiveIndicatorColor);
        activeCircleDrawable = Indicator.newOne(
                builder.indicatorSize, builder.activeIndicatorColor);

        if (builder.customFontPath != null &&
                ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && !builder.customFontPath.isEmpty())
                        || builder.customFontPath.length() > 0)) {
            customTypeFace = Typeface.createFromAsset(context.getAssets(),
                    builder.customFontPath);
        }

        titleTextAppearance = builder.titleTextAppearance;
        descriptionTextAppearance = builder.descriptionTextAppearance;
        descriptionGravity = getGravity(builder.descriptionGravity);

        leftButton = builder.leftButton;
        leftButton.setImageResource(builder.leftButtonResource);

        rightButton = builder.rightButton;
        rightButton.setImageResource(builder.rightButtonResource);

        // Calculate paddings for the content so the left and right buttons
        // don't overlap.
        sixteenDp = (int) PixelUtils.dpToPixel(context, 16);
        contentLeftPadding = ContextCompat.getDrawable(context, builder.leftButtonResource)
                .getIntrinsicWidth() + sixteenDp;
        contentRightPadding = ContextCompat.getDrawable(context, builder.rightButtonResource)
                .getIntrinsicWidth() + sixteenDp;

        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);

        leftButton.setTag(TAG_HIDDEN);
        leftButton.setClickable(false);

        setAlpha(0.0f, leftButton);
    }

    /**
     * Protected methods used by SwipeSelector
     */
    void setOnItemSelectedListener(OnSwipeItemSelectedListener listener) {
        onItemSelectedListener = listener;
    }

    void setItems(List<SwipeItem> items) {
        // If there are SwipeItems constructed using String resources
        // instead of Strings, loop through all of them and get the
        // Strings.
        this.items = items;
        currentPosition = 0;
        setActiveIndicator(0);
        notifyDataSetChanged();
    }

    SwipeItem getSelectedItem() {
        return items.get(currentPosition);
    }

    void selectItemAt(int position, boolean animate) {
        if (position < 0 || position >= items.size()) {
            throw new IndexOutOfBoundsException("This SwipeSelector does " +
                    "not have an item at position " + position + ".");
        }

        viewPager.setCurrentItem(position, animate);
    }

    void selectItemWithValue(@NonNull String value, boolean animate) {
        boolean itemExists = false;

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getValue().equals(value)) {
                viewPager.setCurrentItem(i, animate);
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            throw new IllegalArgumentException("This SwipeSelector " +
                    "does not have an item with the given value " + value + ".");
        }
    }

    Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putInt(STATE_CURRENT_POSITION, currentPosition);
        return bundle;
    }

    void onRestoreInstanceState(Bundle state) {
        viewPager.setCurrentItem(state.getInt(STATE_CURRENT_POSITION), false);
        notifyDataSetChanged();
    }

    /**
     * Override methods / listeners
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LinearLayout layout = (LinearLayout) View.inflate(context, R.layout.swipeselector_content_item, null);
        TextView title = (TextView) layout.findViewById(R.id.swipeselector_content_title);
        TextView description = (TextView) layout.findViewById(R.id.swipeselector_content_description);

        SwipeItem slideItem = items.get(position);
        title.setText(slideItem.getTitle());

        if (slideItem.getDescription() == null) {
            description.setVisibility(View.GONE);
        } else {
            description.setVisibility(View.VISIBLE);
            description.setText(slideItem.getDescription());
        }

        // We shouldn't get here if the typeface didn't exist.
        // But just in case, because we're paranoid.
        if (customTypeFace != null) {
            title.setTypeface(customTypeFace);
            description.setTypeface(customTypeFace);
        }

        if (titleTextAppearance != -1) {
            setTextAppearanceCompat(title, titleTextAppearance);
        }

        if (descriptionTextAppearance != -1) {
            setTextAppearanceCompat(description, descriptionTextAppearance);
        }

        if (descriptionGravity != -1) {
            description.setGravity(descriptionGravity);
        }

        layout.setPadding(contentLeftPadding,
                sixteenDp,
                contentRightPadding,
                sixteenDp);

        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return items != null ? items.size() : 0;
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
        if (v.equals(leftButton) && currentPosition >= 1) {
            viewPager.setCurrentItem(currentPosition - 1, true);
        } else if (v.equals(rightButton) && currentPosition <= getCount() - 1) {
            viewPager.setCurrentItem(currentPosition + 1, true);
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
        if (indicatorContainer.findViewWithTag(TAG_CIRCLE) == null) {
            // No indicators yet, let's make some. Only run once per configuration.
            for (int i = 0; i < getCount(); i++) {
                ImageView indicator = (ImageView) View.inflate(context, R.layout.swipeselector_circle_item, null);

                if (i == position) {
                    indicator.setImageDrawable(activeCircleDrawable);
                } else {
                    indicator.setImageDrawable(inActiveCircleDrawable);
                }

                indicator.setLayoutParams(circleParams);
                indicator.setTag(TAG_CIRCLE);
                indicatorContainer.addView(indicator);
            }
            return;
        }

        ImageView previousActiveIndicator = (ImageView) indicatorContainer.getChildAt(currentPosition);
        ImageView nextActiveIndicator = (ImageView) indicatorContainer.getChildAt(position);

        previousActiveIndicator.setImageDrawable(inActiveCircleDrawable);
        nextActiveIndicator.setImageDrawable(activeCircleDrawable);

        currentPosition = position;

        if (onItemSelectedListener != null) {
            onItemSelectedListener.onItemSelected(getSelectedItem());
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
            case 0:
                realGravityValue = Gravity.START;
                break;
            case 1:
                realGravityValue = Gravity.CENTER_HORIZONTAL;
                break;
            case 2:
                realGravityValue = Gravity.END;
                break;
            default:
                throw new IllegalArgumentException("Invalid value " +
                        "specified for swipe_descriptionGravity. " +
                        "Use \"left\", \"center\", \"right\" or leave " +
                        "blank for default.");
        }

        return realGravityValue;
    }

    private void handleLeftButtonVisibility(int position) {
        if (position < 1) {
            leftButton.setTag(TAG_HIDDEN);
            leftButton.setClickable(false);
            animate(0, leftButton);
        } else if (TAG_HIDDEN.equals(leftButton.getTag())) {
            leftButton.setTag(null);
            leftButton.setClickable(true);
            animate(1, leftButton);
        }
    }

    private void handleRightButtonVisibility(int position) {
        if (position == getCount() - 1) {
            rightButton.setTag(TAG_HIDDEN);
            rightButton.setClickable(false);
            animate(0, rightButton);
        } else if (TAG_HIDDEN.equals(rightButton.getTag())) {
            rightButton.setTag(null);
            rightButton.setClickable(true);
            animate(1, rightButton);
        }
    }

    private void animate(float alpha, ImageView button) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            button.animate()
                    .alpha(alpha)
                    .setDuration(120)
                    .start();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            ObjectAnimator.ofFloat(button, "alpha",
                    alpha == 1 ? 0 : alpha, alpha == 1 ? alpha : 0)
                    .setDuration(120)
                    .start();
        } else {
            setAlpha(alpha, button);
        }
    }

    @SuppressWarnings("deprecation")
    private void setAlpha(float alpha, ImageView button) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            button.setAlpha(alpha);
        } else {
            button.setAlpha((int) (alpha * 255));
        }
    }

    static class Builder {
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

        Builder() {
        }

        Builder viewPager(ViewPager viewPager) {
            this.viewPager = viewPager;
            return this;
        }

        Builder indicatorContainer(ViewGroup indicatorContainer) {
            this.indicatorContainer = indicatorContainer;
            return this;
        }

        Builder indicatorSize(int indicatorSize) {
            this.indicatorSize = indicatorSize;
            return this;
        }

        Builder indicatorMargin(int indicatorMargin) {
            this.indicatorMargin = indicatorMargin;
            return this;
        }

        Builder inActiveIndicatorColor(int inActiveIndicatorColor) {
            this.inActiveIndicatorColor = inActiveIndicatorColor;
            return this;
        }

        Builder activeIndicatorColor(int activeIndicatorColor) {
            this.activeIndicatorColor = activeIndicatorColor;
            return this;
        }

        Builder leftButtonResource(int leftButtonResource) {
            this.leftButtonResource = leftButtonResource;
            return this;
        }

        Builder rightButtonResource(int rightButtonResource) {
            this.rightButtonResource = rightButtonResource;
            return this;
        }

        Builder leftButton(ImageView leftButton) {
            this.leftButton = leftButton;
            return this;
        }

        Builder rightButton(ImageView rightButton) {
            this.rightButton = rightButton;
            return this;
        }

        Builder customFontPath(String customFontPath) {
            this.customFontPath = customFontPath;
            return this;
        }

        Builder titleTextAppearance(int titleTextAppearance) {
            this.titleTextAppearance = titleTextAppearance;
            return this;
        }

        Builder descriptionTextAppearance(int descriptionTextAppearance) {
            this.descriptionTextAppearance = descriptionTextAppearance;
            return this;
        }

        Builder descriptionGravity(int descriptionGravity) {
            this.descriptionGravity = descriptionGravity;
            return this;
        }

        SwipeAdapter build() {
            return new SwipeAdapter(this);
        }
    }
}
