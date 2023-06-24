package com.github.shaart.pstorage.multiplatform.context

data class DefaultSecurityContext(
    val currentEncodingKey: String
) : SecurityContext {

    override fun currentEncodingKey(): String {
        return currentEncodingKey
    }
}