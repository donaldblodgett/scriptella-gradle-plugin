package com.donaldblodgett.scriptella.gradle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.math.IntRange;
import org.gradle.api.NamedDomainObjectContainer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import groovy.lang.Closure;

@RunWith(MockitoJUnitRunner.class)
public class ScriptellaExtensionTest {
  @Mock
  private NamedDomainObjectContainer<ScriptellaActivity> activities;

  private ScriptellaExtension extension;

  @Before
  public void setup() {
    initMocks(this);
    extension = new ScriptellaExtension(activities);
  }

  @Test
  public void activities() {
    Closure<?> closure = mock(Closure.class);
    extension.activities(closure);
    verify(activities).configure(eq(closure));
  }

  @Test
  public void buildActivityListWithZeroConfiguration() {
    when(activities.isEmpty()).thenReturn(true);
    List<ScriptellaActivity> activities = extension.buildActivityList();
    assertEquals(Collections.emptyList(), activities);
  }

  @Test
  public void buildActivitiesListWithNoRunList() {
    List<ScriptellaActivity> activityList = createRandomActivities();
    when(activities.isEmpty()).thenReturn(false);
    when(activities.toArray()).thenReturn(activityList.toArray());
    assertEquals(activityList, extension.buildActivityList());
  }

  @Test(expected = IllegalArgumentException.class)
  public void buildActivitiesListWithNoActivities() {
    extension.setRunList(TestUtils.randomString());
    extension.buildActivityList();
  }

  @Test
  public void buildActivitiesWithConfiguration() {
    List<ScriptellaActivity> activityList = createRandomActivities();
    String randomName = activityList.get(0).getName();
    extension.setRunList(randomName);
    when(activities.findByName(randomName)).thenReturn(activityList.get(0));
    extension.buildActivityList();
  }

  private List<ScriptellaActivity> createRandomActivities() {
    List<ScriptellaActivity> activityList = new ArrayList<ScriptellaActivity>();
    for (@SuppressWarnings("unused")
    int i : new IntRange(2, 5).toArray()) {
      String randomName = TestUtils.randomString();
      activityList.add(new ScriptellaActivity(randomName));
    }
    return activityList;
  }
}
