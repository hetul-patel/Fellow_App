package com.hetulpatel.fellow.fellowv2;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hetulpatel.fellow.fellowv2.model.ComplexPreferences;
import com.hetulpatel.fellow.fellowv2.model.user;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ManageCourseActivity extends AppCompatActivity {

    private RecyclerView currentcourse_recyler ;
    private ArrayList<String> courses ;
    private String[] updatetime;
    private FontTextView currentsem, currentcourses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_course);

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(this, "mypref", MODE_PRIVATE);
        user user = complexPreferences.getObject("user", user.class);

        courses = user.getCurrent_courses();

        /*courses = new String[]{"Compiler Design", "Artificial Intelligence",
                "Compiler Design", "Artificial Intelligence",  "Artificial Intelligence",
                "Compiler Design", "Artificial Intelligence",  "Artificial Intelligence"};*/
        updatetime = new String[]{"1","2",
                "4","5","6",
                "4","5","6"};

        Log.d("NANAMAN: ", user.getCurrent_semester()+"");

        currentcourses = (FontTextView)findViewById(R.id.currentcourse);
        currentsem = (FontTextView) findViewById(R.id.currentsem);

        currentcourses.setText(user.getCurrent_courses_count()+"");
        currentsem.setText(user.getCurrent_semester()+"");

        final recyclerAdapter adapter = new recyclerAdapter();
        currentcourse_recyler = (RecyclerView)findViewById(R.id.currentcourserecyclerview);
        RecyclerView.LayoutManager mGridManager = new GridLayoutManager(this,2);
        currentcourse_recyler.setLayoutManager(mGridManager);
        currentcourse_recyler.setItemAnimator(new DefaultItemAnimator());
        currentcourse_recyler.setAdapter(adapter);
        currentcourse_recyler.setNestedScrollingEnabled(false);



    }

    private class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder>{

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, pages;
            public ImageView plus, dustbin;
            public View left , right;


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

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(recyclerAdapter.MyViewHolder holder, int position) {

            holder.title.setText(courses.get(position));
            holder.pages.setText(position+" pages");
            if(position%2 == 0){
                holder.right.setVisibility(View.VISIBLE);
            }else {
                holder.right.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public int getItemCount() {
            if(courses != null && courses.size() != 0){
                return courses.size();
            }
            return 0;
        }


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
}
