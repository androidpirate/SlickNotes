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
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.data.Note;
import com.github.androidpirate.slicknotes.data.NoteWithLabels;
import com.github.androidpirate.slicknotes.viewmodel.BaseListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class BaseNoteListFragment extends Fragment
    implements NoteListAdapter.NoteClickListener {

    static final int NOTE_LIST_BASE = 0;
    static final int TRASH_LIST_BASE = 1;

    private SharedPreferences sharedPref;
    private RecyclerView recyclerView;
    private NoteListAdapter adapter;
    private TextView emptyListMessage;
    private FloatingActionButton fab;
    private NavController navController;
    private int navigationBase;
    private boolean isLinearLayout;
    BaseListViewModel baseViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        isLinearLayout = sharedPref.getBoolean(getString(R.string.pref_layout_key), true);
        if(adapter == null) {
            adapter = new NoteListAdapter(new ArrayList<NoteWithLabels>(), this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_note_list_base, container, false);
        // Setup vies
        setupViews(rootView);
        // Setup FAB
        setupFab();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Find navigation controller
        navController = Navigation.findNavController(
                        requireActivity(),
                        R.id.nav_host_fragment);
    }

    void toggleAlternateMenu() {
        requireActivity().invalidateOptionsMenu();
    }

    void setNavigationBase(int navigationBase) {
        this.navigationBase = navigationBase;
    }

    void displayNotes(List<NoteWithLabels> notes) {
        if(recyclerView.getVisibility() == View.GONE) {
            displayRecyclerView();
        }
        adapter.loadNotes(notes);
        setLayoutStyle();
        if(baseViewModel.hasAlternateMenu()) {
            setAlternateMenu();
        }
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(getItemScrollPosition());
    }

    void displayEmptyListMessage() {
        recyclerView.setVisibility(View.GONE);
        setEmptyListMessage();
        emptyListMessage.setVisibility(View.VISIBLE);
    }

    void displayToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    boolean getLayout() {
        return isLinearLayout;
    }

    void setLayoutPreference() {
        isLinearLayout = !isLinearLayout;
        sharedPref
                .edit()
                .putBoolean(getString(R.string.pref_layout_key), isLinearLayout)
                .apply();
        setLayoutStyle();
    }

    private void setLayoutStyle() {
        adapter.setLayoutStyle(isLinearLayout);
        recyclerView.setLayoutManager(isLinearLayout ? new LinearLayoutManager(getContext()) :
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.getRecycledViewPool().clear();
    }

    private void setAlternateMenu() {
        adapter.loadSelectedNoteIds(baseViewModel.getSelectedNoteIds());
    }

    private void setupViews(View rootView) {
        recyclerView = rootView.findViewById(R.id.rv_note_list);
        emptyListMessage = rootView.findViewById(R.id.tv_empty_list_message);
        fab = rootView.findViewById(R.id.fab);
    }

    private void setupFab() {
        if(navigationBase == NOTE_LIST_BASE) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToCreateNote();
                }
            });
        } else {
            fab.hide();
        }
    }

    private void displayRecyclerView(){
        recyclerView.setVisibility(View.VISIBLE);
        emptyListMessage.setVisibility(View.GONE);
    }

    private void navigateToCreateNote() {
        baseViewModel.clearSelections();
        baseViewModel.setItemPosition(adapter.getItemCount());
        navController.navigate(R.id.nav_home_to_create);
    }

    private void navigateToNoteDetails(Bundle args) {
        baseViewModel.clearSelections();
        if(navigationBase == NOTE_LIST_BASE) {
            navController.navigate(R.id.nav_home_to_details, args);
        } else if (navigationBase == TRASH_LIST_BASE) {
            navController.navigate(R.id.nav_trash_to_details, args);
        }
    }

    private void setEmptyListMessage() {
        if(navigationBase == NOTE_LIST_BASE) {
            emptyListMessage.setText(getString(R.string.empty_list_message));
        } else if (navigationBase == TRASH_LIST_BASE) {
            emptyListMessage.setText(getString(R.string.empty_trash_message));
        }
    }

    private void setItemScrollPosition(int position) {
        baseViewModel.setItemPosition(position);
    }

    private int getItemScrollPosition() {
        return baseViewModel.getItemPosition();
    }

    /**
     * ---- NoteListAdapter Click Listener Interface Implementation ----
     */
    @Override
    public void onNoteClick(int noteId, boolean notePinStatus, int position) {
        setItemScrollPosition(position);
        Bundle args = new Bundle();
        args.putInt(BaseEditableNoteFragment.EXTRA_NOTE_ID, noteId);
        args.putBoolean(BaseEditableNoteFragment.NOTE_PIN_STATUS, notePinStatus);
        args.putInt(BaseEditableNoteFragment.NAVIGATION_BASE, navigationBase);
        navigateToNoteDetails(args);
    }

    @Override
    public void onLongNoteClick(Note note, boolean isAdded) {
        if(isAdded) {
            baseViewModel.addToSelectedNotes(note);
        } else {
            baseViewModel.removeFromSelectedNotes(note);
        }
        toggleAlternateMenu();
    }
}
