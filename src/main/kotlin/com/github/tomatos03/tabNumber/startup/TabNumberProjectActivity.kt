package com.github.tomatos03.tabNumber.startup

import com.github.tomatos03.tabNumber.listener.TabNumberFileEditorManagerListener
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class TabNumberProjectActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        project.messageBus.connect().subscribe(
            FileEditorManagerListener.FILE_EDITOR_MANAGER,
            TabNumberFileEditorManagerListener(project)
        )
    }
}