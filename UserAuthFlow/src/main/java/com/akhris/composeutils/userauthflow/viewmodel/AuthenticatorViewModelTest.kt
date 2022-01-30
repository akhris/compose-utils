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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class AuthenticatorViewModelTest   : ViewModel(), IAuthenticatorViewModel {


    private val _signStatus: MutableLiveData<SignStatus?> = MutableLiveData(null)

    override val signStatus: LiveData<SignStatus?> = _signStatus


    override fun initiateSignUp(name: String, userEmail: String, userPassword: String) {
        val error = false
        viewModelScope.launch {

            Timber.d("initiateSignUp. Wait for 2s")
            delay(2000)
            _signStatus.value = if (error) {
                SignStatus.Error(IllegalStateException("User was not found"))
            } else {
                Timber.d("sending test confirmation code...")
                SignStatus.SignUp.ConfirmationCodeWasSent
            }
        }
    }

    override fun confirmSignUp(userEmail: String, confirmationCode: String) {
        val error = false
        viewModelScope.launch {
            Timber.d("confirmSignUp. Wait for 3s")
            delay(3000)
            _signStatus.value = if (error) {
                SignStatus.Error(IllegalStateException("User not confirmed"))
            } else {
                Timber.d("sign up confirmed")
                SignStatus.SignUp.SignUpConfirmed
            }
        }
    }

    override fun initiateSignIn(userEmail: String, userPassword: String) {
        val error = false
        viewModelScope.launch {
            Timber.d("initiateSignIn. Wait for 2s")
            _signStatus.value = SignStatus.SigningInProgress
            delay(2000)
            _signStatus.value = if (error) {
                SignStatus.Error(IllegalStateException("Invalid password"))
            } else {
                Timber.d("sign in success")
                SignStatus.SignedIn
            }
        }
    }

    override fun initiateForgotPassword(userEmail: String) {
        TODO("Not yet implemented")
    }

    override fun confirmPasswordChange(newPassword: String, confirmationCode: String) {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        val error = false
        viewModelScope.launch {
            Timber.d("initiateSignOut. Wait for 2s")
            _signStatus.value = SignStatus.SigningOutInProgress
            delay(2000)
            _signStatus.value = if (error) {
                SignStatus.Error(IllegalStateException("Unknown error"))
            } else {
                Timber.d("sign out success")
                SignStatus.SignedOut
            }
        }
    }

    override fun updateCurrentSignStatus() {
        val isSignedIn = false
        _signStatus.value = when (isSignedIn) {
            true -> SignStatus.SignedIn
            false -> SignStatus.SignedOut
        }
    }
}