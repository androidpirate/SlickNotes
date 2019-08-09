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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.data.Note;
import com.github.androidpirate.slicknotes.viewmodel.NoteViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteDetailsFragment extends Fragment {
    private EditText title;
    private EditText details;
    private NoteViewModel viewModel;

    public NoteDetailsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_note_details, container, false);
        title = view.findViewById(R.id.et_title);
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.updateNoteTitle(s.toString());
            }
        });
        details = view.findViewById(R.id.et_details);
        details.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.updateNoteDetails(s.toString());
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        if(getArguments() != null) {
            viewModel.getDatabaseNote(getArguments()
                    .getInt("noteId"))
                    .observe(this, new Observer<Note>() {
                        @Override
                        public void onChanged(Note note) {
                            title.setText(note.getTitle());
                            // Set cursor at the end of title
                            title.setSelection(title.getText().length());
                            details.setText(note.getDetails());
                            // Set cursor at the end of details
                            details.setSelection(details.getText().length());
                            viewModel.updateViewModelNote(note);
                        }
                    });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        String titleText = title.getText().toString();
        String detailsText = details.getText().toString();
        if(!titleText.isEmpty() || !detailsText.isEmpty()) {
            viewModel.updateNote();
        }
    }
}