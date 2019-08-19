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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.data.Note;
import com.github.androidpirate.slicknotes.viewmodel.NoteListViewModel;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteTrashFragment extends Fragment
    implements NoteListAdapter.NoteClickListener {

    private RecyclerView recyclerView;
    private NoteListAdapter adapter;
    private TextView emptyListMessage;
    private NavController navController;
    private NoteListViewModel viewModel;

    public NoteTrashFragment() {
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
        View view = inflater.inflate(R.layout.fragment_note_trash, container, false);
        recyclerView = view.findViewById(R.id.rv_note_trash_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        emptyListMessage = view.findViewById(R.id.tv_empty_trash_list_message);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = Navigation.findNavController(
                Objects.requireNonNull(getActivity()),
                R.id.nav_host_fragment);
        viewModel = ViewModelProviders.of(this).get(NoteListViewModel.class);
        viewModel.getDatabaseTrashNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if(notes == null || notes.size() == 0) {
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
        if(viewModel.hasAlternateMenu()) {
            inflater.inflate(R.menu.note_trash_list_menu, menu);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_restore:
                break;
            case R.id.action_delete:
                break;
        }
        return super.onOptionsItemSelected(item);
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
        if(viewModel.hasAlternateMenu()) {
            adapter.loadSelectedNoteIds(viewModel.getSelectedNoteIds());
        }
        recyclerView.setAdapter(adapter);
    }

    private void displayAlternateMenu() {
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
    }

    private void navigateToNoteList() {
        viewModel.clearSelections();
        navController.navigate(R.id.nav_trash_to_list);
    }

    private void navigateToNoteDetails(Bundle args) {
        viewModel.clearSelections();
        navController.navigate(R.id.nav_trash_to_details, args);
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
            viewModel.addToSelectedNotes(note);
        } else {
            viewModel.removeFromSelectedNotes(note);
        }
        displayAlternateMenu();
    }
}
