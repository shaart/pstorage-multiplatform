package com.github.shaart.pstorage.multiplatform.enums

enum class EncryptionType {
    AES_CODER;

    /**
     * Additional related constants that are not 'encryption types'.
     */
    class Constants {
        companion object {
            /**
             * Hash v1.
             */
            const val HASH = "HASH"
        }
    }
}
