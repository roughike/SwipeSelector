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

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SwipeSelector extends FrameLayout {
    private static final int DEFAULT_INDICATOR_SIZE = 6;
    private static final int DEFAULT_INDICATOR_MARGIN = 8;
    private static final String STATE_SELECTOR = "STATE_SELECTOR";

    private SwipeAdapter adapter;
    private ViewPager pager;
    private ViewGroup indicatorContainer;
    private ImageView leftButton;
    private ImageView rightButton;

    private int itemsXmlResource;
    private String unselectedItemTitle;
    private String unselectedItemDescription;

    public SwipeSelector(Context context) {
        super(context);
        init(context, null);
    }

    public SwipeSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initializeViews(context);
        populateAttrsAndInitAdapter(context, attrs);
        populateItems();
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.swipeselector_layout, this);

        pager = (ViewPager) findViewById(R.id.swipeselector_layout_swipePager);
        indicatorContainer = (ViewGroup) findViewById(R.id.swipeselector_layout_circleContainer);
        leftButton = (ImageView) findViewById(R.id.swipeselector_layout_leftButton);
        rightButton = (ImageView) findViewById(R.id.swipeselector_layout_rightButton);
    }

    private void populateAttrsAndInitAdapter(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SwipeSelector, 0, 0);

        try {
            itemsXmlResource = ta.getResourceId(R.styleable.SwipeSelector_swipe_itemsXmlResource, 0);
            unselectedItemTitle = ta.getString(R.styleable.SwipeSelector_swipe_unselectedItemTitle);
            unselectedItemDescription = ta.getString(R.styleable.SwipeSelector_swipe_unselectedItemDescription);

            int indicatorSize = (int) ta.getDimension(R.styleable.SwipeSelector_swipe_indicatorSize,
                    PixelUtils.dpToPixel(context, DEFAULT_INDICATOR_SIZE));
            int indicatorMargin = (int) ta.getDimension(R.styleable.SwipeSelector_swipe_indicatorMargin,
                    PixelUtils.dpToPixel(context, DEFAULT_INDICATOR_MARGIN));
            int indicatorInActiveColor = ta.getColor(R.styleable.SwipeSelector_swipe_indicatorInActiveColor,
                    ContextCompat.getColor(context, R.color.swipeselector_color_indicator_inactive));
            int indicatorActiveColor = ta.getColor(R.styleable.SwipeSelector_swipe_indicatorActiveColor,
                    ContextCompat.getColor(context, R.color.swipeselector_color_indicator_active));

            int leftButtonResource = ta.getResourceId(R.styleable.SwipeSelector_swipe_leftButtonResource,
                    R.drawable.ic_action_navigation_chevron_left);
            int rightButtonResource = ta.getResourceId(R.styleable.SwipeSelector_swipe_rightButtonResource,
                    R.drawable.ic_action_navigation_chevron_right);

            String customFontPath = ta.getString(R.styleable.SwipeSelector_swipe_customFontPath);
            int titleTextAppearance = ta.getResourceId(R.styleable.SwipeSelector_swipe_titleTextAppearance,
                    -1);
            int descriptionTextAppearance = ta.getResourceId(R.styleable.SwipeSelector_swipe_descriptionTextAppearance,
                    -1);
            int descriptionGravity = ta.getInteger(R.styleable.SwipeSelector_swipe_descriptionGravity,
                    -1);

            adapter = new SwipeAdapter.Builder()
                    .viewPager(pager)
                    .indicatorContainer(indicatorContainer)
                    .indicatorSize(indicatorSize)
                    .indicatorMargin(indicatorMargin)
                    .inActiveIndicatorColor(indicatorInActiveColor)
                    .activeIndicatorColor(indicatorActiveColor)
                    .leftButtonResource(leftButtonResource)
                    .rightButtonResource(rightButtonResource)
                    .leftButton(leftButton)
                    .rightButton(rightButton)
                    .customFontPath(customFontPath)
                    .titleTextAppearance(titleTextAppearance)
                    .descriptionTextAppearance(descriptionTextAppearance)
                    .descriptionGravity(descriptionGravity)
                    .build();
            pager.setAdapter(adapter);
        } finally {
            ta.recycle();
        }
    }

    private void populateItems() {
        List<SwipeItem> pendingItems = new ArrayList<>();

        if (unselectedItemTitle != null && unselectedItemDescription != null) {
            SwipeItem item = new SwipeItem(
                    SwipeItem.UNSELECTED_ITEM_VALUE,
                    unselectedItemTitle,
                    unselectedItemDescription
            );

            pendingItems.add(item);
        }

        inflateItemsFromXml(pendingItems, itemsXmlResource);
    }

    private void inflateItemsFromXml(List<SwipeItem> pendingItems, int itemsXmlResource) {
        if (itemsXmlResource != 0) {
            SwipeItemParser parser = new SwipeItemParser(getContext(), itemsXmlResource);
            pendingItems.addAll(parser.parseItems());

            adapter.setItems(pendingItems);
        }
    }

    /**
     * Set a listener to be fired every time a different item is chosen.
     * @param listener the listener that gets fired on item selection
     */
    public void setOnItemSelectedListener(OnSwipeItemSelectedListener listener) {
        adapter.setOnItemSelectedListener(listener);
    }

    /**
     * A method for giving this SwipeSelector something to show.
     *
     * @param swipeItems an array of {@link SwipeItem} to show
     * inside this view.
     */
    public void setItems(SwipeItem... swipeItems) {
        adapter.setItems(Arrays.asList(swipeItems));
    }

    /**
     * Determine whether this SwipeSelector has a selected item, or if the currently visible
     * item is just a unselected state item.
     *
     * Unselected items are specifed by the "swipe_unselectedItemTitle" and "swipe_unselectedItemDescription"
     * attributes and created automagically for you if those attributes exist.
     *
     * @return true if this item is a real selected item by the user, false otherwise.
     */
    public boolean hasSelection() {
        return getSelectedItem().isRealItem();
    }

    /**
     * @return the selected slides' SwipeItem.
     */
    public SwipeItem getSelectedItem() {
        if (adapter.getCount() == 0) {
            throw new UnsupportedOperationException("The SwipeSelector " +
                    "doesn't have any items! Use the setItems() method " +
                    "for setting the items before calling getSelectedItem().");
        }

        return adapter.getSelectedItem();
    }

    /**
     * Select an item at the specified position and animate the change.
     *
     * @param position the position to select.
     */
    public void selectItemAt(int position) {
        selectItemAt(position, true);
    }

    /**
     * Select an item at the specified position.
     *
     * @param position the position to select.
     * @param animate should the change be animated or not.
     */
    public void selectItemAt(int position, boolean animate) {
        adapter.selectItemAt(position, animate);
    }

    /**
     * Select an item that has the specified value. The value was given
     * to the {@link SwipeItem} when constructing it.
     *
     * For example, an item constructed like this:
     *
     * {@code
     * new SwipeItem(1, "Example title", "Example description");
     * }
     *
     * would have the value "1" (without the quote marks). Then you would
     * select that item by using something like this:
     *
     * {@code
     * swipeSelector.selectItemWithValue(1);
     * }
     *
     * @param value the value of the item to select.
     */
    public void selectItemWithValue(String value) {
        selectItemWithValue(value, true);
    }

    /**
     * Select an item that has the specified value. The value was given
     * to the {@link SwipeItem} when constructing it.
     *
     * For example, an item constructed like this:
     *
     * {@code
     * new SwipeItem(1, "Example title", "Example description");
     * }
     *
     * would have the value "1" (without the quote marks). Then you would
     * select that item by using something like this:
     *
     * {@code
     * swipeSelector.selectItemWithValue(1, true);
     * }
     *
     * @param value the id of the item to select.
     * @param animate should the change be animated or not.
     */
    public void selectItemWithValue(@NonNull String value, boolean animate) {
        adapter.selectItemWithValue(value, animate);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = adapter.onSaveInstanceState();
        bundle.putParcelable(STATE_SELECTOR, super.onSaveInstanceState());
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {//Shouldn't be needed, just in case
            Bundle bundle = (Bundle) state;
            adapter.onRestoreInstanceState(bundle);
            state = bundle.getParcelable(STATE_SELECTOR);
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }
}
