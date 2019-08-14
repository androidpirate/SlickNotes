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
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.data.Note;
import com.github.androidpirate.slicknotes.viewmodel.NoteListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteListFragment extends Fragment
    implements NoteListAdapter.NoteClickListener {

    private RecyclerView recyclerView;
    private NoteListAdapter adapter;
    private TextView emptyListMessage;
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
        emptyListMessage = view.findViewById(R.id.tv_empty_list_message);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToCreateNote();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = Navigation
                .findNavController(
                        Objects.requireNonNull(getActivity()),
                        R.id.nav_host_fragment);
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuInflater inflater = Objects.requireNonNull(getActivity()).getMenuInflater();
        menu.clear();
        if(viewModel.isHasAlternateMenu()) {
            inflater.inflate(R.menu.note_list_menu, menu);
        }
        super.onPrepareOptionsMenu(menu);
    }

    private void displayEmptyListMessage() {
        recyclerView.setVisibility(View.GONE);
        emptyListMessage.setVisibility(View.VISIBLE);
    }

    private void displayRecyclerView(){
        recyclerView.setVisibility(View.VISIBLE);
        emptyListMessage.setVisibility(View.GONE);
    }

    private void displayNotes(List<Note> notes) {
        if(recyclerView.getVisibility() == View.GONE) {
            displayRecyclerView();
        }
        if(adapter == null) {
            adapter = new NoteListAdapter(notes, this);
        } else {
            adapter.loadNotes(notes);
        }
        if(viewModel.isHasAlternateMenu()) {
            adapter.loadSelectedNoteIds(viewModel.getSelectedNoteIds());
        }
        recyclerView.setAdapter(adapter);
    }

    private void displayAlternateMenu() {
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
    }

    private void navigateToCreateNote() {
        viewModel.clearSelectedNotesIds();
        navController.navigate(R.id.nav_home_to_create);
    }

    private void navigateToNoteDetails(Bundle args) {
        viewModel.clearSelectedNotesIds();
        navController.navigate(R.id.nav_home_to_details, args);
    }

    /**
     * ---- NoteListAdapter Click Listener Interface Implementation ----
     */

    @Override
    public void onNoteClick(int noteId) {
        Bundle args = new Bundle();
        args.putInt("noteId", noteId);
        navigateToNoteDetails(args);
    }

    @Override
    public void onLongNoteClick(Note note, boolean isAdded) {
        if(isAdded) {
            viewModel.setHasAlternateMenu(true);
            viewModel.addToSelectedNotes(note.getNoteId());
        } else {
            viewModel.removeFromSelectedNotes(note.getNoteId());
            if(viewModel.getSelectedNoteIds().size() == 0) {
                viewModel.setHasAlternateMenu(false);
            }
        }
        displayAlternateMenu();
    }
}
