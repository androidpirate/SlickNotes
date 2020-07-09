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

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.data.Label;
import com.github.androidpirate.slicknotes.data.NoteWithLabels;
import com.github.androidpirate.slicknotes.util.LabelViewModelFactory;
import com.github.androidpirate.slicknotes.viewmodel.NoteLabelViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.github.androidpirate.slicknotes.ui.fragment.BaseEditableNoteFragment.EXTRA_NOTE_ID;
import static com.github.androidpirate.slicknotes.ui.fragment.BaseEditableNoteFragment.EXTRA_NOTE_LABELS;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteLabelFragment extends Fragment
    implements NoteLabelListAdapter.OnLabelClickListener {

    private static final int EMPTY_LIST_SIZE = 0;
    private static final String CREATE_LABEL_DIALOG_TITLE = "Create New Label";
    private static final String DIALOG_NEGATIVE_BUTTON_TITLE = "Cancel";
    private static final String DIALOG_POSITIVE_BUTTON_TITLE = "Ok";

    private LinearLayout createLabel;
    private RecyclerView recyclerView;
    private TextView emptyLabelsMessage;
    private EditText labelInput;
    private SearchView searchView;
    private NoteLabelListAdapter adapter;
    private NoteLabelViewModel viewModel;
    private int noteId;
    private boolean isLabelsEmpty = false;
    private String queryTextString;
    private ArrayList<String> noteLabels;
    private boolean isKeyboardOn = false;

    public NoteLabelFragment() {
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
            adapter = new NoteLabelListAdapter(new ArrayList<Label>(), this);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note_label, container, false);
        // Set soft keyboard listener
        setSoftKeyboardListener(rootView);
        setupViews(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LabelViewModelFactory factory = new LabelViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(NoteLabelViewModel.class);
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
                    isLabelsEmpty = true;
                    displayEmptyLabelsMessage();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(!isLabelsEmpty) {
            inflater.inflate(R.menu.note_label_list_menu, menu);
            MenuItem searchItem = menu.findItem(R.id.action_search);
            searchView = (SearchView) searchItem.getActionView();
            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchView.clearFocus();
                    clearQueryText();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String queryText) {
                    if(queryText.length() != 0) {
                        createLabel.setVisibility(View.VISIBLE);
                        queryTextString = queryText;
                    }
                    adapter.getFilter().filter(queryText);
                    return false;
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isKeyboardOn) {
            hideSoftKeyboard();
        }
    }

    private void setupViews(View rootView) {
        createLabel = rootView.findViewById(R.id.create_label);
        setCreateLabelClickListener();
        recyclerView = rootView.findViewById(R.id.rv_labels_list);
        emptyLabelsMessage = rootView.findViewById(R.id.tv_empty_labels_message);
    }

    private void setCreateLabelClickListener() {
        createLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(queryTextString != null && queryTextString.length() != 0) {
                    resetSearchView();
                    for(Label label: viewModel.getLabels()) {
                        if(label.getLabelTitle().equals(queryTextString)) {
                            displayLabelExistToast();
                            break;
                        }
                    }
                    viewModel.createNewLabel(queryTextString);
                    clearQueryText();
                } else {
                    displayCreateLabelDialog();
                }
            }
        });
    }

    private void displayCreateLabelDialog() {
        AlertDialog editLabelDialog = getCreateLabelDialogBuilder().create();
        editLabelDialog.show();
    }

    private AlertDialog.Builder getCreateLabelDialogBuilder() {
        // Inflate dialog view
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_label, null);
        labelInput = dialogView.findViewById(R.id.et_label_title);
        // Create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(CREATE_LABEL_DIALOG_TITLE)
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
                                viewModel.createNewLabel(labelInput.getText().toString());
                            }
                        });
        return builder;
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
//        createLabel.setVisibility(View.VISIBLE);
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
        searchView.onActionViewCollapsed();
    }

    private void clearQueryText() {
        queryTextString = "";
    }

    private void setSoftKeyboardListener(final View view) {
        view.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        //r will be populated with the coordinates of
                        // your view that area still visible.
                        view.getWindowVisibleDisplayFrame(r);
                        int heightDiff = view.getRootView().getHeight() - (r.bottom - r.top);
                        if (heightDiff > 500) {
                            // if more than 100 pixels, its probably a keyboard...
                            isKeyboardOn = true;
                        } else if (heightDiff < 500){
                            isKeyboardOn = false;
                        }
                    }
                });
    }

    private void hideSoftKeyboard() {
        @NonNull
        InputMethodManager inputManager = (InputMethodManager) Objects.requireNonNull(
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE));
        View currentFocusedView = requireActivity().getCurrentFocus();
        if(currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(
                    currentFocusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     *  ---- LabelListAdapter Click Listener Interface Implementation ----
     */
    @Override
    public void onCheckBoxChecked(boolean isChecked, Label label) {
        if(isChecked) {
            viewModel.insertNoteLabel(label);
            noteLabels.add(label.getLabelTitle());
            adapter.loadNoteLabels(noteLabels);
        } else {
            viewModel.removeNoteLabel(label);
            noteLabels.remove(label.getLabelTitle());
            adapter.loadNoteLabels(noteLabels);
        }
    }
}
