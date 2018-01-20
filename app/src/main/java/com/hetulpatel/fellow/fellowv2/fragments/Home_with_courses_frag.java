package com.hetulpatel.fellow.fellowv2.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hetulpatel.fellow.fellowv2.FontTextView;
import com.hetulpatel.fellow.fellowv2.R;
import com.hetulpatel.fellow.fellowv2.dgcam.DgCamActivity;
import com.hetulpatel.fellow.fellowv2.model.TinyDB;

import java.text.DateFormat;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home_with_courses_frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home_with_courses_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home_with_courses_frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ListView listofcourses;
    private ArrayList<String> courses ;
    private String[] updatetime;
    private RelativeLayout.LayoutParams list;
    private CheckBox breakaday;
    private SharedPreferences pref;
    private ImageView manage_btn;

    private FontTextView username , dayofweek , dateofmonth ;

    public Home_with_courses_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home_with_courses_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static Home_with_courses_frag newInstance(String param1, String param2) {
        Home_with_courses_frag fragment = new Home_with_courses_frag();
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
        View view = inflater.inflate(R.layout.activity_home_with_courses, container, false);

        TinyDB tiny = new TinyDB(getActivity());

        DateFormat d = new java.text.SimpleDateFormat("EEE");
        String currentday = d.format(java.util.Calendar.getInstance().getTime());

        DateFormat dm = new java.text.SimpleDateFormat("d MMMM");
        String currentdateandmonth = dm.format(java.util.Calendar.getInstance().getTime());

        courses = tiny.getListString(currentday.toLowerCase());

        /*courses = new String[]{"Compiler Design", "Artificial Intelligence",
                "Compiler Design", "Artificial Intelligence",  "Artificial Intelligence",
                "Compiler Design", "Artificial Intelligence",  "Artificial Intelligence"};*/
        updatetime = new String[]{"1","2",
                "4","5","6",
                "4","5","6","9","10","11","1","2",
                "4","5","6",
                "4","5","6","9","10","11","1","2",
                "4","5","6",
                "4","5","6","9","10","11"};

        MyCoursesAdapter mycoursesadapter = new MyCoursesAdapter();
        listofcourses = (ListView) view.findViewById(R.id.listofcourses_homewithcourses);
        listofcourses.setAdapter(mycoursesadapter);
        list = (RelativeLayout.LayoutParams) listofcourses.getLayoutParams();
        list.height = courses.size()*160;

        breakaday = (CheckBox) view.findViewById(R.id.breakaday);

        breakaday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(breakaday.isChecked())
                    listofcourses.setVisibility(View.INVISIBLE);
                else listofcourses.setVisibility(View.VISIBLE);
            }
        });


        username = (FontTextView) view.findViewById(R.id.NameOfUser_homewithcourses);
        dateofmonth = (FontTextView) view.findViewById(R.id.DateAndMonth_homewithcourses);
        dayofweek = (FontTextView) view.findViewById(R.id.DayOfWeek_homewithcouses);

        pref = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);


        String username_string  = pref.getString("username",null);
        username_string = username_string.substring(0,1).toUpperCase() + username_string.substring(1).toLowerCase();
        username.setText(username_string);


        dateofmonth.setText(currentdateandmonth);
        dayofweek.setText(currentday+",");

        manage_btn = (ImageView)view.findViewById(R.id.homewithcourses_plus_btn);
        manage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new Manage_courses());
                ft.commit();
            }
        });

        return view;
    }

    private class MyCoursesAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if(courses != null && courses.size() != 0){
                return courses.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return courses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            if (convertView == null) {

                holder = new ViewHolder();
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.homewithcourse_item, null);
                holder.textView1 = (FontTextView) convertView.findViewById(R.id.titleofcourse);
                holder.textView2 = (FontTextView) convertView.findViewById(R.id.updatetime);
                holder.imageview = (ImageView) convertView.findViewById(R.id.uploadbtn_homewithcourse);

                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            holder.ref = position;

            holder.textView1.setText(courses.get(position));
            holder.textView2.setText("Last updated "+updatetime[position]+" days ago" );
            holder.imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),DgCamActivity.class);
                    intent.putExtra("title",courses.get(position));
                    getActivity().startActivity(intent);
                    //Toast.makeText(getActivity(),courses.get(position),Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }
    }

    private class ViewHolder {
        FontTextView textView1;
        FontTextView textView2;
        ImageView imageview;
        int ref;
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
