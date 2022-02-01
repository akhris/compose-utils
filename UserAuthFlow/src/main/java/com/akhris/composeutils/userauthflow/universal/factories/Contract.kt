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

import com.akhris.composeutils.userauthflow.universal.buttons.Button
import com.akhris.composeutils.userauthflow.universal.entities.IEntity
import com.akhris.composeutils.userauthflow.universal.fields.TextField
import com.akhris.composeutils.userauthflow.universal.screens.Screen

interface IFactory<T : IEntity> {
    fun getEntity(id: String): T
}

typealias ScreenFactory = IFactory<Screen>
typealias TextFieldFactory = IFactory<TextField>
typealias ButtonFactory = IFactory<Button>