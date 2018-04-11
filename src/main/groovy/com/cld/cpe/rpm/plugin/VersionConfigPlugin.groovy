package com.devops.cpe.rpm.plugin

import com.devops.cpe.rpm.extension.VersionConfigExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.IConventionAware
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import java.util.regex.Pattern

/**
 * Version Configuration Plugin to maintain cld-<rpm-name>-<version>-<release>.<arch>.rpm naming standard.
 */
class VersionConfigPlugin implements Plugin<Project> {

    private static Logger logger = Logging.getLogger(VersionConfigPlugin)

    VersionConfigExtension extension
    Pattern cldPrefixRegex = ~/^cld-/
    Pattern rpmVersionRegex = ~/^\d+\.\d+\.\d+/

    static final String CONFIG_NAME = 'versionConfig'
    static final String RPM_PREFIX = 'cld-'
    static final String DEFAULT_RELEASE = '1'
    static final String SNAPSHOT = 'snapshot'

    private Project project

    @Override
    void apply(final Project project) {
        this.project = project

        extension = this.project.extensions.create(CONFIG_NAME, VersionConfigExtension)

        //Enforce rule that name must be prefixed with 'cld-'
        ((IConventionAware) extension).conventionMapping.map('rpmName') {
            this.project.name.toString() =~ cldPrefixRegex ? this.project.name.toString() : RPM_PREFIX + this.project.name.toString()
        }

        //Enforce rule that rpmVersion is only the semantic <major>.<minor>.<patch>
        ((IConventionAware) extension).conventionMapping.map('rpmVersion') {
            getRpmVersion()
        }

        //Enforce rule that rpmRelease is either SNAPSHOT or 1
        //TODO, what should the default release be, are we going to be changing the release, do we even want a release when it's not snapshot???
        ((IConventionAware) extension).conventionMapping.map('rpmRelease') {
            getRpmRelease()
        }

        addReleaseTaskDependency()
    }

    private String getRpmVersion() {
        def semanticVersionMatcher = (this.project.version.toString() =~ rpmVersionRegex)
        String semanticVersion
        try {
            semanticVersion = semanticVersionMatcher[0].toString()
        } catch (Exception e) {
            //TODO: Should I log it and set it to a default or should I throw an exception?
            logger.info("Unable to parse inferred version ${this.project.version}")
            semanticVersion = '0.0.0'
        }
        semanticVersion
    }

    private String getRpmRelease() {
        //todo should I even both with the rcCandidate and devSnapshot tasks??, currently just defaults version to 1
        def taskNames = this.project.gradle.startParameter.taskNames
        if(taskNames.contains(SNAPSHOT)) {
            return SNAPSHOT.toUpperCase()
        } else {
            return DEFAULT_RELEASE
        }
    }

    private void addReleaseTaskDependency() {
        if(project.tasks.findByName('buildRpm') != null && project.tasks.findByName('release') != null) {
            project.tasks.findByName('release').dependsOn(project.tasks.findByName('buildRpm'))
            if(project.tasks.findByName('build') != null) {
                project.tasks.findByName('build').dependsOn(project.tasks.findByName('buildRpm'))
            }
        }
    }
}
