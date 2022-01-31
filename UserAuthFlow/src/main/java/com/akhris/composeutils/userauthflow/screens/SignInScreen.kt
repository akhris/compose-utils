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
import androidx.compose.ui.unit.dp
import com.akhris.composeutils.userauthflow.R
import com.akhris.composeutils.userauthflow.auth.AuthState
import com.akhris.composeutils.userauthflow.auth.EmailField
import com.akhris.composeutils.userauthflow.auth.PasswordField
import com.akhris.composeutils.userauthflow.utils.isValidEmail

@Composable
fun SignInScreen(
    eMail: String = "",
    onEmailChanged: (String) -> Unit,
    onSignInClicked: ((eMail: String, passWord: String) -> Unit)? = null,
    state: AuthState.SignIn? = null
) {

    var userPassword by remember { mutableStateOf("") }

    Column {
        //email field
        EmailField(
            userEmail = eMail,
            onEmailChanged = { onEmailChanged.invoke(it) },
            isValidEmail = eMail.isValidEmail() && state != AuthState.SignIn.Failure.EMailNotExists
        )

        //password field
        PasswordField(
            userPassword = userPassword,
            onPasswordChanged = { userPassword = it },
            passwordValidate = { it.isNotEmpty() })

        //sign in button
        Button(modifier = Modifier
            .align(Alignment.End)
            .padding(vertical = 8.dp),
            onClick = { onSignInClicked?.invoke(eMail, userPassword) }
        ) {
            Text(text = stringResource(id = R.string.user_auth_sign_up))
        }

    }
}