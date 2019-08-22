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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import com.github.androidpirate.slicknotes.data.Note;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteDetailsFragment extends BaseEditableNoteFragment {

    public NoteDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
    public void onPause() {
        super.onPause();
        viewModel.updateNote(title.getText().toString(), details.getText().toString());
        if(isKeyboardOn) {
            hideSoftKeyboard();
        }
    }
}