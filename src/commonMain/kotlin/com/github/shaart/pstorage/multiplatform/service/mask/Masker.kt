package com.github.shaart.pstorage.multiplatform.service.mask

interface Masker {
    fun username(login: String): String
    fun alias(alias: String): String
}
