package com.github.shaart.pstorage.multiplatform.service

import com.github.shaart.pstorage.multiplatform.db.UserQueries
import com.github.shaart.pstorage.multiplatform.exception.AuthNoMatchingUserException
import com.github.shaart.pstorage.multiplatform.model.LoginModel
import com.github.shaart.pstorage.multiplatform.model.RegisterModel
import migrations.Usr_users

class AuthService(
    private val userQueries: UserQueries
) {
    fun register(registerModel: RegisterModel): Usr_users {
        return userQueries.transactionWithResult {
            userQueries.createUser(
                registerModel.login,
                registerModel.password
            )
            val insertedId = userQueries.lastInsertRowId().executeAsOne()
            userQueries.findUserById(insertedId).executeAsOne()
        }
    }

    fun login(loginModel: LoginModel): Usr_users {
        return userQueries.findUserByNameAndPassword(loginModel.login, loginModel.password)
            .executeAsOneOrNull() ?: throw AuthNoMatchingUserException()
    }
}