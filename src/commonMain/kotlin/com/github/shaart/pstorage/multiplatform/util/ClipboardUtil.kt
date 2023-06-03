package com.github.shaart.pstorage.multiplatform.util

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

class ClipboardUtil {
    companion object {
        fun setValueToClipboard(copyValue: String) {
            val systemClipboard = Toolkit.getDefaultToolkit().systemClipboard
            val selection = StringSelection(copyValue)
            systemClipboard.setContents(selection, selection)
        }
    }
}