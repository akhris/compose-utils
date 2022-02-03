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

package com.akhris.composeutils.userauthflow.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.akhris.composeutils.userauthflow.R
import com.akhris.composeutils.userauthflow.auth.AuthState
import com.akhris.composeutils.userauthflow.auth.BaseAuthField
import com.akhris.composeutils.userauthflow.auth.EmailField
import com.akhris.composeutils.userauthflow.auth.PasswordField
import com.akhris.composeutils.userauthflow.composables.ProgressButton
import com.akhris.composeutils.userauthflow.utils.isValidEmail

/**
 * Initial Sign Up Screen
 * Contains fields for:
 * - user name
 * - user email
 * - user password
 * - user password (repeat)
 * and button "sign up"
 */
@Composable
fun SignUpInitialScreenContent(
    eMail: String = "",
    onEmailChanged: ((String) -> Unit)? = null,
    onSignupClicked: ((username: String, email: String, password: String) -> Unit)? = null,
    state: AuthState.SignUp? = null
) {

    var errorChecksEnabled by remember { mutableStateOf(false) }


    var userName by remember { mutableStateOf("") }
    var userPassword1 by remember { mutableStateOf("") }
    var userPassword2 by remember { mutableStateOf("") }

    var isUserNameValid by remember(userName) {
        mutableStateOf(userName.isNotBlank())
    }

    var isEmailValid by remember(eMail) {
        mutableStateOf(eMail.isValidEmail())
    }

    val isPassword1Valid = remember(userPassword1) { userPassword1.isNotBlank() }

    val isPassword2Valid = remember(userPassword1, userPassword2) { userPassword1 == userPassword2 }

    val noErrors = remember(isUserNameValid, isEmailValid, isPassword1Valid, isPassword2Valid) {
        isUserNameValid && isEmailValid && isPassword1Valid && isPassword2Valid
    }

    LaunchedEffect(key1 = state) {
        when (state) {
            AuthState.SignUp.Failure.UserEmailExists -> {
                isEmailValid = false
            }
            AuthState.SignUp.Failure.UsernameExists -> {
                isUserNameValid = false
            }
            else -> {}
        }
    }



    Column(modifier = Modifier.wrapContentHeight()) {        //user name field
        BaseAuthField(
            text = userName,
            onTextChanged = { userName = it },
            withClearIcon = true,
            hintID = R.string.user_auth_name_hint,
            isValid = if (errorChecksEnabled) isUserNameValid else true,
            errorID = when (state) {
                AuthState.SignUp.Failure.UsernameExists -> R.string.user_auth_sign_up_error_user_name_exists
                else -> R.string.user_auth_sign_up_error_empty_user_name
            }
        )

        //email field
        EmailField(
            userEmail = eMail,
            onEmailChanged = { onEmailChanged?.invoke(it) },
            isEmailValid = if (errorChecksEnabled) isEmailValid else true,
            errorRes = when (state) {
                AuthState.SignUp.Failure.UserEmailExists -> R.string.user_auth_sign_up_error_user_email_exists
                else -> R.string.user_auth_wrong_email
            }
        )

        //password field
        PasswordField(
            userPassword = userPassword1,
            onPasswordChanged = { userPassword1 = it },
            isPasswordValid = if (errorChecksEnabled) isPassword1Valid else true,
            errorRes = R.string.user_auth_password_error
        )
//            passwordValidate = { isValidPassword1 || it.isNotEmpty() })

        //password repeat field
        PasswordField(
            userPassword = userPassword2,
            onPasswordChanged = { userPassword2 = it },
            hintRes = R.string.user_auth_repeat_password_hint,
            errorRes = R.string.user_auth_password_repeat_error,
            isPasswordValid = if (errorChecksEnabled) isPassword2Valid else true
        )
//            passwordValidate = { isValidPassword2 || (it == userPassword1) })

        Box(modifier = Modifier.align(Alignment.End)) {
            ProgressButton(
                onClick = {
                    if (!errorChecksEnabled) {
                        errorChecksEnabled = true
                    }
                    if (noErrors)
                        onSignupClicked?.invoke(userName, eMail, userPassword1)
                },
                buttonTextRes = R.string.user_auth_sign_up,
                isEnabled = !errorChecksEnabled || noErrors,
                isProgress = state == AuthState.SignUp.SignUpInProgress
            )
        }

    }
}

@Preview(name = "sign up initial screen", group = "sign up")
@Composable
fun SignUpInitialTest() {
    SignUpInitialScreenContent()
}
