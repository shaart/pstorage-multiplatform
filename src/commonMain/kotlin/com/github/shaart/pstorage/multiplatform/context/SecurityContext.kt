package com.github.shaart.pstorage.multiplatform.context

/**
 * Application's security context
 */
interface SecurityContext {
    /**
     * Get stable key for encoding/decoding operations.
     *
     * This key must be the same between calls within the app instance, but different for re-launch.
     */
    fun currentEncodingKey(): String
}

