## RPM gradle plugin
see [this issue](https://git.target.com/cpe/consolidateplatform/issues/63)
----------
### Version Configuration Plugin: *cpe.rpm.version-config*
##### Purpose
Enforce [these naming/version conventions](https://git.target.com/cpe/consolidateplatform/issues/32):
```
tgt-<rpmName>-<rpmVersion>-<rpmRelease>.<rpmArch>.rpm
```
#### Notes
- These properties are inferred via:
  - *rpmName*: ```project.name``` specified in ```settings.gradle``` file of the project that applied this plugin (e.g., [settings.gradle](settings.gradle)). *If this name is not prefixed with 'tgt-', that prefix will be added.*
  - *rpmVersion*: ```project.version``` set by the [nebula-release-plugin](https://github.com/nebula-plugins/nebula-release-plugin)
  - *rpmRelease*: 'SNAPSHOT' if the nebula-release-plugin's ```snapshot``` task is called, otherwise defaults to '1'
  - *rpmArch*: ```yumArtifactBaseArch``` property set in the ```gradle.properties``` file of the project or defaults to 'x86_64'
  - *rpmOS*: defaults to 'LINUX'
- However, if further customization is necessary, they can be overridden if you use the following block in your ```build.gradle``` file:
```
  versionConfig {
    rpmName = <insert-name>
    rpmVersion = <insert-version>
    rpmRelease = <insert-release>
    rpmArch = <insert-arch>
    rpmOS = <insert-OS>
  }
```
- Note that these properties must be strings and if they don't fit with the [nebula gradle-ospackage-plugin](https://github.com/nebula-plugins/gradle-ospackage-plugin/blob/master/Plugin-Rpm.md) they might cause errors.

#### Usage
```
buildscript {
  repositories {
    mavenLocal()
    maven { url 'http://35.188.144.12//artifactory/cld-plugins-release-local/' }
  }
  dependencies {
    classpath "com.tgt.cpe:cpe-rpm-plugins:0.0.3"
  }
}

apply plugin: 'cpe.rpm.version-config'

repositories {
    maven { url 'http://35.188.144.12//artifactory/cld-plugins-release-local/' }
}
```
----------
### Publish Plugin: *cpe.rpm.publish*
##### Purpose
Configure [defaults](src/main/groovy/com/tgt/cpe/rpm/PublishPluginConstants.groovy) for the jFrog artifactory plugin to publish the resulting RPM to the specified artifactory repository.

#### Notes
- The cpe.rpm.publish plugin will automatically apply the cpe.rpm.version-config plugin to make sure that published artifact is properly named
- This plugin will use the property values from the ```gradle.properties``` file in the gradle project that this plugin has been applied to. It will use the following properties:
  ```
  artifactoryUrl = <insert-artifactory-url>
  artifactoryUser = <insert_artifactory_username>
  artifactoryPassword = <insert_artifactory_password>

  yumArtifactBaseArch = <insert-artifact-basearch>
  yumArtifactReleaseVer = <insert-artifact-releasever>
  yumArtifactReleaseType = <insert-artifact-release-type-(none|mergepull|prerelease|release)>

  yumArtifactoryPublishRepository = <insert-artifactory-publish-repo>
  ```
- If the above properties are not set, the default settings from [PublishPluginConstants](src/main/groovy/com/tgt/cpe/rpm/PublishPluginConstants.groovy) will be used.
- These properties can be overridden if you use the following block in your ```build.gradle``` file:
```
  publishConfig {
    contextUrl = <insert-artifactory-context-url>
    artifactoryUser = <insert-artifactory-user>
    artifactoryPassword = <insert-artifactory-password>
    yumArtifactoryPublishRepository = <insert-artifactory-publish-repo>
    yumArtifactReleaseType = <insert-artifact-release-type>
    yumArtifactReleaseVer = <insert-artifact-releasever>
    yumArtifactBaseArch = <insert-artifact-basearch>
  }
```
- ```contextUrl``` equates to the ```artifactoryUrl``` property where the artifact will be published
- the resulting url to which the artifact is published is: ```contextUrl```/```yumArtifactoryPublishRepository```/```yumArtifactReleaseType```/```yumArtifactReleaseVer```/```yumArtifactBaseArch```

#### Usage
```
buildscript {
  repositories {
    mavenLocal()
    maven { url 'http://35.188.144.12//artifactory/cld-plugins-release-local/' }
  }
  dependencies {
    classpath "com.tgt.cpe:cpe-rpm-plugins:0.0.3"
  }
}

apply plugin: 'cpe.rpm.publish'

repositories {
    maven { url 'http://35.188.144.12//artifactory/cld-plugins-release-local/' }
}

```

----------
### Download Source Plugin: *cpe.rpm.download-source*
##### Purpose
Provide functionality to easily download source files into target directories.

#### Notes
- If this plugin is applied, the ```downloadSource``` task will automatically be made a dependency of the ```buildRpm``` task, so that source files are downloaded prior to buulding the rpm.

#### Usage
```
buildscript {
  repositories {
    mavenLocal()
    maven { url 'http://35.188.144.12//artifactory/cld-plugins-release-local/' }
  }
  dependencies {
    classpath "com.tgt.cpe:cpe-rpm-plugins:0.0.3"
  }
}

apply plugin: 'cpe.rpm.download-source'

repositories {
    maven { url 'http://35.188.144.12//artifactory/cld-plugins-release-local/' }
}

downloadSourceConfig {
  sourceUrl =
  targetFile =
}
```
-----

### Possible Future

- [Jar packaging task](https://git.target.com/cpe/consolidateplatform/issues/76)
- Enforce testing conventions
  - "set up" task to install RPM and configure before testing
  - "test" task that will enable more flexibility with how people choose to test the RPM but will be a common convention that Jenkins can use to run the tests
