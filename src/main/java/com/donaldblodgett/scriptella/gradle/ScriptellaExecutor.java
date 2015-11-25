/*
 * Copyright 2015 Donald Blodgett
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.donaldblodgett.scriptella.gradle;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Map;

import org.gradle.api.PathValidation;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.TaskExecutionException;

import scriptella.execution.EtlExecutor;
import scriptella.execution.EtlExecutorException;
import scriptella.util.IOUtils;

/**
 * This class is used to execute the each activity in the {@code activities}
 * closure or only those defined in the {@code runList} of the
 * {@scriptella} block.
 * 
 * @author Donald Blodgett
 */
public class ScriptellaExecutor {
  private ScriptellaTask task;
  private ScriptellaExtension extension;
  private Logger log;

  /**
   * 
   * @param task
   * @param extension
   */
  public ScriptellaExecutor(ScriptellaTask task, ScriptellaExtension extension) {
    this.task = task;
    this.extension = extension;
    this.log = task.getProject().getLogger();
  }

  /**
   * Used to execute the activities defined on the runList in the order they are
   * supplied or all of the activities defined if no runList is defined. In the
   * case that a runList is not defined, the order of execution of the
   * activities is undefined.
   */
  public void execute() {
    log.info(MessageFormat.format("scriptella-plugin: Running task ''{0}''...", task.getName()));
    for (ScriptellaActivity activity : extension.buildActivityList()) {
      log.info(MessageFormat.format("scriptella-plugin: Running activity ''{0}''...", activity.getName()));
      executeScript(activity.getScript(), activity.getProperties(),
          new ScriptellaProgressIndicator(log, activity.getName()));
    }
    log.info(MessageFormat.format("scriptella-plugin: Completed task ''{0}''", task.getName()));
  }

  /**
   * Executes the provided script using the provided properties and output the
   * progress of the execution to the supplied progress indicator.
   * 
   * @param script
   *          The script to execute
   * @param properties
   *          The properties to by applied to the script
   * @param progressIndicator
   *          The progress indicator to watch progress of the script
   */
  private void executeScript(Object script, Map<String, Object> properties,
      ScriptellaProgressIndicator progressIndicator) {
    URL scriptFile = getScriptUrl(script);
    try {
      EtlExecutor.newExecutor(scriptFile, properties).execute(progressIndicator);
    } catch (EtlExecutorException e) {
      throw new TaskExecutionException(task, e);
    }
  }

  /**
   * Gets the URL of the provided script
   * 
   * @param script
   *          The object representing the script
   * @return A URL to the supplied script
   */
  private URL getScriptUrl(Object script) {
    File file = task.getProject().file(script, PathValidation.FILE);
    try {
      return IOUtils.toUrl(file);
    } catch (MalformedURLException e) {
      throw new TaskExecutionException(task, e);
    }
  }
}
