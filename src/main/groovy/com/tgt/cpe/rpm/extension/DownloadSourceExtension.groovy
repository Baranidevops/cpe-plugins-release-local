package com.tgt.cpe.rpm.extension

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile

/**
 * Created by z001kkh on 5/3/16.
 */
class DownloadSourceExtension {

    @Input
    String sourceUrl

    @OutputFile
    File targetFile
}
