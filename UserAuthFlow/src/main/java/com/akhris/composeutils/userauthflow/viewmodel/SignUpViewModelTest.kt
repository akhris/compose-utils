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
import com.akhris.composeutils.userauthflow.auth.SignUpResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class SignUpViewModelTest : ViewModel(), ISignUpViewModel {

    private val _signupStatus: MutableLiveData<SignUpResult?> = MutableLiveData()

    override val signupStatus: LiveData<SignUpResult?> = _signupStatus

    override fun initiateSignUp(name: String, userEmail: String, userPassword: String) {
        val error = false
        viewModelScope.launch {
            Timber.d("initiateSignUp. Wait for 2s")
            delay(2000)
            _signupStatus.value = if (error) {
                SignUpResult.Failure.UserEmailExists
            } else {
                SignUpResult.Success
            }
        }
    }

    override fun confirmSignUp(userEmail: String, confirmationCode: String) {
        val error = false
        viewModelScope.launch {
            Timber.d("initiateSignUp. Wait for 2s")
            delay(2000)
            _signupStatus.value = if (error) {
                SignUpResult.Failure.CodeMismatch
            } else {
                SignUpResult.Success
            }
        }
    }
}