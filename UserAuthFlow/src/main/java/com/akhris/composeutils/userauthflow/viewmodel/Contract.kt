/*
 * Copyright (c) 2022. Anatoly Khristianovsky.  All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.akhris.composeutils.userauthflow.viewmodel

import androidx.lifecycle.LiveData
import com.akhris.composeutils.userauthflow.auth.AuthStage
import com.akhris.composeutils.userauthflow.auth.SignUpResult

interface IAuthenticatorViewModel {
    val signStatus: LiveData<SignStatus?>
    fun initiateSignUp(name: String, userEmail: String, userPassword: String)
    fun confirmSignUp(userEmail: String, confirmationCode: String)
    fun initiateSignIn(userEmail: String, userPassword: String)
    fun initiateForgotPassword(userEmail: String)
    fun confirmPasswordChange(newPassword: String, confirmationCode: String)
    fun signOut()
    fun updateCurrentSignStatus()
}

interface IAuthViewModel {
    val stage: LiveData<AuthStage>
    fun initiateSignUp(name: String, userEmail: String, userPassword: String)
    fun confirmSignUp(userEmail: String, confirmationCode: String)
    fun initiateSignIn(userEmail: String, userPassword: String)
    fun initiateForgotPassword(userEmail: String)
    fun confirmPasswordChange(newPassword: String, confirmationCode: String)
    fun signOut()
    fun updateCurrentSignStatus()
}

interface ISignUpViewModel {
    val signupStatus: LiveData<SignUpResult?>
    fun initiateSignUp(name: String, userEmail: String, userPassword: String)
    fun confirmSignUp(userEmail: String, confirmationCode: String)
}

sealed class SignStatus {
    object SignedIn : SignStatus()
    object SignedOut : SignStatus()

    sealed class SignUp : SignStatus() {
        object ConfirmationCodeWasSent : SignUp()
        object SignUpConfirmed : SignUp()
    }

    object SigningInProgress : SignStatus()
    object SigningOutInProgress : SignStatus()

    object PasswordChanged : SignStatus()
    data class Error(val error: Exception) : SignStatus()
}