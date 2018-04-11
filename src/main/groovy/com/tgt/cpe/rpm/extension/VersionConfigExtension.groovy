package com.tgt.cpe.rpm.extension

import groovy.transform.ToString

/**
 * Version configuration values
 */
@ToString
class VersionConfigExtension {
    String rpmName
    String rpmVersion
    String rpmRelease
    String rpmArch = 'x86_64'
    String rpmOS = 'LINUX'
}

