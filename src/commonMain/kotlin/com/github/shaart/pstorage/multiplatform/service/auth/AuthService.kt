package com.github.shaart.pstorage.multiplatform.service.auth

import com.github.shaart.pstorage.multiplatform.dto.UserViewDto
import com.github.shaart.pstorage.multiplatform.model.LoginModel
import com.github.shaart.pstorage.multiplatform.model.RegisterModel

interface AuthService {
    fun register(registerModel: RegisterModel): UserViewDto
    fun login(loginModel: LoginModel): UserViewDto
}