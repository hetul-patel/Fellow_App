package com.hetulpatel.fellow.fellowv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hetulpatel.fellow.fellowv2.drawer.DrawerMain;
import com.hetulpatel.fellow.fellowv2.model.ComplexPreferences;
import com.hetulpatel.fellow.fellowv2.model.TinyDB;
import com.hetulpatel.fellow.fellowv2.model.user;

import java.io.File;
import java.util.ArrayList;

public class AddCourses extends AppCompatActivity {

    private EditText course, semester;
    private ListView edittextContainer;
    private String[] arrTemp;
    private MyListAdapterEdit myListAdapter;
    private LinearLayout ok_button ;
    private boolean nullwatcher = false;
    private int courses_number = 0 , semester_number = 0;
    private LinearLayout.LayoutParams list;
    private CoordinatorLayout coordinatorLayout;
    private ArrayList<String> courses_name = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.addcourses_layout);


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinate);


        course = (EditText) findViewById(R.id.numberofcourses);
        semester = (EditText) findViewById(R.id.numberofsemester);

        myListAdapter = new MyListAdapterEdit();
        edittextContainer = (ListView)findViewById(R.id.edittextinflate);
        list = (LinearLayout.LayoutParams) edittextContainer.getLayoutParams();

        ok_button = (LinearLayout) findViewById(R.id.addcourse_btn);

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    String semcount = semester.getText().toString();

                    if(semcount.equals(""))
                        semester_number = 0;
                    else
                        semester_number = Integer.parseInt(semcount);

                    if(semester_number>0 && courses_number>0){



                        for(int k = 0 ; k<courses_number;k++) {
                            courses_name.add(arrTemp[k]);
                            createDirIfNotExists("/fellow/sem"+semcount+"/"+arrTemp[k].toLowerCase());
                        }
                        createDirIfNotExists("/fellow/sem"+semcount+"/pdfs");

                        TinyDB tinydb = new TinyDB(AddCourses.this);
                        tinydb.putInt("semester",semester_number);
                        tinydb.putListString("courses",courses_name);
                        tinydb.putListString("mon",courses_name);
                        tinydb.putListString("tue",courses_name);
                        tinydb.putListString("wed",courses_name);
                        tinydb.putListString("thu",courses_name);
                        tinydb.putListString("fri",courses_name);
                        tinydb.putListString("sat",courses_name);


                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putBoolean("firsttime",false);
                        editor.commit();

                        Intent i = new Intent(AddCourses.this,DrawerMain.class);
                        startActivity(i);
                        finish();
                    }else {
                        Snackbar.make(coordinatorLayout,"Fill empty details.",Snackbar.LENGTH_LONG).show();
                    }


                }catch (Exception e){
                    Snackbar.make(coordinatorLayout,"Fill empty details.",Snackbar.LENGTH_LONG).show();
                }





            }
        });




        course.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String couunt = course.getText().toString();
                courses_number = 0 ;
                if(couunt.equals(""))
                    courses_number = 0;
                else
                    courses_number = Integer.parseInt(course.getText().toString());

                Log.d("NUMBER" , courses_number+"");

                list.height = courses_number*130;
                edittextContainer.setLayoutParams(list);
                edittextContainer.setAdapter(myListAdapter);

                arrTemp = new String[courses_number];
                for(int k = 0 ; k<courses_number;k++)
                    arrTemp[k]="";

            }
        });
    }

    public static boolean createDirIfNotExists(String path) {
        boolean ret = true;

        File file = new File(Environment.getExternalStorageDirectory(), path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("FELLOW :: ", "Problem creating Image folder");
                ret = false;
            }
        }
        return ret;
    }

    private class MyListAdapterEdit extends BaseAdapter {
        @Override
        public int getCount() {
            if(arrTemp != null && arrTemp.length != 0){
                return arrTemp.length;
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return arrTemp[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //ViewHolder holder = null;
            final ViewHolder holder;
            if (convertView == null) {

                holder = new ViewHolder();
                LayoutInflater inflater = AddCourses.this.getLayoutInflater();
                convertView = inflater.inflate(R.layout.item, null);
                holder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
                holder.editText1 = (EditText) convertView.findViewById(R.id.editText1);

                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            holder.ref = position;

            holder.textView1.setText(position+1+"");
            holder.editText1.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                    arrTemp[holder.ref] = arg0.toString();

                    if (arrTemp != null) {

                        for (int k = 0; k < courses_number; k++) {


                            if (arrTemp[k].equals("")) {
                                ok_button.setVisibility(View.INVISIBLE);
                                break;
                            } else {
                                ok_button.setVisibility(View.VISIBLE);
                            }
                        }

                    }

                }
            });

            return convertView;
        }
    }

    private class ViewHolder {
        TextView textView1;
        EditText editText1;
        int ref;
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
