package com.hetulpatel.fellow.fellowv2.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hetulpatel.fellow.fellowv2.AddCourses;
import com.hetulpatel.fellow.fellowv2.FontTextView;
import com.hetulpatel.fellow.fellowv2.R;
import com.hetulpatel.fellow.fellowv2.drawer.DrawerMain;
import com.hetulpatel.fellow.fellowv2.model.TinyDB;
import com.hetulpatel.fellow.fellowv2.model.user;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link add_new_course_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class add_new_course_frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText course;
    private ListView edittextContainer;
    private String[] arrTemp;
    private MyListAdapterEdit myListAdapter;
    private LinearLayout ok_button , backbtn ;
    private boolean nullwatcher = false;
    private int courses_number = 0 , semester_number = 0;
    private LinearLayout.LayoutParams list;
    private CoordinatorLayout coordinatorLayout;
    private ArrayList<String> courses_name = new ArrayList<>();
    private TinyDB tinydb;
    private FontTextView sem , courses_count;



    public add_new_course_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment add_new_course_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static add_new_course_frag newInstance(String param1, String param2) {
        add_new_course_frag fragment = new add_new_course_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_course_frag, container, false);
        tinydb = new TinyDB(getActivity());

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinate_addcourse);


        course = (EditText) view.findViewById(R.id.numberofcourse_addcourse);
        courses_count = (FontTextView) view.findViewById(R.id.currentsem_addcourse);
        sem = (FontTextView)view.findViewById(R.id.currentcourse_addcourse);


        sem.setText(tinydb.getInt("semester")+"");
        courses_count.setText(tinydb.getListString("courses").size()+"");

        myListAdapter = new MyListAdapterEdit();
        edittextContainer = (ListView)view.findViewById(R.id.addcourse_edittext);
        list = (LinearLayout.LayoutParams) edittextContainer.getLayoutParams();

        backbtn = (LinearLayout) view.findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Manage_courses fragment = new Manage_courses();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
        });


        ok_button = (LinearLayout) view.findViewById(R.id.addcourse_btn_newcourse);

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {



                    if(courses_number>0){




                        String[] names = new String[]{"courses","mon","tue","wed","thu","fri","sat"};

                        for(String name : names){
                            addcourse(name);
                        }

                        Manage_courses fragment = new Manage_courses();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                        /*Intent i = new Intent(getActivity(),DrawerMain.class);
                        startActivity(i);
                        getActivity().finish();*/

                    }else {
                        Toast.makeText(getActivity(),"Fill empty details",Toast.LENGTH_SHORT).show();
                    }


                }catch (Exception e){
                    Toast.makeText(getActivity(),"Fill empty details",Toast.LENGTH_SHORT).show();
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

        return view;
    }

    private void addcourse(String name) {

        ArrayList<String> temp = tinydb.getListString(name);

        for (String course : arrTemp){
            if(!temp.contains(course)){
                temp.add(course);
                createDirIfNotExists("/fellow/sem"+tinydb.getInt("semester")+"/"+course.toLowerCase());
            }
        }

        tinydb.putListString(name,temp);
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
                LayoutInflater inflater = getActivity().getLayoutInflater();
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

}
