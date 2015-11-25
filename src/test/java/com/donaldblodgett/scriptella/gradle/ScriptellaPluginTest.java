package com.donaldblodgett.scriptella.gradle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collections;
import java.util.Map;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.ExtensionContainer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScriptellaPluginTest {
  @Mock
  private Project project;
  @Mock
  private NamedDomainObjectContainer<ScriptellaActivity> activities;
  @Mock
  private ExtensionContainer extensionContainer;

  private ScriptellaPlugin plugin;

  @Before
  public void setup() {
    initMocks(this);
    when(project.container(ScriptellaActivity.class)).thenReturn(activities);
    when(project.getExtensions()).thenReturn(extensionContainer);
    plugin = new ScriptellaPlugin();
  }

  @Test
  @SuppressWarnings("unchecked")
  public void applyWhenPrefixNotPresent() {
    plugin.apply(project);
    verify(project, times(1)).container(eq(ScriptellaActivity.class));
    verify(extensionContainer, times(1)).create(eq("scriptella"), eq(ScriptellaExtension.class), eq(activities));

    @SuppressWarnings("rawtypes")
    ArgumentCaptor<Map> argumentsCaptured = ArgumentCaptor.forClass(Map.class);
    verify(project, times(1)).task((Map<String, Object>) argumentsCaptured.capture(), eq("runEtl"));
    assertEquals(ScriptellaTask.class, argumentsCaptured.getValue().get(Task.TASK_TYPE));
    assertEquals(argumentsCaptured.getValue().get(Task.TASK_GROUP), "Scriptella");
    assertTrue(String.class.isInstance(argumentsCaptured.getValue().get(Task.TASK_DESCRIPTION)));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void applyWhenPrefixPresent() {
    when(project.hasProperty("scriptellaTaskPrefix")).thenReturn(true);
    String prefix = TestUtils.randomString();
    when(project.getProperties()).thenReturn(generateProperties(prefix));

    plugin.apply(project);

    verify(project, times(1)).container(eq(ScriptellaActivity.class));
    verify(extensionContainer, times(1)).create(eq("scriptella"), eq(ScriptellaExtension.class), eq(activities));
    @SuppressWarnings("rawtypes")
    ArgumentCaptor<Map> argumentsCaptured = ArgumentCaptor.forClass(Map.class);
    verify(project, times(1)).task((Map<String, Object>) argumentsCaptured.capture(), eq(prefix + "RunEtl"));
    assertEquals(ScriptellaTask.class, argumentsCaptured.getValue().get(Task.TASK_TYPE));
    assertEquals(argumentsCaptured.getValue().get(Task.TASK_GROUP), "Scriptella");
    assertTrue(String.class.isInstance(argumentsCaptured.getValue().get(Task.TASK_DESCRIPTION)));
  }

  @SuppressWarnings("rawtypes")
  private Map generateProperties(String prefix) {
    return Collections.singletonMap("scriptellaTaskPrefix", prefix);
  }
}
