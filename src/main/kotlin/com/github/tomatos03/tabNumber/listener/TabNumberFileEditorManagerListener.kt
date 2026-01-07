package com.github.tomatos03.tabNumber.listener

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.fileEditor.impl.EditorWindow
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

/**
 * 
 * 
 * @author : Tomatos
 * @date : 2026/1/7
 */
class TabNumberFileEditorManagerListener(project: Project) : FileEditorManagerListener {
    private val fileEditorManagerEx = FileEditorManagerEx.getInstanceEx(project)
    private val separator = ".";
    private val registeredTabs = mutableSetOf<Int>()

    init {
        for (window in fileEditorManagerEx.windows) {
            registerTabsListener(window)
        }
    }

    override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
        // 检查是否有新窗口，如果有则添加监听
        for (window in fileEditorManagerEx.windows) {
            registerTabsListener(window)
        }
        refreshTabNumberAsync()
    }

    override fun fileClosed(source: FileEditorManager, file: VirtualFile) {
        cleanupClosedTabs()
        refreshTabNumberAsync()
    }

    override fun selectionChanged(event: FileEditorManagerEvent) {
        refreshTabNumberAsync()
    }

    /**
     * 为窗口的 tabbedPane 添加 TabsListener
     */
    private fun registerTabsListener(window: EditorWindow) {
        val tabs = window.tabbedPane.tabs
        val tabsId = System.identityHashCode(tabs)

        // 防止重复注册监听
        if (registeredTabs.contains(tabsId)) {
            return
        }

        registeredTabs.add(tabsId)

        // 创建独立的 TabNumberTabsListener 实例
        val tabsListener = TabNumberTabsListener(
            onTabsChanged = { 
                refreshTabNumberAsync()
            }
        )

        tabs.addListener(tabsListener)
    }

    /**
     * 清理已关闭窗口的 tabs 记录
     */
    private fun cleanupClosedTabs() {
        val currentTabs = mutableSetOf<Int>()

        // 收集当前存在的所有 tabs 的 id
        for (window in fileEditorManagerEx.windows) {
            val tabs = window.tabbedPane.tabs
            val tabsId = System.identityHashCode(tabs)
            currentTabs.add(tabsId)
        }

        // 移除已关闭的 tabs 记录
        registeredTabs.retainAll(currentTabs)
    }

    fun refreshTabNumberAsync() {
        ApplicationManager.getApplication().invokeLater {
            for (window in fileEditorManagerEx.windows) {
                val jTabs = window.tabbedPane.tabs
                var fileTabIndex = 1

                for (i in jTabs.tabs.indices) {
                    val tabInfo = jTabs.tabs[i]
                    val fileName = window.fileList[i].presentableName

                    val newTabText = "$fileTabIndex$separator$fileName"
                    if (tabInfo.text != newTabText) {
                        tabInfo.setText(newTabText)
                    }
                    fileTabIndex++
                }

            }
        }
    }
}