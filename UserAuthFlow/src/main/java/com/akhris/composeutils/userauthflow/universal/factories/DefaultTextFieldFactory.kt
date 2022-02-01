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

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.akhris.composeutils.userauthflow.R
import com.akhris.composeutils.userauthflow.universal.fields.EndIconType
import com.akhris.composeutils.userauthflow.universal.fields.TextField
import com.akhris.composeutils.userauthflow.universal.ids.DefaultIDs

class DefaultTextFieldFactory : TextFieldFactory {
    override fun getEntity(id: String?): TextField? {
        return when (id) {
            DefaultIDs.Fields.userEmail -> TextField(
                id = id,
                hintRes = R.string.user_auth_email_hint,
                endIconType = EndIconType.ClearText,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            DefaultIDs.Fields.userName -> TextField(
                id = id,
                hintRes = R.string.user_auth_name_hint,
                endIconType = EndIconType.ClearText
            )
            DefaultIDs.Fields.userPassword1 -> TextField(
                id = id,
                hintRes = R.string.user_auth_password_hint,
                endIconType = EndIconType.VisibilityToggle
            )
            DefaultIDs.Fields.userPassword2 -> TextField(
                id = id,
                hintRes = R.string.user_auth_repeat_password_hint,
                endIconType = EndIconType.VisibilityToggle
            )
            else -> null
        }
    }
}