package com.devops.cpe.rpm.plugin

import com.devops.cpe.rpm.PublishPluginConstants
import com.devops.cpe.rpm.extension.PublishExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.IConventionAware
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.jfrog.build.extractor.clientConfiguration.client.ArtifactoryBuildInfoClient
import org.jfrog.gradle.plugin.artifactory.ArtifactoryPlugin
import org.jfrog.gradle.plugin.artifactory.ArtifactoryPluginBase

/**
 * Publish Plugin sets default artifactory configuration and adds maven publication for buildRpm task.
 */
class PublishPlugin implements Plugin<Project>{

    static final String CONFIG_NAME = 'publisher'

    PublishExtension extension
    PublishPluginConstants publishPluginConstants = new PublishPluginConstants()

    private Project project

    @Override
    void apply (Project project) {
        this.project = project
        this.project.apply(plugin: MavenPublishPlugin)
        this.project.apply(plugin: JavaPlugin)
        this.project.apply(plugin: VersionConfigPlugin)

        extension = this.project.extensions.create(CONFIG_NAME, PublishExtension)

        //Use the properties set in the repo, if they don't exist/meet standards, default to values in the publishPluginConstants class
        ((IConventionAware) extension).conventionMapping.map('contextUrl') {
            this.project.hasProperty('artifactoryUrl') ? this.project.artifactoryUrl : publishPluginConstants.CONTEXT_URL
        }

        ((IConventionAware) extension).conventionMapping.map('artifactoryUser') {
            this.project.hasProperty('artifactoryUser') ? this.project.artifactoryUser : publishPluginConstants.ARTIFACTORY_USER
        }

        ((IConventionAware) extension).conventionMapping.map('artifactoryPassword') {
            this.project.hasProperty('artifactoryPassword') ? this.project.artifactoryPassword : publishPluginConstants.ARTIFACTORY_PASSWORD
        }

        ((IConventionAware) extension).conventionMapping.map('yumArtifactoryPublishRepository') {
            this.project.hasProperty('yumArtifactoryPublishRepository') ? this.project.yumArtifactoryPublishRepository : publishPluginConstants.YUM_ARTIFACTORY_PUBLISH_REPOSITORY
        }

        ((IConventionAware) extension).conventionMapping.map('yumArtifactReleaseType') {
            (this.project.hasProperty('yumArtifactReleaseType') && publishPluginConstants.validYumArtifactReleaseTypes.contains(this.project.yumArtifactReleaseType?.toString())) ? this.project.yumArtifactReleaseType : publishPluginConstants.YUM_ARTIFACT_RELEASE_TYPE
        }

        ((IConventionAware) extension).conventionMapping.map('yumArtifactReleaseVer') {
            this.project.hasProperty('yumArtifactReleaseVer') ? this.project.yumArtifactReleaseVer : publishPluginConstants.YUM_ARTIFACT_RELEASEVER
        }

        ((IConventionAware) extension).conventionMapping.map('yumArtifactBaseArch') {
            this.project.hasProperty('yumArtifactBaseArch') ? this.project.yumArtifactBaseArch : publishPluginConstants.YUM_ARTIFACT_BASEARCH
        }

        overrideJsch()
        configureArtifactory()
        addBuildRpmMavenPublication()


    }


    private void configureArtifactory() {
        project.artifactory {
            contextUrl = "${extension.contextUrl}"
            println "Publishing using artifactoryUser: ${extension.artifactoryUser}"
            publish {
                repository {
                    repoKey = "${extension.yumArtifactoryPublishRepository}/${extension.yumArtifactReleaseType}/${extension.yumArtifactReleaseVer}/${extension.yumArtifactBaseArch}"
                    username = "${extension.artifactoryUser}"
                    password = "${extension.artifactoryPassword}"
                    maven = true
                }
                defaults {
                    publications ('mavenJava')
                    publishBuildInfo = extension.yumArtifactReleaseType != 'none'
                    publishArtifacts = extension.yumArtifactReleaseType != 'none'
                }
            }
        }
    }

    private void addBuildRpmMavenPublication() {
        this.project.publishing {
            publications {
                mavenJava(MavenPublication) {
                    artifact this.project.buildRpm
                    artifactId "${this.project.versionConfig.rpmName}"
                }
            }
        }
    }

    private static void overrideJsch() {
        com.jcraft.jsch.JSch.setConfig 'StrictHostKeyChecking', 'no'
        com.jcraft.jsch.JSch.setConfig 'UserKnownHostsFile', '/root/.ssh/known_hosts'
    }
}