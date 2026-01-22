/*
    Opurex Android com.opurex.ortus.client
    Copyright (C) Opurex contributors, see the COPYRIGHT file

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.opurex.ortus.client.test;

import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.opurex.ortus.client.Start;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class StartTest {

    @Rule
    public ActivityTestRule<Start> activityRule = new ActivityTestRule<>(
            Start.class,
            true,     // initialTouchMode
            false);   // launchActivity. False to control when the activity is launched

    @Test
    public void testBasic() {
        // Launch the activity
        Intent intent = new Intent();
        Start activity = activityRule.launchActivity(intent);

        // Perform basic test
        assertTrue(true);
    }
}