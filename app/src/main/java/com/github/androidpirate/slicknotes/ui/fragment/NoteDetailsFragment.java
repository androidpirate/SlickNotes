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

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.data.Note;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteDetailsFragment extends BaseEditableNoteFragment {
    private boolean pinStatus;

    public NoteDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(getArguments() != null) {
            pinStatus = getArguments().getBoolean("notePinStatus");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getArguments() != null) {
            viewModel.getDatabaseNote(getArguments()
                    .getInt("noteId"))
                    .observe(this, new Observer<Note>() {
                        @Override
                        public void onChanged(Note note) {
                            title.setText(note.getTitle());
                            // Set cursor at the end of title
                            title.setSelection(title.getText().length());
                            title.clearFocus();
                            details.setText(note.getDetails());
                            // Set cursor at the end of details
                            details.setSelection(details.getText().length());
                            details.clearFocus();
                            viewModel.updateDatabaseNote(note);
                        }
                    });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.note_details_menu, menu);
        // Set pin icon status
        MenuItem pin = menu.findItem(R.id.action_pin);
        setPinIcon(pinStatus, pin);
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
            case R.id.action_add_label:
                break;
            case R.id.action_send_to_trash:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.updateNote(title.getText().toString(), details.getText().toString());
        if(isKeyboardOn) {
            hideSoftKeyboard();
        }
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