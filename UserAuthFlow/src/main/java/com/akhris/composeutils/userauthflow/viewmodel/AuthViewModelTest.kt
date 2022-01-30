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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhris.composeutils.userauthflow.auth.AuthStage
import com.akhris.composeutils.userauthflow.auth.SignUpResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class AuthViewModelTest : ViewModel(), IAuthViewModel {

    private val _stage: MutableLiveData<AuthStage> = MutableLiveData()

    override val stage: LiveData<AuthStage> = _stage

    override fun initiateSignUp(name: String, userEmail: String, userPassword: String) {
        val error = false
        viewModelScope.launch {
            Timber.d("initiateSignUp. Wait for 2s")
            delay(2000)
//            _stage.value = if (error) {
//                AuthStage.SignUpStage.InitiateSignUp(SignUpResult.Failure.UserEmailExists)
//            } else {
//                AuthStage.SignUpStage.EnterConfirmationCode()
//            }
        }
    }

    override fun confirmSignUp(userEmail: String, confirmationCode: String) {
        val error = false
        viewModelScope.launch {
            Timber.d("initiateSignUp. Wait for 2s")
            delay(2000)
//            _signupStatus.value = if (error) {
//                SignUpResult.Failure.CodeMismatch
//            } else {
//                SignUpResult.Success
//            }
        }
    }

    override fun initiateSignIn(userEmail: String, userPassword: String) {
        TODO("Not yet implemented")
    }

    override fun initiateForgotPassword(userEmail: String) {
        TODO("Not yet implemented")
    }

    override fun confirmPasswordChange(newPassword: String, confirmationCode: String) {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }

    override fun updateCurrentSignStatus() {
        TODO("Not yet implemented")
    }
}