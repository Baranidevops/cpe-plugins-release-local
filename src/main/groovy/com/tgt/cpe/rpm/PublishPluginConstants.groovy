package com.tgt.cpe.rpm

import groovy.transform.ToString

/**
 * Publish plugin constants.
 */
@ToString
class PublishPluginConstants {
    public static final String CONTEXT_URL = 'http://35.188.144.12//artifactory'
    public static final String ARTIFACTORY_USER = '<insert_artifactoryUsername>'
    public static final String ARTIFACTORY_PASSWORD = '<insert_artifactoryPassword>'
    public static final String YUM_ARTIFACTORY_PUBLISH_REPOSITORY = 'cpe-yum-local'
    public static final String YUM_ARTIFACT_RELEASE_TYPE = 'none'
    public static final String YUM_ARTIFACT_RELEASEVER = '7'
    public static final String YUM_ARTIFACT_BASEARCH = 'x86_64'
    public static final List<String> validYumArtifactReleaseTypes = ['mergepull', 'prerelease', 'release', 'none']
}
