package com.hetulpatel.fellow.fellowv2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Home_with_courses extends AppCompatActivity {

    private ListView listofcourses;
    private String[] courses ;
    private String[] updatetime;
    private RelativeLayout.LayoutParams list;
    private CheckBox breakaday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_with_courses);

        courses = new String[]{"Compiler Design", "Artificial Intelligence",
                "Compiler Design", "Artificial Intelligence",  "Artificial Intelligence",
                "Compiler Design", "Artificial Intelligence",  "Artificial Intelligence"};
        updatetime = new String[]{"1","2",
                "4","5","6",
                "4","5","6"};

        MyCoursesAdapter mycoursesadapter = new MyCoursesAdapter();
        listofcourses = (ListView) findViewById(R.id.listofcourses_homewithcourses);
        listofcourses.setAdapter(mycoursesadapter);
        list = (RelativeLayout.LayoutParams) listofcourses.getLayoutParams();
        list.height = courses.length*160;

        breakaday = (CheckBox) findViewById(R.id.breakaday);

        breakaday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(breakaday.isChecked())
                    listofcourses.setVisibility(View.INVISIBLE);
                else listofcourses.setVisibility(View.VISIBLE);
            }
        });



    }

    private class MyCoursesAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            if(courses != null && courses.length != 0){
                return courses.length;
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return courses[position];
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
                LayoutInflater inflater = Home_with_courses.this.getLayoutInflater();
                convertView = inflater.inflate(R.layout.homewithcourse_item, null);
                holder.textView1 = (FontTextView) convertView.findViewById(R.id.titleofcourse);
                holder.textView2 = (FontTextView) convertView.findViewById(R.id.updatetime);
                holder.imageview = (ImageView) convertView.findViewById(R.id.uploadbtn_homewithcourse);

                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            holder.ref = position;

            holder.textView1.setText(courses[position]);
            holder.textView2.setText("Last updated "+updatetime[position]+" days ago" );
            holder.imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Home_with_courses.this,courses[position],Toast.LENGTH_SHORT).show();
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
}
