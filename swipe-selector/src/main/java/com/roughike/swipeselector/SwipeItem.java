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

public class SwipeItem {
    static final String UNSELECTED_ITEM_VALUE = "com.roughike.swipeselector.UNSELECTED_ITEM_VALUE";
    private String value;
    private String title;
    private String description;

    SwipeItem() {
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
    public SwipeItem(String value, String title, String description) {
        this.value = value;
        this.title = title;
        this.description = description;
    }

    /**
     * Set the value for this SwipeItem.
     *
     * @param value The unique value for this item, which is used for identifying which
     *           item the user has selected in this SwipeSelector.
     */
    void setValue(String value) {
        this.value = value;
    }

    /**
     * Set the title for this SwipeItem.
     *
     * @param title A short descriptive title for this item, such as "Pizza".
     */
    void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set the description for this SwipeItem.
     *
     * @param description Longer explanation related to the title, such as
     *                    "Pizzas are healthy, because pizza sauces contain tomato. And tomatoes
     *                    are healthy, just ask anyone."
     */
    void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the value for this SwipeItem.
     *
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the title from a string resource (if available), or straight
     * from the "title" field.
     *
     * @return the title for this SwipeItem.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the description from a string resource (if available), or straight
     * from the "description" field.
     *
     * @return the description for this SwipeItem.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Determines if this item is a unselected state item, in other words, not a real possible
     * selection for this SwipeSelector.
     *
     * Unselected items are specifed by the "swipe_unselectedItemTitle" and "swipe_unselectedItemDescription"
     * attributes and created automagically for you if those attributes exist.
     *
     * @return true if this item is a real selected item by the user, false otherwise.
     */
    boolean isRealItem() {
        return !UNSELECTED_ITEM_VALUE.equals(value);
    }
}
