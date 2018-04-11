package com.tgt.cpe.rpm.extension

import groovy.transform.ToString

/**
 * PublishPlugin extension class
 */
@ToString
class PublishExtension {

    String contextUrl
    String artifactoryUser
    String artifactoryPassword

    String yumArtifactoryPublishRepository
    String yumArtifactReleaseType
    String yumArtifactReleaseVer
    String yumArtifactBaseArch

}
