package com.donaldblodgett.scriptella.gradle;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.IntRange;
import org.gradle.api.PathValidation;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.TaskExecutionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import scriptella.execution.EtlExecutor;
import scriptella.execution.EtlExecutorException;
import scriptella.util.IOUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EtlExecutor.class, IOUtils.class})
public class ScriptellaExecutorTest {
  @Mock
  private Project project;
  @Mock
  private Logger log;
  @Mock
  private ScriptellaTask task;
  @Mock
  private ScriptellaExtension extension;
  @Mock
  private EtlExecutor etlExecutor;

  private ScriptellaExecutor executor;

  @Before
  public void setup() {
    initMocks(this);
    mockStatic(EtlExecutor.class);
    mockStatic(IOUtils.class);
    when(project.getLogger()).thenReturn(log);
    when(task.getProject()).thenReturn(project);
    executor = new ScriptellaExecutor(task, extension);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void executeWithZeroActivities() {
    when(extension.buildActivityList()).thenReturn(new ArrayList<ScriptellaActivity>());
    executor.execute();
    verifyStatic(never());
    EtlExecutor.newExecutor(any(File.class));
    verifyStatic(never());
    EtlExecutor.newExecutor(any(URL.class));
    verifyStatic(never());
    EtlExecutor.newExecutor(any(URL.class), anyMap());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void executeWithActivities() throws MalformedURLException, EtlExecutorException {
    List<ScriptellaActivity> activities = new ArrayList<ScriptellaActivity>();
    for (@SuppressWarnings("unused")
    int i : new IntRange(2, 5).toArray()) {
      ScriptellaActivity activity = mock(ScriptellaActivity.class);
      when(activity.getScript()).thenReturn(new Object());
      activities.add(activity);
    }
    when(extension.buildActivityList()).thenReturn(activities);
    File randomFile = new File(TestUtils.randomString());
    when(project.file(any(), eq(PathValidation.FILE))).thenReturn(randomFile);
    URL randomUrl = randomFile.toURI().toURL();
    when(IOUtils.toUrl(randomFile)).thenReturn(randomUrl);
    when(EtlExecutor.newExecutor(any(URL.class), anyMap())).thenReturn(etlExecutor);
    
    executor.execute();
    
    verify(project, times(activities.size())).file(any(), eq(PathValidation.FILE));
    verifyStatic(never());
    EtlExecutor.newExecutor(any(File.class));
    verifyStatic(never());
    EtlExecutor.newExecutor(any(URL.class));
    verifyStatic(times(activities.size()));
    EtlExecutor.newExecutor(eq(randomUrl), anyMap());
    verify(etlExecutor, times(activities.size())).execute(any(ScriptellaProgressIndicator.class));
  }
  
  @SuppressWarnings("unchecked")
  @Test(expected = TaskExecutionException.class)
  public void executeWithExecutorException() throws MalformedURLException, EtlExecutorException {
    List<ScriptellaActivity> activities = new ArrayList<ScriptellaActivity>();
    ScriptellaActivity activity = mock(ScriptellaActivity.class);
    when(activity.getScript()).thenReturn(new Object());
    activities.add(activity);
    when(extension.buildActivityList()).thenReturn(activities);
    File randomFile = new File(TestUtils.randomString());
    when(project.file(any(), eq(PathValidation.FILE))).thenReturn(randomFile);
    URL randomUrl = randomFile.toURI().toURL();
    when(IOUtils.toUrl(randomFile)).thenReturn(randomUrl);
    when(EtlExecutor.newExecutor(any(URL.class), anyMap())).thenReturn(etlExecutor);
    when(etlExecutor.execute(any(ScriptellaProgressIndicator.class))).thenThrow(EtlExecutorException.class);
    
    executor.execute();
    
    verify(project, times(1)).file(any(), eq(PathValidation.FILE));
    verifyStatic(never());
    EtlExecutor.newExecutor(any(File.class));
    verifyStatic(never());
    EtlExecutor.newExecutor(any(URL.class));
    verifyStatic(times(1));
    EtlExecutor.newExecutor(eq(randomUrl), anyMap());
    verify(etlExecutor, times(1)).execute(any(ScriptellaProgressIndicator.class));
  }
  
  @SuppressWarnings("unchecked")
  @Test(expected = TaskExecutionException.class)
  public void executeWithMalformedUrlException() throws MalformedURLException {
    List<ScriptellaActivity> activities = new ArrayList<ScriptellaActivity>();
    ScriptellaActivity activity = mock(ScriptellaActivity.class);
    when(activity.getScript()).thenReturn(new Object());
    activities.add(activity);
    when(extension.buildActivityList()).thenReturn(activities);
    File randomFile = new File(TestUtils.randomString());
    when(project.file(any(), eq(PathValidation.FILE))).thenReturn(randomFile);
    when(IOUtils.toUrl(randomFile)).thenThrow(MalformedURLException.class);
    
    executor.execute();
    
    verify(project, times(1)).file(any(), eq(PathValidation.FILE));
    verifyStatic(never());
    EtlExecutor.newExecutor(any(File.class));
    verifyStatic(never());
    EtlExecutor.newExecutor(any(URL.class));
    verifyStatic(never());
    EtlExecutor.newExecutor(any(URL.class), anyMap());
  }
}
