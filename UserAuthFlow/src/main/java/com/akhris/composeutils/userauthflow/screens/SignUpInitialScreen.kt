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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.akhris.composeutils.userauthflow.R
import com.akhris.composeutils.userauthflow.auth.AuthState
import com.akhris.composeutils.userauthflow.auth.BaseAuthField
import com.akhris.composeutils.userauthflow.auth.EmailField
import com.akhris.composeutils.userauthflow.auth.PasswordField
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
internal fun SignUpInitialScreen(
    eMail: String = "",
    onEmailChanged: ((String) -> Unit)? = null,
    onSignupClicked: ((username: String, email: String, password: String) -> Unit)? = null,
    state: AuthState.SignUp? = null
) {


    var userName by remember { mutableStateOf("") }
    var userPassword1 by remember { mutableStateOf("") }
    var userPassword2 by remember { mutableStateOf("") }


    Column {
        //user name field
        BaseAuthField(
            text = userName,
            onTextChanged = { userName = it },
            withClearIcon = true,
            hintID = R.string.user_auth_name_hint,
            textValidate = {
                state != AuthState.SignUp.Failure.UsernameExists
            }
        )

        //email field
        EmailField(
            userEmail = eMail,
            onEmailChanged = { onEmailChanged?.invoke(it) },
            isValidEmail = eMail.isValidEmail() && state != AuthState.SignUp.Failure.UserEmailExists
        )

        //password field
        PasswordField(
            userPassword = userPassword1,
            onPasswordChanged = { userPassword1 = it },
            passwordValidate = { it.isNotEmpty() })
//            passwordValidate = { isValidPassword1 || it.isNotEmpty() })

        //password repeat field
        PasswordField(
            userPassword = userPassword2,
            onPasswordChanged = { userPassword2 = it },
            hintRes = R.string.user_auth_repeat_password_hint,
            errorRes = R.string.user_auth_password_repeat_error,
            passwordValidate = { (it == userPassword1) })
//            passwordValidate = { isValidPassword2 || (it == userPassword1) })


        //sign up button
        Button(modifier = Modifier
            .align(Alignment.End)
            .padding(vertical = 8.dp),
            onClick = { onSignupClicked?.invoke(userName, eMail, userPassword1) }
        ) {
            Text(text = stringResource(id = R.string.user_auth_sign_up))
        }
    }
}

@Preview(name = "sign up initial screen", group = "sign up")
@Composable
fun SignUpInitialTest() {
    SignUpInitialScreen()
}
