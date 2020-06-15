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


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.data.NoteWithLabels;
import com.github.androidpirate.slicknotes.util.NoteViewModelFactory;
import com.github.androidpirate.slicknotes.viewmodel.NoteListViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * A simple {@link BaseNoteListFragment} subclass.
 */
public class NoteListFragment extends BaseNoteListFragment {

    private static final String ADD_LABEL_MESSAGE = "Adding labels will be available soon";
    private static final String SEND_TO_TRASH_MESSAGE = "Selected notes are sent to trash";

    private SharedPreferences sharedPref;
    private NoteListViewModel viewModel;

    public NoteListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        setHasOptionsMenu(true);
        setNavigationBase(NOTE_LIST_BASE);
        // If a note is deleted before navigating to list, then display snack bar
        if(getArguments() != null) {
            if (getArguments().getInt(BaseEditableNoteFragment.DELETED_NOTE_ID) != -1) {
                displayTrashSnackBar(getArguments().getInt(BaseEditableNoteFragment.DELETED_NOTE_ID));
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NoteViewModelFactory factory = new NoteViewModelFactory(
                requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(NoteListViewModel.class);
        // Set baseViewModel
        baseViewModel = viewModel;
        // Get preference to order notes
        boolean addNewItemsOnTop = sharedPref.getBoolean(getString(R.string.pref_item_order_key), false);
        viewModel.getDatabaseNotes(addNewItemsOnTop)
            .observe(getViewLifecycleOwner(), new Observer<List<NoteWithLabels>>() {
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
            inflater.inflate(R.menu.note_list_selected_menu, menu);
        } else {
            inflater.inflate(R.menu.note_list_menu, menu);
            if(getLayout()) {
                menu.findItem(R.id.action_change_layout).setIcon(R.drawable.ic_grid);
            } else {
                menu.findItem(R.id.action_change_layout).setIcon(R.drawable.ic_list);
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_layout:
                setLayoutPreference();
            case R.id.action_pin:
                viewModel.setNotePinStatus(baseViewModel.getSelectedNotes());
                toggleAlternateMenu();
                break;
            case R.id.action_send_to_trash:
                viewModel.sendNotesToTrash(baseViewModel.getSelectedNotes());
                toggleAlternateMenu();
                displayToast(SEND_TO_TRASH_MESSAGE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayTrashSnackBar(final int deletedNoteId) {
        Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
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
