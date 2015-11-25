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

import java.text.DecimalFormat;

import org.gradle.api.logging.Logger;

import scriptella.interactive.ProgressIndicatorBase;

/**
 * This class is used to supply progress logging to Gradle for an executing
 * task.
 * 
 * @author Donald Blodgett
 *
 */
public class ScriptellaProgressIndicator extends ProgressIndicatorBase {
  private final Logger log;
  private final DecimalFormat decimalFormat = new DecimalFormat("###%");
  private String name;

  /**
   * Creates a new progress indicator for logging to Gradle
   * 
   * @param log
   *          The Gradle logger to use
   * @param name
   *          The name of the activity being logged
   */
  public ScriptellaProgressIndicator(Logger log, String name) {
    this.log = log;
    this.name = name;
  }

  /**
   * Invoked when script execution progress has been changed.
   *
   * @param label
   *          the label of the event
   * @param percentage
   *          percentage value between 0 and 1 inclusive
   */
  protected void show(String label, double percentage) {
    StringBuilder sb = new StringBuilder(32);

    sb.append("scriptella-plugin: ");
    sb.append(name);
    if (label != null) {
      sb.append(".");
      sb.append(label);
    }

    sb.append(": ");

    sb.append(decimalFormat.format(percentage));
    log.info(sb.toString());
  }

  /**
   * Invoked when the script has completed.
   * 
   * @param label
   *          the label of the event
   */
  protected void onComplete(String label) {
    StringBuilder sb = new StringBuilder(32);
    sb.append("scriptella-plugin: ");
    sb.append(name);
    if (label != null) {
      sb.append(".");
      sb.append(label);
    }
    sb.append(": Complete");
    log.info(sb.toString());
  }
}
