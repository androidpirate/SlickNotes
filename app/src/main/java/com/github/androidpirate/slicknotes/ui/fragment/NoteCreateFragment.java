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
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.viewmodel.NoteViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteCreateFragment extends Fragment {
    private EditText title;
    private EditText details;
    private NoteViewModel viewModel;

    public NoteCreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note_create, container, false);
        title = view.findViewById(R.id.et_title);
        details = view.findViewById(R.id.et_details);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
    }

    @Override
    public void onPause() {
        super.onPause();
        String titleString = title.getText().toString();
        String detailsString = details.getText().toString();
        if(!titleString.isEmpty() || !detailsString.isEmpty()) {
            viewModel.insertDefaultNote(titleString, detailsString);
        }
    }
}
