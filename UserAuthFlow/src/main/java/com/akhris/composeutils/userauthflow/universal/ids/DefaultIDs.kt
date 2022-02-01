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

package com.akhris.composeutils.userauthflow.universal.ids

object DefaultIDs {
    object Screens {
        const val main = "screen_main"
        const val signUp = "screen_sign_up"
        const val signIn = "screen_sign_in"
        const val forgotPassword = "screen_forgot_password"
    }

    object Buttons {
        const val signUp = "button_sign_up"
        const val signIn = "button_sign_in"
        const val forgotPassword = "button_forgot_password"
    }

    object Fields {
        const val userName = "field_user_name"
        const val userEmail = "field_user_email"
        const val userPassword1 = "field_user_password_1"
        const val userPassword2 = "field_user_password_2"
    }
}