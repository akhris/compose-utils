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

package com.akhris.composeutils.userauthflow.universal.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhris.composeutils.userauthflow.universal.events.Event
import com.akhris.composeutils.userauthflow.universal.events.EventResult
import com.akhris.composeutils.userauthflow.universal.events.IEventHandler
import kotlinx.coroutines.launch

class AuthFlowViewModel(private val eventHandler: IEventHandler) : ViewModel() {

    private val _eventResults: MutableLiveData<EventResult> = MutableLiveData()

    val eventResults: LiveData<EventResult> = _eventResults


    fun sendEvent(event: Event) {
        viewModelScope.launch {
            val result = eventHandler.handleEvent(event)
            _eventResults.value = result
        }
    }

}