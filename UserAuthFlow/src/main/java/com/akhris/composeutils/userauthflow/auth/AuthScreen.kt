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

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.akhris.composeutils.userauthflow.R
import com.akhris.composeutils.userauthflow.screens.InitialScreen
import com.akhris.composeutils.userauthflow.screens.SignInScreen
import com.akhris.composeutils.userauthflow.screens.SignUpConfirmationScreen
import com.akhris.composeutils.userauthflow.screens.SignUpInitialScreen
import com.akhris.composeutils.userauthflow.viewmodel.AuthenticatorViewModelTest
import com.akhris.composeutils.userauthflow.viewmodel.IAuthenticator

@Composable
fun AuthScreenContent(
    authenticator: IAuthenticator = AuthenticatorViewModelTest(),
    onDismiss: (() -> Unit)? = null
) {

    val authState by remember(authenticator) {
        authenticator.signStatus
    }.observeAsState()

    var eMail by remember { mutableStateOf("") }

    var authScreen by remember { mutableStateOf<AuthScreen>(AuthScreen.Initial) }
    var previousScreen by remember { mutableStateOf<AuthScreen?>(null) }

    AuthScreenPattern(
        onBack = previousScreen?.let {
            {
                authScreen = it
            }
        },
        onDismiss = onDismiss,
        titleRes = when (authScreen) {
            AuthScreen.ForgotPassword -> R.string.user_auth_forgot_password
            AuthScreen.Initial -> null
            AuthScreen.SignIn -> R.string.user_auth_sign_in
            AuthScreen.SignUp -> R.string.user_auth_sign_up
            AuthScreen.SignUpVerification -> R.string.user_auth_confirm_code
        }
    ) {

        when (authScreen) {
            AuthScreen.Initial -> {
                previousScreen = null
                InitialScreen(
                    eMail = eMail,
                    onEmailChanged = { eMail = it },
                    onForgotPasswordClicked = {
                        previousScreen = authScreen
                        authScreen = AuthScreen.ForgotPassword
                    },
                    onSignUpClicked = {
                        previousScreen = authScreen
                        authScreen = AuthScreen.SignUp
                    },
                    onSignInClicked = {
                        previousScreen = authScreen
                        authScreen = AuthScreen.SignIn
                    }

                )
            }
            AuthScreen.ForgotPassword -> TODO()
            AuthScreen.SignIn -> SignInScreen(
                eMail = eMail,
                onEmailChanged = { eMail = it },
                onSignInClicked = { eMail, passWord ->
                    authenticator.initiateSignIn(eMail, passWord)
                },
                state = authState as? AuthState.SignIn
            )
            AuthScreen.SignUp -> {
                if (authState == AuthState.SignUp.CodeWasSent) {
                    previousScreen = authScreen
                    authScreen = AuthScreen.SignUpVerification
                }
                SignUpInitialScreen(
                    eMail = eMail,
                    onEmailChanged = { eMail = it },
                    onSignupClicked = { userName, email, password ->
                        authenticator.initiateSignUp(userName, email, password)
                    },
                    state = authState as? AuthState.SignUp
                )
            }
            AuthScreen.SignUpVerification -> {
                if (authState == AuthState.Confirmation.CodeConfirmed) {
                    previousScreen = authScreen
//                authScreen = AuthScreen.SignUpVerification
                }
                SignUpConfirmationScreen(
                    eMail = eMail,
                    onConfirmClicked = { code ->
                        authenticator.confirmSignUp(eMail, code)
                    },
                    state = authState as? AuthState.Confirmation
                )
            }
        }
    }

}

sealed class AuthScreen {
    object Initial : AuthScreen()
    object SignUp : AuthScreen()
    object SignUpVerification : AuthScreen()
    object SignIn : AuthScreen()
    object ForgotPassword : AuthScreen()
}

