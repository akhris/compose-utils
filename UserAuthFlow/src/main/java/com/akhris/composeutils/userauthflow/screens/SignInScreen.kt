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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.akhris.composeutils.userauthflow.R
import com.akhris.composeutils.userauthflow.auth.AuthState
import com.akhris.composeutils.userauthflow.auth.EmailField
import com.akhris.composeutils.userauthflow.auth.PasswordField
import com.akhris.composeutils.userauthflow.composables.ProgressButton
import com.akhris.composeutils.userauthflow.utils.isValidEmail

@Composable
fun SignInScreen(
    eMail: String = "",
    onEmailChanged: (String) -> Unit,
    onSignInClicked: ((eMail: String, passWord: String) -> Unit)? = null,
    state: AuthState.SignIn? = null
) {

    var userPassword by remember { mutableStateOf("") }
    var errorChecksEnabled by remember { mutableStateOf(false) }


    var isEmailValid by remember(eMail) {
        mutableStateOf(eMail.isValidEmail())
    }

    var isPasswordValid by remember(userPassword) {
        mutableStateOf(userPassword.isNotEmpty())
    }


    LaunchedEffect(key1 = state) {
        when (state) {
            AuthState.SignIn.Failure.EMailNotExists -> {
                isEmailValid = false
            }
            AuthState.SignIn.Failure.InvalidPassword -> {
                isPasswordValid = false
            }
            else -> {
                //do nothing
            }
        }
    }

    Column {
        //email field
        EmailField(
            userEmail = eMail,
            onEmailChanged = {
                onEmailChanged.invoke(it)
            },
            isEmailValid = if (errorChecksEnabled) isEmailValid else true,
            errorRes = when (state) {
                AuthState.SignIn.Failure.EMailNotExists -> R.string.user_auth_email_not_found
                else -> R.string.user_auth_wrong_email
            }
        )

        //password field
        PasswordField(
            userPassword = userPassword,
            onPasswordChanged = {
                userPassword = it
            },
            isPasswordValid = if (errorChecksEnabled) isPasswordValid else true,
            errorRes = when {
                state == AuthState.SignIn.Failure.InvalidPassword ->
                    R.string.user_auth_invalid_password
                userPassword.isEmpty() -> R.string.user_auth_empty_password_error
                else -> R.string.user_auth_password_error
            },
            withVisibilityToggle = true
        )

        //sign in button
        Box(modifier = Modifier.align(Alignment.End)) {
            ProgressButton(
                isProgress = state == AuthState.SignIn.SignInInProgress,
                isEnabled = !errorChecksEnabled || (isEmailValid && isPasswordValid),
                onClick = {
                    if (!errorChecksEnabled) {
                        errorChecksEnabled = true
                    }
                    if (isPasswordValid && isEmailValid)
                        onSignInClicked?.invoke(eMail, userPassword)
                }, buttonTextRes = R.string.user_auth_sign_in
            )
        }
//        Row(
//            horizontalArrangement = Arrangement.End,
//            modifier = Modifier
//                .height(IntrinsicSize.Min)
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            if (state == AuthState.SignIn.SignInInProgress) {
//                CircularProgressIndicator(
//                    modifier = Modifier
//                        .size(32.dp)
//                        .padding(4.dp),
//                    color = MaterialTheme.colors.onSurface
//                )
//            }
//            Spacer(modifier = Modifier.width(8.dp))
//            Button(
//                modifier = Modifier
//                    .padding(vertical = 8.dp),
//                enabled = !errorChecksEnabled || (isEmailValid && isPasswordValid),
//                onClick = {
//
//                },
//                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
//            ) {
//                Text(text = stringResource(id = R.string.user_auth_sign_in))
//            }
//        }
    }

}