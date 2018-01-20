/**
 * Copyright 2013 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hetulpatel.fellow.fellowv2.driveservice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFolder.DriveFolderResult;
import com.google.android.gms.drive.MetadataChangeSet;
import com.hetulpatel.fellow.fellowv2.Home_activity;

/**
 * An activity to illustrate how to create a new folder.
 */
public class CreateFolderActivity extends BaseDemoActivity {

    private SharedPreferences pref;

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
         pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);


        if(!pref.getBoolean("foldercreated",false)) {

            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                    .setTitle("Fellow2").build();
            Drive.DriveApi.getRootFolder(getGoogleApiClient()).createFolder(
                    getGoogleApiClient(), changeSet).setResultCallback(callback);

        }else {
            Intent intent = new Intent(getBaseContext(), Home_activity.class);
            startActivity(intent);
            finish();
        }
    }

    final ResultCallback<DriveFolderResult> callback = new ResultCallback<DriveFolderResult>() {
        @Override
        public void onResult(DriveFolderResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Error while trying to create the folder");
                return;
            }
            showMessage("Created a folder: " + result.getDriveFolder().getDriveId());

            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("foldercreated", true);
            editor.putString("folderid", ""+result.getDriveFolder().getDriveId());
            editor.commit();

            Intent intent = new Intent(getBaseContext(), Home_activity.class);
            startActivity(intent);
            finish();

        }
    };
}
