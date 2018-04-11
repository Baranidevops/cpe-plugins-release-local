package com.cld.cpe.rpm.tasks

import com.cld.cpe.rpm.extension.DownloadSourceExtension
import com.cld.cpe.rpm.plugin.DownloadSourcePlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Task to download source files.
 */
class DownloadSourceTask extends DefaultTask{
    DownloadSourceExtension extension

    @TaskAction
    public void action() {
        this.extension = this.project.extensions.getByName(DownloadSourcePlugin.CONFIG_NAME)

        ant.get(src: this.extension.sourceUrl, dest: this.extension.targetFile)
    }
}
