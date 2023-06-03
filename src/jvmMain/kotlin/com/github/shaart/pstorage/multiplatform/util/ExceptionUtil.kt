package com.github.shaart.pstorage.multiplatform.util

import java.io.PrintWriter
import java.io.StringWriter

class ExceptionUtil {
    companion object {

        /**
         * Get exception's stacktrace as string.
         *
         * @param e an exception
         * @return string with stacktrace
         */
        fun getStacktrace(e: Throwable): String {
            val stringWriter = StringWriter()
            val printWriter = PrintWriter(stringWriter)
            e.printStackTrace(printWriter)
            return stringWriter.toString()
        }
    }
}