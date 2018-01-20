package com.hetulpatel.fellow.fellowv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.hetulpatel.fellow.fellowv2.drawer.DrawerMain;
import com.hetulpatel.fellow.fellowv2.driveservice.CreateFolderActivity;

public class CheckNewUser extends AppCompatActivity {

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_home_activity);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);


        if(!pref.getBoolean("firsttime",true)){
            Intent i  = new Intent(CheckNewUser.this,DrawerMain.class);
            startActivity(i);
            finish();
        }else {
            Intent i  = new Intent(CheckNewUser.this,CreateFolderActivity.class);
            startActivity(i);
            finish();
        }
    }
}
