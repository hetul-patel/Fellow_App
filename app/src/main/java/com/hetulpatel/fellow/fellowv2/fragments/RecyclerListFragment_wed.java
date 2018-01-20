/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hetulpatel.fellow.fellowv2.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hetulpatel.fellow.fellowv2.FontTextView;
import com.hetulpatel.fellow.fellowv2.R;
import com.hetulpatel.fellow.fellowv2.model.ComplexPreferences;
import com.hetulpatel.fellow.fellowv2.model.TinyDB;
import com.hetulpatel.fellow.fellowv2.tabs.RecyclerListAdapter;
import com.hetulpatel.fellow.fellowv2.tabs.helper.OnStartDragListener;
import com.hetulpatel.fellow.fellowv2.tabs.helper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;

/**
 * @author Paul Burke (ipaulpro)
 */
public class RecyclerListFragment_wed extends Fragment implements OnStartDragListener {

    private ItemTouchHelper mItemTouchHelper;
    private RecyclerView schedule_recycler;
    private final ArrayList<String> mItems = new ArrayList<>();
    private int index;
    private RecyclerListAdapter adapter;
    private TinyDB tiny;
    private FontTextView reset;
    private ComplexPreferences complexPreferences;

    public RecyclerListFragment_wed() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_layout, container, false);

        schedule_recycler = (RecyclerView) view.findViewById(R.id.schedule_recycler);
        reset = (FontTextView)view.findViewById(R.id.reset);
        index = 0;
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        tiny = new TinyDB(getActivity());

        ArrayList<String> courses = new ArrayList<>();

        courses = tiny.getListString("wed");

        mItems.clear();

        for (int k = 0 ; k < courses.size() ; k++)
            mItems.add(courses.get(k));

        adapter = new RecyclerListAdapter(getActivity(), this, mItems) {
            @Override
            public void updateList() {
                tiny.putListString("wed",adapter.getFinalList());
            }
        };

        schedule_recycler.setHasFixedSize(true);
        schedule_recycler.setAdapter(adapter);
        schedule_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(schedule_recycler);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiny.putListString("wed",tiny.getListString("courses"));
                adapter.refreshdata(tiny.getListString("courses"));
            }
        });
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        // TODO Auto-generated method stub
        super.onAttachFragment(fragment);



    }

}
