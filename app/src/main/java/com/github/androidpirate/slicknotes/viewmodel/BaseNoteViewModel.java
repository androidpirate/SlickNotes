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

package com.github.androidpirate.slicknotes.viewmodel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.androidpirate.slicknotes.data.Note;
import com.github.androidpirate.slicknotes.repo.NoteRepository;

public abstract class BaseNoteViewModel extends ViewModel {
    NoteRepository repo;
    LiveData<Note> uiModel;
    Note databaseModel;

    BaseNoteViewModel() {
    }

    public void updateNoteColor(int colorId) {
        databaseModel.setColorId(colorId);
    }

    boolean checkDatabaseModelIsEmpty() {
        return databaseModel.getTitle().isEmpty() && databaseModel.getDetails().isEmpty();
    }

    void displayEmptyNoteDiscardedToast() {
//        Toast.makeText(getApplication(),
//                "Empty note is discarded.",
//                Toast.LENGTH_SHORT)
//                .show();
    }
}
