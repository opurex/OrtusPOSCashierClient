package com.opurex.ortus.client.test;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.opurex.ortus.client.Start;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class StartTest {

    @Rule
    public ActivityScenarioRule<Start> activityRule =
            new ActivityScenarioRule<>(Start.class);

    @Test
    public void testActivityLaunches() {
        activityRule.getScenario().onActivity(activity -> {
            assertNotNull(activity);
        });
    }
}
