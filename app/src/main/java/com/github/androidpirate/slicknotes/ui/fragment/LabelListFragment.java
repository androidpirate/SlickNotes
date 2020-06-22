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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.data.Label;
import com.github.androidpirate.slicknotes.util.LabelViewModelFactory;
import com.github.androidpirate.slicknotes.viewmodel.LabelListViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LabelListFragment extends Fragment
    implements LabelListAdapter.OnLabelClickListener {
    private static final String EDIT_LABEL_DIALOG_TITLE = "Edit Label";
    private static final String DELETE_LABEL_DIALOG_TITLE = "Delete Label";
    private static final String DELETE_LABEL_DIALOG_MESSAGE = "Are you sure you want to delete ";
    private static final String DIALOG_NEGATIVE_BUTTON_TITLE = "Cancel";
    private static final String DIALOG_POSITIVE_BUTTON_TITLE = "Ok";
    private static final String QUOTATION_MARK = "\"";
    private static final String QUESTION_MARK = " ?";
    private static final int EMPTY_LIST = 0;

    private RecyclerView recyclerView;
    private TextView emptyListMessage;
    private EditText labelInput;
    private LabelListAdapter adapter;
    private LabelListViewModel viewModel;

    public LabelListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_label_list, container, false);
        // Setup vies
        setupViews(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LabelViewModelFactory factory = new LabelViewModelFactory(
                requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(LabelListViewModel.class);
        viewModel.getDatabaseLabels().observe(getViewLifecycleOwner(), new Observer<List<Label>>() {
            @Override
            public void onChanged(List<Label> labels) {
                if(labels.size() == EMPTY_LIST) {
                    displayEmptyListMessage();

                } else {
                    if (adapter == null) {
                        adapter = new LabelListAdapter(labels, LabelListFragment.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.loadLabels(labels);
                    }
                }
            }
        });
    }

    private void setupViews(View rootView) {
        recyclerView = rootView.findViewById(R.id.rv_labels_list);
        emptyListMessage = rootView.findViewById(R.id.tv_empty_list_message);
    }

    private void displayEmptyListMessage() {
        recyclerView.setVisibility(View.GONE);
        emptyListMessage.setVisibility(View.VISIBLE);
    }

    /**
     * ----- LabelListAdapter Click Listener Interface Implementation -----
     */
    @Override
    public void onEditLabelClick(Label label) {
        displayEditLabelDialog(label);
    }

    @Override
    public void onDeleteLabelClick(Label label) {
        displayDeleteLabelDialog(label);
    }

    private void displayEditLabelDialog(Label label) {
        AlertDialog editLabelDialog = getEditLabelDialogBuilder(label).create();
        editLabelDialog.show();
    }

    private AlertDialog.Builder getEditLabelDialogBuilder(final Label label) {
        // Inflate dialog view
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_label, null);
        labelInput = dialogView.findViewById(R.id.et_label_title);
        labelInput.setText(label.getLabelTitle());
        labelInput.setSelection(label.getLabelTitle().length());
        // Create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(EDIT_LABEL_DIALOG_TITLE)
                .setView(dialogView)
                .setNegativeButton(
                        DIALOG_NEGATIVE_BUTTON_TITLE,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(
                        DIALOG_POSITIVE_BUTTON_TITLE,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateLabel(label);
                    }
                });
        return builder;
    }

    private void updateLabel(Label label) {
        viewModel.updateLabel(
                label.getLabelTitle(),
                labelInput.getText().toString());
    }

    private void displayDeleteLabelDialog(Label label) {
        AlertDialog deleteLabelDialog = getDeleteLabelDialogBuilder(label).create();
        deleteLabelDialog.show();
    }

    private AlertDialog.Builder getDeleteLabelDialogBuilder(final Label label) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(DELETE_LABEL_DIALOG_TITLE)
                .setMessage(
                        DELETE_LABEL_DIALOG_MESSAGE +
                        QUOTATION_MARK +
                        label.getLabelTitle() +
                        QUOTATION_MARK +
                        QUESTION_MARK)
                .setNegativeButton(
                        DIALOG_NEGATIVE_BUTTON_TITLE,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(
                        DIALOG_POSITIVE_BUTTON_TITLE,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteLabel(label);
                    }
                });
        return builder;
    }

    private void deleteLabel(Label label) {
        viewModel.deleteLabel(label);
    }
}