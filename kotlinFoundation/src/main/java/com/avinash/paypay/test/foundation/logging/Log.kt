package com.avinash.paypay.test.foundation.logging

import com.avinash.paypay.test.foundation.base.ProviderCollection
import com.avinash.paypay.test.foundation.base.interfaces.Capability
import com.avinash.paypay.test.foundation.logging.interfaces.LogLevel
import com.avinash.paypay.test.foundation.logging.interfaces.LogProvider
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * This Singleton class is used to provide logging capability.
 *
 * **Check** [Confluence-Logging](https://confluence.srv.westpac.com.au/display/MO/Logging)
 * @see [Confluence-Logging](https://confluence.srv.westpac.com.au/display/MO/Logging)
 */
object Log : ProviderCollection<LogProvider>(), Capability {

    private const val DEFAULT_CALL_STACK_INDEX = 3
    private const val CALL_STACK_INDEX_WITH_PARAMS = 2
    private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")

    /**
     * Log a information message
     * @param message - message to be logged
     * @param error - error to be logged (optional)
     */
    fun info(message: String, error: Throwable? = null) {
        dispatchToLogger(level = LogLevel.INFO, message = message, error = error)
    }

    /**
     * Log a debug message
     * @param message - message to be logged
     * @param error - error to be logged (optional)
     */
    fun debug(message: String, error: Throwable? = null) {
        dispatchToLogger(level = LogLevel.DEBUG, message = message, error = error)
    }

    /**
     * Log a error message
     * @param message - message to be logged
     * @param error - error to be logged (optional)
     */
    fun error(message: String, error: Throwable? = null) {
        dispatchToLogger(level = LogLevel.ERROR, message = message, error = error)
    }

    /**
     * Log a warning message
     * @param message - message to be logged
     * @param error - error to be logged (optional)
     */
    fun warning(message: String, error: Throwable? = null) {
        dispatchToLogger(level = LogLevel.WARNING, message = message, error = error)
    }

    /**
     * Dispatch log event to providers
     * @param level - level of log message (INFO, DEBUG, WARNING, ERROR)
     * @param tag - tag for the log message
     * @param message - message to be logged
     * @param error - error to be logged (optional)
     */
    private fun dispatchToLogger(
        level: LogLevel,
        message: String,
        error: Throwable? = null,
    ) {
        val callStackIndex =
            if (error != null) CALL_STACK_INDEX_WITH_PARAMS else DEFAULT_CALL_STACK_INDEX
        val stackTrace = (
            error?.let {
                Throwable(error)
            } ?: Throwable()
            ).stackTrace
        check(stackTrace.size > callStackIndex) { "Synthetic stacktrace didn't have enough elements" }
        val stackTraceElement = stackTrace[callStackIndex]
        val fileName = extractClassName(stackTraceElement)
        val functionName = stackTraceElement.methodName
        val lineNumber = stackTraceElement.lineNumber

        val logMessage = listOfNotNull(
            String.format("[C:%s]", fileName),
            String.format("[M:%s]", functionName),
            String.format("[L:%d]", lineNumber),
            message
        ).joinToString(":")

        providers.forEach {
            it.log(
                level = level,
                tag = fileName,
                message = logMessage,
                error = error
            )
        }
    }

    /**
     * Extract the class name without any anonymous class suffixes (e.g., `Foo$1` becomes `Foo`)
     */
    private fun extractClassName(element: StackTraceElement): String {
        var tag = element.className
        val m: Matcher = ANONYMOUS_CLASS.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }
        return tag.substring(tag.lastIndexOf('.') + 1)
    }
}
