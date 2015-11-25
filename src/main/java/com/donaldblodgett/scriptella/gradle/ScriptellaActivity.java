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
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import groovy.lang.Closure;
import groovy.lang.GString;

/**
 * This class represents a single activity that must be performed as part of a
 * Scriptella task. It is essentially a single Scriptella script definition with
 * corresponding parameters. Each named activity in the {@code activities} closure
 * of the {@code scriptella} block will create one of these objects.
 *
 * @author Donald Blodgett
 */
public class ScriptellaActivity {
  private String name;
  private Object script;
  private Map<String, Object> properties;

  /**
   * Creates a new activity with the given name.
   * 
   * @param name
   *          The name of this activity
   */
  public ScriptellaActivity(String name) {
    this.name = name;
  }

  /**
   * Gets the name of this activity.
   * 
   * @return The name of this activity
   */
  public String getName() {
    return name;
  }

  /**
   * Define the script to use for this activity. The script is the Scriptella
   * ETL script that should be executed for this activity.
   * 
   * This method converts the supplied path based on its type:
   * 
   * <ul>
   * <li>A {@link CharSequence}, including {@link String} or {@link GString}.
   * Interpreted relative to the project directory. A string that starts with
   * file: is treated as a file URL.
   * <li>A {@link File}. If the file is an absolute file, it is returned as is.
   * Otherwise, the file's path is interpreted relative to the project
   * directory.
   * <li>A {@link URI} or {@link URL}. The URL's path is interpreted as the file
   * path. Currently, only file: URLs are supported.
   * <li>A {@link Closure}. The closure's return value is resolved recursively.
   * <li>A {@link Callable}. The callable's return value is resolved
   * recursively.
   * </ul>
   * 
   * @param script
   *          The object to resolve as an ETL script
   */
  public void script(Object script) {
    this.script = script;
  }

  /**
   * Gets the object used to reference the ETL script file.
   * 
   * @return The object to resolve as an ETL script
   */
  public Object getScript() {
    return script;
  }

  /**
   * Define the parameters to use for this activity. Parameters are used by
   * Scriptella to perform token substitution in the script.
   * 
   * @param properties
   *          the map of tokens and their values
   */
  public void properties(Map<String, Object> properties) {
    this.properties = new HashMap<String, Object>();
    this.properties.putAll(properties);
  }

  /**
   * Gets the properties to be used by this activity to perform token
   * substitution in the script.
   * 
   * @return The properties to be used by Scriptella for this activity
   */
  public Map<String, Object> getProperties() {
    return properties;
  }
}
