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

sealed class AuthStage {
    object Initial : AuthStage()

    class SignUpStage (val mode: SignUpMode): AuthStage()

    sealed class SignInStage : AuthStage() {
        //states of the sign in screen

        /**
         * Initial state - enter e-mail and password
         */
        class InitiateSignIn(val error: SignInResult.Failure? = null) : SignInStage()

        /**
         * Waiting for sign in result
         */
        object ProcessingSignIn : SignInStage()

        /**
         * Result of the sign in attempt: either success or failure
         */
        class SignInScreen(val result: SignInResult) : SignInStage()

    }

    sealed class ForgotPassword : AuthStage()
}

sealed class SignUpMode{
    class InitiateSignUp(val error: SignUpResult.Failure? = null) : SignUpMode()
    class EnterConfirmationCode(val error: SignUpResult.Failure? = null) : SignUpMode()
    class SignUpScreen(val result: SignUpResult) : SignUpMode()
}

sealed class SignUpResult {
    object Success : SignUpResult()
    sealed class Failure : SignUpResult() {
        //        object UserNotFound : SignUpResult.Failure()
//        object InvalidPassword : SignUpResult.Failure()
        object UsernameExists : SignUpResult.Failure()
        object UserEmailExists : SignUpResult.Failure()
        object ConfirmationCodeDeliveryFailure : SignUpResult.Failure()
        object CodeMismatch : SignUpResult.Failure()
        object CodeExpired : SignUpResult.Failure()
    }
}

sealed class SignInResult {
    object Success : SignInResult()
    sealed class Failure : SignInResult() {

    }
}