package com.roughike.swipeselector;

import android.support.annotation.StringRes;

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
public class SwipeItem {
    /**
     * This boolean is checked when the SwipeAdapters
     * {@link SwipeAdapter#setItems(SwipeItem... items)} method
     * is called. If this is true, meaning there are SwipeItems
     * that are initialized using String resources, we're looping
     * through all of them and getting the strings where applicable.
     *
     * Otherwise, we'll just skip the loop as it's unnecessary.
     */
    protected static boolean checkForStringResources = false;

    public Object value;
    public String title;
    public String description;

    protected int titleRes = -1;
    protected int descriptionRes = -1;

    private SwipeItem() {
    }

    /**
     * Constructor for creating a new item for the {@link SwipeSelector}.
     *
     * @param value The value for this item, which should generally be unique
     * for current {@link SwipeSelector}. This is used the same
     * way one might use radio buttons on webpages with HTML.
     * @param title A short descriptive title for this item, such as "Pizza".
     * @param description Longer explanation related to the title, such as
     * "Pizzas are healthy, because pizza sauces contain tomato. And tomatoes
     * are healthy, just ask anyone."
     */
    public SwipeItem(Object value, String title, String description) {
        this.value = value;
        this.title = title;
        this.description = description;
    }

    /**
     * Constructor for creating a new item for the {@link SwipeSelector}
     * using String resources.
     *
     * @param value The value for this item, which should generally be unique
     * for current {@link SwipeSelector}. This is used the same
     * way one might use radio buttons on webpages with HTML.
     * @param title A short descriptive title for this item, such as "Pizza".
     * @param description Longer explanation related to the title, such as
     * "Pizzas are healthy, because pizza sauces contain tomato. And tomatoes
     * are healthy, just ask anyone."
     */
    public SwipeItem(Object value, @StringRes int title, @StringRes int description) {
        checkForStringResources = true;

        this.value = value;
        this.titleRes = title;
        this.descriptionRes = description;
    }
}
