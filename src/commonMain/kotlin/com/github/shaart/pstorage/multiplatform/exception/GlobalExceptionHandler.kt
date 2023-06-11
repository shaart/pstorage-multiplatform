package com.github.shaart.pstorage.multiplatform.exception

import com.github.shaart.pstorage.multiplatform.config.PstorageProperties
import com.github.shaart.pstorage.multiplatform.logger
import com.github.shaart.pstorage.multiplatform.util.ExceptionUtil
import java.awt.Dimension
import java.util.concurrent.Callable
import javax.swing.JOptionPane
import javax.swing.JScrollPane
import javax.swing.JTextArea

class GlobalExceptionHandler(
    properties: PstorageProperties
) {

    private val log = logger()
    private val applicationName: String = properties.applicationName

    fun <T> runSafely(function: Callable<T>): () -> Unit {
        return {
            try {
                function.call()
            } catch (e: Throwable) {
                when (e) {
                    is AppException -> {
                        log.error("Got application error: {}", e.message)
                        showErrorDialog(e)
                    }
                    else -> {
                        log.error("Got unexpected error", e)
                        showUnexpectedErrorDialog(e)
                    }
                }
            }
        }
    }

    private fun showErrorDialog(error: Throwable) {
        val title = "$applicationName: Error"
        val message = error.message ?: "Unknown error"
        val pane = object : JOptionPane(message, ERROR_MESSAGE) {
            // Limit width for long messages
            override fun getMaxCharactersPerLineCount(): Int = 120
        }
        val dialog = pane.createDialog(title)
        dialog.isVisible = true
        dialog.dispose()
    }

    private fun showUnexpectedErrorDialog(error: Throwable) {
        val title = "$applicationName: Unexpected error"
        val message = ExceptionUtil.getStacktrace(error)
        val textArea = JTextArea(message)
        textArea.lineWrap = true
        textArea.wrapStyleWord = true
        textArea.font = textArea.font.deriveFont(11f)

        val scrollPane = JScrollPane(textArea)
        scrollPane.preferredSize = Dimension(800, 600)

        JOptionPane.showMessageDialog(
            null,
            scrollPane,
            title,
            JOptionPane.ERROR_MESSAGE
        )
    }
}