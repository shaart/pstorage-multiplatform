package com.github.shaart.pstorage.multiplatform.generator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.RepeatedTest
import java.util.stream.Stream

class DefaultPasswordGeneratorTest {

    private val passwordGenerator = DefaultPasswordGenerator()

    @RepeatedTest(25)
    fun generateSecureRandomPassword() {
        val passwords = Stream.generate { passwordGenerator.generateSecureRandomPassword() }
            .limit(10000)
            .toList()

        assertThat(passwords)
            .allSatisfy { generatedPassword ->
                assertThat(generatedPassword)
                    .isNotEmpty()
                    .matches(
                        { it.filter { char -> "0123456789".contains(char) }.length >= 10 },
                        "Should contains more than 10 digits"
                    )
                    .matches(
                        { it.filter { char -> "!@#\$%^&*()-_+=/{}`~<>?".contains(char) }.length >= 7 },
                        "Should contains more than 7 special characters"
                    )
                    .matches(
                        { it.filter { char -> "ABCDEFGHIJKLMNOPQRSTUVWXYZ".contains(char) }.length >= 11 },
                        "Should contains more than 11 uppercase letters"
                    )
                    .matches(
                        { it.filter { char -> "abcdefghijklmnopqrstuvwxyz".contains(char) }.length >= 12 },
                        "Should contains more than 12 lowercase letters"
                    )
            }.doesNotHaveDuplicates()
    }
}