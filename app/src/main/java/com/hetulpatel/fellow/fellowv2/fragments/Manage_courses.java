package com.hetulpatel.fellow.fellowv2.fragments;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hetulpatel.fellow.fellowv2.FontTextView;
import com.hetulpatel.fellow.fellowv2.ManageCourseActivity;
import com.hetulpatel.fellow.fellowv2.R;
import com.hetulpatel.fellow.fellowv2.dgcam.DgCamActivity;
import com.hetulpatel.fellow.fellowv2.model.ComplexPreferences;
import com.hetulpatel.fellow.fellowv2.model.TinyDB;
import com.hetulpatel.fellow.fellowv2.model.user;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Manage_courses.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Manage_courses#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Manage_courses extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView currentcourse_recyler ;
    private ArrayList<String> courses ;
    private String[] updatetime;
    private FontTextView currentsem, currentcourses;
    private ImageView plusenabled;
    private AlertDialog.Builder alertDialog;
    private String NEW_COURSE_NAME;
    private TinyDB tiny;
    private recyclerAdapter adapter;
    private ImageView schedule_btn;

    public Manage_courses() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Manage_courses.
     */
    // TODO: Rename and change types and number of parameters
    public static Manage_courses newInstance(String param1, String param2) {
        Manage_courses fragment = new Manage_courses();
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
        View view = inflater.inflate(R.layout.activity_manage_course, container, false);



        tiny = new TinyDB(getActivity());


        //courses = user.getCurrent_courses();
        courses = tiny.getListString("courses");

        /*courses = new String[]{"Compiler Design", "Artificial Intelligence",
                "Compiler Design", "Artificial Intelligence",  "Artificial Intelligence",
                "Compiler Design", "Artificial Intelligence",  "Artificial Intelligence"};*/

        plusenabled = (ImageView)view.findViewById(R.id.plusenabled);



        plusenabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_new_course_frag fragment = new add_new_course_frag();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                /*createAlert();
                alertDialog.show();*/
            }
        });

        currentcourses = (FontTextView)view.findViewById(R.id.currentcourse);
        currentsem = (FontTextView) view.findViewById(R.id.currentsem);
        schedule_btn = (ImageView) view.findViewById(R.id.currentcourse_managetimtable);

        schedule_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new schedule_frag());
                ft.commit();
            }
        });

        //currentcourses.setText(user.getCurrent_courses_count()+"");
        //currentsem.setText(user.getCurrent_semester()+"");

        currentcourses.setText(courses.size()+"");
        currentsem.setText(tiny.getInt("semester")+"");



        adapter = new recyclerAdapter();
        currentcourse_recyler = (RecyclerView)view.findViewById(R.id.currentcourserecyclerview);
        RecyclerView.LayoutManager mGridManager = new GridLayoutManager(getActivity(),2);
        currentcourse_recyler.setLayoutManager(mGridManager);
        currentcourse_recyler.setItemAnimator(new DefaultItemAnimator());
        currentcourse_recyler.setAdapter(adapter);
        currentcourse_recyler.setNestedScrollingEnabled(false);
        return view;
    }

    private void createAlert() {

        alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Add a new course");
       // alertDialog.setCancelable(false);

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(40, 30, 40, 16);

        //course name
        final EditText input = new EditText(getActivity());
        input.setWidth(100);
        input.setLines(1);
        input.setMaxWidth(50);
        input.setHint("Enter name of course");
        input.setTextSize(16);

        layout.addView(input,params);

        alertDialog.setView(layout);

        alertDialog.setPositiveButton("CREATE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (!input.getText().toString().isEmpty()) {

                            NEW_COURSE_NAME = input.getText().toString();

                            new CreateCourse().execute(NEW_COURSE_NAME);

                        }else {
                            Log.d("COURSE NAME : " , "EMPTY");
                        }
                    }
                });

        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        dialog.dismiss();
                    }
                });


    }

    private void deletecourse( String name ){
        if(courses.contains(new String(name))){
            courses.remove(name);
            currentsem.setText(courses.size()+"");
            adapter.notifyDataSetChanged();

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {


                    tiny.putListString("courses",courses);

                }
            });

        };
    }

    private void createNewCourse( final String name) throws InterruptedException {



        Log.d("COURSE ADDED : " , name);
        if(!courses.contains(new String(name))){
            courses.add(name);



                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 200);

        }
    }

    class CreateCourse extends AsyncTask<String, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("loading");
            pd.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            tiny.putListString("courses",courses);
            createDirIfNotExists("/fellow/sem"+tiny.getInt("semester")+"/"+params[0].toLowerCase());
            return null;
            // Do your request
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pd != null)
            {
                pd.dismiss();
            }
            adapter.notifyDataSetChanged();
            currentsem.setText(courses.size()+"");
        }
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

    private class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder>{

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, pages;
            public ImageView plus, dustbin;
            public View left,right;


            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.item_current_title);
                pages = (TextView) view.findViewById(R.id.item_current_pages);
                plus = (ImageView) view.findViewById(R.id.item_current_plus);
                dustbin = (ImageView) view.findViewById(R.id.item_current_dustbin);
                left = (View) view.findViewById(R.id.left_devider);
                right = (View) view.findViewById(R.id.right_devider);
            }
        }

        @Override
        public recyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_currentcourses, parent, false);

            return new recyclerAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(recyclerAdapter.MyViewHolder holder, final int position) {

            String path2 = Environment.getExternalStorageDirectory().toString()+"/fellow/sem"+currentsem.getText().toString();
            Log.d("PATH: ", path2);
            File dir = new File(path2 +"/"+courses.get(position).toLowerCase());
            final List<File> files = getListFiles(dir);

            holder.title.setText(courses.get(position));
            holder.pages.setText(files.size()+" pages");
            if(position%2 == 0){
                holder.right.setVisibility(View.VISIBLE);

            }else {
                holder.right.setVisibility(View.INVISIBLE);
            }

            holder.dustbin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletecourse(courses.get(position));
                }
            });

            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),DgCamActivity.class);
                    intent.putExtra("title",courses.get(position));
                    getActivity().startActivity(intent);
                    //Toast.makeText(getActivity(),courses.get(position),Toast.LENGTH_SHORT).show();
                }
            });


            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String path = Environment.getExternalStorageDirectory().toString()+"/fellow/sem"+currentsem.getText().toString()+"/pdfs/"
                            +courses.get(position)+".pdf";
                    File file = new File(path);
                    Uri uri = Uri.fromFile(file);
                    Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                    pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pdfOpenintent.setDataAndType(uri, "application/pdf");
                    try {
                        if(files.size()>0)
                            startActivity(pdfOpenintent);
                    }
                    catch (ActivityNotFoundException e) {

                    }
                }
            });



        }

        @Override
        public int getItemCount() {
            if(courses != null && courses.size() != 0){
                return courses.size();
            }
            return 0;
        }

        private List<File> getListFiles(File parentDir) {
            List<File> inFiles = new ArrayList<>();
            Queue<File> files = new LinkedList<>();
            files.addAll(Arrays.asList(parentDir.listFiles()));
            while (!files.isEmpty()) {
                File file = files.remove();
                if (file.isDirectory()) {
                    files.addAll(Arrays.asList(file.listFiles()));
                } else if (file.getName().endsWith(".jpg")) {
                    inFiles.add(file);
                }
            }
            return inFiles;
        }


    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;




    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
