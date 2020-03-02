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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.data.Note;
import com.github.androidpirate.slicknotes.util.CustomTextWatcher;
import com.github.androidpirate.slicknotes.util.NoteViewModelFactory;
import com.github.androidpirate.slicknotes.viewmodel.NoteDetailViewModel;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteDetailsFragment extends BaseEditableNoteFragment {
    private static final int NOTE_LIST_BASE = 0;
    private static final int TRASH_LIST_BASE = 1;
    private NoteDetailViewModel viewModel;
    private boolean pinStatus;
    private int noteId;
    private int navigationBase;

    public NoteDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(getArguments() != null) {
            pinStatus = getArguments().getBoolean(NOTE_PIN_STATUS);
            noteId = getArguments().getInt(EXTRA_NOTE_ID);
            navigationBase = getArguments().getInt(NAVIGATION_BASE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NoteViewModelFactory factory = new NoteViewModelFactory(
                Objects.requireNonNull(getActivity()).getApplication());
        viewModel = ViewModelProviders.of(this, factory).get(NoteDetailViewModel.class);
        viewModel.getDatabaseNote(noteId).observe(this, new Observer<Note>() {
            @Override
            public void onChanged(final Note note) {
                setBackgroundColor(note.getColorId());
                title.setText(note.getTitle());
                // Set cursor at the end of title
                title.setSelection(title.getText().length());
                title.clearFocus();
                details.setText(note.getDetails());
                // Set cursor at the end of details
                details.setSelection(details.getText().length());
                details.clearFocus();
                title.addTextChangedListener(new CustomTextWatcher() {
                    @Override
                    public void onTextChanged(String _after) {
                        note.setTitle(_after);
                   }
                });
                details.addTextChangedListener(new CustomTextWatcher() {
                   @Override
                    public void onTextChanged(String _after) {
                       note.setDetails(_after);
                   }
               });
                viewModel.updateDatabaseNote(note);
            }
        });

        if(navigationBase == TRASH_LIST_BASE) {
            title.setFocusable(false);
            details.setFocusable(false);
            Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
            setFabActionVisibility();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(navigationBase == NOTE_LIST_BASE) {
            inflater.inflate(R.menu.note_details_list_base_menu, menu);
            // Set pin icon status
            MenuItem pin = menu.findItem(R.id.action_pin);
            setPinIcon(pinStatus, pin);
        } else {
            inflater.inflate(R.menu.note_details_trash_base_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pin:
                pinStatus = viewModel.updateNotePinStatus();
                setPinIcon(pinStatus, item);
                displayPinToast();
                break;
            case R.id.action_set_reminder:
                break;
            case R.id.action_send_to_trash:
                viewModel.moveNoteToTrash();
                navigateToList(noteId);
                break;
            case R.id.action_restore:
                viewModel.restoreNote();
                navigateToTrash();
                break;
            case R.id.action_delete:
                viewModel.deleteNote();
                navigateToTrash();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.updateNote();
        if(isKeyboardOn) {
            hideSoftKeyboard();
        }
    }

    @Override
    void onColorPickerFabClick(int colorId) {
        viewModel.updateNoteColor(colorId);
        setBackgroundColor(colorId);
        hideColorPickerDialog();
    }

    private void setPinIcon(boolean isPinned, MenuItem item) {
        if(isPinned) {
            item.setIcon(R.drawable.ic_pin_selected);
        } else {
            item.setIcon(R.drawable.ic_pin);
        }
    }

    private void displayPinToast() {
        if(pinStatus) {
            Toast.makeText(getContext(),
                    getResources().getString(R.string.note_pinned_toast),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(),
                    getResources().getString(R.string.note_unpinned_toast),
                    Toast.LENGTH_SHORT).show();
        }
    }
}