/*
 * Copyright (c) 2021. Dylan Cai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused")

package com.tr.hsyn.launcher

import android.graphics.Bitmap
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts.TakePicturePreview

/**
 * @author Dylan Cai
 */
class TakePicturePreviewLauncher(caller: ActivityResultCaller) :
  BaseActivityResultLauncher<Void, Bitmap>(caller, TakePicturePreview()) {

  fun launch(onActivityResult: ActivityResultCallback<Bitmap?>) = launch(null, onActivityResult)

  suspend fun launchForResult() = launchForResult(null)

  fun launchForFlow() = launchForFlow(null)
}
