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

import java.util.HashMap;
import java.util.Map;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

/**
 * This is the class that represents the Scriptella plugin and creates the
 * runEtl task.
 * 
 * @author Donald Blodgett
 *
 */
public class ScriptellaPlugin implements Plugin<Project> {
  private static final String PREFIX = "scriptellaTaskPrefix";

  /**
   * Enhances the project with the Scriptella plugin
   * 
   * @param The
   *          project to enhance
   */
  public void apply(Project project) {
    applyExtension(project);
    applyTasks(project);
  }

  /**
   * Creates the scriptella extension to be used to configure the Scriptella
   * plugin.
   * 
   * @param project
   *          The project to add the extension to
   */
  private void applyExtension(Project project) {
    NamedDomainObjectContainer<ScriptellaActivity> activities = project.container(ScriptellaActivity.class);
    project.getExtensions().create("scriptella", ScriptellaExtension.class, activities);
  }

  /**
   * Creates the runEtl task and adds it to the project. If the
   * {@code scriptellaTaskPrefix} property is defined on the project it will be
   * added as the prefix to the task name.
   * 
   * @param project
   *          The project to add the task to
   */
  private void applyTasks(Project project) {
    Map<String, Object> config = new HashMap<String, Object>();
    config.put(Task.TASK_TYPE, ScriptellaTask.class);
    config.put(Task.TASK_GROUP, "Scriptella");
    config.put(Task.TASK_DESCRIPTION,
        "Executes the Scriptella ETL activities defined in the the Scriptella runList property or all activities if the runList is not defined");
    if (project.hasProperty(PREFIX)) {
      project.task(config, project.getProperties().get(PREFIX) + "RunEtl");
    } else {
      project.task(config, "runEtl");
    }
  }
}
