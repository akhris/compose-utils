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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.akhris.composeutils.userauthflow.R
import com.akhris.composeutils.userauthflow.utils.isValidEmail
import com.akhris.composeutils.userauthflow.viewmodel.ISignUpViewModel

/*
@Composable
internal fun SignUp(
    initialStage: SignUpStage = SignUpStage.InitiateSignUp(),
    signUpViewModel: ISignUpViewModel
) {
    var currentSignUpState by remember(initialStage) { mutableStateOf(initialStage) }

    val signUpResult by remember(signUpViewModel) {
        signUpViewModel.signupStatus
    }.observeAsState()

    val nextSignUpStage = remember(currentSignUpState, signUpResult) {
        when (currentSignUpState) {
            is SignUpStage.InitiateSignUp -> {
                when (signUpResult) {
                    SignUpResult.Failure.UserEmailExists -> SignUpStage.InitiateSignUp(signUpResult as? SignUpResult.Failure)
                    SignUpResult.Failure.UsernameExists -> SignUpStage.InitiateSignUp(signUpResult as? SignUpResult.Failure)
                    null, SignUpResult.Success -> SignUpStage.EnterConfirmationCode()
                    else -> null
                }
            }
            is SignUpStage.EnterConfirmationCode -> {
                when (signUpResult) {
                    SignUpResult.Failure.CodeExpired -> SignUpStage.EnterConfirmationCode(
                        signUpResult as? SignUpResult.Failure
                    )
                    SignUpResult.Failure.CodeMismatch -> SignUpStage.EnterConfirmationCode(
                        signUpResult as? SignUpResult.Failure
                    )
                    SignUpResult.Failure.ConfirmationCodeDeliveryFailure -> SignUpStage.EnterConfirmationCode(
                        signUpResult as? SignUpResult.Failure
                    )
                    SignUpResult.Success, null -> null
                    else->null
                }
            }
            is SignUpStage.SignUpScreen -> null
        }
    }


    SignUpContent(
        initialStage = nextSignUpStage,
        onSignUp = { name, eMail, password ->
            //do signup
            signUpViewModel.initiateSignUp(
                name = name,
                userEmail = eMail,
                userPassword = password
            )
        },
        onSignUpConfirm = { eMail, confirmationCode ->
            signUpViewModel.confirmSignUp(
                userEmail = eMail,
                confirmationCode = confirmationCode
            )
        }
    )
}


/**
 * Sign up screen that shows content depending on [initialStage]
 * @param initialStage:
 * [SignUpStage.InitiateSignUp] - show initial screen of signing up (fields with user info + sign up button)
 * [SignUpStage.EnterConfirmationCode] - show field for entering confirmation code and confirm button
 */
@Composable
internal fun SignUpContent(
    initialStage: SignUpStage? = null,
    eMail: String = "",
    onEmailChanged: ((String) -> Unit)? = null,
    onSignUp: ((name: String, eMail: String, password: String) -> Unit)? = null,
    onSignUpConfirm: ((eMail: String, confirmationCode: String) -> Unit)? = null
) {



    var userName: String by remember { mutableStateOf("") }
    var userPassword1: String by remember { mutableStateOf("") }
    var userPassword2: String by remember { mutableStateOf("") }
    var confirmationCode: String by remember { mutableStateOf("") }



    AuthScreenPattern(
        title = stringResource(id = R.string.user_auth_sign_up),
        onBack = {}
    ) {

        var isValidEmail by remember { mutableStateOf(true) }
        var isValidConfirmationCode by remember { mutableStateOf(true) }
        var isValidPassword1 by remember { mutableStateOf(true) }
        var isValidPassword2 by remember { mutableStateOf(true) }

        when (initialStage) {
            is SignUpStage.InitiateSignUp -> SignUpInitialStage(
                userName = userName,
                eMail = eMail,
                userPassword1 = userPassword1,
                userPassword2 = userPassword2,
                onUserNameChanged = { userName = it },
                onEmailChanged = { onEmailChanged?.invoke(it) },
                onUserPassword1Changed = { userPassword1 = it },
                onUserPassword2Changed = { userPassword2 = it },
                onSignupClicked = {
                    onSignUp?.invoke(userName, eMail, userPassword1)
                },
                error = initialStage.error
            )

            is SignUpStage.EnterConfirmationCode -> SignUpConfirmationStage(
                eMail = eMail,
                confirmationCode = confirmationCode,
//                onEmailChanged = { onEmailChanged?.invoke(it) },
                onConfirmationCodeChanged = { confirmationCode = it },
                onConfirmClicked = {},
                error = initialStage.error
            )
            is SignUpStage.SignUpScreen -> SignUpResultStage(initialStage.result)
        }
    }
}






@Composable
private fun SignUpResultStage(signUpResult: SignUpResult) {

//
//    val textID = when (signUpResult) {
//        SignUpResult.Success -> R.string.user_auth_sign_up_success
//        SignUpResult.Failure.CodeExpired -> R.string.user_auth_sign_up_error_code_expired
//        SignUpResult.Failure.CodeMismatch -> R.string.user_auth_sign_up_error_code_mismatch
//        SignUpResult.Failure.ConfirmationCodeDeliveryFailure -> R.string.user_auth_sign_up_error_code_delivery
//        SignUpResult.Failure.UsernameExists -> R.string.user_auth_sign_up_error_user_name_exists
//    }
//
//    Text(
//        modifier = Modifier.padding(16.dp),
//        text = stringResource(id = textID),
//        style = MaterialTheme.typography.body1
//    )
}



@Preview(name = "initial state", group = "sign_up_light")
@Composable
fun SignUpInitialTestLight() {
    SignUpContent(initialStage = SignUpStage.InitiateSignUp())
}

@Preview(name = "confirmation state", group = "sign_up_light")
@Composable
fun SignUpEnterConfirmationCodeTestLight() {
    SignUpContent(initialStage = SignUpStage.EnterConfirmationCode())
}

//@Preview(name = "successfull signup", group = "sign_up_light")
//@Composable
//fun SignUpSuccessTestLight() {
//    SignUpContent(state = SignUpState.SignUpScreen(AuthResult.Success))
//}
//
//@Preview(name = "failure: username exists", group = "sign_up_light")
//@Composable
//fun SignUpFailureUsernameExistsTestLight() {
//    SignUpContent(state = SignUpState.SignUpScreen(AuthResult.Failure.UsernameExists))
//}
//
//@Preview(name = "failure: code expired", group = "sign_up_light")
//@Composable
//fun SignUpFailureCodeExpiredTestLight() {
//    SignUpContent(state = SignUpState.SignUpScreen(AuthResult.Failure.CodeExpired))
//}
//
//@Preview(name = "failure: code mismatch", group = "sign_up_light")
//@Composable
//fun SignUpFailureCodeMismatchTestLight() {
//    SignUpContent(state = SignUpState.SignUpScreen(AuthResult.Failure.CodeMismatch))
//}
//
//@Preview(name = "failure: code delivery failure", group = "sign_up_light")
//@Composable
//fun SignUpCodeDeliveryFailureTestLight() {
//    SignUpContent(state = SignUpState.SignUpScreen(AuthResult.Failure.ConfirmationCodeDeliveryFailure))
//}


 */