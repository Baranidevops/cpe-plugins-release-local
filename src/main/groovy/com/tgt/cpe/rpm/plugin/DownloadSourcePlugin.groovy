package com.tgt.cpe.rpm.plugin

import com.tgt.cpe.rpm.extension.DownloadSourceExtension
import com.tgt.cpe.rpm.tasks.DownloadSourceTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin to download source file
 */
class DownloadSourcePlugin implements Plugin<Project> {

    static final String TASK_DOWNLOAD_NAME = 'downloadSource'
    static final String CONFIG_NAME = 'downloadSourceConfig'

    private Project project

    @Override
    void apply(final Project project) {
        this.project = project
        this.project.extensions.create(CONFIG_NAME, DownloadSourceExtension)

        addDownloadSourceTask()
    }

    private addDownloadSourceTask() {
        project.tasks.create(TASK_DOWNLOAD_NAME, DownloadSourceTask)
        if(project.tasks.findByName('buildRpm') != null) {
            project.tasks.findByName('buildRpm').dependsOn(TASK_DOWNLOAD_NAME)
        }
    }
}
