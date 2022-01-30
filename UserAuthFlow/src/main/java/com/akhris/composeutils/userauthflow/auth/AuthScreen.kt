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

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.akhris.composeutils.userauthflow.R
import com.akhris.composeutils.userauthflow.viewmodel.*

@Composable
fun AuthScreenContent(
    stage: AuthStage
//    onDismiss: () -> Unit
) {


//    when (stage) {
//        AuthStage.Initial -> TODO()
//        is AuthStage.SignInStage.InitiateSignIn -> TODO()
//        AuthStage.SignInStage.ProcessingSignIn -> TODO()
//        is AuthStage.SignInStage.SignInScreen -> TODO()
//        is AuthStage.SignUpStage -> SignUp
//    }

//    val loginAction: LoginAction? = remember(state) {
//        when (val s = state) {
//            is SignStatus.Error -> when (s.error) {
//                //map error to AuthResult.Failure and show appropriate screen
//                else -> null
//            }
//            SignStatus.PasswordChanged -> null
//            SignStatus.SignUp.ConfirmationCodeWasSent -> LoginAction.SignUp.EnterConfirmationCode
//            SignStatus.SignUp.SignUpConfirmed -> LoginAction.SignUp.SignUpResult(AuthResult.Success)
//            SignStatus.SignedIn -> LoginAction.SignIn.SignInResult(AuthResult.Success)
//            SignStatus.SignedOut, null -> null
//            SignStatus.SigningInProgress -> LoginAction.SignIn.ProcessingSignIn
//            SignStatus.SigningOutInProgress -> null
//        }
//
//    }
//
//    Timber.d("login action: $loginAction")

//    AuthScreenContent(
//        initLoginAction = loginAction,
//        onDismiss = onDismiss,
//        onSignIn = { eMail, password ->
//            authViewModel.initiateSignIn(userEmail = eMail, userPassword = password)
//        },
//        onSignUp = { name, eMail, password ->
//            authViewModel.initiateSignUp(name = name, userEmail = eMail, userPassword = password)
//        },
//        onSignUpConfirm = { eMail, confirmationCode ->
//            authViewModel.confirmSignUp(userEmail = eMail, confirmationCode = confirmationCode)
//        }
//    )
}


sealed class AuthScreen {
    object Initial : AuthScreen()
    object SignUp : AuthScreen()
    object SignIn : AuthScreen()
    object ForgotPassword : AuthScreen()
}

@Composable
fun AuthScreenPattern(
    onBack: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    title: String? = null,
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
            CompositionLocalProvider(
                LocalLayoutDirection provides
                        if (onBack != null)
                            LayoutDirection.Ltr
                        else LayoutDirection.Rtl
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
                    title?.let {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
                            text = it,
                            style = MaterialTheme.typography.h4,
                            textAlign = when (LocalLayoutDirection.current) {
                                LayoutDirection.Ltr -> TextAlign.Start
                                LayoutDirection.Rtl -> TextAlign.End
                            }
                        )
                    }
                }

            }

            content()
        }
    }
}

