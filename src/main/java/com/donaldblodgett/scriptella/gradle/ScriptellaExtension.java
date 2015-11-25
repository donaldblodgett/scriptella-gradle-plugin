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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.gradle.api.NamedDomainObjectContainer;

import groovy.lang.Closure;

/**
 * This is the Gradle extension that configures the Scriptella plugin. All
 * configuration options will be in the {@code scriptella} block of the
 * build.gradle file. This block consists of a list of activities and a run
 * list.
 *
 * @author Donald Blodgett
 */
public class ScriptellaExtension {
  private NamedDomainObjectContainer<ScriptellaActivity> activities;
  private String runList;

  public ScriptellaExtension(NamedDomainObjectContainer<ScriptellaActivity> activities) {
    this.activities = activities;
  }

  /**
   * Used to configure the activities to be executed by Scriptella.
   * 
   * @param closure
   *          The closure which contains the configurations of all the
   *          activities to be executed
   */
  public void activities(Closure<?> closure) {
    activities.configure(closure);
  }

  /**
   * Define the list of activities that run for scriptella task. This is a
   * string of comma separated activity names. This is a string instead of an
   * array to facilitate the use of Gradle properties. If no runList is defined,
   * the plugin will run all activities.
   */
  public void setRunList(String runList) {
    this.runList = runList;
  }

  /**
   * Builds the list of activities to be executed based on the runList or if no
   * runList is defined a list of all the activities.
   * 
   * @return A list of activities to be executed in the order of execution
   */
  public List<ScriptellaActivity> buildActivityList() {
    if (runList == null) {
      if (activities.isEmpty()) {
        return Collections.emptyList();
      } else {
        List<ScriptellaActivity> list = new ArrayList<ScriptellaActivity>(activities);
        return list;
      }
    }
    List<ScriptellaActivity> list = new ArrayList<ScriptellaActivity>();
    for (String name : Arrays.asList(runList.trim().split("\\s*,\\s*"))) {
      ScriptellaActivity activity = activities.findByName(name);
      if (activity == null) {
        throw new IllegalArgumentException(
            MessageFormat.format("runList activity ''{0}'' is not a defined Scriptella activity", name));
      }
      list.add(activity);
    }
    return list;
  }
}
