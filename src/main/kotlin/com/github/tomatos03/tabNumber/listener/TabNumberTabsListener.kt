package com.github.tomatos03.tabNumber.listener

import com.intellij.ui.tabs.TabInfo
import com.intellij.ui.tabs.TabsListener

/**
 * Tabs 事件监听器，监听 tab 移动、选中等事件
 *
 * @author : Tomatos
 * @date : 2026/1/7
 */
class TabNumberTabsListener( private val onTabsChanged: () -> Unit ) : TabsListener {
    
    override fun selectionChanged(oldSelection: TabInfo?, newSelection: TabInfo?) {
        onTabsChanged()
    }

    override fun tabRemoved(tabToRemove: TabInfo) {
        onTabsChanged()
    }

    override fun tabsMoved() {
        onTabsChanged()
    }
}