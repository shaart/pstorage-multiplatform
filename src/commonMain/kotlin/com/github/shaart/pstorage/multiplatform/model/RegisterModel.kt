package com.github.shaart.pstorage.multiplatform.model

data class RegisterModel(
    val login: String,
    val password: String,
    val confirmationPassword: String,
) {
    fun isNonMatchingPasswords(): Boolean {
        return password != confirmationPassword
    }
}