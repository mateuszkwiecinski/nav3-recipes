/*
 * Copyright 2025 The Android Open Source Project
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

package com.example.nav3recipes.basicsaveable

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import kotlinx.serialization.Serializable

@Serializable
data object RouteA : NavKey


class BasicSaveableActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            setContent {
                val backStack = rememberNavBackStack(RouteA)

                val popBackStack = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                }

                NavDisplay(
                    entryDecorators = listOf(
                        rememberSceneSetupNavEntryDecorator(),
                        rememberSavedStateNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                    backStack = backStack,
                    onBack = { popBackStack() },
                    entryProvider = { key ->
                        NavEntry(key) {
                            val state = rememberPagerState(pageCount = { 2 })

                            BackHandler(enabled = state.currentPage == 1) {
                                Log.i("repro", "BackHandler invoked")
                            }

                            HorizontalPager(state = state) {
                                when (it) {
                                    0 -> Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Red),
                                    )

                                    1 -> Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Blue),
                                    )
                                }
                            }
                        }
                    },
                )
            }
        }
    }
}
