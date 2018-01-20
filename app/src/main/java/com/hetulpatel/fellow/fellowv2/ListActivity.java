package com.hetulpatel.fellow.fellowv2;

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
import android.widget.ListView;
import android.widget.TextView;

public class ListActivity extends AppCompatActivity {

    private ListView list;


    private String[] arrTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.addcourses_layout);
        setContentView(R.layout.activity_list_view);

        MyListAdapter myListAdapter = new MyListAdapter();
        list = (ListView)findViewById(R.id.testinglist);
        list.setAdapter(myListAdapter);

        arrTemp = new String[10];

    }

    private class MyListAdapter extends BaseAdapter {
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
                LayoutInflater inflater = ListActivity.this.getLayoutInflater();
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
                    Log.d("EDITTEXT" , arrTemp[holder.ref]);
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


