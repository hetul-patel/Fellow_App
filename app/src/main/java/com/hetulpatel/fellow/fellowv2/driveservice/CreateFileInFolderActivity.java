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
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveApi.DriveIdResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveFolder.DriveFileResult;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.ExecutionOptions;
import com.google.android.gms.drive.MetadataChangeSet;
import com.hetulpatel.fellow.fellowv2.drawer.DrawerMain;


/**
 * An activity to create a file inside a folder.
 */
public class CreateFileInFolderActivity extends BaseDemoActivity {

    private DriveId mFolderDriveId;
    private SharedPreferences pref;

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        Drive.DriveApi.fetchDriveId(getGoogleApiClient(),EXISTING_FOLDER_ID)
                .setResultCallback(idCallback);
    }

    final private ResultCallback<DriveIdResult> idCallback = new ResultCallback<DriveIdResult>() {
        @Override
        public void onResult(DriveIdResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Cannot find DriveId. Are you authorized to view this file?");
                return;
            }
            mFolderDriveId = result.getDriveId();
            Drive.DriveApi.newDriveContents(getGoogleApiClient())
                    .setResultCallback(driveContentsCallback);
        }
    };

    final private ResultCallback<DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveContentsResult>() {
        @Override
        public void onResult(DriveContentsResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Error while trying to create new file contents");
                return;
            }
            DriveFolder folder = mFolderDriveId.asDriveFolder();
            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                    .setTitle("New file")
                    .setMimeType("text/plain")
                    .setStarred(true).build();
            folder.createFile(getGoogleApiClient(), changeSet, result.getDriveContents())
                    .setResultCallback(fileCallback);
        }
    };

    final private ResultCallback<DriveFileResult> fileCallback =
            new ResultCallback<DriveFileResult>() {
        @Override
        public void onResult(final DriveFileResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Error while trying to create the file");
                return;
            }
            showMessage("Created a file: " + result.getDriveFile().getDriveId());


            DriveFile file = Drive.DriveApi.getFile(getGoogleApiClient(), result.getDriveFile().getDriveId());
            file.open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null).setResultCallback(new ResultCallback<DriveContentsResult>() {
                @Override
                public void onResult(@NonNull DriveContentsResult driveContentsResult) {
                    DriveContents driveContents = driveContentsResult.getDriveContents();



                    ExecutionOptions executionOptions = new ExecutionOptions.Builder()
                            .setNotifyOnCompletion(true)
                            .setTrackingTag("file")
                            .build();
                    driveContents.commit(getGoogleApiClient(),null,executionOptions);
                }
            });






            Intent i  = new Intent(getBaseContext(), DrawerMain.class);
            startActivity(i);
            finish();
        }
    };
}
