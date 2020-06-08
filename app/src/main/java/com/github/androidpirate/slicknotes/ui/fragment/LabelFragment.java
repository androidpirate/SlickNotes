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
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.data.Label;
import com.github.androidpirate.slicknotes.data.NoteWithLabels;
import com.github.androidpirate.slicknotes.util.LabelViewModelFactory;
import com.github.androidpirate.slicknotes.viewmodel.LabelViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.github.androidpirate.slicknotes.ui.fragment.BaseEditableNoteFragment.EXTRA_NOTE_ID;
import static com.github.androidpirate.slicknotes.ui.fragment.BaseEditableNoteFragment.EXTRA_NOTE_LABELS;

/**
 * A simple {@link Fragment} subclass.
 */
public class LabelFragment extends Fragment
    implements LabelListAdapter.OnLabelClickListener {

    private static final int EMPTY_LIST_SIZE = 0;
    private LinearLayout createLabel;
    private RecyclerView recyclerView;
    private TextView emptyLabelsMessage;
    private SearchView searchView;
    private LabelListAdapter adapter;
    private LabelViewModel viewModel;
    private int noteId;
    private ArrayList<String> noteLabels;

    public LabelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            noteId = getArguments().getInt(EXTRA_NOTE_ID);
            noteLabels = getArguments().getStringArrayList(EXTRA_NOTE_LABELS);
        }
        if(adapter == null) {
            adapter = new LabelListAdapter(new ArrayList<Label>(), this);
        }
        setHasOptionsMenu(true);
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
        LabelViewModelFactory factory = new LabelViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(LabelViewModel.class);
        viewModel.getDatabaseNote(noteId).observe(getViewLifecycleOwner(), new Observer<NoteWithLabels>() {
            @Override
            public void onChanged(NoteWithLabels noteWithLabels) {
                viewModel.setDatabaseNote(noteWithLabels);
                viewModel.setNoteLabels(noteLabels);
            }
        });
        viewModel.getDatabaseLabels().observe(getViewLifecycleOwner(), new Observer<List<Label>>() {
            @Override
            public void onChanged(List<Label> labels) {
                if(labels != null && labels.size() != EMPTY_LIST_SIZE) {
                    viewModel.setLabels(labels);
                    displayLabels(labels);
                } else {
                    displayEmptyLabelsMessage();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.label_list_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryText) {
                if(queryText.length() != 0) {
                    createLabel.setVisibility(View.VISIBLE);
                    setCreateLabelClickListener(queryText);
                } else {
                    createLabel.setVisibility(View.GONE);
                }
                adapter.getFilter().filter(queryText);
                return false;
            }
        });
    }

    private void setupViews(View rootView) {
        createLabel = rootView.findViewById(R.id.create_label);
        recyclerView = rootView.findViewById(R.id.rv_labels_list);
        emptyLabelsMessage = rootView.findViewById(R.id.tv_empty_labels_message);
    }

    private void setCreateLabelClickListener(final String queryText) {
        createLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSearchView();
                for(Label label: viewModel.getLabels()) {
                    if(label.getLabelTitle().equals(queryText)) {
                        displayLabelExistToast();
                        break;
                    }
                }
                viewModel.createNewLabel(queryText);
                adapter.loadLabels(viewModel.getLabels());
                adapter.loadNoteLabels(viewModel.getNoteLabels());

            }
        });
    }

    private void displayLabels(List<Label> labels) {
        if(recyclerView.getVisibility() == View.GONE) {
            displayRecyclerView();
        }

        adapter.loadLabels(labels);
        adapter.loadNoteLabels(noteLabels);
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

    private void displayLabelExistToast() {
        Toast.makeText(
                getContext(),
                R.string.label_exist_toast_content,
                Toast.LENGTH_SHORT)
                .show();
    }

    private void setEmptyLabelsMessage() {
        emptyLabelsMessage.setText(getString(R.string.empty_labels_message));
    }

    private void resetSearchView() {
        searchView.setQuery("", true);
        searchView.clearFocus();
    }

    /**
     *  ---- LabelListAdapter Click Listener Interface Implementation ----
     */
    @Override
    public void onCheckBoxChecked(boolean isChecked, Label label) {
        if(isChecked) {
            viewModel.insertNoteLabel(label);
        } else {
            viewModel.removeNoteLabel(label);
        }
    }
}
