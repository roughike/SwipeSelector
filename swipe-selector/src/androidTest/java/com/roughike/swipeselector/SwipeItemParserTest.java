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
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class SwipeItemParserTest {
    private List<SwipeItem> hardCodedItems;
    private List<SwipeItem> resourcedItems;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getContext();
        hardCodedItems = new SwipeItemParser(
                context,
                com.roughike.swipeselector.test.R.xml.swipe_items_harcoded
        ).parseItems();
        resourcedItems = new SwipeItemParser(
                context,
                com.roughike.swipeselector.test.R.xml.swipe_items_with_string_resources
        ).parseItems();
    }

    @Test
    public void shouldHaveCorrectAmountOfItems() {
        assertThat(hardCodedItems.size(), is(3));
        assertThat(resourcedItems.size(), is(3));
    }

    @Test
    public void shouldHaveCorrectHardcodedValues() {
        assertThat(hardCodedItems.get(0).getValue(), is("pizza"));
        assertThat(hardCodedItems.get(1).getValue(), is("burger"));
        assertThat(hardCodedItems.get(2).getValue(), is("sushi"));
    }

    @Test
    public void shouldHaveCorrectStringResourceValues() {
        assertThat(resourcedItems.get(0).getValue(), is("pizza"));
        assertThat(resourcedItems.get(1).getValue(), is("burger"));
        assertThat(resourcedItems.get(2).getValue(), is("sushi"));
    }

    @Test
    public void shouldHaveCorrectHardcodedTitles() {
        assertThat(hardCodedItems.get(0).getTitle(), is("Pizza"));
        assertThat(hardCodedItems.get(1).getTitle(), is("Hamburger"));
        assertThat(hardCodedItems.get(2).getTitle(), is("Sushi"));
    }

    @Test
    public void shouldHaveCorrectStringResourceTitles() {
        assertThat(resourcedItems.get(0).getTitle(), is("Pizza"));
        assertThat(resourcedItems.get(1).getTitle(), is("Hamburger"));
        assertThat(resourcedItems.get(2).getTitle(), is("Sushi"));
    }

    @Test
    public void shouldHaveCorrectHardcodedDescriptions() {
        assertThat(hardCodedItems.get(0).getDescription(), is("Pizza is love. Pizza is life."));
        assertThat(hardCodedItems.get(1).getDescription(), is("EY WOOD LAKE TO BYE A AMBURGER"));
        assertThat(hardCodedItems.get(2).getDescription(), is("Who doesn't love raw fish?"));
    }

    @Test
    public void shouldHaveCorrectStringResourceDescriptions() {
        assertThat(resourcedItems.get(0).getDescription(), is("Pizza is love. Pizza is life."));
        assertThat(resourcedItems.get(1).getDescription(), is("EY WOOD LAKE TO BYE A AMBURGER"));
        assertThat(resourcedItems.get(2).getDescription(), is("Who doesn't love raw fish?"));
    }
}
