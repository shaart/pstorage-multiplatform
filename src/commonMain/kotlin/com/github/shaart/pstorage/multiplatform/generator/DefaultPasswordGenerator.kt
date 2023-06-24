package com.github.shaart.pstorage.multiplatform.generator

import java.security.SecureRandom
import java.util.*
import java.util.stream.Stream

class DefaultPasswordGenerator : PasswordGenerator {
    companion object {
        const val SPECIAL_CHARACTERS = "!@#$%^&*()-_+=/{}`~<>?"
        const val LETTERS_LOWER_CASE = "abcdefghijklmnopqrstuvwxyz"
        const val LETTERS_UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        const val DIGITS = "0123456789"
    }

    override fun generateSecureRandomPassword(
        numbersCount: Int,
        specialCharsCount: Int,
        upperCaseLettersCount: Int,
        lowerCaseLettersCount: Int
    ): String {
        val passwordLength =
            numbersCount + specialCharsCount + upperCaseLettersCount + lowerCaseLettersCount
        val passwordChars: MutableList<Char> = ArrayList(passwordLength)

        randomCharsFrom(DIGITS, numbersCount).forEach(passwordChars::add)
        randomCharsFrom(SPECIAL_CHARACTERS, specialCharsCount).forEach(passwordChars::add)
        randomCharsFrom(LETTERS_UPPER_CASE, upperCaseLettersCount).forEach(passwordChars::add)
        randomCharsFrom(LETTERS_LOWER_CASE, lowerCaseLettersCount).forEach(passwordChars::add)

        passwordChars.shuffle()

        return passwordChars.joinToString("")
    }

    private fun randomCharsFrom(origin: String, count: Int): Stream<Char> {
        val random: Random = SecureRandom()
        val specialChars = random.ints(count.toLong(), 0, origin.length)
        return specialChars.mapToObj { origin[it] }
    }
}
