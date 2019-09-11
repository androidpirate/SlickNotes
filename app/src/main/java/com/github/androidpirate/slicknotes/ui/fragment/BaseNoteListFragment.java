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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.data.Note;
import com.github.androidpirate.slicknotes.viewmodel.NoteListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public abstract class BaseNoteListFragment extends Fragment
    implements NoteListAdapter.NoteClickListener {
    static final int NOTE_LIST_BASE = 0;
    static final int TRASH_LIST_BASE = 1;

    private RecyclerView recyclerView;
    private NoteListAdapter adapter;
    private TextView emptyListMessage;
    private NavController navController;
    private int navigationBase;
    NoteListViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note_list_base, container, false);
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

    }

    void toggleAlternateMenu() {
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
    }

    void setNavigationBase(int navigationBase) {
        this.navigationBase = navigationBase;
    }

    void navigateToNoteList() {
        viewModel.clearSelections();
        navController.navigate(R.id.nav_trash_to_list);
    }

    void displayEmptyListMessage() {
        recyclerView.setVisibility(View.GONE);
        emptyListMessage.setVisibility(View.VISIBLE);
    }

    void displayNotes(List<Note> notes) {
        if(recyclerView.getVisibility() == View.GONE) {
            displayRecyclerView();
        }
        if(adapter == null) {
            adapter = new NoteListAdapter(notes, this);
        } else {
            adapter.loadNotes(notes);
        }
        if(viewModel.hasAlternateMenu()) {
            adapter.loadSelectedNoteIds(viewModel.getSelectedNoteIds());
        }
        recyclerView.setAdapter(adapter);
    }

    private void displayRecyclerView(){
        recyclerView.setVisibility(View.VISIBLE);
        emptyListMessage.setVisibility(View.GONE);
    }

    private void navigateToCreateNote() {
        viewModel.clearSelections();
        navController.navigate(R.id.nav_home_to_create);
    }

    private void navigateToNoteDetails(Bundle args) {
        viewModel.clearSelections();
        if(navigationBase == NOTE_LIST_BASE) {
            navController.navigate(R.id.nav_home_to_details, args);
        } else if (navigationBase == TRASH_LIST_BASE) {
            navController.navigate(R.id.nav_trash_to_details, args);
        }
    }

    /**
     * ---- NoteListAdapter Click Listener Interface Implementation ----
     */
    @Override
    public void onNoteClick(int noteId, boolean notePinStatus) {
        Bundle args = new Bundle();
        args.putInt("noteId", noteId);
        args.putBoolean("notePinStatus", notePinStatus);
        navigateToNoteDetails(args);
    }

    @Override
    public void onLongNoteClick(Note note, boolean isAdded) {
        if(isAdded) {
            viewModel.addToSelectedNotes(note);
        } else {
            viewModel.removeFromSelectedNotes(note);
        }
        toggleAlternateMenu();
    }
}
