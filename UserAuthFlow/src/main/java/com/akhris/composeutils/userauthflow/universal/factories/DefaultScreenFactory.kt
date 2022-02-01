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

package com.akhris.composeutils.userauthflow.universal.factories

import com.akhris.composeutils.userauthflow.R
import com.akhris.composeutils.userauthflow.universal.ids.DefaultIDs
import com.akhris.composeutils.userauthflow.universal.screens.Screen

class DefaultScreenFactory : ScreenFactory {
    override fun getEntity(id: String?): Screen? {
        return when (id) {
            DefaultIDs.Screens.main, null -> Screen(
                id = DefaultIDs.Screens.main,
                titleRes = R.string.user_auth_title,
                textFieldsIDs = listOf(
                    DefaultIDs.Fields.userEmail
                ),
                buttonsIDs = listOf(
                    DefaultIDs.Buttons.forgotPassword,
                    DefaultIDs.Buttons.signUp,
                    DefaultIDs.Buttons.signIn
                )
            )
            DefaultIDs.Screens.signIn -> Screen(
                id = DefaultIDs.Screens.signIn,
                titleRes = R.string.user_auth_sign_in,
                textFieldsIDs = listOf(
                    DefaultIDs.Fields.userEmail,
                    DefaultIDs.Fields.userPassword1
                ),
                buttonsIDs = listOf(
                    DefaultIDs.Buttons.signIn
                )
            )
            DefaultIDs.Screens.signUp -> Screen(
                id = DefaultIDs.Screens.signIn,
                titleRes = R.string.user_auth_sign_in,
                textFieldsIDs = listOf(
                    DefaultIDs.Fields.userEmail,
                    DefaultIDs.Fields.userPassword1
                ),
                buttonsIDs = listOf(
                    DefaultIDs.Buttons.signIn
                )
            )
            else -> null
        }
    }
}