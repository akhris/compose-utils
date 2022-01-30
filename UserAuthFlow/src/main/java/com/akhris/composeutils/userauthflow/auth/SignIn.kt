/*
 * Copyright 2021 Anatoly Khristianovsky
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.akhris.composeutils.userauthflow.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.akhris.composeutils.userauthflow.R
import com.akhris.composeutils.userauthflow.utils.isValidEmail
import timber.log.Timber


/*
@Composable
internal fun SignInContent(
    state: SignInState,
    eMail: String = "",
    onEmailChanged: (String) -> Unit = {},
    onSignIn: (eMail: String, password: String) -> Unit = { _, _ -> }
) {
    Timber.d("SignIn screen. state: $state")

    var userPassword: String by remember { mutableStateOf("") }



    AuthScreenPattern(
        title = stringResource(id = R.string.user_auth_sign_in),
        onBack = {}
    ) {
        if (state is SignInState.SignInResult && state.result == AuthResult.Success) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(id = R.string.user_auth_sign_in_success),
                style = MaterialTheme.typography.body1
            )
            return@AuthScreenPattern
        }

        Column(modifier = Modifier.wrapContentHeight()) {

            val emailNotFound = remember(state) {false
//                ((state is SignInState.SignInResult) && state.result == AuthResult.Failure.UserNotFound)
            }

            val passwordInvalid = remember(state) {
                false
//                ((state is SignInState.SignInResult) && state.result == AuthResult.Failure.InvalidPassword)
            }

            val isEmailValid = remember(emailNotFound, eMail) {
                !emailNotFound && eMail.isValidEmail()
            }

            var isPasswordBlank by remember { mutableStateOf(false) }

            val isPasswordValid = remember(passwordInvalid, userPassword, isPasswordBlank) {
                !passwordInvalid && !isPasswordBlank
//                    && userPassword.isNotBlank()
//            (state is LoginAction.SignIn.SignInResult) && state.result == AuthResult.Failure.InvalidPassword
            }




            EmailField(
                userEmail = eMail,
                onEmailChanged = { onEmailChanged(it) },
                isValidEmail = isEmailValid,
                errorID = if (emailNotFound) R.string.user_auth_email_not_found else R.string.user_auth_wrong_email
            )

            PasswordField(
                userPassword = userPassword,
                onPasswordChanged = {
                    userPassword = it
                    isPasswordBlank = userPassword.isBlank()
                },
                withVisibilityToggle = true,
                passwordValidate = {
                    isPasswordValid
                },
                errorRes = if (passwordInvalid) {
                    R.string.user_auth_invalid_password
                } else R.string.user_auth_password_error
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                if (state == SignInState.ProcessingSignIn) {

                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(32.dp)
                            .padding(8.dp)
                    )
                }

                Button(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    onClick = {
                        isPasswordBlank = userPassword.isBlank()
                        if (isEmailValid && isPasswordValid) {
                            Timber.d("SignIn screen onSignIn callback: $eMail - $userPassword")
                            onSignIn(eMail, userPassword)
                        }
                    },
                    enabled = state != SignInState.ProcessingSignIn && (isEmailValid && isPasswordValid)
                ) {
                    Text(text = stringResource(id = R.string.user_auth_sign_in))
                }
            }
        }
    }
}



@Preview(name = "initial state", group = "sign_in_light")
@Composable
fun SignInInitialTestLight() {
        SignInContent(state = SignInState.InitiateSignIn)
}

@Preview(name = "processing state", group = "sign_in_light")
@Composable
fun SignInProcessingTestLight() {
        SignInContent(SignInState.ProcessingSignIn)
}

//@Preview(name = "failure state: user not found", group = "sign_in_light")
//@Composable
//fun SignInFailureUserNotFoundTestLight() {
//        SignInContent(SignInState.SignInResult(AuthResult.Failure.UserNotFound))
//}
//
//@Preview(name = "failure state: wrong password", group = "sign_in_light")
//@Composable
//fun SignInFailureWrongPasswordTestLight() {
//        SignInContent(SignInState.SignInResult(AuthResult.Failure.InvalidPassword))
//}

@Preview(name = "successful sign in state", group = "sign_in_light")
@Composable
fun SignInSuccessfulTestLight() {
        SignInContent(SignInState.SignInResult(AuthResult.Success))
}

 */