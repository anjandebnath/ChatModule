/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uss.chatmodule.util.lib.dexter;

import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;

import io.left.core.werewolfUltimate.ui.base.BaseActivity;


public class DexPermissionListener implements com.karumi.dexter.listener.single.PermissionListener {

  private final BaseActivity activity;

  public DexPermissionListener(BaseActivity activity) {
    this.activity = activity;
  }

  @Override
  public void onPermissionGranted(PermissionGrantedResponse response) {
    activity.showPermissionGranted(response.getPermissionName());
  }

  @Override
  public void onPermissionDenied(PermissionDeniedResponse response) {
    activity.showPermissionDenied(response.getPermissionName(), response.isPermanentlyDenied());
  }

  @Override
  public void onPermissionRationaleShouldBeShown(PermissionRequest permission,
                                                 PermissionToken token) {
    activity.showPermissionRationale(token);
  }
}
