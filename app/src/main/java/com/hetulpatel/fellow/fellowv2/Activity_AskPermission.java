package com.hetulpatel.fellow.fellowv2;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.hetulpatel.fellow.fellowv2.drawer.DrawerMain;
import com.hetulpatel.fellow.fellowv2.driveservice.CreateFolderActivity;

import java.util.ArrayList;
import java.util.List;

public class Activity_AskPermission extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_MULTIPLE_PERMISSION = 1 ;
    private SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home_activity);



        //Ask Permissions
        askPermissions();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }

    private void askPermissions() {
        List<String> listPermissionsNeeded = new ArrayList<>();
        // No explanation needed, we can request the permission.
        if((ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED))
        {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if((ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED))
        {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if((ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED))
        {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions( this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    MY_PERMISSIONS_REQUEST_MULTIPLE_PERMISSION);
        }else {
            pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);


            if(!pref.getBoolean("firsttime",true)){
                Intent i  = new Intent(Activity_AskPermission.this,CreateFolderActivity.class);
                startActivity(i);
                finish();
            }else {
                Intent i  = new Intent(Activity_AskPermission.this,Home_activity.class);
                startActivity(i);
                finish();
            }
        }




    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}
