package com.cld.cpe.rpm.extension

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile


class DownloadSourceExtension {

    @Input
    String sourceUrl

    @OutputFile
    File targetFile
}
