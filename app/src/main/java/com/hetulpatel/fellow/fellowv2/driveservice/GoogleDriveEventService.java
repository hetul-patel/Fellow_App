package com.hetulpatel.fellow.fellowv2.driveservice;

import android.util.Log;

import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.events.CompletionEvent;
import com.google.android.gms.drive.events.DriveEventService;

/**
 * Created by hetulpatel on 26/06/17.
 */

public class GoogleDriveEventService extends DriveEventService {
    private static final String TAG = "GoogleDriveEventService";

    @Override
    public void onCompletion(CompletionEvent event) {
        super.onCompletion(event);
        Log.d(TAG, "Action completed with status: " + event.getStatus());
        DriveId driveId = event.getDriveId();
        String resourceId = driveId.getResourceId();

        Log.d("RESOURCE ID" , resourceId);

    }
}
