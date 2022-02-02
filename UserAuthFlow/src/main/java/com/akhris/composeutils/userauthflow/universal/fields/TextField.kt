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

package com.akhris.composeutils.userauthflow.universal.fields

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import com.akhris.composeutils.userauthflow.universal.entities.IEntity

class TextField(
    override val id: String,
    @StringRes val hintRes: Int? = null,
    val endIconType: EndIconType? = null,
    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) : IEntity