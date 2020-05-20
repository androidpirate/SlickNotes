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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.data.Label;
import com.github.androidpirate.slicknotes.data.NoteWithLabels;
import com.github.androidpirate.slicknotes.repo.NoteRepository;
import com.github.androidpirate.slicknotes.util.NoteViewModelFactory;
import com.github.androidpirate.slicknotes.viewmodel.NoteDetailViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.github.androidpirate.slicknotes.ui.fragment.BaseEditableNoteFragment.EXTRA_NOTE_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class LabelFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView emptyLabelsMessage;
    private LabelListAdapter adapter;
    private NoteDetailViewModel viewModel;
    private int noteId;

    public LabelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            noteId = getArguments().getInt(EXTRA_NOTE_ID);
        }
        if(adapter == null) {
            adapter = new LabelListAdapter(new ArrayList<Label>());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_label, container, false);
        setupViews(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NoteViewModelFactory factory = new NoteViewModelFactory(
                requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(NoteDetailViewModel.class);
        viewModel.getDatabaseNote(noteId).observe(getViewLifecycleOwner(), new Observer<NoteWithLabels>() {
            @Override
            public void onChanged(NoteWithLabels noteWithLabels) {
                if(noteWithLabels != null && noteWithLabels.getLabels().size() != 0) {
                    displayLabels(noteWithLabels.getLabels());
                } else {
                    displayEmptyLabelsMessage();
                }
            }
        });
    }

    private void setupViews(View rootView) {
        recyclerView = rootView.findViewById(R.id.rv_labels_list);
        emptyLabelsMessage = rootView.findViewById(R.id.tv_empty_labels_message);
    }

    private void displayLabels(List<Label> labels) {
        if(recyclerView.getVisibility() == View.GONE) {
            displayRecyclerView();
        }
        adapter.loadLabels(labels);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void displayRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
        emptyLabelsMessage.setVisibility(View.GONE);
    }

    private void displayEmptyLabelsMessage() {
        setEmptyLabelsMessage();
        emptyLabelsMessage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void setEmptyLabelsMessage() {
        emptyLabelsMessage.setText(getString(R.string.empty_labels_message));
    }
}
