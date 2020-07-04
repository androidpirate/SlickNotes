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


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.data.NoteWithLabels;
import com.github.androidpirate.slicknotes.util.NoteViewModelFactory;
import com.github.androidpirate.slicknotes.viewmodel.NoteTrashViewModel;

import java.util.List;

/**
 * A simple {@link BaseNoteListFragment} subclass.
 */
public class NoteTrashFragment extends BaseNoteListFragment {

    private static final String NOTES_RESTORED_MESSAGE = "Selected notes are restored";
    private static final String DELETE_NOTE_DIALOG_TITLE = "Delete Forever";
    private static final String DELETE_NOTE_DIALOG_MESSAGE = "Are you sure you want to delete note(s) forever?";
    private static final String DELETE_NOTE_DIALOG_POSITIVE_BUTTON = "Ok";
    private static final String DELETE_NOTE_DIALOG_NEGATIVE_BUTTON = "Cancel";

    private NoteTrashViewModel viewModel;

    public NoteTrashFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setNavigationBase(TRASH_LIST_BASE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NoteViewModelFactory factory = new NoteViewModelFactory(
                requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(NoteTrashViewModel.class);
        // Set baseViewModel
        baseViewModel = viewModel;
        viewModel.getDatabaseTrashNotes().observe(getViewLifecycleOwner(), new Observer<List<NoteWithLabels>>() {
            @Override
            public void onChanged(List<NoteWithLabels> notes) {
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
        MenuInflater inflater = requireActivity().getMenuInflater();
        menu.clear();
        if(baseViewModel.hasAlternateMenu()) {
            inflater.inflate(R.menu.note_trash_list_menu, menu);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_restore:
                viewModel.restoreNotes(baseViewModel.getSelectedNotes());
                displayToast(NOTES_RESTORED_MESSAGE);
                break;
            case R.id.action_delete:
                getDeleteNoteDialogBuilder().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private AlertDialog.Builder getDeleteNoteDialogBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(DELETE_NOTE_DIALOG_TITLE)
                .setMessage(
                        DELETE_NOTE_DIALOG_MESSAGE)
                .setNegativeButton(
                        DELETE_NOTE_DIALOG_NEGATIVE_BUTTON,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton(
                        DELETE_NOTE_DIALOG_POSITIVE_BUTTON,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.deleteNotes(baseViewModel.getSelectedNotes());
                            }
                        });
        return builder;
    }
}
