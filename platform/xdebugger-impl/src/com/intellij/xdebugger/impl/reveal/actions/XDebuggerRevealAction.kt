// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.xdebugger.impl.reveal.actions

import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.project.Project
import com.intellij.xdebugger.XDebuggerBundle
import com.intellij.xdebugger.impl.XDebuggerUtilImpl
import com.intellij.xdebugger.impl.reveal.RevealMemberValue
import com.intellij.xdebugger.impl.reveal.RevealParentValue
import com.intellij.xdebugger.impl.reveal.XDebuggerRevealManager
import com.intellij.xdebugger.impl.ui.tree.XDebuggerTree
import com.intellij.xdebugger.impl.ui.tree.actions.XDebuggerTreeActionBase
import com.intellij.xdebugger.impl.ui.tree.nodes.XValueNodeImpl
import icons.PlatformDebuggerImplIcons
import java.awt.event.MouseEvent

class XDebuggerRevealAction : XDebuggerTreeActionBase() {

    companion object {

        fun revealField(event: MouseEvent?, node: XValueNodeImpl) {
            ActionManager.getInstance().getAction("XDebugger.Reveal").actionPerformed(
                AnActionEvent.createFromInputEvent(
                    event,
                    XDebuggerRevealAction::class.java.name,
                    Presentation(),
                    object : DataContext {
                        override fun getData(dataId: String): Any? {
                            if (dataId == XDebuggerTree.XDEBUGGER_TREE_KEY.name) {
                                return node.tree
                            }
                            if (dataId == CommonDataKeys.PROJECT.name) {
                                return node.tree.project
                            }
                            return null
                        }

                    }
                )
            )
        }

    }

    override fun update(e: AnActionEvent) {
        val node = getSelectedNode(e.dataContext)
        val valueContainer = node?.valueContainer as? RevealMemberValue
        val presentation = e.presentation
        val project = e.project
        if (valueContainer == null || project == null) {
            presentation.isEnabledAndVisible = false
            return
        }
        val revealManager = XDebuggerRevealManager.getInstance(project)
        if (!revealManager.isRevealSupported(node)) {
            presentation.isEnabledAndVisible = false
            return
        }
        presentation.isVisible = true
        presentation.isEnabled = valueContainer.canBeRevealed()
        presentation.icon = if (revealManager.isItemRevealed(node)) PlatformDebuggerImplIcons.Reveal.RevealOff else PlatformDebuggerImplIcons.Reveal.RevealOn
        presentation.text = if (revealManager.isItemRevealed(node)) XDebuggerBundle.message("xdebugger.conceal.action") else XDebuggerBundle.message("xdebugger.reveal.action")

    }

    override fun perform(node: XValueNodeImpl?, nodeName: String, e: AnActionEvent) {
        node ?: return
        val project = e.project ?: return
        val nodeValue = node.valueContainer as? RevealMemberValue ?: return
        if (!nodeValue.canBeRevealed()) {
            return
        }
        val parentType = ((node.parent as? XValueNodeImpl)?.valueContainer as? RevealParentValue)?.getTypeName()

        if (parentType.isNullOrEmpty()) {
            return
        }

        if (XDebuggerRevealManager.getInstance(project).isItemRevealed(node)) {
            removePrioritizedItem(parentType, nodeName, project)
        } else {
            addPrioritizedItem(parentType, nodeName, project)
        }

        XDebuggerUtilImpl.rebuildTreeAndViews(node.tree)
    }

    private fun addPrioritizedItem(parentType: String, nodeName: String, project: Project) {
        XDebuggerRevealManager.getInstance(project).addItemInfo(parentType, nodeName)
    }

    private fun removePrioritizedItem(parentType: String, nodeName: String, project: Project) {
        XDebuggerRevealManager.getInstance(project).removeItemInfo(parentType, nodeName)
    }
}