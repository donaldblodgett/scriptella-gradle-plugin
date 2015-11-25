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

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

/**
 * This class represents the runEtl task of the Scriptella plugin
 * 
 * @author Donald Blodgett
 *
 */
public class ScriptellaTask extends DefaultTask {
  /**
   * The action to take when the runEtl task is executed.
   */
  @TaskAction
  public void scriptellaAction() {
    ScriptellaExtension extension = getProject().getExtensions().findByType(ScriptellaExtension.class);
    ScriptellaExecutor executor = new ScriptellaExecutor(this, extension);
    executor.execute();
  }
}
