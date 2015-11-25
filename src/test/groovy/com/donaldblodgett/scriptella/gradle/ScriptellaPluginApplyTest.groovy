package com.donaldblodgett.scriptella.gradle;

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

class ScriptellaPluginApplyTest extends GroovyTestCase {
  Project project;

  public void setUp(){
    project = ProjectBuilder.builder().build();
  }
  
  public void testApplyPluginByType() {
    project.apply plugin: com.donaldblodgett.scriptella.gradle.ScriptellaPlugin
    assertTrue("Project is missing Scriptella plugin", project.plugins.hasPlugin(ScriptellaPlugin))
    def task = project.tasks.findByName("runEtl")
    assertNotNull("Project is missing runEtl task", task)
    assertTrue("runEtl task is the wrong type", task instanceof ScriptellaTask)
    assertTrue("runEtl task should be enabled", task.enabled)
  }
  
  public void testApplyPluginByName() {
    project.apply plugin: 'scriptella'
    assertTrue("Project is missing Scriptella plugin", project.plugins.hasPlugin(ScriptellaPlugin))
    def task = project.tasks.findByName("runEtl")
    assertNotNull("Project is missing runEtl task", task)
    assertTrue("runEtl task is the wrong type", task instanceof ScriptellaTask)
    assertTrue("runEtl task should be enabled", task.enabled)
  }
  
  public void testApplyPluginByNameWithPrefix() {
    project.ext.scriptellaTaskPrefix = 'scriptella'
    project.apply plugin: 'scriptella'
    assertTrue("Project is missing Scriptella plugin", project.plugins.hasPlugin(ScriptellaPlugin))
    def task = project.tasks.findByName('scriptellaRunEtl')
    assertNotNull("Project is missing runEtl task", task)
    assertTrue("runEtl task is the wrong type", task instanceof ScriptellaTask)
    assertTrue("runEtl task should be enabled", task.enabled)

    task = project.tasks.findByName('runEtl')
    assertNull("runEtl task should not have been created", task)
  }
}