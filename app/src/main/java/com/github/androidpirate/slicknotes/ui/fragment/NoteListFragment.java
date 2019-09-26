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

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.data.Note;
import com.github.androidpirate.slicknotes.viewmodel.NoteListViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link BaseNoteListFragment} subclass.
 */
public class NoteListFragment extends BaseNoteListFragment {

    private NoteListViewModel viewModel;

    public NoteListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setNavigationBase(NOTE_LIST_BASE);
        // If a note is deleted before navigating to list, then display snack bar
        if(getArguments() != null && getArguments().getInt("deletedNoteId") != -1) {
            displayTrashSnackBar(getArguments().getInt("deletedNoteId"));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(NoteListViewModel.class);
        // Set baseViewModel
        baseViewModel = viewModel;
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
        if(baseViewModel.hasAlternateMenu()) {
            inflater.inflate(R.menu.note_list_menu, menu);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pin:
                viewModel.setNotePinStatus(baseViewModel.getSelectedNotes());
                toggleAlternateMenu();
            break;
            case R.id.action_add_label:
                Toast.makeText(
                        getContext(),
                        "Action add label is clicked",
                        Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.action_send_to_trash:
                viewModel.sendNotesToTrash(baseViewModel.getSelectedNotes());
                toggleAlternateMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayTrashSnackBar(final int deletedNoteId) {
        Snackbar.make(
                Objects.requireNonNull(getActivity()).findViewById(android.R.id.content),
                "Note is sent to Trash.",
                Snackbar.LENGTH_SHORT)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.restoreNote(deletedNoteId);
                    }
                })
                .show();
    }
}
