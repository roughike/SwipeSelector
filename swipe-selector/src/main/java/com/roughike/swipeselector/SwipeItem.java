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
    private int id;
    private String title;
    private String description;

    /**
     * Constructor for creating a new item for the {@link SwipeSelector}.
     *
     * @param id The unique id for this item, which is used for identifying which
     *           item the user has selected in this SwipeSelector.
     * @param title A short descriptive title for this item, such as "Pizza".
     * @param description Longer explanation related to the title, such as
     * "Pizzas are healthy, because pizza sauces contain tomato. And tomatoes
     * are healthy, just ask anyone."
     */
    public SwipeItem(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    /**
     * Gets the id for this SwipeItem.
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the title from a string resource (if available), or straight
     * from the "title" field.
     * @return the title for this SwipeItem.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the description from a string resource (if available), or straight
     * from the "description" field.
     * @return the description for this SwipeItem.
     */
    public String getDescription() {
        return description;
    }
}
