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

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.androidpirate.slicknotes.R;

import java.util.Objects;

/**
 * A simple {@link BaseNoteListFragment} subclass.
 */
public class NoteListFragment extends BaseNoteListFragment {

    public NoteListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setNavigationBase(NOTE_LIST_BASE);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuInflater inflater = Objects.requireNonNull(getActivity()).getMenuInflater();
        menu.clear();
        if(viewModel.hasAlternateMenu()) {
            inflater.inflate(R.menu.note_list_menu, menu);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pin:
                viewModel.setNotePinStatus(viewModel.getSelectedNotes());
                toggleAlternateMenu();
            break;
            case R.id.action_add_label:
                Toast.makeText(
                        getContext(),
                        "Action add label is clicked",
                        Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.action_send_to_trash:
                viewModel.sendNotesToTrash(viewModel.getSelectedNotes());
                toggleAlternateMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
