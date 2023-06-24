package com.github.shaart.pstorage.multiplatform.generator

interface PasswordGenerator {

    /**
     * Generate secure random password's value with several special chars, letters and digits.
     */
    fun generateSecureRandomPassword(
        numbersCount: Int = 10,
        specialCharsCount: Int = 7,
        upperCaseLettersCount: Int = 11,
        lowerCaseLettersCount: Int = 12,
    ): String
}