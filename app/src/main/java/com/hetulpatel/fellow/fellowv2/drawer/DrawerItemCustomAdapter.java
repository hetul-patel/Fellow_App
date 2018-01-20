package com.hetulpatel.fellow.fellowv2.drawer;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hetulpatel.fellow.fellowv2.R;

/**
 * Created by hetulpatel on 19/06/17.
 */

public class DrawerItemCustomAdapter extends ArrayAdapter<DataModel> {


    Context mContext;
    int layoutResourceId;
    DataModel data[] = null;
    private int selectedPosition = 0;


    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, DataModel[] data) {

        super(mContext,layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);


        TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);

        DataModel folder = data[position];

        textViewName.setText(folder.name);

        if (position == selectedPosition)
            textViewName.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
        else
            textViewName.setTextColor(getContext().getResources().getColor(R.color.navigation_notselected));

        return listItem;
    }

    public void setSelectedPosition(int position) {

        this.selectedPosition = position;

    }
}
