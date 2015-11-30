Scriptella Gradle Plugin
-----------------------
A plugin for [Gradle](http://gradle.org) that allows you to use
[Scriptella](http://scriptella.javaforge.com/) to manage
your database upgrades. This project is created and managed by
Donald Blodgett.

Usage
-----
Build script snippet for use in all Gradle versions:

```groovy
buildscript {
  repositories {
    mavenCentral()
  }
  dependencies {
    classpath "com.donaldblodgett.scriptella:scriptella-gradle-plugin:1.1"
  }
}
apply plugin: 'scriptella'
```

News
----
###November 25, 2015
The plugin has been released with support for Scriptella 1.1.

## Usage
The Scriptella plugin allows you to use Scriptella to execute ETL scripts.
It is meant to be a light weight front end for Scriptella. When the Scriptella plugin
is applied, it creates a Gradle task `runEtl`. If you want to prefix the task to
avoid a task name conflict, set a value for the ```scriptellaTaskPrefix``` property.
This will tell the Scriptella plugin to create the task with the specified prefix.
For example, if Gradle is invoked with ```-PscriptellaTaskPrefix=scriptella```,
or you put ```scriptellaTaskPrefix=scriptella``` in ```gradle.properties```
then this plugin will create a task named ```scriptellaRunEtl```.

Parameters for the commands are configured in the ```scriptella``` block inside
the build.gradle file. This block contains a series of, ```activities```, each
defining a Scriptella ```script``` and associated ```properties```. The ```scriptella```
block also has an optional ```runList```, which determines which activities are run
when the task is executed. If no runList is defined, the Scriptella plugin will run all
the activities.

NOTE: the order of execution when there is no runList is not guaranteed.

*Example:*

Let's suppose that for each deployment, you need to execute a Scriptella script to
process data for your database, and would also need to run some other Scriptella
script in a separate database. The ```scriptella``` block might look like this:

```groovy
scriptella {
  activities {
    main {
      script 'src/main/scripts/main.etl.xml'
      properties {
        url project.ext.mainUrl
        username project.ext.mainUsername
        password project.ext.mainPassword
      }
    }
    secondary {
      script 'src/main/scripts/secondary.etl.xml'
      properties {
        url project.ext.secondaryUrl
        username project.ext.secondaryUsername
        password project.ext.secondaryPassword
      }
    }
  }
  runList = project.ext.runList
}
```

Some things to keep in mind when setting up the ```scriptella``` block:

1. We only need one activity for each script. In the example above,
   the database credentials are driven by build properties so that the correct
   database can be specified at build time so that you don't need a separate
   activity for each database.

2. By making the value of ```runList``` a property, you can determine the
   activities that get run at build time.  For example, if you didn't need to
   run the secondary script, you could type:
   
   ```gradle runEtl -PrunList=main```
   
   For environments where you do need the secondary script, you would use:
   
   ```gradle runEtl -PrunList='main,secondary'```
   
   This is the reason the runList is a string and not an array.
