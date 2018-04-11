package com.devops.cpe.rpm.tasks

import com.devops.cpe.rpm.extension.DownloadSourceExtension
import com.devops.cpe.rpm.plugin.DownloadSourcePlugin
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
