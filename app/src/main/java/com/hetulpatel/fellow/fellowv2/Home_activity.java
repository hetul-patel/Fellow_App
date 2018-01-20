package com.hetulpatel.fellow.fellowv2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.hetulpatel.fellow.fellowv2.drawer.DrawerMain;
import com.hetulpatel.fellow.fellowv2.model.ComplexPreferences;
import com.hetulpatel.fellow.fellowv2.model.user;
import com.hetulpatel.fellow.fellowv2.tabs.Tabbed;

import java.text.DateFormat;

public class Home_activity extends AppCompatActivity {

    private ImageView addcourses;
    private View content;
    private FontTextView username , dayofweek , dateofmonth ;
    private ImageView logout;
    private GoogleApiClient mGoogleApiClient;
    private String username_string;
    private ComplexPreferences complexPreferences;
    private user user;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_home_activity);

        /*complexPreferences = ComplexPreferences.getComplexPreferences(this, "mypref", MODE_PRIVATE);
        user = complexPreferences.getObject("user", user.class);*/

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);



        init();


    }

    private void init() {


        content = findViewById(R.id.content_home_activity);
        username = (FontTextView) content.findViewById(R.id.NameOfUser);
        dateofmonth = (FontTextView) content.findViewById(R.id.DateAndMonth);
        dayofweek = (FontTextView) content.findViewById(R.id.DayOfWeek);
        addcourses = (ImageView) findViewById(R.id.home_plus_btn);
        logout = (ImageView) findViewById(R.id.logout);

        addcourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home_activity.this, AddCourses.class);
                startActivity(i);
            }
        });



        username_string  = pref.getString("username",null);
        username_string = username_string.substring(0,1).toUpperCase() + username_string.substring(1).toLowerCase();
        username.setText(username_string);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Home_activity.this)
                        .setTitle("Sure you want to logout " + username_string + " ?")
                        .setMessage("Press logout to confirm.")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                if (mGoogleApiClient.isConnected()) {
                                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                            new ResultCallback<Status>() {
                                                @Override
                                                public void onResult(Status status) {
                                                    updateUI(false);
                                                }
                                            });
                                }
                            }
                        }).create().show();


            }
        });

        DateFormat d = new java.text.SimpleDateFormat("EEE,");
        String currentday = d.format(java.util.Calendar.getInstance().getTime());

        DateFormat dm = new java.text.SimpleDateFormat("d MMMM");
        String currentdateandmonth = dm.format(java.util.Calendar.getInstance().getTime());

        dateofmonth.setText(currentdateandmonth);
        dayofweek.setText(currentday);
    }

    private void updateUI(boolean isSignedIn) {

        Log.d("STATUS", isSignedIn+"");

        if (!isSignedIn) {
            finish();
        } else {

        }
    }


    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("See you soon!")
                .setMessage("Press ok to exit.")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                }).create().show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