@Composable
fun AuthScreenPattern(
    onBack: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    @StringRes titleRes: Int? = null,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(4f.dp),
        modifier = Modifier
//            .semantics { contentDescription = dialogContentDescription }
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(8.dp)
        ) {

            Row {
                Icon(
                    imageVector = if (onBack != null) Icons.Rounded.ArrowBack else Icons.Rounded.Close,
                    contentDescription = "back",
                    Modifier
                        .padding(16.dp)
                        .clickable {
                            if (onBack != null) {
                                onBack()
                            } else {
                                onDismiss?.invoke()
                            }
                        }
//                            .align(if (onBack != null) Alignment.Start else Alignment.End),
                    ,
                    tint = MaterialTheme.colors.onSurface
                )
                titleRes?.let {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        text = stringResource(id = it),
                        style = MaterialTheme.typography.h4
//                            textAlign = when (LocalLayoutDirection.current) {
//                                LayoutDirection.Ltr -> TextAlign.Start
//                                LayoutDirection.Rtl -> TextAlign.End
//                            }
                    )
                }
            }



            content()
        }
    }
}


@Composable
internal fun PasswordField(
    userPassword: String,
    onPasswordChanged: ((String) -> Unit)? = null,
    @StringRes hintRes: Int = R.string.user_auth_password_hint,
    @StringRes errorRes: Int? = R.string.user_auth_password_error,
    isPasswordValid: Boolean = true,
    withVisibilityToggle: Boolean = false
) {
    //password text field
    var passwordVisible by remember { mutableStateOf(false) }
//    val isPasswordValid = remember(userPassword, passwordValidate) { !passwordValidate(userPassword) }
    Column {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
//                    .testTag(textTestTag)
            ,
            value = userPassword,
            onValueChange = {
                onPasswordChanged?.invoke(it)
            },
            isError = !isPasswordValid,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            label = { Text(stringResource(id = hintRes)) },
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            trailingIcon = if (withVisibilityToggle) {
                {
                    Icon(
                        painter = if (passwordVisible) {
                            painterResource(id = R.drawable.ic_round_visibility_24)
                        } else {
                            painterResource(id = R.drawable.ic_round_visibility_off_24)
                        },
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            passwordVisible = !passwordVisible
                        })
                }
            } else null
        )

        if (!isPasswordValid && errorRes != null) {
            Text(
                text = stringResource(id = errorRes),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(start = 16.dp)
//                    .testTag(errorTestTag)
            )
        }
    }
}

@Composable
internal fun EmailField(
    userEmail: String,
    onEmailChanged: (String) -> Unit,
    isEmailValid: Boolean = true,
    @StringRes errorRes: Int = R.string.user_auth_wrong_email
) {
    //e-mail text field
    BaseAuthField(
        text = userEmail,
        onTextChanged = onEmailChanged,
        withClearIcon = true,
        hintID = R.string.user_auth_email_hint,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        isValid = isEmailValid,
        errorID = errorRes
    )
//    TextField(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp)
////                    .testTag(textTestTag)
//        ,
//        value = userEmail,
//        onValueChange = {
//            onEmailChanged(it)
//        },
//        label = { Text(stringResource(id = R.string.user_auth_email_hint)) },
//        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
//        trailingIcon = if (userEmail.isNotEmpty()) {
//            {
//                Icon(
//                    imageVector = Icons.Rounded.Clear,
//                    contentDescription = null,
//                    modifier = Modifier.clickable {
//                        onEmailChanged("")
//                    })
//            }
//        } else null, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
//    )
}

@Composable
internal fun BaseAuthField(
    text: String,
    onTextChanged: ((String) -> Unit)? = null,
    @StringRes hintID: Int? = null,
    @StringRes errorID: Int? = null,
    withClearIcon: Boolean,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isValid: Boolean = true
) {

    Column {

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)

//                    .testTag(textTestTag)
            ,
            value = text,
            isError = !isValid,
            onValueChange = {
                onTextChanged?.invoke(it)
            },
            label = hintID?.let { { Text(stringResource(id = it)) } },
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            trailingIcon = if (text.isNotEmpty() && withClearIcon) {
                {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            onTextChanged?.invoke("")
                        })
                }
            } else null, keyboardOptions = keyboardOptions
        )

        if (!isValid && errorID != null) {
            Text(
                text = stringResource(id = errorID),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(start = 16.dp)
//                    .testTag(errorTestTag)
            )
        }
    }
}

