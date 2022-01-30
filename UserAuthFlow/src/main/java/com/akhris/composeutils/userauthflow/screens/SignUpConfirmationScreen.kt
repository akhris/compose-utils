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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.akhris.composeutils.userauthflow.R
import com.akhris.composeutils.userauthflow.auth.BaseAuthField
import com.akhris.composeutils.userauthflow.auth.SignUpResult

@Composable
private fun SignUpConfirmationScreen(
    eMail: String = "",
    confirmationCode: String = "",
    onConfirmationCodeChanged: ((String) -> Unit)? = null,
    onConfirmClicked: (() -> Unit)? = null,
    error: SignUpResult.Failure? = null
) {
    Column {

        Text(
            text = stringResource(R.string.user_auth_fill_in_confirmation_code_sent_to, eMail),
            style = MaterialTheme.typography.body2
        )

        BaseAuthField(
            text = confirmationCode,
            onTextChanged = onConfirmationCodeChanged,
            withClearIcon = true,
            hintID = R.string.user_auth_confirmation_code_hint,
            errorID = when (error) {
                SignUpResult.Failure.CodeExpired -> R.string.user_auth_sign_up_error_code_expired
                SignUpResult.Failure.CodeMismatch -> R.string.user_auth_sign_up_error_code_mismatch
                SignUpResult.Failure.ConfirmationCodeDeliveryFailure -> R.string.user_auth_sign_up_error_code_delivery
                else -> null
            },
            textValidate = {
                error !in listOf(
                    SignUpResult.Failure.CodeExpired,
                    SignUpResult.Failure.CodeMismatch,
                    SignUpResult.Failure.ConfirmationCodeDeliveryFailure
                )
            }
        )
        Button(modifier = Modifier
            .align(Alignment.End)
            .padding(vertical = 8.dp), onClick = { onConfirmClicked?.invoke() }
        ) {
            Text(text = stringResource(id = R.string.user_auth_confirm_code))
        }
    }
}


@Preview(name = "sign up confirmation screen", group = "sign up")
@Composable
fun SignUpConfirmationScreenTest() {
    SignUpConfirmationScreen()
}