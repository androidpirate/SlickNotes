/*
 * <!--
 *  Copyright (C) 2016 The Android Open Source Project
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 * -->
 */

package com.github.androidpirate.slicknotes.ui.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.data.Note;
import com.github.androidpirate.slicknotes.viewmodel.NoteListViewModel;
import com.github.androidpirate.slicknotes.viewmodel.NoteViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteListFragment extends Fragment {
    private RecyclerView recyclerView;
    private NoteListAdapter adapter;
    private TextView emptyListMesssage;
    private NavController navController;
    private NoteListViewModel viewModel;

    public NoteListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        recyclerView = view.findViewById(R.id.rv_note_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        emptyListMesssage = view.findViewById(R.id.tv_empty_list_message);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(NoteListViewModel.class);
        viewModel.getDatabaseNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if(notes == null || notes.size() == 0){
                    displayEmptyListMessage();
                } else {
                    displayNotes(notes);
                }
            }
        });
    }

    private void displayEmptyListMessage() {
        recyclerView.setVisibility(View.GONE);
        emptyListMesssage.setVisibility(View.VISIBLE);
    }

    private void displayNoteList(){
        recyclerView.setVisibility(View.VISIBLE);
        emptyListMesssage.setVisibility(View.GONE);
    }

    private void displayNotes(List<Note> notes) {
        if(recyclerView.getVisibility() == View.GONE) {
            displayNoteList();
        }
        if(adapter == null) {
            adapter = new NoteListAdapter(notes);
        } else {
            adapter.loadNotes(notes);
        }
        recyclerView.setAdapter(adapter);
    }
}
