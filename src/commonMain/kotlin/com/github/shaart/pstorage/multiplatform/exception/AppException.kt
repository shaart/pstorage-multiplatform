package com.github.shaart.pstorage.multiplatform.exception

open class AppException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Exception) : super(message, cause)
}