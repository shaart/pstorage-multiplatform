package com.github.shaart.pstorage.multiplatform.service.mask

class DefaultMasker : Masker {
    override fun username(login: String): String {
        if (login.isEmpty()) {
            return login
        }
        if (login.length > 5) {
            return login.substring(0, 3).plus("*".repeat(5))
        }
        return login.substring(0, 1).plus("*".repeat(7))
    }

    override fun alias(alias: String): String {
        if (alias.isEmpty()) {
            return alias
        }
        if (alias.length > 3) {
            return alias.substring(0, 2).plus("*".repeat(6))
        }
        return alias.substring(0, 1).plus("*".repeat(7))
    }
}