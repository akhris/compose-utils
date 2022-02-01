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

package com.akhris.composeutils.userauthflow.auth

sealed class AuthState {
    object SignedIn : AuthState()
    object SignedOut : AuthState()
    sealed class SignUp : AuthState() {
        object SignUpInProgress : SignUp()
        object CodeWasSent : SignUp()
        sealed class Failure : SignUp() {
            object UsernameExists : Failure()
            object UserEmailExists : Failure()
            class Other(exception: Exception) : Failure()
        }
    }

    sealed class Confirmation : AuthState() {
        object CodeConfirmed : Confirmation()
        sealed class Failure : Confirmation() {
            object ConfirmationCodeDeliveryFailure : Failure()
            object CodeMismatch : Failure()
            object CodeExpired : Failure()
            class Other(exception: Exception) : SignUp.Failure()
        }
    }

    sealed class SignIn : AuthState() {
        object SignInInProgress : SignIn()
        sealed class Failure : SignIn() {
            object EMailNotExists : Failure()
            object InvalidPassword : Failure()
            class Other(exception: Exception) : SignIn.Failure()
        }
    }

    sealed class ForgotPassword : AuthState() {
        object SendingCodeInProgress : ForgotPassword()
        object CodeSent : ForgotPassword()
        sealed class Failure : ForgotPassword() {
            object EMailNotExists : Failure()
        }
    }
}