@Composable
fun AuthScreen(
    initLoginAction: LoginAction? = null,
    onDismiss: () -> Unit = {},

    onSignIn: (eMail: String, password: String) -> Unit = { _, _ -> },
    onSignUp: (name: String, eMail: String, password: String) -> Unit = { _, _, _ -> },
    onSignUpConfirm: (eMail: String, confirmationCode: String) -> Unit = { _, _ -> }
) {
    Surface(
        shape = RoundedCornerShape(4f.dp),
        modifier = Modifier
            .fillMaxHeight()
//            .semantics { contentDescription = dialogContentDescription }
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(8.dp)
        ) {

            var loginAction by remember(initLoginAction) {
                mutableStateOf<LoginAction?>(
                    initLoginAction
                )
            }

            var userEmail by remember { mutableStateOf("") }



            Icon(
                imageVector = if (loginAction != null) Icons.Rounded.ArrowBack else Icons.Rounded.Close,
                contentDescription = "back",
                Modifier
                    .padding(16.dp)
                    .clickable {
                        if (loginAction != null)
                            loginAction = null
                        else onDismiss()
                    }
                    .align(if (loginAction != null) Alignment.Start else Alignment.End),
                tint = MaterialTheme.colors.onSurface
            )


            Icon(
                modifier = Modifier
                    .size(128.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(32.dp),
                imageVector = Icons.Rounded.AccountBox,
                tint = MaterialTheme.colors.onSurface,
                contentDescription = "account_icon"
            )

            if (loginAction == null) {
                EmailField(userEmail = userEmail, onEmailChanged = { userEmail = it })
            }

            when (val state = loginAction) {
//                is LoginAction.SignIn -> {
//                    SignInContent(
//                        state = state,
//                        eMail = userEmail,
//                        onEmailChanged = {
//                            userEmail = it
//                        },
//                        onSignIn = onSignIn
//                    )
//                }
                LoginAction.ForgotPassword -> {
                    ForgotPassword(eMail = userEmail) {
                        userEmail = it
                    }
                }
//                is LoginAction.SignUp -> SignUp(
//                    eMail = userEmail,
//                    onEmailChanged = {
//                        userEmail = it
//                    },
//                    onSignUp = onSignUp,
//                    onSignUpConfirm = onSignUpConfirm,
//                    withConfirmationCode = loginAction == LoginAction.SignUp.EnterConfirmationCode
//                )
                null -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {

                        TextButton(onClick = {
//                            if (loginAction != LoginAction.ForgotPassword) {
                            loginAction = LoginAction.ForgotPassword
//                            }
                        }) {
                            Text(
                                text = stringResource(id = R.string.user_auth_forgot_password),
                                color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.5f)
                            )
                        }


                        TextButton(onClick = {
//                            if (loginAction !is LoginAction.SignUp) {
//                            loginAction = LoginAction.SignUp.InitiateSignUp
//                            }
                        }) {
                            Text(
                                text = stringResource(id = R.string.user_auth_sign_up),
                                color = MaterialTheme.colors.primaryVariant
                            )
                        }


                        Button(onClick = {
//                            if (loginAction != LoginAction.SignIn) {
//                            loginAction = LoginAction.SignIn.InitiateSignIn
//                            }
                        }) {
                            Text(text = stringResource(id = R.string.user_auth_sign_in))
                        }
                    }
                }
            }


        }
    }
}

@Composable
private fun ForgotPassword(eMail: String, onEmailChanged: (String) -> Unit) {

}


@Composable
internal fun PasswordField(
    userPassword: String,
    onPasswordChanged: ((String) -> Unit)? = null,
    @StringRes hintRes: Int = R.string.user_auth_password_hint,
    @StringRes errorRes: Int? = R.string.user_auth_password_error,
    passwordValidate: (String) -> Boolean,
    withVisibilityToggle: Boolean = false
) {
    //password text field
    var passwordVisible by remember { mutableStateOf(false) }
    val isError = remember(userPassword, passwordValidate) { !passwordValidate(userPassword) }
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
            isError = isError,
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

        if (isError && errorRes != null) {
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
    isValidEmail: Boolean = true,
    @StringRes errorID: Int = R.string.user_auth_wrong_email
) {
    //e-mail text field
    BaseAuthField(
        text = userEmail,
        onTextChanged = onEmailChanged,
        withClearIcon = true,
        hintID = R.string.user_auth_email_hint,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        textValidate = { isValidEmail },
        errorID = errorID
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
    textValidate: (text: String) -> Boolean = { true }
) {

    var errorCheckAllowed by remember { mutableStateOf(false) }

    val isError = remember(
        text,
        textValidate,
        errorCheckAllowed
    ) { if (errorCheckAllowed) !textValidate(text) else false }

    Column {

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .onFocusChanged {
                    errorCheckAllowed = !it.isFocused && text.isNotEmpty()
                }
//                    .testTag(textTestTag)
            ,
            value = text,
            isError = isError,
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

        if (isError && errorID != null) {
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


sealed class LoginAction {
    /**
     * Sign in screen
     */


    object ForgotPassword : LoginAction()
}

sealed class AuthResult {
    object Success : AuthResult()
    data class Failure(val error: Throwable?) : AuthResult()
}

@Preview(group = "auth_light")
@Composable
fun InitialAuthScreenTestLight() {
    AuthScreen(null)
}


@Preview(group = "auth_light")
@Composable
fun ForgotPasswordTestLight() {
    AuthScreen(LoginAction.ForgotPassword)
}


@Preview(group = "auth_dark")
@Composable
fun InitialAuthScreenTestDark() {
    AuthScreen(null)
}


@Preview(group = "auth_dark")
@Composable
fun ForgotPasswordTestDark() {
    AuthScreen(LoginAction.ForgotPassword)
}
