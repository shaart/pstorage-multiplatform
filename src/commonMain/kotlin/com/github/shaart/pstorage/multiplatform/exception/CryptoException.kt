package com.github.shaart.pstorage.multiplatform.exception

class CryptoException : AppException {
    constructor(message: String, cause: Exception) : super(message, cause)
    constructor(message: String) : super(message)
}
