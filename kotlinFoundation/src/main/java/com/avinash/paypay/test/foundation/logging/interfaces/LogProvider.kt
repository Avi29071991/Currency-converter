package com.avinash.paypay.test.foundation.logging.interfaces

/**
 * Interface used to provide logging capability
 */
interface LogProvider {
    /**
     * Log message
     * @param level - log level for the message or event
     * @param tag - tag for the log message
     * @param message - message that needs to be logged
     * @param error - error to be logged
     */
    fun log(level: LogLevel, tag: String, message: String, error: Throwable? = null)
}

/**
 * Log levels that can be used are:
 * - Use [LogLevel.INFO] for additional information for a event
 * - Use [LogLevel.DEBUG] for useful debugging information
 * - Use [LogLevel.WARNING] for warnings about possible awkward situations
 * - Use [LogLevel.ERROR] for major errors/events
 * - Use [LogLevel.CUSTOM] for custom errors/events
 */
enum class LogLevel {
    INFO, DEBUG, ERROR, WARNING, CUSTOM
}
