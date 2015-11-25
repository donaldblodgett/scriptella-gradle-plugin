package com.donaldblodgett.scriptella.gradle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.IntRange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScriptellaActivityTest {
  private ScriptellaActivity activity;

  private String name;

  @Before
  public void setup() {
    name = TestUtils.randomString();
    activity = new ScriptellaActivity(name);
  }

  @Test
  public void getName() {
    assertEquals(name, activity.getName());
  }

  @Test
  public void script() {
    Object script = mock(Object.class);
    activity.script(script);
    assertEquals(script, activity.getScript());
  }

  @Test
  public void properties() {
    Map<String, Object> properties = new HashMap<String, Object>();
    for (@SuppressWarnings("unused")
    int n : new IntRange(5, 10).toArray()) {
      properties.put(TestUtils.randomString(), TestUtils.randomString());
    }
    activity.properties(Collections.unmodifiableMap(properties));
    assertEquals(properties, activity.getProperties());
  }
}
