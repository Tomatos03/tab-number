package com.github.tomatos03.tabNumber.provider

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.fileEditor.impl.EditorTabTitleProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

/**
 * 编辑器 Tab 标题提供者
 * 通过官方扩展点控制 Tab 标题的生成，从源头解决标题被重置的问题
 *
 * @author : Tomatos
 * @date : 2026/1/7
 */
class TabNumberEditorTabTitleProvider : EditorTabTitleProvider {
    private val separator = "."

    override fun getEditorTabTitle(project: Project, file: VirtualFile): String? {
        val fileEditorManagerEx = FileEditorManagerEx.getInstanceEx(project)
        val windows = fileEditorManagerEx.windows

        for (window in windows) {
            val files = window.fileList
            for ((index, f) in files.withIndex()) {
                if (f == file) {
                    val fileName = f.presentableName
                    return "${index + 1}$separator$fileName"
                }
            }
        }
        return null
    }
}
